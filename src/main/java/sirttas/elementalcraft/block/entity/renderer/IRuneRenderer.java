package sirttas.elementalcraft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public interface IRuneRenderer<T extends BlockEntity> extends BlockEntityRenderer<T> {

	static <T extends BlockEntity> BlockEntityRenderer<T> create() {
		return new IRuneRenderer<>() {};
	}

	@Override
	default void render(@Nonnull T te, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
		ECRendererHelper.renderRunes(poseStack, buffer, te, partialTicks, light, overlay);
	}
}
