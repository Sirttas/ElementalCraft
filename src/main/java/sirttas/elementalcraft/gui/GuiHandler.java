package sirttas.elementalcraft.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorageWrapper;
import sirttas.elementalcraft.block.anchor.TranslocationAnchorsSaveData;
import sirttas.elementalcraft.block.shrine.ShrineElementStorage;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlockItem;
import sirttas.elementalcraft.client.LevelRenderHandler;
import sirttas.elementalcraft.client.renderer.ECRendererHelper;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.entity.player.PlayerElementStorage;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.spell.ISpellHolder;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.handler.IJewelHandler;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.spell.air.TranslocationSpell;
import sirttas.elementalcraft.spell.tick.SpellCooldownItemDecorator;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class GuiHandler {

	public static final Material TRANSLOCATION_ANCHOR_MARKER = ECRendererHelper.getBlockMaterial("gui/translocation_anchor_marker");

    private static final Identifier GAUGE_LAYER = ElementalCraftApi.createRL("gauge");
    private static final Identifier TRANSLOCATION_ANCHOR_MARKER_LAYER = ElementalCraftApi.createRL("translocation_anchor_marker");
    private static final Identifier SINGLE_TRANSLOCATION_ANCHOR_MARKER_LAYER = ElementalCraftApi.createRL("single_translocation_anchor_marker");

	private GuiHandler() {}

	@SubscribeEvent
	public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
		var spellCooldown = new SpellCooldownItemDecorator();

		event.register(ECItems.SCROLL.get(), spellCooldown);
		event.register(ECItems.FOCUS.get(), spellCooldown);
		event.register(ECItems.STAFF.get(), spellCooldown);
	}

	@SubscribeEvent
	public static void onDrawScreenPost(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.CROSSHAIR, GAUGE_LAYER, GuiHandler::drawGauge);
		event.registerAbove(VanillaGuiLayers.CAMERA_OVERLAYS, TRANSLOCATION_ANCHOR_MARKER_LAYER, GuiHandler::drawAnchors);
		event.registerBelow(TRANSLOCATION_ANCHOR_MARKER_LAYER, SINGLE_TRANSLOCATION_ANCHOR_MARKER_LAYER, GuiHandler::drawAnchor);
	}

	public static void drawGauge(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
		var player = Minecraft.getInstance().player;

		if (player == null) {
			return;
		}

		RenderSystem.enableBlend();

		var spell = getSpell(player);
		var i = 0;

		for (var storage : getElementStorage(player)) {
			ElementType type = storage.getElementType();

			renderElementGauge(guiGraphics, Minecraft.getInstance().font, storage.getElementAmount(), storage.getElementCapacity(), type, i);
			if (storage instanceof ShrineElementStorage shrineStorage) {
				renderShrineCheck(guiGraphics, storage, shrineStorage);
			} else if (spell.isValid() && spell.getElementType() == type && i == 0 && isPlayerOwned(player, storage)) {
				renderSpellCheck(guiGraphics, player, spell);
			}
			i++;
		}
		RenderSystem.disableBlend();
	}

	private static boolean isPlayerOwned(Player player, ISingleElementStorage storage) {
		if (storage instanceof SingleElementStorageWrapper wrapper && wrapper.getParent() instanceof PlayerElementStorage playerStorage) {
			return player.equals(playerStorage.getPlayer());
		}
		return false;
	}

	public static void drawAnchors(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
		var player = Minecraft.getInstance().player;
		var worldMatrix = LevelRenderHandler.getWorldMatrix();

		if (worldMatrix == null || player == null || !TranslocationSpell.holdsTranslocation(player) || TranslocationAnchorsSaveData.CLIENT_SET.isEmpty()) {
			return;
		}

		var targetAnchor = TranslocationSpell.getTargetAnchor(player, TranslocationAnchorsSaveData.CLIENT_SET);

		var range = Spells.TRANSLOCATION.get().getRange(player);
		var rangeSq = range * range;
		var falloffSq = (range / 2) * (range / 2);
		var cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().position();
		var playerPos = player.position();
		var buffer = createBufferSource();

		RenderSystem.enableBlend();
		for (var anchor : TranslocationAnchorsSaveData.CLIENT_SET) {
			var center = Vec3.atCenterOf(anchor);
			var distanceSq = center.distanceToSqr(playerPos);

			if (distanceSq <= rangeSq) {
				var v = getPositionInScreen(worldMatrix, cameraPos, center);

				if (v.z() <= 0F || v.z() >= 1F) {
					continue;
				}
				drawAnchor(guiGraphics.pose(), guiGraphics.guiWidth(), guiGraphics.guiHeight(), anchor.equals(targetAnchor) ? 1.5f : getAnchorScale(falloffSq, (float) distanceSq), buffer, v);
			}
		}
		RenderSystem.disableBlend();
		buffer.endBatch();
	}

	public static void drawAnchor(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
		var player = Minecraft.getInstance().player;
		var worldMatrix = LevelRenderHandler.getWorldMatrix();

		if (worldMatrix == null || player == null || TranslocationAnchorsSaveData.CLIENT_SET.isEmpty()) {
			return;
		}

		var anchor = TranslocationShrineUpgradeBlockItem.getTargetAnchor(player);

		if (anchor == null || !TranslocationAnchorsSaveData.CLIENT_SET.contains(anchor)) {
			return;
		}

		var range = Spells.TRANSLOCATION.get().getRange(null);
		var rangeSq = range * range;
		var center = Vec3.atCenterOf(anchor);
		var distanceSq = center.distanceToSqr(player.position());

		if (distanceSq > rangeSq) {
			return;
		}

		var v = getPositionInScreen(worldMatrix, Minecraft.getInstance().gameRenderer.getMainCamera().position(), center);

		if (v.z() <= 0F || v.z() >= 1F) {
			return;
		}

		var buffer = createBufferSource();

		RenderSystem.enableBlend();
		drawAnchor(guiGraphics.pose(), guiGraphics.guiWidth(), guiGraphics.guiHeight(), 1.5f, buffer, v);
		RenderSystem.disableBlend();
		buffer.endBatch();
	}

	private static @NotNull MultiBufferSource.BufferSource createBufferSource() {
		return MultiBufferSource.immediate(Tesselator.getInstance().buffer);
	}


	private static void drawAnchor(PoseStack poseStack, int width, int height, float scale, MultiBufferSource.BufferSource buffer, Vector4f v) {
		var w = width / 2F;
		var h = height / 2F;

		var x = Mth.clamp(w + v.x() * w, 16f, width - 16f);
		var y = Mth.clamp(h - v.y() * h, 16f, height - 16f);

		poseStack.pushPose();
		poseStack.translate(x, y, 1F);
		poseStack.scale(0.25F, 0.25F, 1F);
		poseStack.scale(scale, scale, 1F);
		poseStack.translate(-64, -64, 1F);
		ECRendererHelper.renderIcon(poseStack, buffer, TRANSLOCATION_ANCHOR_MARKER, 128, 128);
		poseStack.popPose();
	}

	private static float getAnchorScale(float falloffSq, float distanceSq) {
		return Mth.clamp(1f - distanceSq / falloffSq, 0.2f, 1f);

	}

	@Nonnull
	private static Vector4f getPositionInScreen(Matrix4f worldMatrix, Vec3 cameraPos, Vec3 center) {
		var v = new Vector4f((float) (center.x - cameraPos.x), (float) (center.y - cameraPos.y), (float) (center.z - cameraPos.z), 1F);

		worldMatrix.transform(v);
		v.div(v);
		return v;
	}

	private static List<ISingleElementStorage> getElementStorage(Player player) {
		Minecraft minecraft = Minecraft.getInstance();
		HitResult result = minecraft.hitResult;

		if (result != null && minecraft.options.getCameraType().isFirstPerson()) {
			BlockPos pos = result.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) result).getBlockPos() : null;
			var storage = pos != null ? player.level().getCapability(ElementalCraftCapabilities.ElementStorages.BLOCK, pos, null) : null;
			List<ISingleElementStorage> storages = storage != null && (storage.doesRenderGauge(player) || GuiHelper.showDebugInfo()) ? splitStorage(storage) : Collections.emptyList();

			if (!storages.isEmpty()) {
				return storages;
			}
		}

		var holder = EntityHelper.handStream(player)
				.map(stack -> stack.getCapability(ElementalCraftCapabilities.ElementStorages.ITEM))
				.filter(Objects::nonNull)
				.findFirst();

		if (holder.isPresent()) {
			return splitStorage(holder.get());
		}

		var playerStorage = player.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY);

		if (playerStorage == null) {
			return Collections.emptyList();
		}

		var spellElementType = EntityHelper.handStream(player).map(stack -> {
			if (!stack.isEmpty() && stack.getItem() instanceof ISpellHolder) {
				return SpellHelper.getSpell(stack).value().getElementType();
			}
			return ElementType.NONE;
		}).filter(type -> type != ElementType.NONE).findFirst().orElse(ElementType.NONE);
		var list = new ArrayList<ElementType>(4);

		if (spellElementType != ElementType.NONE) {
			list.add(spellElementType);
		}
		var jewelHandler = player.getCapability(IJewelHandler.CAPABILITY);

		if (jewelHandler != null) {
			jewelHandler.getActiveJewels().stream()
					.map(Jewel::getElementType)
					.distinct()
					.filter(type -> type != ElementType.NONE && type != spellElementType)
					.forEach(list::add);
		}
		return splitStorage(playerStorage, list);
	}

	private static List<ISingleElementStorage> splitStorage(IElementStorage storage) {
		if (storage instanceof ISingleElementStorage singleElementStorage) {
			return Collections.singletonList(singleElementStorage);
		}
		return splitStorage(storage, ElementType.ALL_VALID);
	}
	private static List<ISingleElementStorage> splitStorage(IElementStorage storage, List<ElementType> elementTypes) {
		if (storage instanceof ISingleElementStorage singleElementStorage) {
			return elementTypes.contains(singleElementStorage.getElementType()) ? List.of(singleElementStorage) : Collections.emptyList();
		}
		return elementTypes.stream()
				.<ISingleElementStorage>mapMulti((type, downstream) -> downstream.accept(storage.forElement(type)))
				.filter(s -> s.getElementCapacity() > 0)
				.toList();
	}

	private static Spell getSpell(LivingEntity player) {
		return EntityHelper.handStream(player).map(stack -> {
			if (!stack.isEmpty() && stack.getItem() instanceof ISpellHolder) {
				return SpellHelper.getSpell(stack);
			}
			return Spells.NONE;
		}).filter(SpellHelper::isValid)
				.findFirst()
				.map(Holder::value)
				.orElseGet(Spells.NONE);
	}

	private static void renderElementGauge(GuiGraphicsExtractor guiGraphics, Font font, int element, int max, ElementType type, int index) {
		GuiHelper.renderElementGauge(guiGraphics, font, getXOffset() - 32 - (20 * index), getYOffset() - 8, element, max, type);
	}

	public static int getYOffset() {
		return Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 + ECConfig.CLIENT.gaugeOffsetX.get();
	}

	public static int getXOffset() {
		return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + ECConfig.CLIENT.gaugeOffsetY.get();
	}

	private static void renderShrineCheck(GuiGraphicsExtractor guiGraphics, ISingleElementStorage storage, ShrineElementStorage shrineStorage) {
		var shrine = shrineStorage.getShrine();

		if (shrine.isRunning()) {
			renderCheck(guiGraphics, GuiHelper.Check.VALID);
		} else if (storage.getElementAmount() >= shrine.getConsumeAmount()) {
			renderCheck(guiGraphics, GuiHelper.Check.PAUSED);
		} else {
			renderCheck(guiGraphics, GuiHelper.Check.INVALID);
		}
	}

	private static void renderSpellCheck(GuiGraphicsExtractor guiGraphics, LocalPlayer player, Spell spell) {
		var canCast = spell.consume(player, true);
		var isInCooldown = SpellTickHelper.hasCooldown(player, spell);

		if (canCast && !isInCooldown) {
			renderCheck(guiGraphics, GuiHelper.Check.VALID);
		} else if (isInCooldown) {
			renderCheck(guiGraphics, GuiHelper.Check.PAUSED);
		} else {
			renderCheck(guiGraphics, GuiHelper.Check.INVALID);
		}
	}

	private static void renderCheck(GuiGraphicsExtractor guiGraphics, GuiHelper.Check valid) {
		GuiHelper.renderCheck(guiGraphics, valid, getXOffset() - 21, getYOffset() + 3);
	}
}
