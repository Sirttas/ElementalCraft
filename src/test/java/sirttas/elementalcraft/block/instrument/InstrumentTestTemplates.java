package sirttas.elementalcraft.block.instrument;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.rune.Runes;

import java.util.function.Supplier;

import static sirttas.elementalcraft.template.StructureTemplateHelper.runeHandler;
import static sirttas.elementalcraft.template.StructureTemplateHelper.withValue;

public class InstrumentTestTemplates {

    public static final String INFUSER_TEMPLATE_NAME = "elementalcraft:infuser";
    public static final String BINDER_TEMPLATE_NAME = "elementalcraft:binder";
    public static final String CRYSTALLIZER_TEMPLATE_NAME = "elementalcraft:crystallizer";
    public static final String INSCRIBER_TEMPLATE_NAME = "elementalcraft:inscriper";
    public static final String ORE_PURIFIER_TEMPLATE_NAME = "elementalcraft:ore_purifier";
    public static final String ENCHANTMENT_LIQUEFIER_TEMPLATE_NAME = "elementalcraft:enchantment_liquefier";
    public static final String FIRE_FURNACE_TEMPLATE_NAME = "elementalcraft:fire_furnace";
    public static final String FIRE_BLAST_FURNACE_TEMPLATE_NAME = "elementalcraft:fire_blast_furnace";
    public static final String AIR_MILL_GRINDSTONE_TEMPLATE_NAME = "elementalcraft:air_mill_grindstone";
    public static final String AIR_MILL_WOOD_SAW_TEMPLATE_NAME = "elementalcraft:air_mill_wood_saw";
    public static final String WATER_MILL_GRINDSTONE_TEMPLATE_NAME = "elementalcraft:water_mill_grindstone";
    public static final String WATER_MILL_WOOD_SAW_TEMPLATE_NAME = "elementalcraft:water_mill_wood_saw";

    @RegisterStructureTemplate(INFUSER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> INFUSER_TEMPLATE = createInstrumentTemplateWithCreativeRune(ECBlocks.INFUSER);
    @RegisterStructureTemplate(BINDER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> BINDER_TEMPLATE = createInstrumentTemplateWithCreativeRune(ECBlocks.BINDER);
    @RegisterStructureTemplate(CRYSTALLIZER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> CRYSTALLIZER_TEMPLATE = createInstrumentTemplateWithCreativeRune(ECBlocks.CRYSTALLIZER);
    @RegisterStructureTemplate(INSCRIBER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> INSCRIBER_TEMPLATE = createInstrumentTemplate(ECBlocks.INSCRIBER, Runes.MEWTWO);
    @RegisterStructureTemplate(ORE_PURIFIER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> ORE_PURIFIER_TEMPLATE = createInstrumentTemplateWithCreativeRune(ECBlocks.PURIFIER);
    @RegisterStructureTemplate(ENCHANTMENT_LIQUEFIER_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> ENCHANTMENT_LIQUEFIER_TEMPLATE = createDoubleHalfInstrumentTemplateWithCreativeRune(ECBlocks.ENCHANTMENT_LIQUEFIER);
    @RegisterStructureTemplate(FIRE_FURNACE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> FIRE_FURNACE_TEMPLATE = createInstrumentTemplateWithCreativeRune(ECBlocks.FIRE_FURNACE);
    @RegisterStructureTemplate(FIRE_BLAST_FURNACE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> FIRE_BLAST_FURNACE_TEMPLATE = createInstrumentTemplateWithCreativeRune(ECBlocks.FIRE_BLAST_FURNACE);
    @RegisterStructureTemplate(AIR_MILL_GRINDSTONE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> AIR_MILL_GRINDSTONE_TEMPLATE = createDoubleHalfInstrumentTemplateWithCreativeRune(ECBlocks.AIR_MILL_GRINDSTONE);
    @RegisterStructureTemplate(AIR_MILL_WOOD_SAW_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> AIR_MILL_WOOD_SAW_TEMPLATE = createDoubleHalfInstrumentTemplateWithCreativeRune(ECBlocks.AIR_MILL_WOOD_SAW);
    @RegisterStructureTemplate(WATER_MILL_GRINDSTONE_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> WATER_MILL_GRINDSTONE_TEMPLATE = createInstrumentTemplateWithCreativeRune(ECBlocks.WATER_MILL_GRINDSTONE);
    @RegisterStructureTemplate(WATER_MILL_WOOD_SAW_TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> WATER_MILL_WOOD_SAW_TEMPLATE = createInstrumentTemplateWithCreativeRune(ECBlocks.WATER_MILL_WOOD_SAW);


    private InstrumentTestTemplates() {}

    private static @NotNull Supplier<StructureTemplate> createInstrumentTemplateWithCreativeRune(Holder<Block> instrumentBlock) {
        return createInstrumentTemplate(instrumentBlock, Runes.CREATIVE);
    }

    @SafeVarargs
    private static @NotNull Supplier<StructureTemplate> createInstrumentTemplate(Holder<Block> instrumentBlock, ResourceKey<Rune>...runes) {
        return StructureTemplateBuilder.lazy(1, 2, 1, builder -> builder
                .set(0, 0, 0, ECBlocks.CONTAINER.get().defaultBlockState())
                .set(0, 1, 0, instrumentBlock.value().defaultBlockState(), withValue(runeHandler(runes))));
    }

    private static @NotNull Supplier<StructureTemplate> createDoubleHalfInstrumentTemplateWithCreativeRune(Holder<Block> instrumentBlock) {
        return  StructureTemplateBuilder.lazy(1, 3, 1, builder -> builder
                        .set(0, 0, 0, ECBlocks.CONTAINER.get().defaultBlockState())
                        .set(0, 1, 0, instrumentBlock.value().defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER), withValue(runeHandler(Runes.CREATIVE)))
                        .set(0, 2, 0, instrumentBlock.value().defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER)));
    }

}
