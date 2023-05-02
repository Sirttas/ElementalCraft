package sirttas.elementalcraft.block.shrine.upgrade.translocation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.upgrade.directional.AbstractDirectionalShrineUpgradeBlock;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class TranslocationShrineUpgradeRenderer implements BlockEntityRenderer<TranslocationShrineUpgradeBlockEntity> {
    public static final ResourceLocation RING_LOCATION = ElementalCraft.createRL("block/shrine_upgrade_translocation_ring");

    private BakedModel ringModel;

    @Override
    public void render(TranslocationShrineUpgradeBlockEntity te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        BlockState state = te.getBlockState();
        Direction facing = state.getValue(AbstractDirectionalShrineUpgradeBlock.FACING);
        Quaternion rotation = facing.getRotation();

        if (ringModel == null) {
            ringModel = Minecraft.getInstance().getModelManager().getModel(RING_LOCATION);
        }
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.mulPose(rotation);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(ECRendererHelper.getClientTicks(partialTicks) * 2));
        matrixStack.translate(-0.5, -0.5, -0.5);
        ECRendererHelper.renderModel(ringModel, matrixStack, buffer, te, light, overlay);
    }
}
