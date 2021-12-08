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

import java.util.OptionalDouble;

public class ECRenderTypes extends RenderType {

	private static final String GHOST_NAME = "elementalcraft:ghost";
	private static final String PIPE_DEBUG_LINE_NAME = "elementalcraft:pipe_debug_line";
	
	@SuppressWarnings("deprecation") 
	public static final RenderType GHOST = create(GHOST_NAME, DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false,
			CompositeState.builder()
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

	public static final RenderType PIPE_DEBUG_LINE = create(PIPE_DEBUG_LINE_NAME, DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 256, false, false,
			CompositeState.builder().setShaderState(RENDERTYPE_LINES_SHADER)
					.setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
					.setLayeringState(VIEW_OFFSET_Z_LAYERING)
					.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
					.setOutputState(ITEM_ENTITY_TARGET)
					.setWriteMaskState(COLOR_DEPTH_WRITE)
					.setCullState(NO_CULL)
					.setDepthTestState(NO_DEPTH_TEST)
					.createCompositeState(false));

	private ECRenderTypes(String name, VertexFormat fmt, VertexFormat.Mode glMode, int size, boolean doCrumbling, boolean depthSorting, Runnable onEnable, Runnable onDisable) {
		super(name, fmt, glMode, size, doCrumbling, depthSorting, onEnable, onDisable);
		throw new IllegalStateException("This class must not be instantiated");
	}
}
