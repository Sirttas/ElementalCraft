package sirttas.elementalcraft.world.feature.placement;

import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECPlacements {

	@ObjectHolder(ElementalCraft.MODID + ":" + SourcePlacement.NAME) public static final SourcePlacement SOURCE = null;

	private ECPlacements() {}
	
	@SubscribeEvent
	public static void registerPlacements(RegistryEvent.Register<Placement<?>> event) {
		IForgeRegistry<Placement<?>> r = event.getRegistry();

		RegistryHelper.register(r, new SourcePlacement(), SourcePlacement.NAME);
	}

}
