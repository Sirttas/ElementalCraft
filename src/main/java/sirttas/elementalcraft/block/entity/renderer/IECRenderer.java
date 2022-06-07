package sirttas.elementalcraft.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import sirttas.elementalcraft.renderer.IECGenericRenderer;

public interface IECRenderer<T extends BlockEntity> extends BlockEntityRenderer<T>, IECGenericRenderer {

	default void renderModel(BakedModel model, PoseStack matrixStack, MultiBufferSource buffer, T te, int light, int overlay) {
		renderModel(model, matrixStack, buffer, te.getBlockState(), light, overlay, getModelData(model, te));
	}

	default IModelData getModelData(BakedModel model, T te) {
		Level level = te.getLevel();
		BlockPos pos = te.getBlockPos();

		if (level == null) {
			return EmptyModelData.INSTANCE;
		}
		return model.getModelData(level, pos, te.getBlockState(), getModelData(level, pos));
	}

}
