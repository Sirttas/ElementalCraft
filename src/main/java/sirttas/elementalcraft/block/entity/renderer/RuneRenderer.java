package sirttas.elementalcraft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.rune.handler.CapabilityRuneHandler;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public interface RuneRenderer<T extends BlockEntity> extends IECRenderer<T> {

	static <T extends BlockEntity> BlockEntityRenderer<T> create() {
		return new RuneRenderer<>() {};
	}

	@Override
	default void render(@Nonnull T te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		CapabilityRuneHandler.get(te).ifPresent(handler -> renderRunes(matrixStack, buffer, handler, getClientTicks(partialTicks), light, overlay));
	}


}
