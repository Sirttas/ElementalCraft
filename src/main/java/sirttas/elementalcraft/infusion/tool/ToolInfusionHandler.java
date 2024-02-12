package sirttas.elementalcraft.infusion.tool;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class ToolInfusionHandler {

	private ToolInfusionHandler() {}

	@SubscribeEvent
	public static void addInfusionAttributes(ItemAttributeModifierEvent event) {
		ToolInfusionHelper.getInfusionAttribute(event.getItemStack(), event.getSlotType()).forEach(event::addModifier);
	}

	@SubscribeEvent
	public static void addInfusionLevel(GetEnchantmentLevelEvent event) {
		var enchantments = event.getEnchantments();

		ToolInfusionHelper.getAllInfusionEnchantments(event.getStack()).forEach((enchantment, level) -> enchantments.merge(enchantment, level, Integer::sum));
	}
}
