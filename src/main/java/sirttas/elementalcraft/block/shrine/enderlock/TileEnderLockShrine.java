package sirttas.elementalcraft.block.shrine.enderlock;

import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractTileShrine;
import sirttas.elementalcraft.config.ECConfig;

public class TileEnderLockShrine extends AbstractTileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockEnderLockShrine.NAME) public static final TileEntityType<TileEnderLockShrine> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.WATER).consumeAmount(ECConfig.COMMON.enderLockShrineConsumeAmount.get()).range(ECConfig.COMMON.enderLockShrineRange.get());

	protected static final List<Direction> UPGRRADE_DIRECTIONS = ImmutableList.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	public TileEnderLockShrine() {
		super(TYPE, PROPERTIES);
	}

	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		int range = ECConfig.COMMON.enderLockShrineRange.get();

		return new AxisAlignedBB(this.getPos()).grow(range, 0, range).expand(0, 2, 0);
	}

	@Override
	protected boolean doTick() {
		return false;
	}

	public boolean doLock() {
		int consumeAmount = this.getConsumeAmount();

		if (this.elementStorage.getElementAmount() >= consumeAmount) {
			this.consumeElement(consumeAmount);
			return true;
		}
		return false;
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRRADE_DIRECTIONS;
	}
}
