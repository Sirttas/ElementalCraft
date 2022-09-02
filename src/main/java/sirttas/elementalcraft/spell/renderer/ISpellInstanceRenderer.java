package sirttas.elementalcraft.spell.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.tick.AbstractSpellInstance;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;

public interface ISpellInstanceRenderer extends ISpellRenderer {

    void render(AbstractSpellInstance spell, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight);

    default void renderFirstPerson(AbstractSpellInstance instance, LocalPlayer caster, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.render(instance, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    default void render(Spell spell, Entity caster, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        var instance = SpellTickHelper.getSpellInstance(caster, spell);

        if (instance != null) {
            this.render(instance, partialTicks, poseStack, buffer, packedLight);
        }
    }

    @Override
    default void renderFirstPerson(Spell spell, LocalPlayer caster, InteractionHand hand, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        var instance = SpellTickHelper.getSpellInstance(caster, spell);

        if (instance != null) {
            this.renderFirstPerson(instance, caster, partialTicks, poseStack, buffer, packedLight);
        }
    }
}
