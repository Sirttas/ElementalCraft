package sirttas.elementalcraft.block.entity.renderer;

import org.lwjgl.opengl.GL14;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import sirttas.elementalcraft.ElementalCraft;

public class ECRenderTypes {

	private static final String GHOST_NAME = ElementalCraft.createRL("ghost").toString();
	
	@SuppressWarnings("deprecation") 
	public static final RenderType GHOST = RenderType.create(GHOST_NAME, DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false,
			RenderType.CompositeState.builder()
					.setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEntityShadowShader))
					.setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
					.setTransparencyState(new RenderStateShard.TransparencyStateShard(GHOST_NAME,
						() -> {
							RenderSystem.enableBlend();
							RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA.value);
							GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.75F);
						},
						() -> {
							GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
							RenderSystem.disableBlend();
							RenderSystem.defaultBlendFunc();
						}))
					.createCompositeState(false));
	
	private ECRenderTypes() {}
}
