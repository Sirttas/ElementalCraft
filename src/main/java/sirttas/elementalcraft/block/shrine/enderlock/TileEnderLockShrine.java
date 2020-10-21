package sirttas.elementalcraft.block.shrine.enderlock;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.config.ECConfig;

public class TileEnderLockShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockEnderLockShrine.NAME) public static TileEntityType<TileEnderLockShrine> TYPE;

	public TileEnderLockShrine() {
		super(TYPE, ElementType.WATER);
	}

	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		int range = ECConfig.CONFIG.enderLockShrineRange.get();

		return new AxisAlignedBB(this.getPos()).grow(range, 0, range).expand(0, 2, 0);
	}

	@Override
	protected void doTick() {
		// nothing to do
	}

	public boolean doLock() {
		int consumeAmount = ECConfig.CONFIG.enderLockShrineConsumeAmount.get();

		if (this.getElementAmount() >= consumeAmount) {
			this.consumeElement(consumeAmount);
			return true;
		}
		return false;
	}
}
