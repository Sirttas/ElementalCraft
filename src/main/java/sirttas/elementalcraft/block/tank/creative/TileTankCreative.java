package sirttas.elementalcraft.block.tank.creative;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tank.ITank;
import sirttas.elementalcraft.block.tile.TileEC;
import sirttas.elementalcraft.nbt.ECNames;

public class TileTankCreative extends TileEC implements ITank {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTankCreative.NAME) public static TileEntityType<TileTankCreative> TYPE;

	private ElementType elementType = ElementType.NONE;

	public TileTankCreative() {
		super(TYPE);
	}

	@Override
	public int inserElement(int count, ElementType type, boolean simulate) {
		if (!simulate) {
			this.elementType = type;
			this.markDirty();
		}
		return 0;
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		return count;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public int getElementAmount() {
		return 1000000;
	}

	@Override
	public int getMaxElement() {
		return 1000000;
	}

	@Override
	public boolean doesRenderGauge() {
		return true;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		elementType = ElementType.byName(compound.getString(ECNames.ELEMENT_TYPE));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putString(ECNames.ELEMENT_TYPE, elementType.getName());
		return compound;
	}

}
