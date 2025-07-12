package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class BinderBlockEntity extends AbstractInstrumentBlockEntity<MultipleItemsSingleElementRecipeInput, AbstractBindingRecipe> implements IBinder {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(BinderBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	private final InstrumentContainer inventory;

	public BinderBlockEntity(BlockPos pos, BlockState state) {
		this(ECBlockEntityTypes.BINDER, PROPERTIES, pos, state);
	}

	protected BinderBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, Holder<IConfigurableBlockEntityProperties> properties, BlockPos pos, BlockState state) {
		super(blockEntityType, properties, pos, state);
		inventory = new InstrumentContainer(this::setChanged, 20);
		particleOffset = new Vec3(0, 0.2, 0);
	}

	@Override
	public int getItemCount() {
		return inventory.getItemCount();
	}
	
	@Nonnull
	@Override
	public Container getInventory() {
		return inventory;
	}

	@Override
	protected void assemble() {
		var remainingItem = recipe.getRemainingItems(createRecipeInput()).getFirst();

		super.assemble();
		if (!remainingItem.isEmpty()) {
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				if (inventory.getItem(i).isEmpty()) {
					inventory.setItem(i, remainingItem);
					break;
				}
			}
		}
	}
	@Override
	protected void setRemainingItems(NonNullList<ItemStack> remainingItems) {
		var targetIndex = 1;

        for (var stack : remainingItems) {
            if (targetIndex >= 20) {
                return;
            }
            if (!stack.isEmpty()) {
                inventory.setItem(targetIndex++, stack);
            }
        }
	}

	@Override
	protected @NotNull MultipleItemsSingleElementRecipeInput createRecipeInput() {
		return new MultipleItemsSingleElementRecipeInput(
				inventory.getStacks(),
				getElementType(),
				getContainer().getElementAmount());
	}
}
