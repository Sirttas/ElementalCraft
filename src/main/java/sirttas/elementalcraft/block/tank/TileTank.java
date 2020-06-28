package sirttas.elementalcraft.block.tank;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tile.TileEC;
import sirttas.elementalcraft.block.tile.element.IElementReceiver;
import sirttas.elementalcraft.block.tile.element.IElementSender;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.nbt.ECNBTTags;

public class TileTank extends TileEC implements IElementSender, IElementReceiver {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTank.NAME) public static TileEntityType<TileTank> TYPE;

	private int elementAmount = 0;
	private int elementAmountMax = ECConfig.CONFIG.tankMaxAmount.get();
	private ElementType elementType = ElementType.NONE;

	public TileTank() {
		super(TYPE);
	}

	@Override
	public int inserElement(int count, ElementType type, boolean simulate) {
		if (type != this.elementType && this.elementType != ElementType.NONE) {
			return this.extractElement(count, this.elementType, simulate);
		} else {
			int newCount = elementAmount + count;

			newCount = newCount < elementAmountMax ? newCount : elementAmountMax;

			int ret = count - newCount + elementAmount;

			if (!simulate) {
				elementAmount = newCount;
				if (this.elementType == ElementType.NONE) {
					this.elementType = type;
				}
			}
			return ret;
		}
	}

	@Override
	public int extractElement(int count, ElementType type, boolean simulate) {
		if (type != this.elementType) {
			return 0;
		}
		int newCount = elementAmount - count;

		newCount = newCount > 0 ? newCount : 0;

		int ret = elementAmount - newCount;

		if (!simulate) {
			elementAmount = newCount;
			if (this.elementAmount == 0) {
				this.elementType = ElementType.NONE;
			}
		}
		return ret;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public int getMaxElement() {
		return elementAmountMax;
	}

	@Override
	public boolean doesRenderGauge() {
		return true;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		elementType = ElementType.byName(compound.getString(ECNBTTags.ELEMENT_TYPE));
		elementAmount = compound.getInt(ECNBTTags.ELEMENT_AMOUNT);
		elementAmountMax = compound.getInt(ECNBTTags.ELEMENT_MAX);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putString(ECNBTTags.ELEMENT_TYPE, elementType.getName());
		compound.putInt(ECNBTTags.ELEMENT_AMOUNT, elementAmount);
		compound.putInt(ECNBTTags.ELEMENT_MAX, elementAmountMax);
		return compound;
	}

}
