package sirttas.elementalcraft.datagen.interaction.patchouli.builder.page;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.datagen.interaction.patchouli.builder.PatchouliFile;
import sirttas.elementalcraft.datagen.language.TranslationKeyValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiblockPageBuilder implements PageBuilder {

    private static final MapCodec<MultiblockPageBuilder> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Multiblock.CODEC.fieldOf("multiblock").forGetter(b -> b.multiblock)
    ).apply(builder, (a1) -> {
        throw new UnsupportedOperationException("Builder deserialization is not supported.");
    }));
    private static final PageBuilderType TYPE = PageBuilderType.register("multiblock", CODEC);

    private final Multiblock multiblock;

    public MultiblockPageBuilder() {
        this.multiblock = new Multiblock();
    }

    public MultiblockPageBuilder define(Character symbol, BlockState block) {
        this.multiblock.mapping.put(symbol.toString(), block);
        return this;
    }

    public MultiblockPageBuilder define(Character symbol, Block block) {
        return define(symbol, block.defaultBlockState());
    }

    public MultiblockPageBuilder patternLayer(String... pattern) {
        this.multiblock.pattern.add(List.of(pattern));
        return this;
    }

    @Override
    public PageBuilderType getType() {
        return TYPE;
    }

    @Override
    public void validate(TranslationKeyValidator translationKeyValidator) {
        for (List<String> layer : multiblock.pattern) {
            for (String row : layer) {
                for (char symbol : row.toCharArray()) {
                    if (!multiblock.mapping.containsKey(String.valueOf(symbol)) && symbol != ' ') {
                        throw new IllegalStateException("Multiblock pattern contains undefined symbol: " + symbol);
                    }
                }
            }
        }
    }

    private static class Multiblock {

        private static final Codec<Multiblock> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.unboundedMap(Codec.STRING, PatchouliFile.STATE_CODEC).optionalFieldOf("mapping", null).forGetter(b -> b.mapping),
                Codec.STRING.listOf().listOf().optionalFieldOf("pattern", null).forGetter(b -> b.pattern)
        ).apply(builder, (a1, a2) -> {
            throw new UnsupportedOperationException("Builder deserialization is not supported.");
        }));

        private final Map<String, BlockState> mapping;
        private final List<List<String>> pattern;

        Multiblock() {
            this.mapping = new HashMap<>();
            this.pattern = new ArrayList<>();
        }
    }
}
