package sirttas.elementalcraft.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
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

    public static final RenderType GHOST = RenderType.create(GHOST_NAME, RenderSetup.builder(ECRenderPipelines.GHOST)
            .useLightmap()
            .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS, () -> RenderSystem.getSamplerCache().getSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, FilterMode.LINEAR, FilterMode.NEAREST, true))
            .affectsCrumbling()
            .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
            .createRenderSetup());


	private static final Function<Identifier, RenderType> SOURCE = Util.memoize(location -> RenderType.create(SOURCE_NAME, RenderSetup.builder(ECRenderPipelines.SOURCE)
			.useLightmap()
            .withTexture("Sampler0", location, () -> RenderSystem.getSamplerCache().getSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, FilterMode.LINEAR, FilterMode.NEAREST, true))
			.affectsCrumbling()
			.setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
			.sortOnUpload()
			.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
            .createRenderSetup()));

	public static RenderType source(Identifier location) {
		return SOURCE.apply(location);
	}
}
