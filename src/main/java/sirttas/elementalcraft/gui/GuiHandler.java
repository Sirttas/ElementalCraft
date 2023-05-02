package sirttas.elementalcraft.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.block.anchor.TranslocationAnchorList;
import sirttas.elementalcraft.block.shrine.ShrineElementStorage;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlockItem;
import sirttas.elementalcraft.client.LevelRenderHandler;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.spell.ISpellHolder;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.handler.IJewelHandler;
import sirttas.elementalcraft.renderer.ECRendererHelper;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.spell.air.TranslocationSpell;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("resource")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GuiHandler {

	public static final Material TRANSLOCATION_ANCHOR_MARKER = ECRendererHelper.getBlockMaterial("gui/translocation_anchor_marker");

	private GuiHandler() {}

	@SubscribeEvent
	public static void onDrawScreenPost(RegisterGuiOverlaysEvent event) {
		event.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "gauge", (g, p, t, w, h) -> drawGauge(g, p));
		event.registerBelow(VanillaGuiOverlay.PORTAL.id(), "translocation_anchor_marker", (g, p, t, w, h) -> drawAnchors(g, p, w, h));
		event.registerBelow(ElementalCraft.createRL("translocation_anchor_marker"), "single_translocation_anchor_marker", (g, p, t, w, h) -> drawAnchor(g, p, w, h));
	}

	public static void drawGauge(ForgeGui gui, PoseStack poseStack) {
		var player = Minecraft.getInstance().player;

		if (player == null) {
			return;
		}

		gui.setupOverlayRenderState(true, false);

		var spell = getSpell();
		var i = 0;

		for (var storage : getElementStorage(player)) {
			ElementType type = storage.getElementType();

			renderElementGauge(poseStack, storage.getElementAmount(), storage.getElementCapacity(), type, i);
			gui.setupOverlayRenderState(true, false);
			if (storage instanceof ShrineElementStorage shrineStorage) {
				renderShrineCheck(poseStack, storage, shrineStorage);
			} else if (spell.isValid() && spell.getElementType() == type && i == 0 && isPlayerOwned(player, storage)) {
				renderSpellCheck(poseStack, player, spell);
			}
			i++;
		}
	}

	private static boolean isPlayerOwned(Player player, ISingleElementStorage storage) {
		return ElementStorageHelper.get(player)
				.map(s -> s.forElement(storage.getElementType()))
				.map(s -> s.getElementAmount() == storage.getElementAmount() && s.getElementCapacity() == storage.getElementCapacity())
				.orElse(false);
	}

	public static void drawAnchors(ForgeGui gui, PoseStack poseStack, int width, int height) {
		var player = Minecraft.getInstance().player;
		var worldMatrix = LevelRenderHandler.getWorldMatrix();

		if (worldMatrix == null || player == null || !TranslocationSpell.holdsTranslocation(player) || TranslocationAnchorList.CLIENT_LIST.isEmpty()) {
			return;
		}

		var targetAnchor = TranslocationSpell.getTargetAnchor(player, TranslocationAnchorList.CLIENT_LIST);

		var range = Spells.TRANSLOCATION.get().getRange(player);
		var rangeSq = range * range;
		var falloffSq = (range / 2) * (range / 2);
		var cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
		var playerPos = player.position();
		var buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

		gui.setupOverlayRenderState(true, false);
		for (var anchor : TranslocationAnchorList.CLIENT_LIST) {
			var center = Vec3.atCenterOf(anchor);
			var distanceSq = center.distanceToSqr(playerPos);

			if (distanceSq <= rangeSq) {
				var v = getPositionInScreen(worldMatrix, cameraPos, center);

				if (v.z() <= 0F || v.z() >= 1F) {
					continue;
				}
				drawAnchor(poseStack, width, height, anchor.equals(targetAnchor) ? 1.5f : getAnchorScale(falloffSq, (float) distanceSq), buffer, v);
			}
		}
		buffer.endBatch();
	}

	public static void drawAnchor(ForgeGui gui, PoseStack poseStack, int width, int height) {
		var player = Minecraft.getInstance().player;
		var worldMatrix = LevelRenderHandler.getWorldMatrix();

		if (worldMatrix == null || player == null || TranslocationAnchorList.CLIENT_LIST.isEmpty()) {
			return;
		}

		var anchor = TranslocationShrineUpgradeBlockItem.getTargetAnchor(player);

		if (anchor == null || !TranslocationAnchorList.CLIENT_LIST.contains(anchor)) {
			return;
		}

		var range = Spells.TRANSLOCATION.get().getRange(null);
		var rangeSq = range * range;
		var center = Vec3.atCenterOf(anchor);
		var distanceSq = center.distanceToSqr(player.position());

		if (distanceSq > rangeSq) {
			return;
		}

		var v = getPositionInScreen(worldMatrix, Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), center);

		if (v.z() <= 0F || v.z() >= 1F) {
			return;
		}

		var buffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

		gui.setupOverlayRenderState(true, false);
		drawAnchor(poseStack, width, height, 1.5f, buffer, v);
		buffer.endBatch();
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

		v.transform(worldMatrix);
		v.perspectiveDivide();
		return v;
	}

	private static List<ISingleElementStorage> getElementStorage(Player player) {
		Minecraft minecraft = Minecraft.getInstance();
		HitResult result = minecraft.hitResult;

		if (result != null && minecraft.options.getCameraType().isFirstPerson()) {
			BlockPos pos = result.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) result).getBlockPos() : null;
			BlockEntity tile = pos != null ? minecraft.player.level.getBlockEntity(pos) : null;

			if (tile != null) {
				var storages = ElementStorageHelper.get(tile)
						.filter(storage -> storage.doesRenderGauge() || GuiHelper.showDebugInfo())
						.map(GuiHandler::splitStorage)
						.orElse(Collections.emptyList());

				if (!storages.isEmpty()) {
					return storages;
				}
			}
		}

		var holder = EntityHelper.handStream(player)
				.map(stack -> ElementStorageHelper.get(stack).resolve())
				.<IElementStorage>mapMulti(Optional::ifPresent)
				.findFirst();

		if (holder.isPresent()) {
			return splitStorage(holder.get());
		}

		var playerStorage = ElementStorageHelper.get(player).resolve();

		if (playerStorage.isEmpty()) {
			return Collections.emptyList();
		}

		var spellElementType = EntityHelper.handStream(player).map(stack -> {
			if (!stack.isEmpty() && stack.getItem() instanceof ISpellHolder) {
				return SpellHelper.getSpell(stack).getElementType();
			}
			return ElementType.NONE;
		}).filter(type -> type != ElementType.NONE).findFirst().orElse(ElementType.NONE);
		var list = new ArrayList<ElementType>(4);

		if (spellElementType != ElementType.NONE) {
			list.add(spellElementType);
		}
		player.getCapability(IJewelHandler.CAPABILITY).ifPresent(handler -> handler.getActiveJewels().stream()
				.map(Jewel::getElementType)
				.distinct()
				.filter(type -> type != ElementType.NONE && type != spellElementType)
				.forEach(list::add));
		return splitStorage(playerStorage.get(), list);
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

	private static Spell getSpell() {
		return EntityHelper.handStream(Minecraft.getInstance().player).map(stack -> {
			if (!stack.isEmpty() && stack.getItem() instanceof ISpellHolder) {
				return SpellHelper.getSpell(stack);
			}
			return Spells.NONE.get();
		}).filter(Spell::isValid).findFirst().orElseGet(Spells.NONE);
	}

	private static void renderElementGauge(PoseStack matrixStack, int element, int max, ElementType type, int index) {
		GuiHelper.renderElementGauge(matrixStack, getXOffset() - 32 - (20 * index), getYOffset() - 8, element, max, type);
	}

	public static int getYOffset() {
		return Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 + ECConfig.CLIENT.gaugeOffsetX.get();
	}

	public static int getXOffset() {
		return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + ECConfig.CLIENT.gaugeOffsetY.get();
	}

	private static void renderShrineCheck(PoseStack poseStack, ISingleElementStorage storage, ShrineElementStorage shrineStorage) {
		var shrine = shrineStorage.getShrine();

		if (shrine.isRunning()) {
			renderCheck(poseStack, GuiHelper.Check.VALID);
		} else if (storage.getElementAmount() >= shrine.getConsumeAmount()) {
			renderCheck(poseStack, GuiHelper.Check.PAUSED);
		} else {
			renderCheck(poseStack, GuiHelper.Check.INVALID);
		}
	}

	private static void renderSpellCheck(PoseStack poseStack, LocalPlayer player, Spell spell) {
		var canCast = spell.consume(player, true);
		var isInCooldown = SpellTickHelper.hasCooldown(player, spell);

		if (canCast && !isInCooldown) {
			renderCheck(poseStack, GuiHelper.Check.VALID);
		} else if (isInCooldown) {
			renderCheck(poseStack, GuiHelper.Check.PAUSED);
		} else {
			renderCheck(poseStack, GuiHelper.Check.INVALID);
		}
	}

	private static void renderCheck(PoseStack poseStack, GuiHelper.Check valid) {
		GuiHelper.renderCheck(poseStack, valid, getXOffset() - 21, getYOffset() + 3);
	}
}
