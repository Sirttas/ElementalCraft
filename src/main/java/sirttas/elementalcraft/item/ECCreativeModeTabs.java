package sirttas.elementalcraft.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.container.AbstractElementContainerBlock;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.item.holder.ElementHolderItem;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellHelper;
import sirttas.elementalcraft.spell.Spells;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public class ECCreativeModeTabs {

    private static final DeferredRegister<CreativeModeTab> DEFERRED_REGISTER = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ElementalCraftApi.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ELEMENTAL_CRAFT_CREATIVE_TAB = DEFERRED_REGISTER.register("elemental_craft", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.elementalcraft"))
            .icon(() -> new ItemStack(ECItems.FOCUS.get()))
            .displayItems((p, o) -> {
                o.accept(ECBlocks.EXTRACTOR.get());
                o.accept(ECBlocks.EXTRACTOR_IMPROVED.get());
                o.accept(ECBlocks.EVAPORATOR.get());
                o.accept(ECBlocks.INFUSER.get());
                o.accept(ECBlocks.SOLAR_SYNTHESIZER.get());
                if (ECinteractions.isBotaniaActive()) {
                    o.accept(ECBlocks.MANA_SYNTHESIZER.get());
                }
                o.accept(ECBlocks.DIFFUSER.get());
                o.accept(ECBlocks.BINDER.get());
                o.accept(ECBlocks.BINDER_IMPROVED.get());
                o.accept(ECBlocks.CRYSTALLIZER.get());
                o.accept(ECBlocks.INSCRIBER.get());
                o.accept(ECBlocks.WATER_MILL_GRINDSTONE.get());
                o.accept(ECBlocks.AIR_MILL_GRINDSTONE.get());
                o.accept(ECBlocks.WATER_MILL_WOOD_SAW.get());
                o.accept(ECBlocks.AIR_MILL_WOOD_SAW.get());
                o.accept(ECBlocks.ENCHANTMENT_LIQUEFIER.get());
                o.accept(ECBlocks.FIRE_PEDESTAL.get());
                o.accept(ECBlocks.WATER_PEDESTAL.get());
                o.accept(ECBlocks.EARTH_PEDESTAL.get());
                o.accept(ECBlocks.AIR_PEDESTAL.get());
                o.accept(ECBlocks.PURE_INFUSER.get());
                o.accept(ECBlocks.FIRE_FURNACE.get());
                o.accept(ECBlocks.FIRE_BLAST_FURNACE.get());
                o.accept(ECBlocks.PURIFIER.get());
                generateElementContainer(o, ECBlocks.SMALL_CONTAINER);
                generateElementContainer(o, ECBlocks.CONTAINER);
                generateElementContainer(o, ECBlocks.FIRE_RESERVOIR);
                generateElementContainer(o, ECBlocks.WATER_RESERVOIR);
                generateElementContainer(o, ECBlocks.EARTH_RESERVOIR);
                generateElementContainer(o, ECBlocks.AIR_RESERVOIR);
                generateElementContainer(o, ECBlocks.CREATIVE_CONTAINER);
                o.accept(ECBlocks.PIPE_IMPAIRED.get());
                o.accept(ECBlocks.PIPE.get());
                o.accept(ECBlocks.PIPE_IMPROVED.get());
                o.accept(ECBlocks.PIPE_CREATIVE.get());
                o.accept(ECItems.COVER_FRAME.get());
                o.accept(ECItems.ELEMENT_PUMP.get());
                o.accept(ECItems.PIPE_PRIORITY_RINGS.get());
                o.accept(ECItems.ELEMENT_VALVE.get());
                o.accept(ECItems.ELEMENT_BEAM.get());
                o.accept(ECBlocks.RETRIEVER.get());
                o.accept(ECBlocks.SORTER.get());
                o.accept(ECBlocks.SPELL_DESK.get());
                o.accept(ECBlocks.FIRE_PYLON.get());
                o.accept(ECBlocks.VACUUM_SHRINE.get());
                o.accept(ECBlocks.GROWTH_SHRINE.get());
                o.accept(ECBlocks.HARVEST_SHRINE.get());
                o.accept(ECBlocks.LUMBER_SHRINE.get());
                o.accept(ECBlocks.LAVA_SHRINE.get());
                o.accept(ECBlocks.ORE_SHRINE.get());
                o.accept(ECBlocks.OVERLOAD_SHRINE.get());
                o.accept(ECBlocks.SWEET_SHRINE.get());
                o.accept(ECBlocks.ENDER_LOCK_SHRINE.get());
                o.accept(ECBlocks.BREEDING_SHRINE.get());
                o.accept(ECBlocks.GROVE_SHRINE.get());
                o.accept(ECBlocks.SPRING_SHRINE.get());
                o.accept(ECBlocks.BUDDING_SHRINE.get());
                o.accept(ECBlocks.SPAWNING_SHRINE.get());
                o.accept(ECBlocks.ACCELERATION_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.RANGE_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.CAPACITY_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.EFFICIENCY_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.STRENGTH_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.OVERWHELMING_STRENGTH_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.OPTIMIZATION_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.FORTUNE_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.SILK_TOUCH_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.PLANTING_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.BONELESS_GROWTH_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.PICKUP_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.VORTEX_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.NECTAR_SHRINE_UPGRADE.get());
                if (ECinteractions.isBotaniaActive()) {
                    o.accept(ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE.get());
                }
                o.accept(ECBlocks.STEM_POLLINATION_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.PROTECTION_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.FILLING_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.SPRINGALINE_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.CRYSTAL_HARVEST_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.CRYSTAL_GROWTH_SHRINE_UPGRADE.get());
                o.accept(ECBlocks.TRANSLOCATION_SHRINE_UPGRADE.get());

                o.accept(ECBlocks.BROKEN_SOURCE_DISPLACEMENT_PLATE.get());
                o.accept(ECBlocks.FIRE_SOURCE_DISPLACEMENT_PLATE.get());
                o.accept(ECBlocks.WATER_SOURCE_DISPLACEMENT_PLATE.get());
                o.accept(ECBlocks.EARTH_SOURCE_DISPLACEMENT_PLATE.get());
                o.accept(ECBlocks.AIR_SOURCE_DISPLACEMENT_PLATE.get());
                o.accept(ECBlocks.SOURCE_BREEDER.get());
                o.accept(ECBlocks.SOURCE_BREEDER_PEDESTAL.get());
                o.accept(ECItems.ARTIFICIAL_FIRE_SOURCE_SEED.get());
                o.accept(ECItems.ARTIFICIAL_WATER_SOURCE_SEED.get());
                o.accept(ECItems.ARTIFICIAL_EARTH_SOURCE_SEED.get());
                o.accept(ECItems.ARTIFICIAL_AIR_SOURCE_SEED.get());
                o.accept(ECItems.NATURAL_FIRE_SOURCE_SEED.get());
                o.accept(ECItems.NATURAL_WATER_SOURCE_SEED.get());
                o.accept(ECItems.NATURAL_EARTH_SOURCE_SEED.get());
                o.accept(ECItems.NATURAL_AIR_SOURCE_SEED.get());
                o.accept(ECBlocks.TRANSLOCATION_ANCHOR.get());
                o.accept(ECBlocks.CRYSTAL_ORE.get());
                o.accept(ECBlocks.DEEPSLATE_CRYSTAL_ORE.get());
                o.accept(ECBlocks.WHITE_ROCK.get());
                o.accept(ECBlocks.WHITE_ROCK_SLAB.get());
                o.accept(ECBlocks.WHITE_ROCK_STAIRS.get());
                o.accept(ECBlocks.WHITE_ROCK_WALL.get());
                o.accept(ECBlocks.WHITE_ROCK_FENCE.get());
                o.accept(ECBlocks.WHITE_ROCK_BRICK.get());
                o.accept(ECBlocks.WHITE_ROCK_BRICK_SLAB.get());
                o.accept(ECBlocks.WHITE_ROCK_BRICK_STAIRS.get());
                o.accept(ECBlocks.WHITE_ROCK_BRICK_WALL.get());
                o.accept(ECBlocks.MOSSY_WHITE_ROCK.get());
                o.accept(ECBlocks.MOSSY_WHITE_ROCK_SLAB.get());
                o.accept(ECBlocks.MOSSY_WHITE_ROCK_STAIRS.get());
                o.accept(ECBlocks.MOSSY_WHITE_ROCK_WALL.get());
                o.accept(ECBlocks.BURNT_WHITE_ROCK.get());
                o.accept(ECBlocks.BURNT_WHITE_ROCK_SLAB.get());
                o.accept(ECBlocks.BURNT_WHITE_ROCK_STAIRS.get());
                o.accept(ECBlocks.BURNT_WHITE_ROCK_WALL.get());
                o.accept(ECBlocks.BURNT_GLASS.get());
                o.accept(ECBlocks.BURNT_GLASS_PANE.get());
                o.accept(ECBlocks.PURE_ROCK.get());
                o.accept(ECBlocks.PURE_ROCK_SLAB.get());
                o.accept(ECBlocks.PURE_ROCK_STAIRS.get());
                o.accept(ECBlocks.PURE_ROCK_WALL.get());

                generateElementopedia(o);
                o.accept(ECItems.FOCUS.get());
                o.accept(ECItems.STAFF.get());
                generateSpells(o);
                o.accept(ECItems.SPELL_BOOK.get());
                o.accept(ECItems.SOURCE_ANALYSIS_GLASS.get());
                generateReceptacles(o);
                o.accept(ECItems.SOURCE_STABILIZER.get());
                generateElementHolder(o, ECItems.FIRE_HOLDER);
                generateElementHolder(o, ECItems.WATER_HOLDER);
                generateElementHolder(o, ECItems.EARTH_HOLDER);
                generateElementHolder(o, ECItems.AIR_HOLDER);
                o.accept(ECItems.PURE_HOLDER_CORE.get());
                generatePureElementHolder(o);
                o.accept(ECItems.CHISEL.get());
                o.accept(ECItems.ELEMENTAL_FIREFUEL.get());
                generatePureOres(o);
                o.accept(ECItems.INERT_CRYSTAL.get());
                o.accept(ECBlocks.INERT_CRYSTAL_BLOCK.get());
                o.accept(ECItems.CONTAINED_CRYSTAL.get());
                o.accept(ECItems.STRONGLY_CONTAINED_CRYSTAL.get());
                o.accept(ECItems.FIRE_CRYSTAL.get());
                o.accept(ECBlocks.FIRE_CRYSTAL_BLOCK.get());
                o.accept(ECItems.WATER_CRYSTAL.get());
                o.accept(ECBlocks.WATER_CRYSTAL_BLOCK.get());
                o.accept(ECItems.EARTH_CRYSTAL.get());
                o.accept(ECBlocks.EARTH_CRYSTAL_BLOCK.get());
                o.accept(ECItems.AIR_CRYSTAL.get());
                o.accept(ECBlocks.AIR_CRYSTAL_BLOCK.get());
                o.accept(ECItems.PURE_CRYSTAL.get());
                o.accept(ECItems.FIRE_SHARD.get());
                o.accept(ECItems.WATER_SHARD.get());
                o.accept(ECItems.EARTH_SHARD.get());
                o.accept(ECItems.AIR_SHARD.get());
                o.accept(ECItems.POWERFUL_FIRE_SHARD.get());
                o.accept(ECItems.POWERFUL_WATER_SHARD.get());
                o.accept(ECItems.POWERFUL_EARTH_SHARD.get());
                o.accept(ECItems.POWERFUL_AIR_SHARD.get());
                o.accept(ECItems.CRUDE_FIRE_GEM.get());
                o.accept(ECItems.CRUDE_WATER_GEM.get());
                o.accept(ECItems.CRUDE_EARTH_GEM.get());
                o.accept(ECItems.CRUDE_AIR_GEM.get());
                o.accept(ECItems.FINE_FIRE_GEM.get());
                o.accept(ECItems.FINE_WATER_GEM.get());
                o.accept(ECItems.FINE_EARTH_GEM.get());
                o.accept(ECItems.FINE_AIR_GEM.get());
                o.accept(ECItems.PRISTINE_FIRE_GEM.get());
                o.accept(ECItems.PRISTINE_WATER_GEM.get());
                o.accept(ECItems.PRISTINE_EARTH_GEM.get());
                o.accept(ECItems.PRISTINE_AIR_GEM.get());
                o.accept(ECItems.DRENCHED_IRON_NUGGET.get());
                o.accept(ECItems.DRENCHED_IRON_INGOT.get());
                o.accept(ECBlocks.DRENCHED_IRON_BLOCK.get());
                o.accept(ECItems.SWIFT_ALLOY_NUGGET.get());
                o.accept(ECItems.SWIFT_ALLOY_INGOT.get());
                o.accept(ECBlocks.SWIFT_ALLOY_BLOCK.get());
                o.accept(ECItems.FIREITE_NUGGET.get());
                o.accept(ECItems.FIREITE_INGOT.get());
                o.accept(ECBlocks.FIREITE_BLOCK.get());
                o.accept(ECItems.SPRINGALINE_SHARD.get());
                o.accept(ECBlocks.SPRINGALINE_BLOCK.get());
                o.accept(ECBlocks.SMALL_SPRINGALINE_BUD.get());
                o.accept(ECBlocks.MEDIUM_SPRINGALINE_BUD.get());
                o.accept(ECBlocks.LARGE_SPRINGALINE_BUD.get());
                o.accept(ECBlocks.SPRINGALINE_CLUSTER.get());
                o.accept(ECBlocks.SPRINGALINE_GLASS.get());
                o.accept(ECBlocks.SPRINGALINE_GLASS_PANE.get());
                o.accept(ECBlocks.SPRINGALINE_LANTERN.get());
                o.accept(ECItems.SOLAR_PRISM.get());
                o.accept(ECItems.FIRE_LENS.get());
                o.accept(ECItems.WATER_LENS.get());
                o.accept(ECItems.EARTH_LENS.get());
                o.accept(ECItems.AIR_LENS.get());
                o.accept(ECItems.AIR_SILK.get());
                o.accept(ECItems.HARDENED_HANDLE.get());
                o.accept(ECItems.DRENCHED_SAW_BLADE.get());
                o.accept(ECItems.SCROLL_PAPER.get());
                o.accept(ECItems.SHRINE_BASE.get());
                o.accept(ECItems.SHRINE_UPGRADE_CORE.get());
                o.accept(ECItems.ADVANCED_SHRINE_UPGRADE_CORE.get());
                o.accept(ECItems.MINOR_RUNE_SLATE.get());
                o.accept(ECItems.RUNE_SLATE.get());
                o.accept(ECItems.MAJOR_RUNE_SLATE.get());
                generateRunes(o);
                o.accept(ECItems.UNSET_JEWEL.get());
                generateJewels(o);
            }).build());

    private static void generateElementopedia(@Nonnull CreativeModeTab.Output output) {
        if (ECinteractions.isPatchouliActive()) {
            output.accept(createElementopedia());
        }
    }

    @NotNull
    public static ItemStack createElementopedia() {
        var book = new ItemStack(ECItems.ELEMENTOPEDIA.get());

        book.getOrCreateTag().putString("patchouli:book", "elementalcraft:element_book");
        return book;
    }

    private static void generateElementContainer(@Nonnull CreativeModeTab.Output output, @Nonnull Supplier<? extends AbstractElementContainerBlock> supplier) {
        var block = supplier.get();
        var item = block.asItem();

        output.accept(new ItemStack(item));
        for (ElementType type : block instanceof IElementTypeProvider provider ? List.of(provider.getElementType()) : ElementType.ALL_VALID) {
            ItemStack stack = new ItemStack(item);
            CompoundTag tag = stack.getOrCreateTagElement(ECNames.BLOCK_ENTITY_TAG);

            tag.put(ECNames.ELEMENT_STORAGE, new SingleElementStorage(type, block.getDefaultCapacity(), block.getDefaultCapacity()).serializeNBT());
            output.accept(stack);
        }
    }

    private static void generateElementHolder(@Nonnull CreativeModeTab.Output output, @Nonnull Supplier<? extends ElementHolderItem> supplier) {
        var item = supplier.get();
        var full = new ItemStack(item);
        var storage = item.getElementStorage(full);

        storage.insertElement(item.getElementCapacity(), false);
        output.accept(new ItemStack(item));
        output.accept(full);
    }

    private static void generatePureElementHolder(@Nonnull CreativeModeTab.Output output) {
        var item = ECItems.PURE_HOLDER.get();
        var full = new ItemStack(item);
        var storage = item.getElementStorage(full);

        ElementType.ALL_VALID.forEach(elementType -> storage.insertElement(item.getElementCapacity(), elementType, false));
        output.accept(new ItemStack(item));
        output.accept(full);
    }

    private static void generateSpells(@Nonnull CreativeModeTab.Output output) {
        Spells.REGISTRY.stream()
                .filter(Spell::isVisible)
                .map(s -> {
                    var stack = new ItemStack(ECItems.SCROLL.get());

                    SpellHelper.setSpell(stack, s);
                    return stack;
                }).forEach(output::accept);
    }

    private static void generateReceptacles(@Nonnull CreativeModeTab.Output output) {
        ElementType.ALL_VALID.stream()
                .map(ReceptacleHelper::create)
                .forEach(output::accept);
    }

    private static void generatePureOres(@Nonnull CreativeModeTab.Output output) {
        ElementalCraft.PURE_ORE_MANAGER.getOres().forEach(id -> output.accept(ElementalCraft.PURE_ORE_MANAGER.createPureOre(id)));
    }
    private static void generateRunes(@Nonnull CreativeModeTab.Output output) {
        var item = ECItems.RUNE.get();

        ElementalCraftApi.RUNE_MANAGER.getData().forEach((l, r) -> output.accept(item.getRuneStack(r)));
    }

    private static void generateJewels(@Nonnull CreativeModeTab.Output output) {
        var item = ECItems.JEWEL.get();

        Jewels.REGISTRY.forEach(j -> {
            if (j != Jewels.NONE.get()) {
                output.accept(item.getJewelStack(j));
            }
        });
    }

    private ECCreativeModeTabs() { }

    public static void register(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }

}
