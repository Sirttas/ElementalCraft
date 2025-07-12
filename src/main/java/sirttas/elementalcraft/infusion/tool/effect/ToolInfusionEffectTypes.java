package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;
import sirttas.elementalcraft.api.registry.ElementalCraftRegistries;

public class ToolInfusionEffectTypes {

	private static final DeferredRegister<ToolInfusionEffectType<?>> DEFERRED_REGISTER = DeferredRegister.create(ElementalCraftRegistries.Keys.TOOL_INFUSION_EFFECT_TYPE, ElementalCraftApi.MODID);

	public static final DeferredHolder<ToolInfusionEffectType<?>, ToolInfusionEffectType<EnchantmentToolInfusionEffect>> ENCHANTMENT = register(EnchantmentToolInfusionEffect.NAME, EnchantmentToolInfusionEffect.CODEC);
	public static final DeferredHolder<ToolInfusionEffectType<?>, ToolInfusionEffectType<AttributeToolInfusionEffect>> ATTRIBUTE = register(AttributeToolInfusionEffect.NAME, AttributeToolInfusionEffect.CODEC);
	public static final DeferredHolder<ToolInfusionEffectType<?>, ToolInfusionEffectType<AutoSmeltToolInfusionEffect>> AUTO_SMELT = register(AutoSmeltToolInfusionEffect.NAME, AutoSmeltToolInfusionEffect.CODEC);
	public static final DeferredHolder<ToolInfusionEffectType<?>, ToolInfusionEffectType<DodgeToolInfusionEffect>> DODGE = register(DodgeToolInfusionEffect.NAME, DodgeToolInfusionEffect.CODEC);
	public static final DeferredHolder<ToolInfusionEffectType<?>, ToolInfusionEffectType<FastDrawToolInfusionEffect>> FAST_DRAW = register(FastDrawToolInfusionEffect.NAME, FastDrawToolInfusionEffect.CODEC);
	public static final DeferredHolder<ToolInfusionEffectType<?>, ToolInfusionEffectType<ElementCostReductionToolInfusionEffect>> ELEMENT_COST_REDUCTION = register(ElementCostReductionToolInfusionEffect.NAME, ElementCostReductionToolInfusionEffect.CODEC);

	private ToolInfusionEffectTypes() {}


	private static <T extends IToolInfusionEffect> DeferredHolder<ToolInfusionEffectType<?>, ToolInfusionEffectType<T>> register(String name, MapCodec<T> codec) {
		return DEFERRED_REGISTER.register(name, () -> new ToolInfusionEffectType<>(codec));
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
	
}
