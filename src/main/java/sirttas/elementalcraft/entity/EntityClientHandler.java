package sirttas.elementalcraft.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
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
		poseStack.mulPose(Axis.YP.rotationDegrees(180 - Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot)));
		renderSingleSpellFirstPerson(spell, null, entity, partialTicks, poseStack, buffer, packedLight);
		SpellTickHelper.getSpellInstances(entity).forEach(i -> renderSingleSpellFirstPerson(i.getSpell(), i, entity, partialTicks, poseStack, buffer, packedLight));
		poseStack.popPose();
	}

	private static void renderSingleSpellFirstPerson(Spell spell, @Nullable AbstractSpellInstance instance, LivingEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
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

	@SubscribeEvent
	public static void renderSpellEffects(final RenderHandEvent event) {
		var player = Minecraft.getInstance().player;

		if (player == null) {
			return;
		}

		var inUseRenderer = SpellRenderers.get(SpellHelper.getSpellInUse(player));
		var hand = event.getHand();

		if (inUseRenderer != null && inUseRenderer.hideHand(hand)) {
			event.setCanceled(true);
		}
		renderSpellEffectFirstPerson(player, event.getItemStack(), hand, event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
	}

	private static void renderSpellEffectFirstPerson(AbstractClientPlayer player, ItemStack stack, InteractionHand hand, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		if (player instanceof LocalPlayer localPlayer && !localPlayer.isScoping()) {
			var spell = SpellHelper.getSpell(stack);

			if (localPlayer.isUsingItem() && localPlayer.getUsedItemHand() == hand && !stack.isEmpty()) {
				renderSingleSpellFirstPerson(spell, null, localPlayer, hand, partialTicks, poseStack, buffer, packedLight);
			}
			if (hand == InteractionHand.MAIN_HAND) {
				SpellTickHelper.getSpellInstances(localPlayer).forEach(i -> renderSingleSpellFirstPerson(i.getSpell(), i, localPlayer, hand, partialTicks, poseStack, buffer, packedLight));
			}
		}
	}

	private static void renderSingleSpellFirstPerson(Spell spell, @Nullable AbstractSpellInstance instance, LocalPlayer localPlayer, InteractionHand hand, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		if (!spell.isValid()) {
			return;
		}
		var renderer = SpellRenderers.get(spell);

		if (renderer == null) {
			return;
		}
		poseStack.pushPose();
		if (renderer instanceof ISpellInstanceRenderer spellInstanceRenderer && instance != null) {
			spellInstanceRenderer.renderFirstPerson(instance, localPlayer, partialTicks, poseStack, buffer, packedLight);
		} else {
			renderer.renderFirstPerson(spell, localPlayer, hand, partialTicks, poseStack, buffer, packedLight);
		}
		poseStack.popPose();
	}
}
