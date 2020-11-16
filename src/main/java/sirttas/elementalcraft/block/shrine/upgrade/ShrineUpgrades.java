package sirttas.elementalcraft.block.shrine.upgrade;

import java.util.function.Supplier;

import sirttas.elementalcraft.block.ECBlocks;

public class ShrineUpgrades {

	public static final Supplier<ShrineUpgrade> FORTUNE = ECBlocks.fortuneShrineUpgrade::getUpgrade;
	public static final Supplier<ShrineUpgrade> SILK_TOUCH = ECBlocks.silkTouchShrineUpgrade::getUpgrade;
	public static final Supplier<ShrineUpgrade> PLANTING = ECBlocks.plantingShrineUpgrade::getUpgrade;
	public static final Supplier<ShrineUpgrade> BONELESS_GROWTH = ECBlocks.bonelessGrowthShrineUpgrade::getUpgrade;

}
