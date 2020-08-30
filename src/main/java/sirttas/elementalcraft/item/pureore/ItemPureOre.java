package sirttas.elementalcraft.item.pureore;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import sirttas.elementalcraft.item.ItemEC;

public class ItemPureOre extends ItemEC {

	public static final String NAME = "pure_ore";

	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		ItemStack ore = PureOreHelper.getOre(stack);

		if (!ore.isEmpty()) {
			return new TranslationTextComponent("tooltip.elementalcraft.pure_ore", ore.getDisplayName());
		}
		return super.getDisplayName(stack);
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			PureOreHelper.PURE_ORE_MAP.keySet().stream().distinct().forEach(o -> items.add(PureOreHelper.createPureOre(o)));
		}
	}
}
