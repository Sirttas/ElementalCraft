package sirttas.elementalcraft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.rune.handler.CapabilityRuneHandler;

@OnlyIn(Dist.CLIENT)
public interface RuneRenderer<T extends BlockEntity> extends IECRenderer<T> {

	@Override
	default void render(T te, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
		CapabilityRuneHandler.get(te).ifPresent(handler -> renderRunes(matrixStack, buffer, handler, getAngle(partialTicks), light, overlay));
	}
}
