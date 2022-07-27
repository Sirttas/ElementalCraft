package sirttas.elementalcraft.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.particle.element.ElementCraftingParticle;
import sirttas.elementalcraft.particle.element.ElementFlowParticle;
import sirttas.elementalcraft.particle.element.ElementTypeParticleData;
import sirttas.elementalcraft.particle.element.source.SourceParticle;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECParticles {

	private static final DeferredRegister<ParticleType<?>> DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ElementalCraftApi.MODID);

	public static final RegistryObject<ParticleType<ElementTypeParticleData>> SOURCE = register(() -> ElementTypeParticleData.createParticleType(true), "source");
	public static final RegistryObject<ParticleType<ElementTypeParticleData>> ELEMENT_FLOW = register(() -> ElementTypeParticleData.createParticleType(false), "element_flow");
	public static final RegistryObject<ParticleType<ElementTypeParticleData>> ELEMENT_CRAFTING = register(() -> ElementTypeParticleData.createParticleType(false), "elementcrafting");

	private ECParticles() {}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerFactories(RegisterParticleProvidersEvent evt) {
		registerFactory(SOURCE.get(), SourceParticle.FACTORY);
		registerFactory(ELEMENT_FLOW.get(), ElementFlowParticle.FACTORY);
		registerFactory(ELEMENT_CRAFTING.get(), ElementCraftingParticle.FACTORY);
	}

	@SuppressWarnings("resource")
	private static void registerFactory(ParticleType<ElementTypeParticleData> particleType, ParticleEngine.SpriteParticleRegistration<ElementTypeParticleData> factory) {
		Minecraft.getInstance().particleEngine.register(particleType, factory);
	}

	private static <O extends ParticleOptions, T extends ParticleType<O>> RegistryObject<T> register(Supplier<T> type, String name) {
		return DEFERRED_REGISTER.register(name, type);
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
