package sirttas.elementalcraft.infusion.tool;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import sirttas.dpanvil.api.DataPackAnvilApi;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.infusion.tool.effect.AttributeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.AutoSmeltToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.DodgeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.ElementCostReductionToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.EnchantmentToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.FastDrawToolInfusionEffect;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ToolInfusionHelper {

	private static final Holder<ToolInfusion> NONE = ElementalCraftApi.TOOL_INFUSION_MANAGER.getOrCreateHolder(DataPackAnvilApi.ID_NONE);

	private ToolInfusionHelper() {}

	@Nonnull
	public static Holder<ToolInfusion> getInfusion(@Nonnull ItemStack stack) {
		if (stack.isEmpty()) {
			return NONE;
		}
		var infusion = stack.get(ECDataComponents.TOOL_INFUSION);

		return infusion != null ? infusion : NONE;
	}
	
	public static void setInfusion(@Nonnull ItemStack stack, @Nonnull Holder<ToolInfusion> infusion) {
		if (stack.isEmpty()) {
			return;
		} else if (NONE.is(infusion)) {
			stack.remove(ECDataComponents.TOOL_INFUSION);
			return;
		}
		stack.set(ECDataComponents.TOOL_INFUSION, infusion);
	}

	public static void removeInfusion(@Nonnull ItemStack stack) {
		if (stack.isEmpty()) {
			return;
		}
		stack.remove(ECDataComponents.TOOL_INFUSION);
	}

	public static boolean hasAutoSmelt(ItemStack stack) {
		return getInfusionEffects(stack, AutoSmeltToolInfusionEffect.class).findAny().isPresent();
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
		if (entity instanceof LivingEntity livingEntity) {
			return StreamSupport.stream(livingEntity.getAllSlots().spliterator(), false)
					.map(s -> getInfusion(s).value());
		}
		return Stream.empty();
	}
	
	private static Stream<IToolInfusionEffect> getInfusionEffects(Entity entity) {
		return getInfusions(entity).flatMap(t -> t.getEffects().stream());
	}
	
	private static <T extends IToolInfusionEffect> Stream<T> getInfusionEffects(ItemStack stack, Class<T> type) {
		return getInfusion(stack).value().getEffects().stream().mapMulti(ElementalCraftUtils.cast(type));
	}

	public static Map<Holder<Enchantment>, Integer> getAllInfusionEnchantments(ItemStack stack) {
		return getInfusionEffects(stack, EnchantmentToolInfusionEffect.class)
				.collect(Collectors.toMap(EnchantmentToolInfusionEffect::getEnchantment, EnchantmentToolInfusionEffect::getLevel, Integer::sum));
	}
	
	public static List<AttributeToolInfusionEffect> getInfusionAttribute(ItemStack stack) {
		return getInfusionEffects(stack, AttributeToolInfusionEffect.class).toList();
	}
	
	public static float getElementCostReduction(Entity entity) {
		return (float) getInfusionEffects(entity)
				.mapMulti(ElementalCraftUtils.cast(ElementCostReductionToolInfusionEffect.class))
				.mapToDouble(infusion -> 1D - infusion.value())
				.reduce(1D, (a, b) -> a * b);
	}
}
