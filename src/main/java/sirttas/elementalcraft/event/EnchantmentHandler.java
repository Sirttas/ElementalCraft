package sirttas.elementalcraft.event;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.spell.SpellHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public class EnchantmentHandler {

	private EnchantmentHandler() {}
	
	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();

		if (left.getItem() == ECItems.FOCUS && right.getItem() == ECItems.SCROLL && SpellHelper.getSpellCount(left) < ECConfig.COMMON.focusMaxSpell.get()) {
			ItemStack result = left.copy();
			ListNBT list = SpellHelper.getSpellList(left);
			int n = 4 * (list != null ? list.size() + 1 : 1);

			if (StringUtils.isBlank(event.getName())) {
				if (left.hasDisplayName()) {
					n++;
					result.clearCustomName();
				}
			} else if (!event.getName().equals(left.getDisplayName().getString())) {
				n++;
				result.setDisplayName(new StringTextComponent(event.getName()));
			}
			SpellHelper.addSpell(result, SpellHelper.getSpell(right));
			event.setCost(n);
			event.setOutput(result);
		}
	}
}
