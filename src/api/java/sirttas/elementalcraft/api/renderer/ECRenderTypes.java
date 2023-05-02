package sirttas.elementalcraft.api.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL14;

import java.util.function.Function;

public class ECRenderTypes extends RenderType {

	private static final String GHOST_NAME = "elementalcraft:ghost";
	private static final String SOURCE_NAME = "elementalcraft:source";

	@SuppressWarnings("deprecation")
	public static final RenderType GHOST = create(GHOST_NAME, DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, false,
			CompositeState.builder()
					.setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEntityShadowShader))
					.setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
					.setTransparencyState(new RenderStateShard.TransparencyStateShard(GHOST_NAME,
						() -> {
							RenderSystem.enableBlend();
							RenderSystem.blendFunc(GlStateManager.SourceFactor.CONSTANT_ALPHA, GlStateManager.DestFactor.ONE_MINUS_CONSTANT_ALPHA);
							GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.5F);
						},
						() -> {
							GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
							RenderSystem.disableBlend();
							RenderSystem.defaultBlendFunc();
						}))
					.createCompositeState(false));

	protected static final RenderStateShard.TransparencyStateShard SOURCE_TRANSPARENCY = new RenderStateShard.TransparencyStateShard(SOURCE_NAME,
			() -> {
				RenderSystem.enableDepthTest();
				RenderSystem.depthMask(false);
				RenderSystem.enableBlend();
				RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
			}, () -> {
				RenderSystem.disableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.disableDepthTest();
				RenderSystem.depthMask(true);
			});

	private static final Function<ResourceLocation, RenderType> SOURCE = Util.memoize(location -> {
		var rendertype = RenderType.CompositeState.builder()
				.setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
				.setTextureState(new RenderStateShard.TextureStateShard(location, false, false))
				.setTransparencyState(SOURCE_TRANSPARENCY)
				.setCullState(NO_CULL)
				.setLightmapState(LIGHTMAP)
				.setOverlayState(OVERLAY)
				.createCompositeState(false);

		return create(SOURCE_NAME, DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype);
	});


	public static RenderType source(ResourceLocation location) {
		return SOURCE.apply(location);
	}

	private ECRenderTypes(String name, VertexFormat fmt, VertexFormat.Mode glMode, int size, boolean doCrumbling, boolean depthSorting, Runnable onEnable, Runnable onDisable) {
		super(name, fmt, glMode, size, doCrumbling, depthSorting, onEnable, onDisable);
		throw new IllegalStateException("This class must not be instantiated");
	}
}
