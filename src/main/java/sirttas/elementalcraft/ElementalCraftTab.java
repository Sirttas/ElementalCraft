package sirttas.elementalcraft;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;

public class ElementalCraftTab extends ItemGroup {

	public static final @Nonnull ItemGroup tabElementalCraft = new ElementalCraftTab();

	public ElementalCraftTab() {
		super(ElementalCraft.MODID);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(ECItems.tank);
	}

	@Override
	public void fill(@Nonnull NonNullList<ItemStack> list) {
		addItem(ECBlocks.extractor, list);
		addItem(ECBlocks.improvedExtractor, list);
		addItem(ECBlocks.infuser, list);
		addItem(ECBlocks.binder, list);
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
		addItem(ECBlocks.impairedElementPipe, list);
		addItem(ECBlocks.elementPipe, list);
		addItem(ECBlocks.improvedElementPipe, list);
		addItem(ECBlocks.instrumentRetriever, list);
		addItem(ECBlocks.firePylon, list);
		addItem(ECBlocks.vacuumShrine, list);
		addItem(ECBlocks.growthShrine, list);
		addItem(ECBlocks.harvestShrine, list);
		addItem(ECBlocks.lavaShrine, list);
		addItem(ECBlocks.oreShrine, list);
		addItem(ECBlocks.overloadShrine, list);
		addItem(ECBlocks.sweetShrine, list);

		addItem(ECBlocks.crystalOre, list);
		addItem(ECBlocks.whiteRock, list);
		addItem(ECBlocks.whiteRockSlab, list);
		addItem(ECBlocks.whiteRockStairs, list);
		addItem(ECBlocks.whiteRockWall, list);
		addItem(ECBlocks.burntGlass, list);
		addItem(ECBlocks.burntGlassPane, list);
		addItem(ECBlocks.pureRock, list);
		addItem(ECBlocks.pureRockSlab, list);
		addItem(ECBlocks.pureRockStairs, list);
		addItem(ECBlocks.pureRockWall, list);

		addItem(ECItems.elementScript, list);
		addItem(ECItems.focus, list);
		addItem(ECItems.scroll, list);
		addItem(ECItems.emptyReceptacle, list);
		addItem(ECItems.receptacle, list);
		addItem(ECItems.pureOre, list);
		addItem(ECItems.inertCrystal, list);
		addItem(ECItems.containedCrystal, list);
		addItem(ECItems.fireCrystal, list);
		addItem(ECItems.waterCrystal, list);
		addItem(ECItems.earthCrystal, list);
		addItem(ECItems.airCrystal, list);
		addItem(ECItems.pureCrystal, list);
		addItem(ECItems.drenchedIronIngot, list);
		addItem(ECItems.drenchedIronNugget, list);
		addItem(ECItems.swiftAlloyIngot, list);
		addItem(ECItems.swiftAlloyNugget, list);
		addItem(ECItems.shrineBase, list);
	}

	private void addItem(IItemProvider item, @Nonnull NonNullList<ItemStack> list) {
		item.asItem().fillItemGroup(this, list);

	}

}
