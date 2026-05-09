package sirttas.elementalcraft.client.renderer.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.anchor.TranslocationAnchors;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeItem;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.spell.air.TranslocationSpell;

public class TranslocationAnchorGui {

    public static final Identifier TRANSLOCATION_ANCHOR_MARKER = ElementalCraftApi.identifier("textures/gui/translocation_anchor_marker.png");

    private TranslocationAnchorGui() {}

    public static void drawAnchors(GuiGraphicsExtractor guiGraphics, @SuppressWarnings("unused") DeltaTracker deltaTracker) {
        var player = Minecraft.getInstance().player;

        if (player == null || !TranslocationSpell.holdsTranslocation(player) || TranslocationAnchors.CLIENT_SET.isEmpty()) {
            return;
        }

        var targetAnchor = TranslocationSpell.getTargetAnchor(player, TranslocationAnchors.CLIENT_SET);

        var range = Spells.TRANSLOCATION.get().getRange(player);
        var rangeSq = range * range;
        var falloffSq = (range / 2) * (range / 2);
        var playerPos = player.position();

        for (var anchor : TranslocationAnchors.CLIENT_SET) {
            var center = Vec3.atCenterOf(anchor);
            var distanceSq = center.distanceToSqr(playerPos);

            if (distanceSq <= rangeSq) {
                drawAnchor(guiGraphics, center, anchor.equals(targetAnchor) ? 1.5f : getAnchorScale(falloffSq, (float) distanceSq));
            }
        }
    }

    public static void drawAnchor(GuiGraphicsExtractor guiGraphics, @SuppressWarnings("unused") DeltaTracker deltaTracker) {
        var player = Minecraft.getInstance().player;

        if (player == null || TranslocationAnchors.CLIENT_SET.isEmpty()) {
            return;
        }

        var anchor = TranslocationShrineUpgradeItem.getTargetAnchor(player);

        if (anchor == null || !TranslocationAnchors.CLIENT_SET.contains(anchor)) {
            return;
        }

        var range = Spells.TRANSLOCATION.get().getRange(null);
        var rangeSq = range * range;
        var center = Vec3.atCenterOf(anchor);
        var distanceSq = center.distanceToSqr(player.position());

        if (distanceSq > rangeSq) {
            return;
        }
        drawAnchor(guiGraphics, center, 1.5f);
    }

    private static void drawAnchor(GuiGraphicsExtractor guiGraphics, Vec3 position, float scale) {
        var positionInScreen = Minecraft.getInstance().gameRenderer.projectPointToScreen(position);

        if (positionInScreen.z > 1.0) {
            return;
        }

        var w = guiGraphics.guiWidth() / 2;
        var h = guiGraphics.guiHeight() / 2;

        var imageScale = 16 * scale;

        var x = Mth.clamp(w + positionInScreen.x * w, imageScale, guiGraphics.guiWidth() - imageScale);
        var y = Mth.clamp(h - positionInScreen.y * h, imageScale, guiGraphics.guiHeight() - imageScale);

        guiGraphics.blit(TRANSLOCATION_ANCHOR_MARKER,
                (int) Math.round(x - imageScale),
                (int) Math.round(y - imageScale),
                (int) Math.round(x + imageScale),
                (int) Math.round(y + imageScale),
                0, 1, 0, 1);
    }

    private static float getAnchorScale(float falloffSq, float distanceSq) {
        return Mth.clamp(1f - distanceSq / falloffSq, 0.2f, 1f);
    }
}
