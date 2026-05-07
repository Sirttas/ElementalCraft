package sirttas.elementalcraft.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.jewel.JewelItem;
import sirttas.elementalcraft.jewel.JewelHelper;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.SpellList;
import sirttas.elementalcraft.tag.ECTags;

@EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class AnvilEnchantmentHandler {

	private AnvilEnchantmentHandler() {}
	
	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		var left = event.getLeft();
		var right = event.getRight();

		var spellList = SpellHelper.getSpellList(left);

		if (left.is(ECTags.Items.SPELL_CAST_TOOLS) && right.is(ECItems.SCROLL) && !spellList.isFull()) {
			applySpell(event, spellList, left, right);
		} else if (left.is(ECTags.Items.JEWEL_SOCKETABLES) && right.getItem() instanceof JewelItem jewelItem && JewelHelper.getJewel(left) == null) {
			applyJewel(event, jewelItem, left);
		}
	}

	private static void applySpell(AnvilUpdateEvent event, SpellList spellList, ItemStack left, ItemStack right) {
		var result = left.copy();
		var n = 4 * (spellList.count() + 1);

		if (applyRename(left, result, event.getName())) {
			n++;
		}
		SpellHelper.addSpell(result, SpellHelper.getSpell(right));
		event.setXpCost(n);
		event.setOutput(result);
	}

	private static void applyJewel(AnvilUpdateEvent event, JewelItem jewelItem, ItemStack left) {
		var result = left.copy();
		var n = 10;

		if (applyRename(left, result, event.getName())) {
			n++;
		}
		JewelHelper.setJewel(result, jewelItem.getJewel());
		event.setXpCost(n);
		event.setOutput(result);
	}

	private static boolean applyRename(ItemStack input, ItemStack result, String name) {
		if (StringUtils.isBlank(name)) {
			if (input.has(DataComponents.CUSTOM_NAME)) {
				result.remove(DataComponents.CUSTOM_NAME);
				return true;
			}
		} else if (!name.equals(input.getHoverName().getString())) {
			result.set(DataComponents.CUSTOM_NAME, Component.literal(name));
			return true;
		}
		return false;
	}
}
