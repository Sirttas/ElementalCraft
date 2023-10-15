package sirttas.elementalcraft.block.instrument.io.mill;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Rotation;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.element.ElementType;

import java.util.List;
import java.util.function.BiConsumer;

public record MillTestHolder(
        String template,
        ItemLike input,
        ItemLike output,
        ElementType type
) {
    public static final String BATCH_NAME = "mill";

    public static final List<MillTestHolder> HOLDERS = List.of(
            of("water_mill_grindstone", Items.GRAVEL, Items.SAND, ElementType.WATER),
            of("air_mill_grindstone", Items.GRAVEL, Items.SAND, ElementType.AIR),
            of("water_mill_wood_saw", Items.OAK_LOG, Items.STRIPPED_OAK_LOG, ElementType.WATER),
            of("air_mill_wood_saw", Items.OAK_LOG, Items.STRIPPED_OAK_LOG, ElementType.AIR)
    );

    public static MillTestHolder of(String template, ItemLike input, ItemLike output, ElementType type) {
        return new MillTestHolder("millgametests." + template, input, output, type);
    }

    public TestFunction createTestFunction(String name, BiConsumer<GameTestHelper, MillTestHolder> function) {
        return ECGameTestHelper.createTestFunction(BATCH_NAME, name, template, Rotation.NONE, h -> function.accept(h, this));
    }
}
