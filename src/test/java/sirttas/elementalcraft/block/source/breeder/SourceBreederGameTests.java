package sirttas.elementalcraft.block.source.breeder;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.element.storage.ElementStorageGameTestHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleGameTestHelper;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.rune.RuneGameTestHelper;

import java.util.function.Consumer;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
public class SourceBreederGameTests {

    // elementalcraft:sourcebreedergametests.source_breeder
    @GameTest(template = "source_breeder")
    public static void should_breedArtificialSource(GameTestHelper helper) {
        setupBreedingTest(helper, ECItems.ARTIFICIAL_AIR_SOURCE_SEED.get(), h -> {
            assertThat(h.isArtificial()).isTrue();
            assertThat(h.getTraits())
                    .isNotNull()
                    .isNotEmpty()
                    .hasSizeGreaterThanOrEqualTo(3)
                    .containsKeys(SourceTraits.ELEMENT_CAPACITY, SourceTraits.RECOVER_RATE, SourceTraits.ARTIFICIAL);
        });
    }

    // elementalcraft:sourcebreedergametests.source_breeder
    @GameTest(template = "source_breeder")
    public static void should_breedNaturalSource(GameTestHelper helper) {
        setupBreedingTest(helper, ECItems.NATURAL_AIR_SOURCE_SEED.get(), h -> {
            assertThat(h.isArtificial()).isFalse();
            assertThat(h.getTraits())
                    .hasSizeGreaterThanOrEqualTo(2)
                    .containsKeys(SourceTraits.ELEMENT_CAPACITY, SourceTraits.RECOVER_RATE)
                    .doesNotContainKey(SourceTraits.ARTIFICIAL);
        });
    }

    // elementalcraft:sourcebreedergametests.source_breeder
    @GameTest(template = "source_breeder")
    public static void should_dropOnePedestalAndRune(GameTestHelper helper) {
        var pos = new BlockPos(0, 1, 2);

        helper.getLevel().destroyBlock(helper.absolutePos(pos), true, null);

        var items = helper.getEntities(EntityType.ITEM, pos, 1);

        assertThat(items).hasSize(2)
                        .allSatisfy(e -> {
                            var stack = e.getItem();

                            assertThat(stack).isNotNull().hasCount(1).satisfiesAnyOf(
                                    s -> assertThat(s).is(ECBlocks.SOURCE_BREEDER),
                                    s -> assertThat(s).is(ECItems.RUNE).satisfies(s2 -> RuneGameTestHelper.assertRuneIs(s2, "creative"))
                            );
                        });
        items.forEach(Entity::discard);
        helper.succeed();
    }

    private static void setupBreedingTest(GameTestHelper helper, Item seed, Consumer<ISourceTraitHolder> assertions) {
        assertThat(seed).isInstanceOf(IElementTypeProvider.class);

        var breeder = (SourceBreederBlockEntity) helper.getBlockEntity(new BlockPos(0, 1, 2));

        var type = ((IElementTypeProvider) seed).getElementType();
        var breederItemHandler = ECContainerHelper.getItemHandler(breeder, null);
        var pedestal1 = helper.getBlockEntity(new BlockPos(0, 1, 0));
        var pedestal2 = helper.getBlockEntity(new BlockPos(0, 1, 4));
        var pedestal1ItemHandler = ECContainerHelper.getItemHandler(pedestal1, null);
        var pedestal2ItemHandler = ECContainerHelper.getItemHandler(pedestal2, null);
        var pedestal1ElementStorage = ElementStorageGameTestHelper.get(pedestal1);
        var pedestal2ElementStorage = ElementStorageGameTestHelper.get(pedestal2);

        helper.startSequence().thenExecute(() -> {
            breederItemHandler.insertItem(0, new ItemStack(seed), false);
        }).thenExecuteAfter(1, ECGameTestHelper.fixAssertions(() -> {
            assertThat(breeder).isNotNull().satisfies(b -> {
                assertThat(b.getElementType()).isEqualTo(type);
                assertThat(b.getPedestalsDirections()).hasSize(2);
            });
            pedestal1ItemHandler.insertItem(0, ReceptacleGameTestHelper.createSimpleReceptacle(type), false);
            pedestal2ItemHandler.insertItem(0, ReceptacleGameTestHelper.createSimpleReceptacle(type), false);
            pedestal1ElementStorage.fill();
            pedestal2ElementStorage.fill();
        })).thenExecuteFor(10, () -> {
            pedestal1ElementStorage.fill();
            pedestal2ElementStorage.fill();

            assertThat(breederItemHandler).isNotEmpty();
        }).thenExecuteAfter(1, () -> {
            assertThat(breederItemHandler)
                    .isNotEmpty()
                    .satisfies(0, s -> {
                        assertThat(s).isNotEmpty().is(ECItems.RECEPTACLE);
                        assertThat(ReceptacleHelper.getElementType(s)).isEqualTo(type);
                        assertThat(s.getCapability(ElementalCraftCapabilities.SourceTrait.ITEM))
                                .isNotNull()
                                .satisfies(assertions);
                    });
                })
                .thenSucceed();
    }
}
