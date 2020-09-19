package sirttas.elementalcraft.event;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.infusion.InfusionHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.spell.SpellHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public class EnchantmentHandler {

	/**
	 * code from
	 * {@link net.minecraft.inventory.container.RepairContainer#updateRepairOutput()}
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public static void onAnvilUpdate(AnvilUpdateEvent event) { // NOSONAR - vanilla code
		if (addSpell(event)) {
			return;
		}

		if (!InfusionHelper.hasInfusion(event.getLeft()) && !InfusionHelper.hasInfusion(event.getRight())) {
			return;
		}
		ElementType infusion = InfusionHelper.hasInfusion(event.getLeft()) ? InfusionHelper.getInfusion(event.getLeft()) : InfusionHelper.getInfusion(event.getRight());

		ItemStack left = event.getLeft().copy();
		InfusionHelper.unapplyInfusion(left);
		int i = 0;
		int j = 0;
		int k = 0;
		ItemStack result = left.copy();
		ItemStack right = event.getRight().copy();
		InfusionHelper.unapplyInfusion(right);
		Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(result);
		j = j + left.getRepairCost() + (right.isEmpty() ? 0 : right.getRepairCost());
		boolean flag = false;

		if (!right.isEmpty()) {
			flag = right.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantments(right).isEmpty();
			if (result.isDamageable() && result.getItem().getIsRepairable(left, right)) {
				int l2 = Math.min(result.getDamage(), result.getMaxDamage() / 4);
				if (l2 <= 0) {
					event.setOutput(ItemStack.EMPTY);
					event.setMaterialCost(0);
					return;
				}

				int i3;
				for (i3 = 0; l2 > 0 && i3 < right.getCount(); ++i3) {
					int j3 = result.getDamage() - l2;
					result.setDamage(j3);
					++i;
					l2 = Math.min(result.getDamage(), result.getMaxDamage() / 4);
				}

				event.setMaterialCost(i3);
			} else {
				if (!flag && (result.getItem() != right.getItem() || !result.isDamageable())) {
					event.setOutput(ItemStack.EMPTY);
					event.setMaterialCost(0);
					return;
				}

				if (result.isDamageable() && !flag) {
					int l = left.getMaxDamage() - left.getDamage();
					int i1 = right.getMaxDamage() - right.getDamage();
					int j1 = i1 + result.getMaxDamage() * 12 / 100;
					int k1 = l + j1;
					int l1 = result.getMaxDamage() - k1;
					if (l1 < 0) {
						l1 = 0;
					}

					if (l1 < result.getDamage()) {
						result.setDamage(l1);
						i += 2;
					}
				}

				Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(right);
				boolean flag2 = false;
				boolean flag3 = false;

				for (Enchantment enchantment1 : map1.keySet()) { // NOSONAR - vanilla code
					if (enchantment1 != null) {
						int i2 = map.containsKey(enchantment1) ? map.get(enchantment1) : 0;
						int j2 = map1.get(enchantment1);
						j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
						boolean flag1 = enchantment1.canApply(left);
						if (left.getItem() == Items.ENCHANTED_BOOK) {
							flag1 = true;
						}

						for (Enchantment enchantment : map.keySet()) {
							if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
								flag1 = false;
								++i;
							}
						}

						if (!flag1) {
							flag3 = true;
						} else {
							flag2 = true;
							if (j2 > enchantment1.getMaxLevel()) {
								j2 = enchantment1.getMaxLevel();
							}

							map.put(enchantment1, j2);
							int k3 = 0;
							switch (enchantment1.getRarity()) {
							case COMMON:
								k3 = 1;
								break;
							case UNCOMMON:
								k3 = 2;
								break;
							case RARE:
								k3 = 4;
								break;
							case VERY_RARE:
								k3 = 8;
							}

							if (flag) {
								k3 = Math.max(1, k3 / 2);
							}

							i += k3 * j2;
							if (left.getCount() > 1) {
								i = 40;
							}
						}
					}
				}

				if (flag3 && !flag2) {
					event.setOutput(ItemStack.EMPTY);
					event.setMaterialCost(0);
					return;
				}
			}
		}

		if (StringUtils.isBlank(event.getName())) {
			if (left.hasDisplayName()) {
				k = 1;
				i += k;
				result.clearCustomName();
			}
		} else if (!event.getName().equals(left.getDisplayName().getString())) {
			k = 1;
			i += k;
			result.setDisplayName(new StringTextComponent(event.getName()));
		}
		if (flag && !result.isBookEnchantable(right))
			result = ItemStack.EMPTY;

		event.setMaterialCost(j + i);
		if (i <= 0) {
			result = ItemStack.EMPTY;
		}

		if (k == i && k > 0 && event.getMaterialCost() >= 40) {
			event.setMaterialCost(39);
		}

		if (event.getMaterialCost() >= 40) {
			result = ItemStack.EMPTY;
		}

		if (!result.isEmpty()) {
			int k2 = result.getRepairCost();
			if (!right.isEmpty() && k2 < right.getRepairCost()) {
				k2 = right.getRepairCost();
			}

			if (k != i || k == 0) {
				k2 = k2 * 2 + 1;
			}

			result.setRepairCost(k2);
			EnchantmentHelper.setEnchantments(map, result);
			InfusionHelper.setInfusion(result, infusion);
			InfusionHelper.applyInfusion(result);
		}

		event.setOutput(result);
	}

	private static boolean addSpell(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();

		if (left.getItem() == ECItems.focus && right.getItem() == ECItems.scroll && SpellHelper.getSpellCount(left) < ECConfig.CONFIG.focusMaxSpell.get()) {
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
			return true;
		}
		return false;
	}
}
