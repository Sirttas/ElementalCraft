package sirttas.elementalcraft.infusion.tool.effect;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ToolInfusionEffectTypes {

	private ToolInfusionEffectTypes() {}
	
	@SubscribeEvent
	public static void registerToolInfusionTypes(RegistryEvent.Register<ToolInfusionEffectType<?>> event) {
		IForgeRegistry<ToolInfusionEffectType<?>> registry = event.getRegistry();
		
		RegistryHelper.register(registry, new ToolInfusionEffectType<>(EnchantmentToolInfusionEffect.CODEC), EnchantmentToolInfusionEffect.NAME);
		RegistryHelper.register(registry, new ToolInfusionEffectType<>(AttributeToolInfusionEffect.CODEC), AttributeToolInfusionEffect.NAME);
		RegistryHelper.register(registry, new ToolInfusionEffectType<>(AutoSmeltToolInfusionEffect.CODEC), AutoSmeltToolInfusionEffect.NAME);
		RegistryHelper.register(registry, new ToolInfusionEffectType<>(DodgeToolInfusionEffect.CODEC), DodgeToolInfusionEffect.NAME);
		RegistryHelper.register(registry, new ToolInfusionEffectType<>(FastDrawToolInfusionEffect.CODEC), FastDrawToolInfusionEffect.NAME);
		RegistryHelper.register(registry, new ToolInfusionEffectType<>(ElementCostReductionToolInfusionEffect.CODEC), ElementCostReductionToolInfusionEffect.NAME);
	}
	
}
