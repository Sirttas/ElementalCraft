package sirttas.elementalcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.block.instrument.InstrumentTestTemplates;
import sirttas.elementalcraft.block.instrument.io.mill.MillTestCaseHolder;
import sirttas.elementalcraft.item.ECItems;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record ECContainerBlockTestCaseHolder(
        String template,
        Supplier<ItemLike> item,
        BlockPos pos,
        int slot
) {
    public static final String GROUP = "level.blocks.container";

    public static final List<ECContainerBlockTestCaseHolder> HOLDERS = Stream.concat(Stream.of(
            of(InstrumentTestTemplates.INFUSER_TEMPLATE_NAME, ECItems.INERT_CRYSTAL, new BlockPos(0, 1, 0), 0),
            of(InstrumentTestTemplates.BINDER_TEMPLATE_NAME, ECItems.FIRE_CRYSTAL, new BlockPos(0, 1, 0), 0),
            of(InstrumentTestTemplates.CRYSTALLIZER_TEMPLATE_NAME, ECItems.CRUDE_FIRE_GEM, new BlockPos(0, 1, 0), 0),
            of(InstrumentTestTemplates.CRYSTALLIZER_TEMPLATE_NAME, ECItems.FIRE_CRYSTAL, new BlockPos(0, 1, 0), 1),
            of(InstrumentTestTemplates.INSCRIBER_TEMPLATE_NAME, ECItems.RUNE_SLATE, new BlockPos(0, 1, 0), 0),
            of(InstrumentTestTemplates.ORE_PURIFIER_TEMPLATE_NAME, Items.DIAMOND_ORE, new BlockPos(0, 1, 0), 0),
            of(InstrumentTestTemplates.ENCHANTMENT_LIQUEFIER_TEMPLATE_NAME, Items.DIAMOND_SWORD, new BlockPos(0, 1, 0), 1),
            of(InstrumentTestTemplates.FIRE_FURNACE_TEMPLATE_NAME, Items.DIAMOND_ORE, new BlockPos(0, 1, 0), 0),
            of(InstrumentTestTemplates.FIRE_BLAST_FURNACE_TEMPLATE_NAME, Items.DIAMOND_ORE, new BlockPos(0, 1, 0), 0)
    ), MillTestCaseHolder.HOLDERS.stream()
            .map(t -> of(t.template(), t.input(), new BlockPos(0, 1, 0), 0))
    ).toList();


    public static ECContainerBlockTestCaseHolder of(String template, ItemLike item, BlockPos pos, int slot) {
        return new ECContainerBlockTestCaseHolder(template, () -> item, pos, slot);
    }

    public static ECContainerBlockTestCaseHolder of(String template, Supplier<? extends ItemLike> item, BlockPos pos, int slot) {
        return new ECContainerBlockTestCaseHolder(template, item::get, pos, slot);
    }

    public Test createTest(String name, String description, BiConsumer<ECGameTestHelper, ECContainerBlockTestCaseHolder> function) {
        return ECGameTestUtils.createTest(GROUP, name, description, template, h -> function.accept(h, this));
    }
}
