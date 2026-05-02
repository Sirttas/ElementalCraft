package sirttas.elementalcraft.client.renderer;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
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

    public static final RenderPipeline GHOST = RenderPipelines.SOLID_BLOCK.toBuilder()
            .withLocation(ElementalCraftApi.createRL("pipeline/ghost"))
            .withColorTargetState(new ColorTargetState(new BlendFunction(SourceFactor.CONSTANT_ALPHA, DestFactor.ONE_MINUS_CONSTANT_ALPHA)))
            .withCull(false)
            .build();

    public static final RenderPipeline SOURCE = RenderPipelines.SOLID_BLOCK.toBuilder()
            .withLocation(ElementalCraftApi.createRL("pipeline/source"))
            .withColorTargetState(new ColorTargetState(new BlendFunction(SourceFactor.CONSTANT_ALPHA, DestFactor.ONE)))
            .withDepthStencilState(new DepthStencilState(CompareOp.LESS_THAN_OR_EQUAL, false))
            .withCull(false)
            .build();

    @SubscribeEvent
    public static void registerPipelines(RegisterRenderPipelinesEvent event) {
        event.registerPipeline(GHOST);
        event.registerPipeline(SOURCE);
    }
}
