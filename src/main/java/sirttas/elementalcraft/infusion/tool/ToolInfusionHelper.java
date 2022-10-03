package sirttas.elementalcraft.infusion.tool;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.infusion.tool.effect.AttributeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.AutoSmeltToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.DodgeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.ElementCostReductionToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.EnchantmentToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.FastDrawToolInfusionEffect;
import sirttas.elementalcraft.nbt.NBTHelper;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ToolInfusionHelper {

	private ToolInfusionHelper() {}
	
	public static ToolInfusion getInfusion(ItemStack stack) {
		if (stack.isEmpty()) {
			return null;
		}

		CompoundTag nbt = NBTHelper.getECTag(stack);

		if (nbt != null && nbt.contains(ECNames.INFUSION, 8)) {
			return ElementalCraftApi.TOOL_INFUSION_MANAGER.get(new ResourceLocation(nbt.getString(ECNames.INFUSION)));
		}
		return null;
	}
	
	public static void setInfusion(ItemStack stack, ToolInfusion infusion) {
		if (stack.isEmpty()) {
			return;
		}

		CompoundTag nbt = NBTHelper.getOrCreateECTag(stack);
		
		nbt.putString(ECNames.INFUSION, infusion.getId().toString());
	}

	public static void removeInfusion(ItemStack stack) {
		if (stack.isEmpty()) {
			return;
		}

		CompoundTag nbt = NBTHelper.getECTag(stack);

		if (!stack.isEmpty() && nbt != null && nbt.contains(ECNames.INFUSION)) {
			nbt.remove(ECNames.INFUSION);
		}
	}

	public static boolean hasAutoSmelt(ItemStack stack) {
		return getInfusionEffects(stack, AutoSmeltToolInfusionEffect.class).count() > 0;
	}
	
	public static int getFasterDraw(ItemStack stack) {
		return getInfusionEffects(stack, FastDrawToolInfusionEffect.class).findAny().map(FastDrawToolInfusionEffect::value).orElse(-1);
	}
	
	public static boolean hasFireInfusion(Entity entity) {
		return getInfusions(entity)
				.anyMatch(infusion -> infusion.getElementType() == ElementType.FIRE);
	}
	
	public static double getDodge(Entity entity) {
		return getInfusionEffects(entity)
				.mapMulti(ElementalCraftUtils.cast(DodgeToolInfusionEffect.class))
				.mapToDouble(infusion -> 1D - infusion.value())
				.reduce(1D, (a, b) -> a * b);
	}

	private static Stream<ToolInfusion> getInfusions(Entity entity) {
		return StreamSupport.stream(entity.getAllSlots().spliterator(), false)
				.map(ToolInfusionHelper::getInfusion)
				.filter(Objects::nonNull);
	}
	
	private static Stream<IToolInfusionEffect> getInfusionEffects(Entity entity) {
		return getInfusions(entity).flatMap(t -> t.getEffects().stream());
	}
	
	private static <T extends IToolInfusionEffect> Stream<T> getInfusionEffects(ItemStack stack, Class<T> type) {
		ToolInfusion infusion = getInfusion(stack);
		
		return infusion != null ? infusion.getEffects().stream().mapMulti(ElementalCraftUtils.cast(type)) : Stream.empty();
	}
	
	public static int getInfusionEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
		return getInfusionEffects(stack, EnchantmentToolInfusionEffect.class)
				.filter(i -> i.getEnchantment() == enchantment)
				.mapToInt(EnchantmentToolInfusionEffect::getLevel)
				.sum();
	}

	public static Map<Enchantment, Integer> getAllInfusionEnchantments(ItemStack stack) {
		return getInfusionEffects(stack, EnchantmentToolInfusionEffect.class)
				.collect(Collectors.toMap(EnchantmentToolInfusionEffect::getEnchantment, EnchantmentToolInfusionEffect::getLevel, Integer::sum));
	}
	
	public static Multimap<Attribute, AttributeModifier> getInfusionAttribute(ItemStack stack, EquipmentSlot slot) {
		Multimap<Attribute, AttributeModifier> map = ArrayListMultimap.create();
		
		getInfusionEffects(stack, AttributeToolInfusionEffect.class)
				.filter(i -> i.slots().contains(slot))
				.forEach(i -> map.put(i.attribute(), i.modifier()));
		return map;
	}
	
	public static float getElementCostReduction(Entity entity) {
		return (float) getInfusionEffects(entity)
				.mapMulti(ElementalCraftUtils.cast(ElementCostReductionToolInfusionEffect.class))
				.mapToDouble(infusion -> 1D - infusion.value())
				.reduce(1D, (a, b) -> a * b);
	}
}
