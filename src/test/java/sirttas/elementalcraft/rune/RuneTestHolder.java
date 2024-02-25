package sirttas.elementalcraft.rune;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestFunction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Rotation;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.instrument.io.mill.MillTestHolder;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public record RuneTestHolder(
        String template,
        BlockPos pos,
        Direction side,
        List<ResourceKey<Rune>> runes
) {

    public static final List<RuneTestHolder> HOLDERS = Stream.concat(Stream.of(
                of("chiselgametests.inscriber", Runes.MEWTWO),
                of("extractor_with_runes", Runes.MEWTWO, Runes.TANO, Runes.TANO),
                of("infusergametests.infuser", Runes.CREATIVE),
                of("bindergametests.binder", Runes.CREATIVE),
                of("purifiergametests.ore_purifier", Runes.CREATIVE),
                of("chiselgametests.sorter_with_rune", new BlockPos(0, 1, 0), Runes.ZOD),
                of("enchantmentliquefiergametests.should_transferenchantment", Runes.CREATIVE),
                of("enchantmentliquefiergametests.should_transferenchantment", new BlockPos(0, 3, 0), Runes.CREATIVE),
                of("sourcebreedergametests.source_breeder", new BlockPos(0, 1, 2), Runes.CREATIVE),
                of("pureinfusergametests.pure_infuser", new BlockPos(3, 1, 3), Runes.CREATIVE),
                of("greaterfortuneshrineupgradegametests.should_increaseoreloot", new BlockPos(12, 2, 13), Runes.TZEENTCH),
                of("elementpumpgametests.should_transfer1250elements", new BlockPos(0, 2, 1), Direction.NORTH, Runes.ZOD, Runes.ZOD, Runes.ZOD)
        ), MillTestHolder.HOLDERS.stream()
            .map(MillTestHolder::template)
            .distinct()
            .map(t -> of(t, Runes.CREATIVE))
    ).toList();

    public static final String BATCH_NAME = "rune";

    public static RuneTestHolder of(String template, BlockPos pos, Direction side, ResourceKey<Rune>... runes) {
        return new RuneTestHolder(template, pos, side, List.of(runes));
    }

    public static RuneTestHolder of(String template, BlockPos pos, ResourceKey<Rune>... runes) {
        return of(template, pos, null, runes);
    }

    public static RuneTestHolder of(String template, ResourceKey<Rune>... runes) {
        return of(template, new BlockPos(0, 2, 0), runes);
    }

    public TestFunction createTestFunction(String name, BiConsumer<GameTestHelper, RuneTestHolder> function) {
        return ECGameTestHelper.createTestFunction(BATCH_NAME, name, template, Rotation.NONE, h -> function.accept(h, this));
    }
}
