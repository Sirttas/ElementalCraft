package sirttas.elementalcraft.block.instrument.firefurnace;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.instrument.AbstractTileInstrument;
import sirttas.elementalcraft.inventory.IOInventory;
import sirttas.elementalcraft.inventory.InventoryTileWrapper;
import sirttas.elementalcraft.recipe.instrument.io.FurnaceRecipeWrapper;

public abstract class AbstractTileFireFurnace<T extends AbstractCookingRecipe> extends AbstractTileInstrument<AbstractTileFireFurnace<T>, FurnaceRecipeWrapper<T>> {

	private float exp;
	private IRecipeType<T> furnaceRecipeType;
	private final IOInventory inventory;

	protected AbstractTileFireFurnace(TileEntityType<? extends AbstractTileFireFurnace<T>> tileEntityTypeIn, IRecipeType<T> recipeType, int transferSpeed, int maxRunes) {
		super(tileEntityTypeIn, null, transferSpeed, maxRunes);
		this.furnaceRecipeType = recipeType;
		exp = 0;
		outputSlot = 1;
		inventory = new IOInventory(this::markDirty);
	}

	@Override
	protected IItemHandler createHandler() {
		return new SidedInvWrapper(inventory, null);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		this.exp = compound.getFloat(ECNames.XP);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putFloat(ECNames.XP, this.exp);
		return compound;
	}

	@Override
	protected FurnaceRecipeWrapper<T> lookupRecipe() {
		return this.getWorld().getRecipeManager().getRecipe(furnaceRecipeType, InventoryTileWrapper.from(this), this.getWorld()).map(FurnaceRecipeWrapper::new).orElse(null);
	}

	@Override
	protected void onProgress() {
		if (world.isRemote) {
			Random rand = world.rand;
			double x = pos.getX() + (5 + rand.nextDouble() * 6) / 16;
			double y = pos.getY() + 6D / 16;
			double z = pos.getZ() + (5 + rand.nextDouble() * 6) / 16;

			world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.SMOKE, x, y + 0.5D, z, 0.0D, 0.0D, 0.0D);
		}
		super.onProgress();
	}

	public void dropExperience(PlayerEntity player) {
		dropExperience(player.getPositionVec());
	}

	public void dropExperience(Vector3d pos) {
		while (exp > 0) {
			int j = ExperienceOrbEntity.getXPSplit((int) exp);
			exp -= j;
			world.addEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY() + 0.5D, pos.getZ() + 0.5D, j));
		}
		exp = 0;
	}

	public void addExperience(float exp) {
		this.exp += exp;
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}

}