package sirttas.elementalcraft.block.shrine.budding;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelIdentifier;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;
import sirttas.elementalcraft.block.shrine.ShrineRenderer;
import sirttas.elementalcraft.renderer.ECRendererHelper;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class BuddingShrineRenderer extends ShrineRenderer<BuddingShrineBlockEntity> {

    private static final Map<Identifier, BakedModel> PLATE_MODELS = new HashMap<>();

    @Override
    public void render(@NotNull BuddingShrineBlockEntity shrine, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        super.render(shrine, partialTicks, poseStack, bufferSource, combinedLight, combinedOverlay);
        ECRendererHelper.renderModel(getPlateModel(shrine.getBudType()), poseStack, bufferSource, shrine, combinedLight, combinedOverlay);
    }

    public static BakedModel getPlateModel(BuddingShrineBudType budType) {
        return PLATE_MODELS.computeIfAbsent(budType.plateModel(), loc -> Minecraft.getInstance().getModelManager().getModel(ModelIdentifier.standalone(loc)));
    }

}
