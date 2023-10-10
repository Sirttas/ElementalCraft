package sirttas.elementalcraft.rune;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.world.level.block.Rotation;
import sirttas.elementalcraft.ECGameTestHelper;

import java.util.List;
import java.util.function.BiConsumer;

public record RuneTestHolder(
        String template,
        BlockPos pos,
        Direction side,
        List<String> runes
) {

    public static final List<RuneTestHolder> HOLDERS = List.of(
            of("chiselgametests.inscriber", "mewtwo"),
            of("extractor_with_runes", "mewtwo", "tano", "tano"),
            of("infusergametests.infuser", "creative"),
            of("airmillgrindstonegametests.air_mill_grindstone", "creative"),
            of("watermillwoodsawgametests.water_mill_wood_saw", "creative"),
            of("purifiergametests.ore_purifier", "creative"),
            of("chiselgametests.sorter_with_rune", new BlockPos(0, 1, 0), "zod"),
            of("sourcebreedergametests.source_breeder", new BlockPos(0, 1, 2), "creative"),
            of("pureinfusergametests.pure_infuser", new BlockPos(3, 1, 3), "creative")
    );

    public static final String BATCH_NAME = "rune";

    public static RuneTestHolder of(String template, BlockPos pos, Direction side, String... runes) {
        return new RuneTestHolder(template, pos, side, List.of(runes));
    }

    public static RuneTestHolder of(String template, BlockPos pos, String... runes) {
        return of(template, pos, null, runes);
    }

    public static RuneTestHolder of(String template, String... runes) {
        return of(template, new BlockPos(0, 2, 0), runes);
    }

    public TestFunction createTestFunction(String name, BiConsumer<GameTestHelper, RuneTestHolder> function) {
        return ECGameTestHelper.createTestFunction(BATCH_NAME, name, template, Rotation.NONE, h -> function.accept(h, this));
    }
}
