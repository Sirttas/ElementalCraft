package sirttas.elementalcraft.item.pureore;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.ECItem;

public class PureOreItem extends ECItem {

	public static final String NAME = "pure_ore";

	@Override
	public Component getName(ItemStack stack) {
		ItemStack ore = ElementalCraft.PURE_ORE_MANAGER.getOre(stack);

		if (!ore.isEmpty()) {
			return new TranslatableComponent("tooltip.elementalcraft.pure_ore", ore.getHoverName());
		}
		return super.getName(stack);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			ElementalCraft.PURE_ORE_MANAGER.getOres().forEach(o -> items.add(ElementalCraft.PURE_ORE_MANAGER.createPureOre(o)));
		}
	}
}
