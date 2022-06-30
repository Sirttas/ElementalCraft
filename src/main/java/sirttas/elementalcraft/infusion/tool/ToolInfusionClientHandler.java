package sirttas.elementalcraft.infusion.tool;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;

import java.util.List;
import java.util.stream.IntStream;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class ToolInfusionClientHandler {

	private ToolInfusionClientHandler() {}

	@SubscribeEvent
	public static void addInfusionTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		List<Component> tooltip = event.getToolTip();
		ToolInfusion infusion = ToolInfusionHelper.getInfusion(stack);

		if (infusion != null) {
			var index = IntStream.range(0, tooltip.size())
					.filter(i -> ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().equals(tooltip.get(i).getString()))
					.findFirst()
					.orElse(tooltip.size());

			tooltip.addAll(index, infusion.getTooltipInformation());
		}
	}
}
