package sirttas.elementalcraft.block.shrine;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tile.TileECTickable;
import sirttas.elementalcraft.block.tile.element.IElementReceiver;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.nbt.ECNames;

public abstract class TileShrine extends TileECTickable implements IElementReceiver {

	private ElementType elementType = ElementType.NONE;
	private int elementAmount = 0;
	protected int elementMax = ECConfig.CONFIG.shrineMaxAmount.get();
	private boolean running = false;
	private double tick = 0;
	private double periode = 1;

	public TileShrine(TileEntityType<?> tileEntityTypeIn, ElementType type) {
		this(tileEntityTypeIn, type, 1);
	}

	public TileShrine(TileEntityType<?> tileEntityTypeIn, ElementType type, double periode) {
		super(tileEntityTypeIn);
		elementType = type;
		this.periode = periode;
	}


	protected int consumeElement(int i) {
		if (this.isPowered()) {
			return 0;
		}
		int newCount = Math.max(elementAmount - i, 0);
		int ret = elementAmount - newCount;

		elementAmount = newCount;
		if (ret > 0) {
			running = true;
		}
		this.forceSync();
		return ret;
	}

	@Override
	public int inserElement(int count, ElementType type, boolean simulate) {
		if (type != this.elementType && this.elementType != ElementType.NONE) {
			return 0;
		} else {
			int newCount = Math.min(elementAmount + count, elementMax);
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

	protected abstract void doTick();

	@Override
	public final void tick() {
		super.tick();
		running = false;
		if (this.hasWorld() && !this.isPowered()) {
			tick++;
			if (periode == 0) {
				ElementalCraft.LOGGER.warn("Shrine periode should not be 0");
				periode = 1;
			}
			while (tick >= periode) {
				doTick();
				tick -= periode;
			}
		}
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
		elementType = ElementType.byName(compound.getString(ECNames.ELEMENT_TYPE));
		elementAmount = compound.getInt(ECNames.ELEMENT_AMOUNT);
		running = compound.getBoolean(ECNames.RUNNING);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putString(ECNames.ELEMENT_TYPE, elementType.getName());
		compound.putInt(ECNames.ELEMENT_AMOUNT, elementAmount);
		compound.putBoolean(ECNames.RUNNING, running);
		return compound;
	}

	protected boolean randomChance(double chance) {
		return world.rand.nextDouble() <= chance;
	}
}
