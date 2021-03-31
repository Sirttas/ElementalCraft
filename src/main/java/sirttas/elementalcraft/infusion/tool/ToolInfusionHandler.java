package sirttas.elementalcraft.infusion.tool;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraft.MODID)
public class ToolInfusionHandler {

	private ToolInfusionHandler() {}
	
	@SubscribeEvent
	public static void addInfusionTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		List<ITextComponent> tooltip = event.getToolTip();
		ToolInfusion infusion = ToolInfusionHelper.getInfusion(stack);

		if (infusion != null) {
			OptionalInt indexOpt = IntStream.range(0, tooltip.size()).filter(i -> stack.getItem().getRegistryName().toString().equals(tooltip.get(i).getString())).findFirst();

			tooltip.addAll(indexOpt.isPresent() ? indexOpt.getAsInt() : tooltip.size(), infusion.getTooltipInformation());
		}
	}
}
