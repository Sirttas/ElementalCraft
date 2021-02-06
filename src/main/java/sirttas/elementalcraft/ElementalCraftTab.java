package sirttas.elementalcraft;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;

public class ElementalCraftTab extends ItemGroup {

	public static final @Nonnull ItemGroup TAB = new ElementalCraftTab();

	public ElementalCraftTab() {
		super(ElementalCraft.MODID);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(ECItems.focus);
	}

	@Override
	public void fill(@Nonnull NonNullList<ItemStack> list) {
		addItem(ECBlocks.extractor, list);
		addItem(ECBlocks.improvedExtractor, list);
		addItem(ECBlocks.evaporator, list);
		addItem(ECBlocks.infuser, list);
		addItem(ECBlocks.binder, list);
		addItem(ECBlocks.improvedBinder, list);
		addItem(ECBlocks.crystallizer, list);
		addItem(ECBlocks.inscriber, list);
		addItem(ECBlocks.firePedestal, list);
		addItem(ECBlocks.waterPedestal, list);
		addItem(ECBlocks.earthPedestal, list);
		addItem(ECBlocks.airPedestal, list);
		addItem(ECBlocks.pureInfuser, list);
		addItem(ECBlocks.fireFurnace, list);
		addItem(ECBlocks.fireBlastFurnace, list);
		addItem(ECBlocks.purifier, list);
		addItem(ECBlocks.tankSmall, list);
		addItem(ECBlocks.tank, list);
		addItem(ECBlocks.tankCreative, list);
		addItem(ECBlocks.impairedElementPipe, list);
		addItem(ECBlocks.elementPipe, list);
		addItem(ECBlocks.improvedElementPipe, list);
		addItem(ECBlocks.instrumentRetriever, list);
		addItem(ECBlocks.sorter, list);
		addItem(ECBlocks.spellDesk, list);
		addItem(ECBlocks.firePylon, list);
		addItem(ECBlocks.vacuumShrine, list);
		addItem(ECBlocks.growthShrine, list);
		addItem(ECBlocks.harvestShrine, list);
		addItem(ECBlocks.lavaShrine, list);
		addItem(ECBlocks.oreShrine, list);
		addItem(ECBlocks.overloadShrine, list);
		addItem(ECBlocks.sweetShrine, list);
		addItem(ECBlocks.enderLockShrine, list);
		addItem(ECBlocks.breedingShrine, list);
		addItem(ECBlocks.groveShrine, list);
		addItem(ECBlocks.accelerationShrineUpgrade, list);
		addItem(ECBlocks.rangeShrineUpgrade, list);
		addItem(ECBlocks.capacityShrineUpgrade, list);
		addItem(ECBlocks.efficiencyShrineUpgrade, list);
		addItem(ECBlocks.strengthShrineUpgrade, list);
		addItem(ECBlocks.optimizationShrineUpgrade, list);
		addItem(ECBlocks.fortuneShrineUpgrade, list);
		addItem(ECBlocks.silkTouchShrineUpgrade, list);
		addItem(ECBlocks.plantingShrineUpgrade, list);
		addItem(ECBlocks.bonelessGrowthShrineUpgrade, list);
		addItem(ECBlocks.pickupShrineUpgrade, list);
		addItem(ECBlocks.nectarShrineUpgrade, list);

		addItem(ECBlocks.crystalOre, list);
		addItem(ECBlocks.whiteRock, list);
		addItem(ECBlocks.whiteRockSlab, list);
		addItem(ECBlocks.whiteRockStairs, list);
		addItem(ECBlocks.whiteRockWall, list);
		addItem(ECBlocks.whiteRockFence, list);
		addItem(ECBlocks.whiteRockBrick, list);
		addItem(ECBlocks.whiteRockBrickSlab, list);
		addItem(ECBlocks.whiteRockBrickStairs, list);
		addItem(ECBlocks.whiteRockBrickWall, list);
		addItem(ECBlocks.burntGlass, list);
		addItem(ECBlocks.burntGlassPane, list);
		addItem(ECBlocks.pureRock, list);
		addItem(ECBlocks.pureRockSlab, list);
		addItem(ECBlocks.pureRockStairs, list);
		addItem(ECBlocks.pureRockWall, list);

		addItem(ECItems.elementopedia, list);
		addItem(ECItems.focus, list);
		addItem(ECItems.scroll, list);
		addItem(ECItems.spellBook, list);
		addItem(ECItems.emptyReceptacle, list);
		addItem(ECItems.receptacle, list);
		addItem(ECItems.fireElementHolder, list);
		addItem(ECItems.waterElementHolder, list);
		addItem(ECItems.earthElementHolder, list);
		addItem(ECItems.airElementHolder, list);
		addItem(ECItems.chisel, list);
		addItem(ECItems.pureOre, list);
		addItem(ECItems.inertCrystal, list);
		addItem(ECBlocks.inertCrystalBlock, list);
		addItem(ECItems.containedCrystal, list);
		addItem(ECItems.fireCrystal, list);
		addItem(ECBlocks.fireCrystalBlock, list);
		addItem(ECItems.waterCrystal, list);
		addItem(ECBlocks.waterCrystalBlock, list);
		addItem(ECItems.earthCrystal, list);
		addItem(ECBlocks.earthCrystalBlock, list);
		addItem(ECItems.airCrystal, list);
		addItem(ECBlocks.airCrystalBlock, list);
		addItem(ECItems.pureCrystal, list);
		addItem(ECItems.fireShard, list);
		addItem(ECItems.waterShard, list);
		addItem(ECItems.earthShard, list);
		addItem(ECItems.airShard, list);
		addItem(ECItems.powerfulFireShard, list);
		addItem(ECItems.powerfulWaterShard, list);
		addItem(ECItems.powerfulEarthShard, list);
		addItem(ECItems.powerfulAirShard, list);
		addItem(ECItems.crudeFireGem, list);
		addItem(ECItems.crudeWaterGem, list);
		addItem(ECItems.crudeEarthGem, list);
		addItem(ECItems.crudeAirGem, list);
		addItem(ECItems.fineFireGem, list);
		addItem(ECItems.fineWaterGem, list);
		addItem(ECItems.fineEarthGem, list);
		addItem(ECItems.fineAirGem, list);
		addItem(ECItems.pristineFireGem, list);
		addItem(ECItems.pristineWaterGem, list);
		addItem(ECItems.pristineEarthGem, list);
		addItem(ECItems.pristineAirGem, list);
		addItem(ECItems.drenchedIronNugget, list);
		addItem(ECItems.drenchedIronIngot, list);
		addItem(ECItems.drenchedIronBlock, list);
		addItem(ECItems.swiftAlloyNugget, list);
		addItem(ECItems.swiftAlloyIngot, list);
		addItem(ECItems.swiftAlloyBlock, list);
		addItem(ECItems.fireiteNugget, list);
		addItem(ECItems.fireiteIngot, list);
		addItem(ECItems.fireiteBlock, list);
		addItem(ECItems.airSilk, list);
		addItem(ECItems.scrollPaper, list);
		addItem(ECItems.shrineBase, list);
		addItem(ECItems.shrineUpgradeCore, list);
		addItem(ECItems.minorRuneSlate, list);
		addItem(ECItems.runeSlate, list);
		addItem(ECItems.majorRuneSlate, list);
		addItem(ECItems.rune, list);
	}

	private void addItem(IItemProvider item, @Nonnull NonNullList<ItemStack> list) {
		if (item != null) {
			item.asItem().fillItemGroup(this, list);
		}
	}

}
