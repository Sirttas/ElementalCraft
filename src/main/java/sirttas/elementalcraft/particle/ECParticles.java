package sirttas.elementalcraft.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.particle.element.ElementCraftingParticle;
import sirttas.elementalcraft.particle.element.ElementFlowParticle;
import sirttas.elementalcraft.particle.element.ElementTypeParticleData;
import sirttas.elementalcraft.particle.element.source.SourceParticle;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECParticles {

	private static final DeferredRegister<ParticleType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.PARTICLE_TYPE, ElementalCraftApi.MODID);

	public static final DeferredHolder<ParticleType<?>, ParticleType<ElementTypeParticleData>> SOURCE = register(() -> ElementTypeParticleData.createParticleType(true), "source");
	public static final DeferredHolder<ParticleType<?>, ParticleType<ElementTypeParticleData>> ELEMENT_FLOW = register(() -> ElementTypeParticleData.createParticleType(false), "element_flow");
	public static final DeferredHolder<ParticleType<?>, ParticleType<ElementTypeParticleData>> ELEMENT_CRAFTING = register(() -> ElementTypeParticleData.createParticleType(false), "elementcrafting");

	private ECParticles() {}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerFactories(RegisterParticleProvidersEvent evt) {
		evt.registerSpriteSet(SOURCE.get(), SourceParticle.FACTORY);
		evt.registerSpriteSet(ELEMENT_FLOW.get(), ElementFlowParticle.FACTORY);
		evt.registerSpriteSet(ELEMENT_CRAFTING.get(), ElementCraftingParticle.FACTORY);
	}

	private static <O extends ParticleOptions, T extends ParticleType<O>> DeferredHolder<ParticleType<?>, T> register(Supplier<T> type, String name) {
		return DEFERRED_REGISTER.register(name, type);
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}
}
