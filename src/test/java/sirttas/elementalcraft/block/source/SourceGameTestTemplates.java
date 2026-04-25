package sirttas.elementalcraft.block.source;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.source.trait.SourceTraitTestHelper;

import java.util.function.Supplier;

public class SourceGameTestTemplates {

    public static final String FIRE_SOURCE_TEMPLATE_NAME = "elementalcraft:fire_source";
    public static final String FIRE_SOURCE_ITH_STABILIZER_TEMPLATE_NAME = "elementalcraft:fire_source_with_stabilizer";
    public static final String WATER_SOURCE_TEMPLATE_NAME = "elementalcraft:water_source";
    public static final String WATER_SOURCE_WITH_STABILIZER_TEMPLATE_NAME = "elementalcraft:water_source_with_stabilizer";
    public static final String EARTH_SOURCE_TEMPLATE_NAME = "elementalcraft:earth_source";
    public static final String EARTH_SOURCE_WITH_STABILIZER_TEMPLATE_NAME = "elementalcraft:earth_source_with_stabilizer";
    public static final String AIR_SOURCE_TEMPLATE_NAME = "elementalcraft:air_source";
    public static final String AIR_SOURCE_WITH_STABILIZER_TEMPLATE_NAME = "elementalcraft:air_source_with_stabilizer";
    public static final String EMPTY_FOR_SOURCE_TEMPLATE_NAME = "elementalcraft:empty_for_source";

    @RegisterStructureTemplate(FIRE_SOURCE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> FIRE_SOURCE_TEMPLATE = createSourceTemplate(ElementType.FIRE);
    @RegisterStructureTemplate(FIRE_SOURCE_ITH_STABILIZER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> FIRE_SOURCE_WITH_STABILIZER_TEMPLATE = createSourceWithStabilizerTemplate(ElementType.FIRE);
    @RegisterStructureTemplate(WATER_SOURCE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> WATER_SOURCE_TEMPLATE = createSourceTemplate(ElementType.WATER);
    @RegisterStructureTemplate(WATER_SOURCE_WITH_STABILIZER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> WATER_SOURCE_WITH_STABILIZER_TEMPLATE = createSourceWithStabilizerTemplate(ElementType.WATER);
    @RegisterStructureTemplate(EARTH_SOURCE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> EARTH_SOURCE_TEMPLATE = createSourceTemplate(ElementType.EARTH);
    @RegisterStructureTemplate(EARTH_SOURCE_WITH_STABILIZER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> EARTH_SOURCE_WITH_STABILIZER_TEMPLATE = createSourceWithStabilizerTemplate(ElementType.EARTH);
    @RegisterStructureTemplate(AIR_SOURCE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> AIR_SOURCE_TEMPLATE = createSourceTemplate(ElementType.AIR);
    @RegisterStructureTemplate(AIR_SOURCE_WITH_STABILIZER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> AIR_SOURCE_WITH_STABILIZER_TEMPLATE = createSourceWithStabilizerTemplate(ElementType.AIR);
    @RegisterStructureTemplate(EMPTY_FOR_SOURCE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> EMPTY_FOR_SOURCE_TEMPLATE = StructureTemplateBuilder.lazy(1, 2, 1, builder -> builder.set(0, 0, 0, ECBlocks.WHITE_ROCK.get().defaultBlockState()));

    private SourceGameTestTemplates() {}

    public static String getSourceTemplate(ElementType type) {
        return switch (type) {
            case FIRE -> FIRE_SOURCE_TEMPLATE_NAME;
            case WATER -> WATER_SOURCE_TEMPLATE_NAME;
            case EARTH -> EARTH_SOURCE_TEMPLATE_NAME;
            case AIR -> AIR_SOURCE_TEMPLATE_NAME;
            default -> throw new IllegalArgumentException();
        };
    }

    public static String getSourceWithStabilizerTemplate(ElementType type) {
        return switch (type) {
            case FIRE -> FIRE_SOURCE_ITH_STABILIZER_TEMPLATE_NAME;
            case WATER -> WATER_SOURCE_WITH_STABILIZER_TEMPLATE_NAME;
            case EARTH -> EARTH_SOURCE_WITH_STABILIZER_TEMPLATE_NAME;
            case AIR -> AIR_SOURCE_WITH_STABILIZER_TEMPLATE_NAME;
            default -> throw new IllegalArgumentException();
        };
    }

    private static @NotNull Supplier<StructureTemplate> createSourceTemplate(ElementType type) {
        return StructureTemplateBuilder.lazy(1, 1, 1, builder -> {
            var sourceTag = new CompoundTag();

            sourceTag.put(ECNames.SOURCE_TRAITS_HOLDER, SourceTraitTestHelper.createDefaultTraits());
            return builder.set(0, 0, 0, SourceBlock.findSourceBlock(type).defaultBlockState(), sourceTag);
        });
    }

    private static @NotNull Supplier<StructureTemplate> createSourceWithStabilizerTemplate(ElementType type) {
        return StructureTemplateBuilder.lazy(1, 1, 1, builder -> {
            var sourceTag = new CompoundTag();

            sourceTag.put(ECNames.SOURCE_TRAITS_HOLDER, SourceTraitTestHelper.createDefaultTraits());
            sourceTag.putBoolean(ECNames.STABILIZED, true);
            return builder.set(0, 0, 0, SourceBlock.findSourceBlock(type).defaultBlockState(), sourceTag);
        });
    }
}
