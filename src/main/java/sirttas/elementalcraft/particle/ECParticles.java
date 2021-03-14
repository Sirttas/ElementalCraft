package sirttas.elementalcraft.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.particle.element.ElementTypeParticleData;
import sirttas.elementalcraft.particle.element.ParticleElementCrafting;
import sirttas.elementalcraft.particle.element.ParticleElementFlow;
import sirttas.elementalcraft.particle.element.source.ParticleSource;
import sirttas.elementalcraft.particle.element.source.ParticleSourceExhausted;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECParticles {

	public static final ParticleType<ElementTypeParticleData> SOURCE = ElementTypeParticleData.createParticeLtype(true);
	public static final ParticleType<ElementTypeParticleData> SOURCE_EXHAUSTED = ElementTypeParticleData.createParticeLtype(true);
	public static final ParticleType<ElementTypeParticleData> ELEMENT_FLOW = ElementTypeParticleData.createParticeLtype(false);
	public static final ParticleType<ElementTypeParticleData> ELEMENT_CRAFTING = ElementTypeParticleData.createParticeLtype(false);

	private ECParticles() {}
	
	@SubscribeEvent
	public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
		IForgeRegistry<ParticleType<?>> r = event.getRegistry();

		RegistryHelper.register(r, SOURCE, "source");
		RegistryHelper.register(r, SOURCE_EXHAUSTED, "source_exhausted");
		RegistryHelper.register(r, ELEMENT_FLOW, "elementflow");
		RegistryHelper.register(r, ELEMENT_CRAFTING, "elementcrafting");
	}

	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	public static void registerFactories(ParticleFactoryRegisterEvent evt) {
		Minecraft.getInstance().particles.registerFactory(SOURCE, ParticleSource.Factory::new);
		Minecraft.getInstance().particles.registerFactory(SOURCE_EXHAUSTED, ParticleSourceExhausted.Factory::new);
		Minecraft.getInstance().particles.registerFactory(ELEMENT_FLOW, ParticleElementFlow.Factory::new);
		Minecraft.getInstance().particles.registerFactory(ELEMENT_CRAFTING, ParticleElementCrafting.Factory::new);
	}

}
