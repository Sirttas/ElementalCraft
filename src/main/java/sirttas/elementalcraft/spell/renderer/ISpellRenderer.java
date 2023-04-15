package sirttas.elementalcraft.spell.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import sirttas.elementalcraft.spell.Spell;

public interface ISpellRenderer {

    void render(Spell spell, Entity caster, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight);

    default void renderFirstPerson(Spell spell, LocalPlayer caster, InteractionHand hand, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.render(spell, caster, partialTicks, poseStack, buffer, packedLight);
    }

    default boolean hideHand(InteractionHand hand) {
        return false;
    }
}
