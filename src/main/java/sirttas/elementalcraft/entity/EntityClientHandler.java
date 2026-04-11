package sirttas.elementalcraft.entity;

import com.google.common.reflect.TypeToken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.advancements.LookAtSourcePayload;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.data.attachment.ECDataAttachments;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.renderer.SpellRenderState;
import sirttas.elementalcraft.spell.renderer.SpellRenderer;
import sirttas.elementalcraft.spell.renderer.SpellRenderers;
import sirttas.elementalcraft.spell.tick.AbstractSpellInstance;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class EntityClientHandler {

    private static final ContextKey<@NotNull List<SpellRenderState>> SPELL_RENDER_STATES = new ContextKey<>(ElementalCraftApi.createRL("spell_render_states"));

	private EntityClientHandler() {}

    @SubscribeEvent
    public static void registerRenderStateModifiers(RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier(new TypeToken<@NotNull EntityRenderer<@NotNull Entity, @NotNull EntityRenderState>>() {}, EntityClientHandler::extractSpellRenderStates);
    }

    private static void extractSpellRenderStates(Entity entity, EntityRenderState state) {
        var spell = SpellHelper.getSpellInUse(entity);
        var instances = SpellTickHelper.getSpellInstances(entity);
        var states = new ArrayList<SpellRenderState>(instances.size() + 1);
        var mainState = extractSingleSpellRenderState(spell.value(), null, entity, state);

        if (mainState != null) {
            states.add(mainState);
        }
        instances.forEach(instance -> {
            var instanceState = extractSingleSpellRenderState(instance.getSpell(), instance, entity, state);

            if (instanceState != null) {
                states.add(instanceState);
            }
        });
        state.setRenderData(SPELL_RENDER_STATES, states);
    }

    private static <S extends SpellRenderState> S extractSingleSpellRenderState(Spell spell, @Nullable AbstractSpellInstance instance, Entity caster, EntityRenderState entityRenderState) {
        if (!spell.isValid()) {
            return null;
        }
        SpellRenderer<S> renderer = SpellRenderers.get(spell);

        if (renderer == null) {
            return null;
        }
        S state = renderer.createRenderState();

        if (state == null) {
            return null;
        }
        renderer.extractRenderState(state, spell, instance, caster, spell.getHand(caster), entityRenderState.partialTick, entityRenderState.lightCoords);
        return state;
    }

	@SubscribeEvent
	public static void submitSpellEffects(final RenderLivingEvent.Post<?, ?, ?> event) {
        var state = event.getRenderState();
        var states = state.getRenderData(SPELL_RENDER_STATES);

        if (states == null) {
            return;
        }

        var poseStack = event.getPoseStack();
        var submitNodeCollector = event.getSubmitNodeCollector();
        var cameraRenderState = Minecraft.getInstance().gameRenderer.getGameRenderState().levelRenderState.cameraRenderState;

		poseStack.pushPose();
		poseStack.mulPose(Axis.YP.rotationDegrees(180 - state.bodyRot));
        states.forEach(s -> {
            var renderer = SpellRenderers.get(s.spell);

            if (renderer != null) {
                renderer.submit(s, poseStack, submitNodeCollector, cameraRenderState);
            }
        });
		poseStack.popPose();
	}

	@SubscribeEvent
	public static void submitSpellEffectsFirstPerson(final RenderHandEvent event) {
		var player = Minecraft.getInstance().player;

		if (player == null) {
			return;
		}

		var inUseRenderer = SpellRenderers.get(SpellHelper.getSpellInUse(player).value());
		var hand = event.getHand();

		if (inUseRenderer != null && inUseRenderer.hideHand(hand)) {
			event.setCanceled(true);
		}
        submitSpellEffectsFirstPerson(player, event.getItemStack(), hand, event.getPartialTick(), event.getPoseStack(), event.getSubmitNodeCollector(), event.getPackedLight());
	}

	private static void submitSpellEffectsFirstPerson(AbstractClientPlayer player, ItemStack stack, InteractionHand hand, float partialTicks, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight) {
		if (!(player instanceof LocalPlayer localPlayer) || localPlayer.isScoping()) {
            return;
        }

        var spell = SpellHelper.getSpell(stack);
        var cameraRenderState = Minecraft.getInstance().gameRenderer.getGameRenderState().levelRenderState.cameraRenderState;

        if (localPlayer.isUsingItem() && localPlayer.getUsedItemHand() == hand && !stack.isEmpty()) {
            submitSingleSpellFirstPerson(spell.value(), null, localPlayer, hand, partialTicks, poseStack, submitNodeCollector, cameraRenderState, packedLight);
        }
        if (hand == InteractionHand.MAIN_HAND) {
            SpellTickHelper.getSpellInstances(localPlayer).forEach(i -> submitSingleSpellFirstPerson(i.getSpell(), i, localPlayer, hand, partialTicks, poseStack, submitNodeCollector, cameraRenderState, packedLight));
        }
	}

	private static void submitSingleSpellFirstPerson(Spell spell, @Nullable AbstractSpellInstance instance, LocalPlayer localPlayer, InteractionHand hand, float partialTicks, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, int packedLight) {
		if (!spell.isValid()) {
			return;
		}
		var renderer = SpellRenderers.get(spell);

		if (renderer == null) {
			return;
		}

        var state = renderer.createRenderState();

        if (state == null) {
            return;
        }

        renderer.extractRenderState(state, spell, instance, localPlayer, hand, partialTicks, packedLight);

		poseStack.pushPose();
		renderer.submitFirstPerson(state, poseStack, submitNodeCollector, cameraRenderState);
		poseStack.popPose();
	}

	@SubscribeEvent
	public static void onClientPostTick(ClientTickEvent.Post event) {
		var minecraft = Minecraft.getInstance();

		if (minecraft.hitResult instanceof BlockHitResult hitResult && hitResult.getType() == HitResult.Type.BLOCK && minecraft.level != null && minecraft.player != null && !minecraft.player.getData(ECDataAttachments.HAS_SEEN_SOURCE)) {
			var state = minecraft.level.getBlockState(hitResult.getBlockPos());

			if (state.is(ECTags.Blocks.SOURCES)) {
                ClientPacketDistributor.sendToServer(new LookAtSourcePayload(hitResult));
				minecraft.player.setData(ECDataAttachments.HAS_SEEN_SOURCE, true);
			}
		}
	}
}
