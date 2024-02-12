package sirttas.elementalcraft.block.shrine.upgrade.translocation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.upgrade.directional.AbstractDirectionalShrineUpgradeBlock;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;

public class TranslocationShrineUpgradeRenderer implements BlockEntityRenderer<TranslocationShrineUpgradeBlockEntity> {
    public static final ResourceLocation RING_LOCATION = ElementalCraftApi.createRL("block/shrine_upgrade_translocation_ring");

    private BakedModel ringModel;

    @Override
    public void render(TranslocationShrineUpgradeBlockEntity te, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int light, int overlay) {
        var state = te.getBlockState();
        var facing = state.getValue(AbstractDirectionalShrineUpgradeBlock.FACING);
        var rotation = facing.getRotation();

        if (ringModel == null) {
            ringModel = Minecraft.getInstance().getModelManager().getModel(RING_LOCATION);
        }
        matrixStack.translate(0.5, 0.5, 0.5);
        matrixStack.mulPose(rotation);
        matrixStack.mulPose(Axis.YP.rotationDegrees(ECRendererHelper.getClientTicks(partialTicks) * 2));
        matrixStack.translate(-0.5, -0.5, -0.5);
        ECRendererHelper.renderModel(ringModel, matrixStack, buffer, te, light, overlay);
    }
}
