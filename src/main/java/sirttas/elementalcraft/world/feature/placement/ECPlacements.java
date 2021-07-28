package sirttas.elementalcraft.world.feature.placement;

import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECPlacements {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SourcePlacement.NAME) public static final SourcePlacement SOURCE = null;

	private ECPlacements() {}
	
	@SubscribeEvent
	public static void registerPlacements(RegistryEvent.Register<FeatureDecorator<?>> event) {
		IForgeRegistry<FeatureDecorator<?>> r = event.getRegistry();

		RegistryHelper.register(r, new SourcePlacement(), SourcePlacement.NAME);
	}

}
