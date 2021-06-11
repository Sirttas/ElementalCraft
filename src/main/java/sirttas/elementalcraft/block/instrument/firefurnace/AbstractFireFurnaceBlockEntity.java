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
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.inventory.IOInventory;
import sirttas.elementalcraft.inventory.InventoryTileWrapper;
import sirttas.elementalcraft.recipe.instrument.io.FurnaceRecipeWrapper;

public abstract class AbstractFireFurnaceBlockEntity<T extends AbstractCookingRecipe> extends AbstractInstrumentBlockEntity<AbstractFireFurnaceBlockEntity<T>, FurnaceRecipeWrapper<T>> {

	private float exp;
	private IRecipeType<T> furnaceRecipeType;
	private final IOInventory inventory;

	protected AbstractFireFurnaceBlockEntity(TileEntityType<? extends AbstractFireFurnaceBlockEntity<T>> tileEntityTypeIn, IRecipeType<T> recipeType, int transferSpeed, int maxRunes) {
		super(tileEntityTypeIn, null, transferSpeed, maxRunes);
		this.furnaceRecipeType = recipeType;
		exp = 0;
		outputSlot = 1;
		inventory = new IOInventory(this::setChanged);
	}

	@Override
	protected IItemHandler createHandler() {
		return new SidedInvWrapper(inventory, null);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.exp = compound.getFloat(ECNames.XP);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putFloat(ECNames.XP, this.exp);
		return compound;
	}

	@Override
	protected FurnaceRecipeWrapper<T> lookupRecipe() {
		return this.getLevel().getRecipeManager().getRecipeFor(furnaceRecipeType, InventoryTileWrapper.from(this), this.getLevel()).map(FurnaceRecipeWrapper::new).orElse(null);
	}

	@Override
	protected void onProgress() {
		if (level.isClientSide) {
			Random rand = level.random;
			double x = worldPosition.getX() + (5 + rand.nextDouble() * 6) / 16;
			double y = worldPosition.getY() + 6D / 16;
			double z = worldPosition.getZ() + (5 + rand.nextDouble() * 6) / 16;

			level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
			level.addParticle(ParticleTypes.SMOKE, x, y + 0.5D, z, 0.0D, 0.0D, 0.0D);
		}
		super.onProgress();
	}

	public void dropExperience(PlayerEntity player) {
		dropExperience(player.position());
	}

	public void dropExperience(Vector3d pos) {
		while (exp > 0) {
			int j = ExperienceOrbEntity.getExperienceValue((int) exp);
			exp -= j;
			level.addFreshEntity(new ExperienceOrbEntity(level, pos.x(), pos.y() + 0.5D, pos.z() + 0.5D, j));
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