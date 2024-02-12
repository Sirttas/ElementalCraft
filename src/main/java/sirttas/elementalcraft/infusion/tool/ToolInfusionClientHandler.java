package sirttas.elementalcraft.infusion.tool;

import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;

import java.util.stream.IntStream;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class ToolInfusionClientHandler {

	private ToolInfusionClientHandler() {}

	@SubscribeEvent
	public static void addInfusionTooltip(ItemTooltipEvent event) {
		var stack = event.getItemStack();
		var infusion = ToolInfusionHelper.getInfusion(stack);

		if (infusion == ToolInfusion.NONE) {
			return;
		}

		var tooltip = event.getToolTip();
		var index = IntStream.range(0, tooltip.size())
				.filter(i -> BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().equals(tooltip.get(i).getString()))
				.findFirst()
				.orElse(tooltip.size());

		tooltip.addAll(index, infusion.getTooltipInformation());
	}
}
