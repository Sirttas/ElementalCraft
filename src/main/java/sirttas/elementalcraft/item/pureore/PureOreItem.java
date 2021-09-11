package sirttas.elementalcraft.item.pureore;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.item.ECItem;

public class PureOreItem extends ECItem {

	public static final String NAME = "pure_ore";

	@Override
	public Component getName(ItemStack stack) {
		var name = ElementalCraft.PURE_ORE_MANAGER.getPureOreName(stack);

		if (name != null) {
			return new TranslatableComponent("tooltip.elementalcraft.pure_ore", name);
		}
		return super.getName(stack);
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			ElementalCraft.PURE_ORE_MANAGER.getOres().forEach(id -> items.add(ElementalCraft.PURE_ORE_MANAGER.createPureOre(id)));
		}
	}
}
