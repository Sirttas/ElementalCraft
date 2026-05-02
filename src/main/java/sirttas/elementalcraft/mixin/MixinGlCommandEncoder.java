package sirttas.elementalcraft.mixin;

import com.mojang.blaze3d.opengl.GlCommandEncoder;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import org.lwjgl.opengl.GL14;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sirttas.elementalcraft.client.renderer.ECRenderPipelines;

@Mixin(GlCommandEncoder.class)
public class MixinGlCommandEncoder {

    @Shadow
    @Final
    private RenderPipeline lastPipeline;

    @Unique
    private static final ThreadLocal<RenderPipeline> CACHE_LAST_PIPELINE = new ThreadLocal<>();

    @Inject(method = "applyPipelineState(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V",
            at = @At("HEAD"))
    private void applyPipelineState$head(RenderPipeline pipeline, CallbackInfo ci) {
        CACHE_LAST_PIPELINE.set(lastPipeline);
    }

    @Inject(method = "applyPipelineState(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V",
            at = @At("RETURN"))
    private void applyPipelineState$return(RenderPipeline pipeline, CallbackInfo ci) {
        if (pipeline == ECRenderPipelines.GHOST) {
            GL14.glBlendColor(1.0F, 1.0F, 1.0F, 0.5F);
        } else if (CACHE_LAST_PIPELINE.get() == ECRenderPipelines.GHOST) {
            GL14.glBlendColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
        CACHE_LAST_PIPELINE.remove();
    }
}
