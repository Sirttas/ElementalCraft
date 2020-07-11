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
		addItem(ECBlocks.tank, list);
		addItem(ECBlocks.elementPipe, list);
		addItem(ECBlocks.firePylon, list);
		addItem(ECBlocks.vacuumShrine, list);
		addItem(ECBlocks.growthShrine, list);
		addItem(ECBlocks.lavaShrine, list);
		addItem(ECBlocks.oreShrine, list);
		addItem(ECBlocks.overloadShrine, list);
		addItem(ECBlocks.crystalOre, list);
		addItem(ECBlocks.whiteRock, list);
		addItem(ECBlocks.whiteRockSlab, list);
		addItem(ECBlocks.whiteRockStairs, list);
		addItem(ECBlocks.whiteRockWall, list);

		addItem(ECItems.focus, list);
		addItem(ECItems.emptyReceptacle, list);
		addItem(ECItems.receptacle, list);
		addItem(ECItems.inertCrystal, list);
		addItem(ECItems.containedCrystal, list);
		addItem(ECItems.fireCrystal, list);
		addItem(ECItems.waterCrystal, list);
		addItem(ECItems.earthCrystal, list);
		addItem(ECItems.airCrystal, list);
		addItem(ECItems.pureCrystal, list);
		addItem(ECItems.shrineBase, list);
	}

	private void addItem(IItemProvider item, @Nonnull NonNullList<ItemStack> list) {
		item.asItem().fillItemGroup(this, list);

	}

}
