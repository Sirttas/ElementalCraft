package sirttas.elementalcraft.rune;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.List;

public record RuneTestHolder(
        String template,
        BlockPos pos,
        Direction side,
        List<String> runes
) {

    public static final List<RuneTestHolder> HOLDERS = List.of(
            of("elementalcraft:chiselitemgametests.inscriber", "mewtwo"),
            of("elementalcraft:extractor_with_runes", "mewtwo", "tano", "tano"),
            of("elementalcraft:infusergametests.infuser", "creative"),
            of("elementalcraft:chiselitemgametests.sorter_with_rune", new BlockPos(0, 1, 0), "zod"),
            of("elementalcraft:sourcebreedergametests.source_breeder", new BlockPos(0, 1, 2), "creative"),
            of("elementalcraft:pureinfusergametests.pure_infuser", new BlockPos(3, 1, 3), "creative")
    );

    public static RuneTestHolder of(String template, BlockPos pos, Direction side, String... runes) {
        return new RuneTestHolder(template, pos, side, List.of(runes));
    }

    public static RuneTestHolder of(String template, BlockPos pos, String... runes) {
        return of(template, pos, null, runes);
    }

    public static RuneTestHolder of(String template, String... runes) {
        return of(template, new BlockPos(0, 2, 0), runes);
    }
}
