package sirttas.elementalcraft.world.dimension;

import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.world.dimension.boss.BossDimension;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECDimensions {

	@ObjectHolder(ElementalCraft.MODID + ":" + BossDimension.NAME) public static ModDimension bossDimension;

	@SubscribeEvent
	public static void registerDimensions(RegistryEvent.Register<ModDimension> event) {
		IForgeRegistry<ModDimension> r = event.getRegistry();

		RegistryHelper.register(r, ModDimension.withFactory(BossDimension::new), BossDimension.NAME);
	}

}
