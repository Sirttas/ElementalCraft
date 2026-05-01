package sirttas.elementalcraft.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorageWrapper;
import sirttas.elementalcraft.block.shrine.ShrineElementStorage;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.entity.player.PlayerElementStorage;
import sirttas.elementalcraft.item.spell.ISpellHolder;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.handler.IJewelHandler;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ElementGaugeGui {

    private static final Identifier GAUGE = ElementalCraftApi.createRL("textures/gui/element_gauge.png");

    private ElementGaugeGui() {}

    public static void drawGauge(GuiGraphicsExtractor guiGraphics, @SuppressWarnings("unused") DeltaTracker deltaTracker) {
        var player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

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
    }

    private static boolean isPlayerOwned(Player player, ISingleElementStorage storage) {
        if (storage instanceof SingleElementStorageWrapper wrapper && wrapper.getParent() instanceof PlayerElementStorage playerStorage) {
            return player.equals(playerStorage.getPlayer());
        }
        return false;
    }

    private static List<ISingleElementStorage> getElementStorage(Player player) {
        Minecraft minecraft = Minecraft.getInstance();
        HitResult result = minecraft.hitResult;

        if (result != null && minecraft.options.getCameraType().isFirstPerson()) {
            BlockPos pos = result.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) result).getBlockPos() : null;
            var storage = pos != null ? player.level().getCapability(ElementalCraftCapabilities.ElementStorages.BLOCK, pos, null) : null;
            List<ISingleElementStorage> storages = storage != null && (storage.doesRenderGauge(player) || showDebugInfo()) ? splitStorage(storage) : Collections.emptyList();

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
        renderElementGauge(guiGraphics, font, getXOffset() - 32 - (20 * index), getYOffset() - 8, element, max, type, true);
    }

    private static void renderShrineCheck(GuiGraphicsExtractor guiGraphics, ISingleElementStorage storage, ShrineElementStorage shrineStorage) {
        var shrine = shrineStorage.getShrine();

        if (shrine.isRunning()) {
            renderCheck(guiGraphics, Check.VALID);
        } else if (storage.getElementAmount() >= shrine.getConsumeAmount()) {
            renderCheck(guiGraphics, Check.PAUSED);
        } else {
            renderCheck(guiGraphics, Check.INVALID);
        }
    }

    private static void renderSpellCheck(GuiGraphicsExtractor guiGraphics, LocalPlayer player, Spell spell) {
        var canCast = spell.consume(player, true);
        var isInCooldown = SpellTickHelper.hasCooldown(player, spell);

        if (canCast && !isInCooldown) {
            renderCheck(guiGraphics, Check.VALID);
        } else if (isInCooldown) {
            renderCheck(guiGraphics, Check.PAUSED);
        } else {
            renderCheck(guiGraphics, Check.INVALID);
        }
    }

    private static void renderCheck(GuiGraphicsExtractor guiGraphics, Check valid) {
        renderCheck(guiGraphics, valid, getXOffset() - 21, getYOffset() + 3);
    }

    private static int getYOffset() {
        return Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2 + ECConfig.CLIENT.gaugeOffsetX.get();
    }

    private static int getXOffset() {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + ECConfig.CLIENT.gaugeOffsetY.get();
    }

    private static int getElementTypeOffset(ElementType type) {
        return switch (type) {
            case WATER -> 1;
            case FIRE -> 2;
            case EARTH -> 3;
            case AIR -> 4;
            default -> 0;
        };
    }

    public static void renderElementGauge(GuiGraphicsExtractor guiGraphics, Font font, int x, int y, int amount, int max, ElementType type) {
        renderElementGauge(guiGraphics, font, x, y, amount, max, type, false);
    }

    private static void renderElementGauge(GuiGraphicsExtractor guiGraphics, Font font, int x, int y, int amount, int max, ElementType type, boolean showDebugInfo) {
        blitGauge(guiGraphics, x, y, 0.0F, 0.0F, 16, 16);

        int progress = Math.max(0, (int) ((double) Math.min(amount, max) / (double) max * 16));

        if (progress <= 1 && amount > 0) {
            progress = 2;
        }
        blitGauge(guiGraphics, x, y + 16 - progress, getElementTypeOffset(type) * 16, 16 - progress + (ECConfig.CLIENT.usePaleElementGauge.get() ? 16 : 0), 16, progress);
        if (showDebugInfo() && showDebugInfo) {
            guiGraphics.text(font, amount + "/" + max, x, y + 16, -2039584, true);
        }
    }

    private static void renderCheck(GuiGraphicsExtractor guiGraphics, Check check, int x, int y) {
        blitGauge(guiGraphics, x, y, 0, 16 + check.offset, 6, 6);
    }

    private static boolean showDebugInfo() {
        Minecraft minecraft = Minecraft.getInstance();

        return minecraft.player.isCreative() && minecraft.options.advancedItemTooltips;
    }

    private static void blitGauge(GuiGraphicsExtractor guiGraphics, int x, int y, float u, float v, int width, int height) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GAUGE, x, y, u, v, width, height, 256, 256);
    }

    private enum Check {
        VALID(0),
        PAUSED(6),
        INVALID(12);

        private final int offset;

        Check(int offset) {
            this.offset = offset;
        }

        public int getOffset() {
            return offset;
        }
    }
}
