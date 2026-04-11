package sirttas.elementalcraft.spell.airshield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.renderer.SpellRenderer;
import sirttas.elementalcraft.spell.tick.AbstractSpellInstance;

public class AirShieldSpellRenderer implements SpellRenderer<AirShieldSpellRenderState> {

    public static final Material BACKGROUND = ECRendererHelper.getBlockMaterial("effect/air_shield_background");
    public static final Material BLADE = ECRendererHelper.getBlockMaterial("effect/air_shield_blade");

    @Override
    public AirShieldSpellRenderState createRenderState() {
        return new AirShieldSpellRenderState();
    }

    @Override
    public void extractRenderState(AirShieldSpellRenderState state, Spell spell, @Nullable AbstractSpellInstance instance, Entity caster, InteractionHand hand, float partialTicks, int lightCoords) {
        SpellRenderer.super.extractRenderState(state, spell, instance, caster, hand, partialTicks, lightCoords);
        state.angle = ECRendererHelper.getClientTicks(partialTicks) * 10;
    }

    @Override
    public void submit(AirShieldSpellRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.translate(-1, 0, -1);
        poseStack.scale(1/64f, 1/64f, 1/64f);
        ECRendererHelper.submitIcon(poseStack, submitNodeCollector, BACKGROUND, 128, 128, state.lightCoords);
        submitBlade(state, poseStack, submitNodeCollector);
        submitBlade(state, poseStack, submitNodeCollector);
    }

    @Override
    public void submitFirstPerson(AirShieldSpellRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.translate(0, -1, 0);
        SpellRenderer.super.submitFirstPerson(state, poseStack, submitNodeCollector, camera);
    }

    private void submitBlade(AirShieldSpellRenderState state, PoseStack poseStack,SubmitNodeCollector submitNodeCollector) {
        poseStack.translate(64, 64, -0.01f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.angle));
        poseStack.translate(-64, -64, 0);
        ECRendererHelper.submitIcon(poseStack, submitNodeCollector, BLADE, 128, 128, state.lightCoords);
    }
}
