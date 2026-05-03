package sirttas.elementalcraft.block.instrument;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.crafting.AbstractECCraftingBlockEntity;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.recipe.instrument.InstrumentRecipe;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public abstract class AbstractInstrumentBlockEntity<I extends RecipeInput, R extends InstrumentRecipe<I>> extends AbstractECCraftingBlockEntity<I, R> implements IInstrument {

	private int progress = 0;
	private ISingleElementStorage containerCache;
	protected Vec3 particleOffset;

	protected AbstractInstrumentBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, Holder<@NotNull IConfigurableBlockEntityProperties> properties, BlockPos pos, BlockState state) {
		super(blockEntityType, properties, pos, state);
		particleOffset = Vec3.ZERO;
	}

	@Override
	public void process() {
		super.process();
		ParticleHelper.createCraftingParticle(getElementType(), level, Vec3.atCenterOf(worldPosition).add(particleOffset), level.getRandom());
	}

	public static <I extends RecipeInput, R extends InstrumentRecipe<I>> void tick(Level level, BlockPos pos, BlockState state, AbstractInstrumentBlockEntity<I, R> instrument) {
		if (!instrument.isPowered() && instrument.progressOnTick()) {
			instrument.makeProgress();
		}
		AbstractECCraftingBlockEntity.tick(instrument);
	}
	
	protected boolean progressOnTick() {
		return true;
	}

	protected boolean makeProgress() {
		var container = getContainer();
		var recipeCost = recipe == null ? 0 : recipe.getElementAmount(createRecipeInput());

		if (recipe != null && progress >= recipeCost) {
			process();
			progress = 0;
			return true;
		} else if (this.isRecipeAvailable() && container != null) {
			float preservation = runeHandler.getElementPreservation();
			int oldProgress = progress;
			var transfer = ceilTransfer(container, Math.round(runeHandler.getTransferSpeed(this.getTransferSpeed()) / preservation), recipeCost);

			progress += Math.round(container.extractElement(transfer, getRecipeElementType(), false) * preservation);
			if (level.isClientSide() && progress > 0 && getProgressRounded(transfer, progress) > getProgressRounded(transfer, oldProgress)) {
				ParticleHelper.createElementFlowParticle(getElementType(), level, Vec3.atCenterOf(worldPosition).add(particleOffset), Direction.UP, 1, level.getRandom());
				renderProgressParticles();
			}
			return true;
		} else if (recipe == null) {
			progress = 0;
		}
		return false;
	}

	private int ceilTransfer(ISingleElementStorage container, int transfer, int recipeCost) {
		var max = container.getElementAmount();

		if (max <= 0) {
			return 0;
		} else if (transfer >= max) {
			if (progress + max < recipeCost) {
				transfer = max - 1; // -1 to avoid draining the container
			} else {
				transfer = max; // we have enough element to finish the recipe, so we don't care if we drain the container
			}
		}
		return Math.max(0, transfer);
	}


	protected void renderProgressParticles() {}
	
	protected ElementType getRecipeElementType() {
		if (recipe instanceof IElementTypeProvider provider) {
			return provider.getElementType();
		}
		return ElementType.NONE;
	}

	@Override
	protected void assemble() {
		var input = createRecipeInput();
		var remainingItems = getRemainingItems(input);

		getInventory().setItem(getOutputSlot(), recipe.assemble(createRecipeInput()));
		setRemainingItems(remainingItems);
	}

    protected NonNullList<@NotNull ItemStack> getRemainingItems(I input) {
        var size = input.size();
        var remainingItems = NonNullList.withSize(size, ItemStack.EMPTY);

        for (int i = 0; i < size; i++) {
            var remainder = input.getItem(i).getCraftingRemainder();

            remainingItems.set(i, remainder != null ? remainder.create() : ItemStack.EMPTY);
        }
        return remainingItems;
    }

    protected void setRemainingItems(NonNullList<@NotNull ItemStack> remainingItems) {
		var inv = getInventory();
		var size = inv.getContainerSize();
		var outputSlot = getOutputSlot();

		for (int i = 0; i < size; i++) {
			if (i != outputSlot) {
				inv.setItem(i, remainingItems.get(i));
			}
		}
	}

	@Override
	public @NotNull ElementType getElementType() {
		ElementType containerType = this.getContainerElementType();
		
		return containerType != ElementType.NONE || recipe == null ? containerType : getRecipeElementType();
	}

	@Override
	public boolean isRunning() {
		return progress > 0;
	}

    @Override
    protected void loadAdditional(@Nonnull ValueInput input) {
        super.loadAdditional(input);
        progress = input.getIntOr(ECNames.PROGRESS, 0);
    }

	@Override
	protected void saveAdditional(@Nonnull ValueOutput output) {
		super.saveAdditional(output);
        output.putInt(ECNames.PROGRESS, progress);
	}

	@Override
	public void clearContent() {
		super.clearContent();
		progress = 0;
	}

	@Override
	public ISingleElementStorage getContainer() {
		if (containerCache == null) {
			containerCache = IInstrument.super.getContainer();
		}
		return containerCache;
	}

	@Override
	protected R lookupRecipe(@NotNull ServerLevel level, @NotNull I recipeInput) {
		if (getContainerElementType() == ElementType.NONE) {
			return null;
		}
		return super.lookupRecipe(level, recipeInput);
	}
}
