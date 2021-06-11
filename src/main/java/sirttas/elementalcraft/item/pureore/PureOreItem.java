package sirttas.elementalcraft.item.pureore;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.ECItem;

public class PureOreItem extends ECItem {

	public static final String NAME = "pure_ore";

	@Override
	public ITextComponent getName(ItemStack stack) {
		ItemStack ore = ElementalCraft.PURE_ORE_MANAGER.getOre(stack);

		if (!ore.isEmpty()) {
			return new TranslationTextComponent("tooltip.elementalcraft.pure_ore", ore.getHoverName());
		}
		return super.getName(stack);
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			ElementalCraft.PURE_ORE_MANAGER.getOres().forEach(o -> items.add(ElementalCraft.PURE_ORE_MANAGER.createPureOre(o)));
		}
	}
}
