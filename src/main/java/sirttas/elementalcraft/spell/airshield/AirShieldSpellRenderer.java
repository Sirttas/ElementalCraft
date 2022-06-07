package sirttas.elementalcraft.spell.airshield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.ForgeHooksClient;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.renderer.ISpellRenderer;

public class AirShieldSpellRenderer implements ISpellRenderer {

    public static final Material BACKGROUND = ForgeHooksClient.getBlockMaterial(ElementalCraft.createRL("effect/air_shield_background"));
    public static final Material BLADE = ForgeHooksClient.getBlockMaterial(ElementalCraft.createRL("effect/air_shield_blade"));

    @Override
    public void render(Spell spell, Entity caster, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        var angle = getAngle(partialTicks) * 10;

        poseStack.translate(-1, 0, -1);
        poseStack.scale(1/64f, 1/64f, 1/64f);
        this.renderIcon(poseStack, buffer, BACKGROUND, 128, 128, packedLight, OverlayTexture.NO_OVERLAY);
        renderBlade(poseStack, buffer, packedLight, angle);
        renderBlade(poseStack, buffer, packedLight, angle);
    }

    @Override
    public void renderFirstPerson(Spell spell, LocalPlayer caster, InteractionHand hand, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.translate(0, -1, 0);
        ISpellRenderer.super.renderFirstPerson(spell, caster, hand, partialTicks, poseStack, buffer, packedLight);
    }

    private void renderBlade(PoseStack poseStack, MultiBufferSource buffer, int packedLight, float angle) {
        poseStack.translate(64, 64, -0.01f);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(angle));
        poseStack.translate(-64, -64, 0);
        this.renderIcon(poseStack, buffer, BLADE, 128, 128, packedLight, OverlayTexture.NO_OVERLAY);
    }

}
