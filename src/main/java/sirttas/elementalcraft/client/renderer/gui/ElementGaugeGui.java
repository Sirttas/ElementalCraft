package sirttas.elementalcraft.client.renderer.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
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
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.handler.IJewelHandler;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.spell.tick.SpellTickHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ElementGaugeGui {

    private static final Identifier GAUGE = ElementalCraftApi.identifier("textures/gui/element_gauge.png");

    private ElementGaugeGui() {}

    public static void drawGauge(GuiGraphicsExtractor guiGraphics, @SuppressWarnings("unused") DeltaTracker deltaTracker) {
        var minecraft = Minecraft.getInstance();
        var player = minecraft.player;

        if (player == null) {
            return;
        }

        var font = minecraft.font;
        var i = 0;

        var x = guiGraphics.guiWidth() / 2 + ECConfig.CLIENT.gaugeOffsetX.get();
        var y = guiGraphics.guiHeight() / 2 + ECConfig.CLIENT.gaugeOffsetY.get();

        for (var state : getGaugesFromTargetBlock(player)) {
            render(guiGraphics, font, x + 16 + (20 * i), y - 8, state);
            i++;
        }

        i = 0;

        for (var state : getGaugesFromPlayerInventory(player)) {
            render(guiGraphics, font, x - 32 - (20 * i), y - 8, state);
            i++;
        }
    }

    private static void render(GuiGraphicsExtractor guiGraphics, Font font, int x, int y, GaugeRenderState state) {
        renderElementGauge(guiGraphics, font, x, y, state.elementType, state.amount, state.max, true);
        if (state.check != Check.NONE) {
            renderCheck(guiGraphics, state.check, x + 11, y + 11);
        }
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x - 4, y - 4);
        guiGraphics.pose().scale(0.5F);
        guiGraphics.item(state.itemStack, 0, 0);
        guiGraphics.pose().popMatrix();
    }

    private static List<GaugeRenderState> getGaugesFromTargetBlock(Player player) {
        Minecraft minecraft = Minecraft.getInstance();
        HitResult result = minecraft.hitResult;

        if (result != null && minecraft.options.getCameraType().isFirstPerson()) {
            var level = player.level();
            BlockPos pos = result.getType() == HitResult.Type.BLOCK ? ((BlockHitResult) result).getBlockPos() : null;
            var state = pos != null ? level.getBlockState(pos) : null;
            var storage = pos != null ? level.getCapability(ElementalCraftCapabilities.ElementStorages.BLOCK, pos, state, null, null) : null;
            List<ISingleElementStorage> storages = storage != null && (storage.doesRenderGauge(player) || showDebugInfo()) ? splitStorage(storage) : List.of();

            if (!storages.isEmpty()) {
                return storages.stream()
                        .map(s -> GaugeRenderState.from(s, state))
                        .toList();
            }
        }
        return List.of();
    }

    private static List<GaugeRenderState> getGaugesFromPlayerInventory(Player player) {
        var inHandStates = EntityHelper.handStream(player)
                .flatMap(stack -> {
                    var storage = stack.getCapability(ElementalCraftCapabilities.ElementStorages.ITEM);

                    if (storage == null) {
                        return Stream.empty();
                    }
                    return splitStorage(storage).stream()
                            .map(s -> GaugeRenderState.from(s, Check.NONE, stack));
                }).toList();

        if (!inHandStates.isEmpty()) {
            return inHandStates;
        }

        var playerStorage = player.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY);

        if (playerStorage == null) {
            return List.of();
        }

        var holder = EntityHelper.handStream(player)
                .map(stack -> {
                    if (!stack.isEmpty()) {
                        return SpellHelper.getSpell(stack);
                    }
                    return Spells.NONE;
                })
                .filter(h -> h.value().isValid())
                .findFirst()
                .orElse(Spells.NONE);
        var spell = holder.value();

        var spellElementType = spell.getElementType();
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
        return splitStorage(playerStorage, list).stream()
                .map(s -> GaugeRenderState.from(s, getSpellCheck(player, s, holder), ItemStack.EMPTY))
                .toList();
    }

    private static Check getSpellCheck(Player player, ISingleElementStorage storage, Holder<Spell> holder) {
        var spell = holder.value();

        if (!spell.isValid() || spell.getElementType() != storage.getElementType() || !isPlayerOwned(player, storage)) {
            return Check.NONE;
        }
        var canCast = spell.consume(player, true);
        var isInCooldown = SpellTickHelper.hasCooldown(player, holder);

        if (canCast && !isInCooldown) {
            return Check.VALID;
        } else if (isInCooldown) {
            return Check.PAUSED;
        }
        return Check.INVALID;
    }

    private static boolean isPlayerOwned(Player player, ISingleElementStorage storage) {
        if (storage instanceof SingleElementStorageWrapper wrapper && wrapper.getParent() instanceof PlayerElementStorage playerStorage) {
            return player.equals(playerStorage.getPlayer());
        }
        return false;
    }

    private static List<ISingleElementStorage> splitStorage(IElementStorage storage) {
        if (storage instanceof ISingleElementStorage singleElementStorage) {
            return List.of(singleElementStorage);
        }
        return splitStorage(storage, ElementType.ALL_VALID);
    }

    private static List<ISingleElementStorage> splitStorage(IElementStorage storage, List<ElementType> elementTypes) {
        if (storage instanceof ISingleElementStorage singleElementStorage) {
            return elementTypes.contains(singleElementStorage.getElementType()) ? List.of(singleElementStorage) : List.of();
        }
        return elementTypes.stream()
                .<ISingleElementStorage>mapMulti((type, downstream) -> downstream.accept(storage.forElement(type)))
                .filter(s -> s.getElementCapacity() > 0)
                .toList();
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

    public static void renderElementGauge(GuiGraphicsExtractor guiGraphics, Font font, int x, int y, ElementType elementType, int amount, int max) {
        renderElementGauge(guiGraphics, font, x, y, elementType, amount, max, false);
    }

    private static void renderElementGauge(GuiGraphicsExtractor guiGraphics, Font font, int x, int y, ElementType elementType, int amount, int max, boolean showDebugInfo) {
        blitGauge(guiGraphics, x, y, 0.0F, 0.0F, 16, 16);

        int progress = Math.max(0, (int) ((double) Math.min(amount, max) / (double) max * 16));

        if (progress <= 1 && amount > 0) {
            progress = 2;
        }
        blitGauge(guiGraphics, x, y + 16 - progress, getElementTypeOffset(elementType) * 16, 16 - progress + (ECConfig.CLIENT.usePaleElementGauge.get() ? 16 : 0), 16, progress);
        if (showDebugInfo() && showDebugInfo) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(x, y + 18);
            guiGraphics.pose().scale(0.5F);
            guiGraphics.text(font, String.valueOf(amount), 0, 0, -2039584, true);
            guiGraphics.text(font, String.valueOf(max), 0, font.lineHeight + 2, -2039584, true);
            guiGraphics.pose().popMatrix();

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
        NONE(-1),
        VALID(0),
        PAUSED(6),
        INVALID(12);

        private final int offset;

        Check(int offset) {
            this.offset = offset;
        }
    }

    private record GaugeRenderState(ElementType elementType, int amount, int max, Check check, ItemStack itemStack) {

        public static GaugeRenderState from(ISingleElementStorage storage, Check check, ItemStack itemStack) {
            return new GaugeRenderState(storage.getElementType(), storage.getElementAmount(), storage.getElementCapacity(), check,  itemStack);
        }

        public static GaugeRenderState from(ISingleElementStorage storage, BlockState state) {
            if (storage instanceof ShrineElementStorage shrineStorage) {
                return from(storage, getShrineCheck(shrineStorage), new ItemStack(shrineStorage.getShrine().getBlockState().getBlock().asItem()));
            }
            return from(storage, Check.NONE, state != null && !state.isAir() ? new ItemStack(state.getBlock().asItem()) : ItemStack.EMPTY);
        }

        private static Check getShrineCheck(ShrineElementStorage shrineStorage) {
            var shrine = shrineStorage.getShrine();

            if (shrine.isRunning()) {
                return Check.VALID;
            } else if (shrineStorage.getElementAmount() >= shrine.getConsumeAmount()) {
                return Check.PAUSED;
            }
            return Check.INVALID;
        }
    }
}
