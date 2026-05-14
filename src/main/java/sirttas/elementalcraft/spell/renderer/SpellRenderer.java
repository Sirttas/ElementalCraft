package sirttas.elementalcraft.spell.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.tick.SpellInstance;

public interface SpellRenderer<S extends SpellRenderState> {

    S createRenderState();

    default void extractRenderState(S state, Holder<Spell> spell, @Nullable SpellInstance instance, Entity caster, InteractionHand hand, float partialTicks, int lightCoords) {
        SpellRenderState.extractBase(state, spell, hand, lightCoords);
    }

    void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera);

    default void submitFirstPerson(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.submit(state, poseStack, submitNodeCollector, camera);
    }

    default boolean hideHand(InteractionHand hand) {
        return false;
    }
}
