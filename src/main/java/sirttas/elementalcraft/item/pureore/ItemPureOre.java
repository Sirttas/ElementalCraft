package sirttas.elementalcraft.item.pureore;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.ItemEC;

public class ItemPureOre extends ItemEC {

	public static final String NAME = "pure_ore";

	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		ItemStack ore = ElementalCraft.PURE_ORE_MANAGER.getOre(stack);

		if (!ore.isEmpty()) {
			return new TranslationTextComponent("tooltip.elementalcraft.pure_ore", ore.getDisplayName());
		}
		return super.getDisplayName(stack);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			ElementalCraft.PURE_ORE_MANAGER.getOres().forEach(o -> items.add(ElementalCraft.PURE_ORE_MANAGER.createPureOre(o)));
		}
	}
}
