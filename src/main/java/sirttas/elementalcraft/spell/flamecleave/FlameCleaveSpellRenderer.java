package sirttas.elementalcraft.spell.flamecleave;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import sirttas.elementalcraft.spell.AbstractSpellInstance;
import sirttas.elementalcraft.spell.renderer.ISpellInstanceRenderer;

public class FlameCleaveSpellRenderer implements ISpellInstanceRenderer {

    @Override
    public void render(AbstractSpellInstance spell, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (spell.getCaster() instanceof LivingEntity livingEntity) {
            var weapon = livingEntity.getMainHandItem();
            var angle = ((spell.getTicks() + partialTicks) / spell.getDuration()) * 360;

            poseStack.translate(0, 1, 0);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-angle));
            poseStack.translate(0, 0, 1);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(45));
            this.renderItem(weapon, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }

    @Override
    public void renderFirstPerson(AbstractSpellInstance instance, LocalPlayer caster, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.translate(0, -1.25, 0);
        ISpellInstanceRenderer.super.renderFirstPerson(instance, caster, partialTicks, poseStack, buffer, packedLight);
    }
}
