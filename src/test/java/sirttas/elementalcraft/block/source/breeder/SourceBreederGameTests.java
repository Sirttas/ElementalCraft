package sirttas.elementalcraft.block.source.breeder;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.gametest.GameTestHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.ElementStorageHelper;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.api.source.trait.holder.SourceTraitHolderHelper;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleGameTestHelper;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;

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

    private static void setupBreedingTest(GameTestHelper helper, Item seed, Consumer<ISourceTraitHolder> assertions) {
        assertThat(seed).isInstanceOf(IElementTypeProvider.class);

        var breeder = (SourceBreederBlockEntity) helper.getBlockEntity(new BlockPos(0, 1, 2));
        var type = ((IElementTypeProvider) seed).getElementType();
        var breederItemHandler = ECContainerHelper.getItemHandler(breeder, null);
        var pedestal1 = helper.getBlockEntity(new BlockPos(0, 1, 0));
        var pedestal2 = helper.getBlockEntity(new BlockPos(0, 1, 4));
        var pedestal1ItemHandler = ECContainerHelper.getItemHandler(pedestal1, null);
        var pedestal2ItemHandler = ECContainerHelper.getItemHandler(pedestal2, null);
        var pedestal1ElementStorage = ElementStorageHelper.get(pedestal1);
        var pedestal2ElementStorage = ElementStorageHelper.get(pedestal2);

        helper.startSequence().thenExecute(() -> {
            breederItemHandler.insertItem(0, new ItemStack(seed), false);
            pedestal1ItemHandler.insertItem(0, ReceptacleGameTestHelper.createSimpleReceptacle(type), false);
            pedestal2ItemHandler.insertItem(0, ReceptacleGameTestHelper.createSimpleReceptacle(type), false);
            pedestal1ElementStorage.ifPresent(s -> s.insertElement(1000000, type, false));
            pedestal2ElementStorage.ifPresent(s -> s.insertElement(1000000, type, false));

            assertThat(breeder).isNotNull().satisfies(b -> {
                assertThat(b.getElementType()).isEqualTo(type);
                assertThat(b.getPedestalsDirections()).hasSize(2);
            });
        }).thenExecuteFor(10, () -> {
            pedestal1ElementStorage.ifPresent(s -> s.insertElement(1000000, type, false));
            pedestal2ElementStorage.ifPresent(s -> s.insertElement(1000000, type, false));

            assertThat(breederItemHandler).isNotEmpty();
        }).thenExecuteAfter(1, () -> assertThat(breederItemHandler)
                        .isNotEmpty()
                        .satisfies(0, s -> {
                            assertThat(s).isNotEmpty().is(ECItems.RECEPTACLE);
                            assertThat(ReceptacleHelper.getElementType(s)).isEqualTo(type);
                            assertThat(SourceTraitHolderHelper.get(s).resolve())
                                    .isNotEmpty()
                                    .hasValueSatisfying(assertions);
                        }))
                .thenSucceed();
    }
}
