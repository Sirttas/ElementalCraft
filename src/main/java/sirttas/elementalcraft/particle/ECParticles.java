package sirttas.elementalcraft.particle;

import java.util.function.Function;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.Codec;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.registry.RegistryHelper;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECParticles {

	@SubscribeEvent
	public static void registerParticles(RegistryEvent.Register<ParticleType<?>> event) {
		IForgeRegistry<ParticleType<?>> r = event.getRegistry();

		register(r, ElementTypeParticleData.DESERIALIZER, ElementTypeParticleData::getCodec, ParticleSource.NAME);
		register(r, ElementTypeParticleData.DESERIALIZER, ElementTypeParticleData::getCodec, ParticleElementFlow.NAME);
	}

	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void registerFactories(ParticleFactoryRegisterEvent evt) {
		Minecraft.getInstance().particles.registerFactory(ParticleSource.TYPE, ParticleSource.Factory::new);
		Minecraft.getInstance().particles.registerFactory(ParticleElementFlow.TYPE, ParticleElementFlow.Factory::new);
	}


	static final IParticleRenderType EC_RENDER = new IParticleRenderType() {
		@Override
		public void beginRender(BufferBuilder buffer, TextureManager textureManager) {
			RenderSystem.depthMask(false);
			textureManager.bindTexture(AtlasTexture.LOCATION_PARTICLES_TEXTURE);
			RenderSystem.enableBlend();
			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			RenderSystem.alphaFunc(516, 0.003921569F);
			buffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		}

		@Override
		public void finishRender(Tessellator tessellator) {
			tessellator.draw();
			RenderSystem.depthMask(true);
		}

		@Override
		public String toString() {
			return "elementalcraft:renderer";
		}
	};

	private static <T extends IParticleData> void register(IForgeRegistry<ParticleType<?>> reg, IParticleData.IDeserializer<T> deserializer, final Function<ParticleType<T>, Codec<T>> function,
			String key) {
		RegistryHelper.register(reg, new ParticleType<T>(false, deserializer) {
			@Override
			public Codec<T> func_230522_e_() {
				return function.apply(this);
			}
		}, key);
	}
}
