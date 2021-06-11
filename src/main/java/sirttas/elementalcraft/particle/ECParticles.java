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
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.particle.element.ElementCraftingParticle;
import sirttas.elementalcraft.particle.element.ElementFlowParticle;
import sirttas.elementalcraft.particle.element.ElementTypeParticleData;
import sirttas.elementalcraft.particle.element.source.SourceExhaustedParticle;
import sirttas.elementalcraft.particle.element.source.SourceParticle;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
		Minecraft.getInstance().particleEngine.register(SOURCE, SourceParticle.FACTORY);
		Minecraft.getInstance().particleEngine.register(SOURCE_EXHAUSTED, SourceExhaustedParticle.FACTORY);
		Minecraft.getInstance().particleEngine.register(ELEMENT_FLOW, ElementFlowParticle.FACTORY);
		Minecraft.getInstance().particleEngine.register(ELEMENT_CRAFTING, ElementCraftingParticle.FACTORY);
	}

}
