package sirttas.elementalcraft.block.source.breeder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
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
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.runeHandler;
import static sirttas.elementalcraft.template.StructureTemplateNbtHelper.withValue;

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
        var breederItemHandler = ECContainerHelper.getItemResourceHandler(breeder, null);
        var pedestal1 = helper.getBlockEntity(new BlockPos(0, 0, 0), SourceBreederPedestalBlockEntity.class);
        var pedestal2 = helper.getBlockEntity(new BlockPos(0, 0, 4), SourceBreederPedestalBlockEntity.class);
        var pedestal1ItemHandler = ECContainerHelper.getItemResourceHandler(pedestal1, null);
        var pedestal2ItemHandler = ECContainerHelper.getItemResourceHandler(pedestal2, null);
        var pedestal1ElementStorage = ElementStorageGameTestHelper.get(pedestal1);
        var pedestal2ElementStorage = ElementStorageGameTestHelper.get(pedestal2);

        helper.startSequence()
                .thenExecute(transaction -> {
                    breederItemHandler.insert(0, ItemResource.of(ECItems.AIR_SOURCE_SEED), 1, transaction);
                }).thenExecuteAfter(1, transaction -> {
                    assertThat(breeder).isNotNull().satisfies(b -> {
                        assertThat(b.getElementType()).isEqualTo(type);
                        assertThat(b.getPedestalsDirections()).hasSize(2);
                    });
                    pedestal1ItemHandler.insert(0, ItemResource.of(ReceptacleGameTestHelper.createSimpleReceptacle(type)), 1, transaction);
                    pedestal2ItemHandler.insert(0, ItemResource.of(ReceptacleGameTestHelper.createSimpleReceptacle(type)), 1, transaction);
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

    private static void assertHasValidReceptacle(ResourceHandler<ItemResource> itemHandler, ElementType type) {
        assertThat(itemHandler)
                .isNotEmpty()
                .satisfies(0, rs -> assertThat(rs)
                        .isItem()
                        .isNotEmpty()
                        .is(ECTags.Items.RECEPTACLES)
                        .hasCapabilitySatisfying(ElementalCraftCapabilities.SourceTraits.ITEM, sourceTraitHolder -> assertThat(sourceTraitHolder.getTraits())
                                .hasSizeGreaterThanOrEqualTo(1)
                                .containsKeys(sirttas.elementalcraft.block.source.trait.SourceTraits.ELEMENT_CAPACITY))
                        .hasCapabilitySatisfying(ElementalCraftCapabilities.ElementStorages.ITEM, storage -> {
                            assertThat(storage.getElementCapacity(type)).isPositive();
                            assertThat(storage.getElementAmount(type)).isPositive();
                        })
                        .satisfies(s -> assertThat(ReceptacleHelper.getElementType(s)).isEqualTo(type)));
    }
}
