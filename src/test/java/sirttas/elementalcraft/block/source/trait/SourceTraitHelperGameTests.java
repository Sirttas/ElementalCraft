package sirttas.elementalcraft.block.source.trait;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.stream.IntStream;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
@PrefixGameTestTemplate(false)
public class SourceTraitHelperGameTests {

    // elementalcraft:empty
    @GameTest(template = "empty")
    public void breed_should_createArtificialTraitMap(GameTestHelper helper) {
        var traits = SourceTraitHelper.breed(helper.getLevel().random, 0, false, SourceTraitGameTestHelper.getDefaultTraits(), SourceTraitGameTestHelper.getDefaultTraits());

        assertThat(traits)
                .isNotNull()
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(3)
                .containsKeys(SourceTraits.ELEMENT_CAPACITY, SourceTraits.RECOVER_RATE, SourceTraits.ARTIFICIAL);
        helper.succeed();
    }

    // elementalcraft:empty
    @GameTest(template = "empty")
    public void breed_should_createNaturalTraitMap(GameTestHelper helper) {
        var traits = SourceTraitHelper.breed(helper.getLevel().random, 0, true, SourceTraitGameTestHelper.getDefaultTraits(), SourceTraitGameTestHelper.getDefaultTraits());

        assertThat(traits)
                .isNotNull()
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(2)
                .containsKeys(SourceTraits.ELEMENT_CAPACITY, SourceTraits.RECOVER_RATE)
                .doesNotContainKey(SourceTraits.ARTIFICIAL);
        helper.succeed();
    }

    // elementalcraft:empty
    @GameTest(template = "empty", required = false)
    public void breed_should_addFertilityInAboutOneQuarter(GameTestHelper helper) {
        var fertileCount = IntStream.range(0, 1000)
                .mapToObj(i -> SourceTraitHelper.breed(helper.getLevel().random, 0, true, SourceTraitGameTestHelper.getDefaultTraits(), SourceTraitGameTestHelper.getDefaultTraits()))
                .filter(traits -> traits.containsKey(SourceTraits.FERTILITY))
                .count();

        ElementalCraftApi.LOGGER.info("Fertile count: {}", fertileCount);
        assertThat(fertileCount).isBetween(200L, 300L);
        helper.succeed();
    }
}
