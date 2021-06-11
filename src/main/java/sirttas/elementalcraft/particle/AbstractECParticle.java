package sirttas.elementalcraft.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GraphicsFanciness;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractECParticle extends SpriteTexturedParticle {

	protected final double coordX;
	protected final double coordY;
	protected final double coordZ;

	@SuppressWarnings("deprecation")
	static final IParticleRenderType EC_RENDER = new IParticleRenderType() {
		@Override
		public void begin(BufferBuilder buffer, TextureManager textureManager) {
			RenderSystem.depthMask(false);
			textureManager.bind(AtlasTexture.LOCATION_PARTICLES);
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			RenderSystem.alphaFunc(516, 0.003921569F);
			buffer.begin(7, DefaultVertexFormats.PARTICLE);
		}

		@Override
		public void end(Tessellator tessellator) {
			tessellator.end();
			RenderSystem.depthMask(true);
		}

		@Override
		public String toString() {
			return "elementalcraft:renderer";
		}
	};
	
	protected AbstractECParticle(ClientWorld world, Vector3d coord) {
		super(world, coord.x(), coord.y(), coord.z());
		this.coordX = coord.x();
		this.coordY = coord.y();
		this.coordZ = coord.z();
	}

	protected boolean checkLife() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
			return false;
		}
		return true;
	}

	@SuppressWarnings("resource")
	@Override
	@OnlyIn(Dist.CLIENT)
	public IParticleRenderType getRenderType() {
		return Minecraft.getInstance().options.graphicsMode == GraphicsFanciness.FAST ? IParticleRenderType.PARTICLE_SHEET_OPAQUE : EC_RENDER;
	}

	@Override
	public void move(double x, double y, double z) {
		this.setBoundingBox(this.getBoundingBox().move(x, y, z));
		this.setLocationFromBoundingbox();
	}

	@Override
	public int getLightColor(float partialTick) {
		int i = super.getLightColor(partialTick);
		float f = (float) this.age / (float) this.lifetime;
		f = f * f;
		f = f * f;
		int j = i & 255;
		int k = i >> 16 & 255;
		k = k + (int) (f * 15.0F * 16.0F);
		if (k > 240) {
			k = 240;
		}
	
		return j | k << 16;
	}

}