package sirttas.elementalcraft.renderer;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import net.minecraft.client.renderer.RenderPipelines;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;

@EventBusSubscriber(modid = ElementalCraftApi.MODID, value = Dist.CLIENT)
public class ECRenderPipelines {

    public static final RenderPipeline GHOST_PIPELINE = RenderPipelines.SOLID_BLOCK.toBuilder()
            .withLocation(ElementalCraftApi.createRL("pipeline/ghost"))
            .withShaderDefine("ALPHA_CUTOUT", 0.5F)
            .withBlend(new BlendFunction(SourceFactor.CONSTANT_ALPHA, DestFactor.ONE_MINUS_CONSTANT_ALPHA))
            .withCull(false)
            .build();

    public static final RenderPipeline SOURCE_PIPELINE = RenderPipelines.SOLID_BLOCK.toBuilder()
            .withLocation(ElementalCraftApi.createRL("pipeline/ghost"))
            .withBlend(new BlendFunction(SourceFactor.CONSTANT_ALPHA, DestFactor.ONE))
            .withDepthWrite(false)
            .withCull(false)
            .build();

    @SubscribeEvent
    public static void registerPipelines(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(GHOST_PIPELINE);
        event.registerPipeline(SOURCE_PIPELINE);
    }
}
