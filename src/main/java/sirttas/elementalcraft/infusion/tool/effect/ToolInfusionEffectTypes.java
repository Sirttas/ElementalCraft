package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.effect.IToolInfusionEffect;
import sirttas.elementalcraft.api.infusion.tool.effect.ToolInfusionEffectType;

import java.util.function.Supplier;

public class ToolInfusionEffectTypes {

	private static final DeferredRegister<ToolInfusionEffectType<?>> DEFERRED_REGISTER = DeferredRegister.create(ElementalCraftApi.TOOL_INFUSION_EFFECT_TYPE_REGISTRY_KEY, ElementalCraftApi.MODID);
	public static final Supplier<IForgeRegistry<ToolInfusionEffectType<?>>> REGISTRY = DEFERRED_REGISTER.makeRegistry(RegistryBuilder::new);

	public static final RegistryObject<ToolInfusionEffectType<EnchantmentToolInfusionEffect>> ENCHANTMENT = register(EnchantmentToolInfusionEffect.CODEC, EnchantmentToolInfusionEffect.NAME);
	public static final RegistryObject<ToolInfusionEffectType<AttributeToolInfusionEffect>> ATTRIBUTE = register(AttributeToolInfusionEffect.CODEC, AttributeToolInfusionEffect.NAME);
	public static final RegistryObject<ToolInfusionEffectType<AutoSmeltToolInfusionEffect>> AUTO_SMELT = register(AutoSmeltToolInfusionEffect.CODEC, AutoSmeltToolInfusionEffect.NAME);
	public static final RegistryObject<ToolInfusionEffectType<DodgeToolInfusionEffect>> DODGE = register(DodgeToolInfusionEffect.CODEC, DodgeToolInfusionEffect.NAME);
	public static final RegistryObject<ToolInfusionEffectType<FastDrawToolInfusionEffect>> FAST_DRAW = register(FastDrawToolInfusionEffect.CODEC, FastDrawToolInfusionEffect.NAME);
	public static final RegistryObject<ToolInfusionEffectType<ElementCostReductionToolInfusionEffect>> ELEMENT_COST_REDUCTION = register(ElementCostReductionToolInfusionEffect.CODEC, ElementCostReductionToolInfusionEffect.NAME);

	private ToolInfusionEffectTypes() {}


	private static <T extends IToolInfusionEffect> RegistryObject<ToolInfusionEffectType<T>> register(Codec<T> codec, String name) {
		return DEFERRED_REGISTER.register(name, () -> new ToolInfusionEffectType<>(codec));
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
	
}
