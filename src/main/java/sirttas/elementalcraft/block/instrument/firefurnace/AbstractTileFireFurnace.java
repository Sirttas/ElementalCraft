package sirttas.elementalcraft.block.instrument.firefurnace;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import sirttas.elementalcraft.block.instrument.TileInstrument;
import sirttas.elementalcraft.inventory.IOInventory;
import sirttas.elementalcraft.inventory.InventoryTileWrapper;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.recipe.instrument.FurnaceRecipeWrapper;
import sirttas.elementalcraft.recipe.instrument.IInstrumentRecipe;

public abstract class AbstractTileFireFurnace<T extends AbstractCookingRecipe> extends TileInstrument {

	private float exp;
	private IRecipeType<T> recipeType;
	private final IOInventory inventory;

	protected AbstractTileFireFurnace(TileEntityType<? extends AbstractTileFireFurnace<T>> tileEntityTypeIn, IRecipeType<T> recipeType) {
		this(tileEntityTypeIn);
		this.recipeType = recipeType;
	}

	public AbstractTileFireFurnace(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		exp = 0;
		outputSlot = 1;
		inventory = new IOInventory(this::forceSync);
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

	@SuppressWarnings("unchecked")
	@Override
	protected IInstrumentRecipe<AbstractTileFireFurnace<T>> lookupRecipe() {
		return this.getWorld().getRecipeManager().getRecipe(recipeType, InventoryTileWrapper.from(this), this.getWorld()).map(FurnaceRecipeWrapper::new).orElse(null);
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