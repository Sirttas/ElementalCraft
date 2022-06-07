package sirttas.elementalcraft.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.particle.element.ElementCraftingParticle;
import sirttas.elementalcraft.particle.element.ElementFlowParticle;
import sirttas.elementalcraft.particle.element.ElementTypeParticleData;
import sirttas.elementalcraft.particle.element.source.SourceExhaustedParticle;
import sirttas.elementalcraft.particle.element.source.SourceParticle;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECParticles {

	public static final ParticleType<ElementTypeParticleData> SOURCE = ElementTypeParticleData.createParticleType(true);
	public static final ParticleType<ElementTypeParticleData> SOURCE_EXHAUSTED = ElementTypeParticleData.createParticleType(true);
	public static final ParticleType<ElementTypeParticleData> ELEMENT_FLOW = ElementTypeParticleData.createParticleType(false);
	public static final ParticleType<ElementTypeParticleData> ELEMENT_CRAFTING = ElementTypeParticleData.createParticleType(false);

	private ECParticles() {}
	
	@SubscribeEvent
	public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
		IForgeRegistry<ParticleType<?>> r = event.getRegistry();

		RegistryHelper.register(r, SOURCE, "source");
		RegistryHelper.register(r, SOURCE_EXHAUSTED, "source_exhausted");
		RegistryHelper.register(r, ELEMENT_FLOW, "element_flow");
		RegistryHelper.register(r, ELEMENT_CRAFTING, "elementcrafting");
	}


	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerFactories(ParticleFactoryRegisterEvent evt) {
		registerFactory(SOURCE, SourceParticle.FACTORY);
		registerFactory(SOURCE_EXHAUSTED, SourceExhaustedParticle.FACTORY);
		registerFactory(ELEMENT_FLOW, ElementFlowParticle.FACTORY);
		registerFactory(ELEMENT_CRAFTING, ElementCraftingParticle.FACTORY);
	}

	@SuppressWarnings("resource")
	private static void registerFactory(ParticleType<ElementTypeParticleData> particleType, ParticleEngine.SpriteParticleRegistration<ElementTypeParticleData> factory) {
		Minecraft.getInstance().particleEngine.register(particleType, factory);
	}

}
