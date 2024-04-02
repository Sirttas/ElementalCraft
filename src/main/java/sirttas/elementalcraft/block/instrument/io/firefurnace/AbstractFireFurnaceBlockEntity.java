package sirttas.elementalcraft.block.instrument.io.firefurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.io.AbstractIOInstrumentBlockEntity;
import sirttas.elementalcraft.container.ContainerBlockEntityWrapper;
import sirttas.elementalcraft.container.IOContainer;
import sirttas.elementalcraft.recipe.instrument.io.FurnaceRecipeWrapper;

import javax.annotation.Nonnull;

public abstract class AbstractFireFurnaceBlockEntity<T extends AbstractCookingRecipe> extends AbstractIOInstrumentBlockEntity<AbstractFireFurnaceBlockEntity<T>, FurnaceRecipeWrapper<T>> {

	private float exp;
	private final RecipeType<T> furnaceRecipeType;
	private final IOContainer inventory;

	protected AbstractFireFurnaceBlockEntity(Config<AbstractFireFurnaceBlockEntity<T>, FurnaceRecipeWrapper<T>> config, RecipeType<T> recipeType, BlockPos pos, BlockState state) {
		super(config, pos, state);
		this.furnaceRecipeType = recipeType;
		exp = 0;
		inventory = new IOContainer(this::setChanged);
	}

	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		this.exp = compound.getFloat(ECNames.XP);
	}

	@Nonnull
    @Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.putFloat(ECNames.XP, this.exp);
	}

	@Override
	protected FurnaceRecipeWrapper<T> lookupRecipe() {
		return this.getLevel().getRecipeManager().getRecipeFor(furnaceRecipeType, ContainerBlockEntityWrapper.from(this), this.getLevel())
				.map(h -> new FurnaceRecipeWrapper<>(h.value()))
				.orElse(null);
	}

	@Override
	protected void renderProgressParticles() {
		var rand = level.random;
		double x = worldPosition.getX() + (5 + rand.nextDouble() * 6) / 16;
		double y = worldPosition.getY() + 6D / 16;
		double z = worldPosition.getZ() + (5 + rand.nextDouble() * 6) / 16;
		
		level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
		level.addParticle(ParticleTypes.SMOKE, x, y + 0.5D, z, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void assemble() {
		super.assemble();
		addExperience(recipe.getExperience());
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
