package sirttas.elementalcraft.infusion.tool;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;

import java.util.stream.IntStream;

@EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class ToolInfusionHandler {

	private ToolInfusionHandler() {}

	@SubscribeEvent
	public static void addInfusionAttributes(ItemAttributeModifierEvent event) {
		ToolInfusionHelper.getInfusionAttribute(event.getItemStack()).forEach(i -> event.addModifier(i.attribute(), i.modifier(), i.slotGroup()));
	}

	@SubscribeEvent
	public static void addInfusionLevel(GetEnchantmentLevelEvent event) {
		var enchantments = event.getEnchantments();

		ToolInfusionHelper.getAllInfusionEnchantments(event.getStack()).forEach((enchantment, level) -> enchantments.set(enchantment, enchantments.getLevel(enchantment) + level));
	}

	@SubscribeEvent
	public static void addInfusionTooltip(ItemTooltipEvent event) {
		var stack = event.getItemStack();
		var infusion = ToolInfusionHelper.getInfusion(stack);

		if (infusion.value() == ToolInfusion.NONE) {
			return;
		}

		var tooltip = event.getToolTip();
		var index = IntStream.range(0, tooltip.size())
				.filter(i -> BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().equals(tooltip.get(i).getString()))
				.findFirst()
				.orElse(tooltip.size());

		tooltip.addAll(index, infusion.value().getTooltipInformation());
	}
}
