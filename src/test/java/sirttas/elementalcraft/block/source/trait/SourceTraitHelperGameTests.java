package sirttas.elementalcraft.block.source.trait;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import org.assertj.core.data.Offset;
import sirttas.elementalcraft.ECGameTestHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.stream.IntStream;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@GameTestHolder(ElementalCraftApi.MODID)
@PrefixGameTestTemplate(false)
public class SourceTraitHelperGameTests {

    @GameTest(template = ECGameTestHelper.EMPTY_TEMPLATE)
    public void breed_should_createArtificialTraitMap(GameTestHelper helper) {
        var traits = SourceTraitHelper.breed(helper.getLevel().random, 0, false, SourceTraitGameTestHelper.getDefaultTraits(), SourceTraitGameTestHelper.getDefaultTraits());

        assertThat(traits)
                .isNotNull()
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(3)
                .containsKeys(SourceTraits.ELEMENT_CAPACITY, SourceTraits.RECOVER_RATE, SourceTraits.ARTIFICIAL);
        helper.succeed();
    }

    @GameTest(template = ECGameTestHelper.EMPTY_TEMPLATE)
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

    @GameTest(template = ECGameTestHelper.EMPTY_TEMPLATE, required = false)
    public void breed_should_addFertilityInAboutOneQuarter_with_luck0(GameTestHelper helper) {
        var fertileCount = IntStream.range(0, 1000)
                .mapToObj(i -> SourceTraitHelper.breed(helper.getLevel().random, 0, true, SourceTraitGameTestHelper.getDefaultTraits(), SourceTraitGameTestHelper.getDefaultTraits()))
                .filter(traits -> traits.containsKey(SourceTraits.FERTILITY))
                .count();

        ElementalCraftApi.LOGGER.info("Fertile count: {}", fertileCount);
        assertThat(fertileCount).isCloseTo(250L, Offset.offset(50L));
        helper.succeed();
    }

    @GameTest(template = ECGameTestHelper.EMPTY_TEMPLATE, required = false)
    public void breed_should_addFertilityInAboutFiveEighth_with_luck3(GameTestHelper helper) {
        var fertileCount = IntStream.range(0, 1000)
                .mapToObj(i -> SourceTraitHelper.breed(helper.getLevel().random, 3, true, SourceTraitGameTestHelper.getDefaultTraits(), SourceTraitGameTestHelper.getDefaultTraits()))
                .filter(traits -> traits.containsKey(SourceTraits.FERTILITY))
                .count();

        ElementalCraftApi.LOGGER.info("Fertile count: {}", fertileCount);
        assertThat(fertileCount).isCloseTo(625L, Offset.offset(100L));
        helper.succeed();
    }
}
