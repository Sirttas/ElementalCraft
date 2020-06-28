package sirttas.elementalcraft.block.shrine;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.tile.TileECTickable;
import sirttas.elementalcraft.block.tile.element.IElementReceiver;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.nbt.ECNBTTags;

public abstract class TileShrine extends TileECTickable implements IElementReceiver {

	private ElementType elementType = ElementType.NONE;
	private int elementAmount = 0;
	protected int elementMax = ECConfig.CONFIG.shrineMaxAmount.get();
	private boolean running = false;

	public TileShrine(TileEntityType<?> tileEntityTypeIn, ElementType type) {
		super(tileEntityTypeIn);
		this.setPasive(true);
		elementType = type;
	}

	protected int consumeElement(int i) {
		if (this.isPowered()) {
			return 0;
		}
		int newCount = elementAmount - i;
		newCount = newCount > 0 ? newCount : 0;

		int ret = elementAmount - newCount;

		elementAmount = newCount;
		if (ret > 0) {
			running = true;
		}
		return ret;
	}

	@Override
	public int inserElement(int count, ElementType type, boolean simulate) {
		if (type != this.elementType && this.elementType != ElementType.NONE) {
			return 0;
		} else {
			int newCount = elementAmount + count;

			newCount = newCount < elementMax ? newCount : elementMax;

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
	public void tick() {
		super.tick();
		running = false;
	}

	public boolean isRunning() {
		return running;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public int getMaxElement() {
		return elementMax;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		elementType = ElementType.byName(compound.getString(ECNBTTags.ELEMENT_TYPE));
		elementAmount = compound.getInt(ECNBTTags.ELEMENT_AMOUNT);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putString(ECNBTTags.ELEMENT_TYPE, elementType.getName());
		compound.putInt(ECNBTTags.ELEMENT_AMOUNT, elementAmount);
		return compound;
	}

	protected boolean randomChance(double chance) {
		return world.rand.nextDouble() <= chance;
	}
}
