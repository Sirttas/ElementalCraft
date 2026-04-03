package sirttas.elementalcraft.block.instrument.io.firefurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.io.AbstractIOInstrumentBlockEntity;
import sirttas.elementalcraft.container.IOContainer;
import sirttas.elementalcraft.recipe.instrument.io.FurnaceRecipeWrapper;
import sirttas.elementalcraft.recipe.instrument.io.IOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public abstract class AbstractFireFurnaceBlockEntity<T extends AbstractCookingRecipe> extends AbstractIOInstrumentBlockEntity<IOInstrumentRecipeInput, FurnaceRecipeWrapper> {

	private float exp;
	private final IOContainer inventory;

	protected AbstractFireFurnaceBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, Holder<IConfigurableBlockEntityProperties> properties, BlockPos pos, BlockState state) {
		super(blockEntityType, properties, pos, state);
		exp = 0;
		inventory = new IOContainer(this::setChanged);
	}

	@Override
	public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		this.exp = compound.getFloat(ECNames.XP);
	}

    @Override
	public void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.putFloat(ECNames.XP, this.exp);
	}

	@NotNull
	@Override
	protected SimpleIOInstrumentRecipeInput createRecipeInput() {
		return createSimpleIORecipeInput();
	}

	@SuppressWarnings({"DataFlowIssue"})
    @Override
	protected FurnaceRecipeWrapper lookupRecipe(@NotNull IOInstrumentRecipeInput recipeInput) {
		return this.getLevel().getRecipeManager().getRecipeFor(getProperties().<SingleRecipeInput, T>getRecipeType(), recipeInput.toSingleRecipeInput(), this.getLevel())
				.map(h -> new FurnaceRecipeWrapper(h.value()))
				.orElse(null);
	}

	@Override
	protected void renderProgressParticles() {
		var rand = level.random;
		var x = worldPosition.getX() + (5 + rand.nextDouble() * 6) / 16;
		var y = worldPosition.getY() + 6D / 16;
		var z = worldPosition.getZ() + (5 + rand.nextDouble() * 6) / 16;
		
		level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
		level.addParticle(ParticleTypes.SMOKE, x, y + 0.5D, z, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void assemble() {
		super.assemble();
		addExperience(recipe.experience());
	}
	
	public void dropExperience(ServerPlayer player) {
		ExperienceOrb.award(player.serverLevel(), player.position(), Math.round(exp));
		exp = 0;
	}

	public void addExperience(float exp) {
		this.exp += exp;
	}

	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}

}
