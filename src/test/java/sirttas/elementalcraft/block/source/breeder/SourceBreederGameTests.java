package sirttas.elementalcraft.block.source.breeder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.testframework.annotation.ForEachTest;
import net.neoforged.testframework.annotation.TestHolder;
import net.neoforged.testframework.gametest.GameTest;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.element.storage.ElementStorageGameTestHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleGameTestHelper;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.rune.Runes;
import sirttas.elementalcraft.tag.ECTags;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ForEachTest(groups = SourceBreederGameTests.GROUP)
public class SourceBreederGameTests {
    public static final String GROUP = "level.blocks.sources.breeders";


    @TestHolder(description = "Checks if the source breeder can breed sources.")
    @GameTest(template = "elementalcraft:sourcebreedergametests.source_breeder")
    public static void should_breedSource(ECGameTestHelper helper) {
        var breeder = (SourceBreederBlockEntity) helper.getBlockEntity(new BlockPos(0, 1, 2));

        var type = ElementType.AIR;
        var breederItemHandler = ECContainerHelper.getItemHandler(breeder, null);
        var pedestal1 = helper.getBlockEntity(new BlockPos(0, 1, 0));
        var pedestal2 = helper.getBlockEntity(new BlockPos(0, 1, 4));
        var pedestal1ItemHandler = ECContainerHelper.getItemHandler(pedestal1, null);
        var pedestal2ItemHandler = ECContainerHelper.getItemHandler(pedestal2, null);
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

    @TestHolder(description = "Checks if the source breeder drops a source breeder and a rune.")
    @GameTest(template = "elementalcraft:sourcebreedergametests.source_breeder")
    public static void should_dropOneSourceBreederAndRune(ECGameTestHelper helper) {
        var pos = new BlockPos(0, 1, 2);

        helper.getLevel().destroyBlock(helper.absolutePos(pos), true, null);

        var items = helper.getEntities(EntityType.ITEM, pos, 1);

        assertThat(items).hasSize(2)
                        .allSatisfy(e -> {
                            var stack = e.getItem();

                            assertThat(stack).isNotNull().hasCount(1).satisfiesAnyOf(
                                    s -> assertThat(s).is(ECBlocks.SOURCE_BREEDER),
                                    s -> assertThat(s).is(ECItems.RUNE).satisfies(s2 -> helper.assertRuneIs(s2, Runes.CREATIVE))
                            );
                        });
        items.forEach(Entity::discard);
        helper.succeed();
    }
}
