package sirttas.elementalcraft.api.clinet.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.lwjgl.opengl.GL14;

public class ECRenderTypes extends RenderType {

	private static final String GHOST_NAME = "elementalcraft:ghost";
	
	@SuppressWarnings("deprecation") 
	public static final RenderType GHOST = create(GHOST_NAME, DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false,
			CompositeState.builder()
					.setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEntityShadowShader))
					.setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
					.setTransparencyState(new RenderStateShard.TransparencyStateShard(GHOST_NAME,
						() -> {
							RenderSystem.enableBlend();
							RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA.value);
							GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.5F);
						},
						() -> {
							GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
							RenderSystem.disableBlend();
							RenderSystem.defaultBlendFunc();
						}))
					.createCompositeState(false));

	private ECRenderTypes(String name, VertexFormat fmt, VertexFormat.Mode glMode, int size, boolean doCrumbling, boolean depthSorting, Runnable onEnable, Runnable onDisable) {
		super(name, fmt, glMode, size, doCrumbling, depthSorting, onEnable, onDisable);
		throw new IllegalStateException("This class must not be instantiated");
	}
}
