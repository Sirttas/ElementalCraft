package sirttas.elementalcraft.block.instrument.io.mill;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.testframework.Test;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.ECGameTestUtils;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.InstrumentTestTemplates;

import java.util.List;
import java.util.function.BiConsumer;

public record MillTestCaseHolder(
        String template,
        ItemLike input,
        ItemLike output,
        ElementType type
) {
    public static final String GROUP = "level.blocks.instruments.mills";

    public static final List<MillTestCaseHolder> HOLDERS = List.of(
            of(InstrumentTestTemplates.WATER_MILL_GRINDSTONE_TEMPLATE_NAME, Items.GRAVEL, Items.SAND, ElementType.WATER),
            of(InstrumentTestTemplates.AIR_MILL_GRINDSTONE_TEMPLATE_NAME, Items.GRAVEL, Items.SAND, ElementType.AIR),
            of(InstrumentTestTemplates.WATER_MILL_WOOD_SAW_TEMPLATE_NAME, Items.OAK_LOG, Items.STRIPPED_OAK_LOG, ElementType.WATER),
            of(InstrumentTestTemplates.AIR_MILL_WOOD_SAW_TEMPLATE_NAME, Items.OAK_LOG, Items.STRIPPED_OAK_LOG, ElementType.AIR)
    );

    public static MillTestCaseHolder of(String template, ItemLike input, ItemLike output, ElementType type) {
        return new MillTestCaseHolder(template, input, output, type);
    }

    public Test createTest(String name, String description, BiConsumer<ECGameTestHelper, MillTestCaseHolder> function) {
        return ECGameTestUtils.createTest(GROUP, name, description, template, h -> function.accept(h, this));
    }
}
