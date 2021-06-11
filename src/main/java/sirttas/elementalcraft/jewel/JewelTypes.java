package sirttas.elementalcraft.jewel;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JewelTypes {

	private JewelTypes() {}
	
	@SubscribeEvent
	public static void registerToolInfusionTypes(RegistryEvent.Register<JewelType<?>> event) {
		IForgeRegistry<JewelType<?>> registry = event.getRegistry();
		
		RegistryHelper.register(registry, new JewelType<>(AttributeJewel.CODEC), AttributeJewel.NAME);
	}
	
}

