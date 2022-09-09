package sirttas.elementalcraft.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.renderer.ISpellInstanceRenderer;
import sirttas.elementalcraft.spell.renderer.SpellRenderers;
import sirttas.elementalcraft.spell.tick.AbstractSpellInstance;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class EntityClientHandler {

	private EntityClientHandler() {}

	@SubscribeEvent
	public static void renderSpellEffects(final RenderLivingEvent.Post<?, ?> event) {
		var poseStack = event.getPoseStack();
		var entity = event.getEntity();
		var spell = SpellHelper.getSpellInUse(entity);
		var partialTicks = event.getPartialTick();
		var buffer = event.getMultiBufferSource();
		var packedLight = event.getPackedLight();

		poseStack.pushPose();
		poseStack.mulPose(Vector3f.YP.rotationDegrees(180 - Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot)));
		renderSingleSpell(spell, null, entity, partialTicks, poseStack, buffer, packedLight);
		SpellTickHelper.getSpellInstances(entity).forEach(i -> renderSingleSpell(i.getSpell(), i, entity, partialTicks, poseStack, buffer, packedLight));
		poseStack.popPose();
	}

	private static void renderSingleSpell(Spell spell, @Nullable AbstractSpellInstance instance, LivingEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		if (!spell.isValid()) {
			return;
		}
		var renderer = SpellRenderers.get(spell);

		if (renderer == null) {
			return;
		}

		poseStack.pushPose();
		if (renderer instanceof ISpellInstanceRenderer spellInstanceRenderer && instance != null) {
			spellInstanceRenderer.render(instance, partialTicks, poseStack, buffer, packedLight);
		} else {
			renderer.render(spell, entity, partialTicks, poseStack, buffer, packedLight);
		}
		poseStack.popPose();
	}

}
