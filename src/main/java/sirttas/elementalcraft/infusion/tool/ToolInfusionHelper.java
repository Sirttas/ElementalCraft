package sirttas.elementalcraft.infusion.tool;

import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.infusion.tool.effect.AttributeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.AutoSmeltToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.DodgeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.EnchantmentToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.FastDrawToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.nbt.NBTHelper;

public class ToolInfusionHelper {

	private ToolInfusionHelper() {}
	
	public static ToolInfusion getInfusion(ItemStack stack) {
		CompoundNBT nbt = NBTHelper.getECTag(stack);

		if (nbt != null) {
			if (nbt.contains(ECNames.INFUSION, 8)) {
				return ElementalCraft.TOOL_INFUSION_MANAGER.get(new ResourceLocation(nbt.getString(ECNames.INFUSION)));
			} else if (nbt.contains(ECNames.INFUSION, 10)) { // TODO 1.17 remove
				nbt.remove(ECNames.INFUSION);
			}
		}
		return null;
	}
	
	public static void setInfusion(ItemStack stack, ToolInfusion infusion) {
		CompoundNBT nbt = NBTHelper.getOrCreateECTag(stack);
		
		nbt.putString(ECNames.INFUSION, infusion.getId().toString());
	}

	public static void removeInfusion(ItemStack stack) {
		CompoundNBT nbt = NBTHelper.getECTag(stack);

		if (!stack.isEmpty() && nbt != null && nbt.contains(ECNames.INFUSION)) {
			nbt.remove(ECNames.INFUSION);
		}
	}

	public static boolean hasAutoSmelt(ItemStack stack) {
		return getInfusionEffects(stack, AutoSmeltToolInfusionEffect.class).count() > 0;
	}
	
	public static int getFasterDraw(ItemStack stack) {
		return getInfusionEffects(stack, FastDrawToolInfusionEffect.class).findAny().map(FastDrawToolInfusionEffect::getValue).orElse(-1);
	}
	
	public static boolean hasFireInfusion(LivingEntity entity) {
		return getInfusions(entity)
				.anyMatch(infusion -> infusion.getElementType() == ElementType.FIRE);
	}
	
	public static double getDodge(LivingEntity entity) {
		return getInfusionEffects(entity)
				.filter(DodgeToolInfusionEffect.class::isInstance)
				.map(DodgeToolInfusionEffect.class::cast)
				.mapToDouble(infusion -> 1D - infusion.getValue())
				.reduce(1D, (a, b) -> a * b);
	}

	private static Stream<ToolInfusion> getInfusions(LivingEntity entity) {
		return StreamSupport.stream(entity.getEquipmentAndArmor().spliterator(), false)
				.map(ToolInfusionHelper::getInfusion)
				.filter(Objects::nonNull);
	}
	
	private static Stream<IToolInfusionEffect> getInfusionEffects(LivingEntity entity) {
		return getInfusions(entity).flatMap(t -> t.getEffects().stream());
	}
	
	private static <T extends IToolInfusionEffect> Stream<T> getInfusionEffects(ItemStack stack, Class<T> type) {
		ToolInfusion infusion = getInfusion(stack);
		
		return infusion != null ? infusion.getEffects().stream().filter(type::isInstance).map(type::cast) : Stream.empty();
	}
	
	public static int getInfusionEnchantmentLevel(ItemStack stack, Enchantment ench) {
		return getInfusionEffects(stack, EnchantmentToolInfusionEffect.class)
				.filter(i -> i.getEnchantment() == ench)
				.mapToInt(EnchantmentToolInfusionEffect::getLevel)
				.sum();
	}
	
	public static Multimap<Attribute, AttributeModifier> getInfusionAttribute(ItemStack stack, EquipmentSlotType slot) {
		Multimap<Attribute, AttributeModifier> map = ArrayListMultimap.create();
		
		getInfusionEffects(stack, AttributeToolInfusionEffect.class)
				.filter(i -> i.getSlots().contains(slot))
				.forEach(i -> map.put(i.getAttribute(), i.getModifier()));
		return map;
	}
}
