package sirttas.elementalcraft.block.source.breeder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.RegisterStructureTemplate;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import net.neoforged.testframework.gametest.StructureTemplateBuilder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.source.breeder.pedestal.SourceBreederPedestalBlockEntity;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.element.storage.ElementStorageGameTestHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleGameTestHelper;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.rune.Runes;
import sirttas.elementalcraft.tag.ECTags;

import java.util.function.Supplier;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;
import static sirttas.elementalcraft.template.StructureTemplateHelper.runeHandler;
import static sirttas.elementalcraft.template.StructureTemplateHelper.withValue;

@ForEachTest(groups = SourceBreederGameTests.GROUP)
public class SourceBreederGameTests {
    public static final String GROUP = "level.blocks.sources.breeders";

    public static final String TEMPLATE_NAME = "elementalcraft:source_breeder";

    @RegisterStructureTemplate(TEMPLATE_NAME)
    public static final Supplier<StructureTemplate> TEMPLATE = StructureTemplateBuilder.lazy(1, 2, 5, builder -> builder
            .set(0, 0, 0, ECBlocks.SOURCE_BREEDER_PEDESTAL.get().defaultBlockState())
            .set(0, 0, 2, ECBlocks.SOURCE_BREEDER.get().defaultBlockState()
                    .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER), withValue(runeHandler(Runes.CREATIVE)))
            .set(0, 1, 2, ECBlocks.SOURCE_BREEDER.get().defaultBlockState()
                    .setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER))
            .set(0, 0, 4, ECBlocks.SOURCE_BREEDER_PEDESTAL.get().defaultBlockState()));


    @TestHolder(description = "Checks that the source breeder can breed sources.")
    @GameTest(template = TEMPLATE_NAME)
    public static void should_breedSource(ECGameTestHelper helper) {
        var breeder = helper.getBlockEntity(new BlockPos(0, 0, 2), SourceBreederBlockEntity.class);

        var type = ElementType.AIR;
        var breederItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(breeder, null));
        var pedestal1 = helper.getBlockEntity(new BlockPos(0, 0, 0), SourceBreederPedestalBlockEntity.class);
        var pedestal2 = helper.getBlockEntity(new BlockPos(0, 0, 4), SourceBreederPedestalBlockEntity.class);
        var pedestal1ItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(pedestal1, null));
        var pedestal2ItemHandler = IItemHandler.of(ECContainerHelper.getItemResourceHandler(pedestal2, null));
        var pedestal1ElementStorage = ElementStorageGameTestHelper.get(pedestal1);
        var pedestal2ElementStorage = ElementStorageGameTestHelper.get(pedestal2);

        helper.startSequence().thenExecute(() -> {
                    breederItemHandler.insertItem(0, new ItemStack(ECItems.AIR_SOURCE_SEED), false);
                }).thenExecuteAfter(1, () -> {
                    assertThat(breeder).isNotNull().satisfies(b -> {
                        assertThat(b.getElementType()).isEqualTo(type);
                        assertThat(b.getPedestalsDirections()).hasSize(2);
                    });
                    pedestal1ItemHandler.insertItem(0, ReceptacleGameTestHelper.createSimpleReceptacle(type), false);
                    pedestal2ItemHandler.insertItem(0, ReceptacleGameTestHelper.createSimpleReceptacle(type), false);
                    pedestal1ElementStorage.fill();
                    pedestal2ElementStorage.fill();
                }).thenExecuteFor(10, () -> {
                    pedestal1ElementStorage.fill();
                    pedestal2ElementStorage.fill();

                    assertThat(breederItemHandler).isNotEmpty();
                }).thenExecuteAfter(1, () -> {
                    assertHasValidReceptacle(breederItemHandler, type);
                    assertHasValidReceptacle(pedestal1ItemHandler, type);
                    assertHasValidReceptacle(pedestal2ItemHandler, type);})
                .thenSucceed();
    }

    private static void assertHasValidReceptacle(IItemHandler itemHandler, ElementType type) {
        assertThat(itemHandler)
                .isNotEmpty()
                .satisfies(0, s -> {
                    assertThat(s).isNotEmpty().is(ECTags.Items.RECEPTACLES);
                    assertThat(ReceptacleHelper.getElementType(s)).isEqualTo(type);
                    assertThat(s.getCapability(ElementalCraftCapabilities.SourceTraits.ITEM))
                            .isNotNull()
                            .satisfies(h -> assertThat(h.getTraits())
                                    .hasSizeGreaterThanOrEqualTo(1)
                                    .containsKeys(sirttas.elementalcraft.block.source.trait.SourceTraits.ELEMENT_CAPACITY));
                    assertThat(s.getCapability(ElementalCraftCapabilities.ElementStorages.ITEM))
                            .isNotNull()
                            .satisfies(storage -> {
                                assertThat(storage.getElementCapacity(type)).isPositive();
                                assertThat(storage.getElementAmount(type)).isPositive();
                            });
                });
    }
}
