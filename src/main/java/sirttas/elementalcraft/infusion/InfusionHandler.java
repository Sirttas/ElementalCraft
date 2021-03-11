package sirttas.elementalcraft.infusion;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraft.MODID)
public class InfusionHandler {

	private InfusionHandler() {}
	
	@SubscribeEvent
	public static void addInfusionTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		List<ITextComponent> tooltip = event.getToolTip();

		if (InfusionHelper.hasInfusion(stack)) {
			OptionalInt indexOpt = IntStream.range(0, tooltip.size()).filter(i -> stack.getItem().getRegistryName().toString().equals(tooltip.get(i).getString())).findFirst();

			tooltip.add(indexOpt.isPresent() ? indexOpt.getAsInt() : tooltip.size(),
					new TranslationTextComponent("tooltip.elementalcraft.infused", InfusionHelper.getInfusion(stack).getDisplayName()).mergeStyle(TextFormatting.YELLOW));
			if (!InfusionHelper.isApplied(stack)) {
				tooltip.add(indexOpt.isPresent() ? indexOpt.getAsInt() : tooltip.size(),
						new StringTextComponent("This item is infuse but the infusion is never applied, this should never had happened!!").mergeStyle(TextFormatting.RED));
			}
		}
	}
}
