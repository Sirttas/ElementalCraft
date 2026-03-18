package sirttas.elementalcraft.renderer;

import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.function.Function;

@EventBusSubscriber(modid = ElementalCraftApi.MODID, value = Dist.CLIENT)
public class ECRenderTypes {

	private static final String GHOST_NAME = "elementalcraft:ghost";
	private static final String SOURCE_NAME = "elementalcraft:source";

    public static final RenderType GHOST = RenderType.create(GHOST_NAME, RenderSetup.builder(ECRenderPipelines.GHOST_PIPELINE)
            .useLightmap()
            .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS, RenderTypes.MOVING_BLOCK_SAMPLER)
            .affectsCrumbling()
            .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
            .createRenderSetup());


	private static final Function<Identifier, RenderType> SOURCE = Util.memoize(location -> RenderType.create(SOURCE_NAME, RenderSetup.builder(ECRenderPipelines.SOURCE_PIPELINE)
            .withTexture("Sampler0", location)
            .sortOnUpload()
            .createRenderSetup()));

	public static RenderType source(Identifier location) {
		return SOURCE.apply(location);
	}
}
