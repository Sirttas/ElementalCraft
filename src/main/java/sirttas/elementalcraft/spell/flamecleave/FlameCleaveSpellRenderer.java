package sirttas.elementalcraft.spell.flamecleave;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.renderer.SpellRenderer;
import sirttas.elementalcraft.spell.tick.SpellInstance;

public class FlameCleaveSpellRenderer implements SpellRenderer<FlameCleaveSpellRenderState> {

    private final ItemModelResolver itemModelResolver;

    public FlameCleaveSpellRenderer() {
        this.itemModelResolver = Minecraft.getInstance().getItemModelResolver(); // TODO use context
    }

    @Override
    public FlameCleaveSpellRenderState createRenderState() {
        return new FlameCleaveSpellRenderState();
    }

    @Override
    public void extractRenderState(FlameCleaveSpellRenderState state, Holder<Spell> spell, @Nullable SpellInstance instance, Entity caster, InteractionHand hand, float partialTicks, int lightCoords) {
        SpellRenderer.super.extractRenderState(state, spell, instance, caster, hand, partialTicks, lightCoords);
        state.weapon.clear();
        if (instance == null || !(caster instanceof LivingEntity livingEntity)) {
            return;
        }

        itemModelResolver.updateForLiving(state.weapon, livingEntity.getMainHandItem(), ItemDisplayContext.GROUND, livingEntity);
        state.angle = -((instance.getTicks() + partialTicks) / instance.getDuration()) * 360;
    }

    @Override
    public void submit(FlameCleaveSpellRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.weapon.isEmpty()) {
            return;
        }
        poseStack.translate(0, 1, 0);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.angle));
        poseStack.translate(0, 0, 1);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(45));
        state.weapon.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    }

    @Override
    public void submitFirstPerson(FlameCleaveSpellRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.translate(0, -1.25, 0);
        SpellRenderer.super.submitFirstPerson(state, poseStack, submitNodeCollector, camera);
    }
}
