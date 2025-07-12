package sirttas.elementalcraft.test.block.source.trait;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sirttas.elementalcraft.MockRandomSource;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.source.trait.SourceTraitHelper;
import sirttas.elementalcraft.block.source.trait.SourceTraitTestHelper;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.test.annotation.ElementalCraftTest;

import java.util.stream.IntStream;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ElementalCraftTest
public class SourceTraitHelperTests {

    private final MockRandomSource random = new MockRandomSource();

    @Test
    @DisplayName("Checks if the breed method creates a trait map.")
    public void breed_should_createTraitMap() {
        // Given
        var defaultSourceTraits = SourceTraitTestHelper.getDefaultTraits();

        // When
        var traits = SourceTraitHelper.breed(random, 0, defaultSourceTraits, defaultSourceTraits);

        // Then
        assertThat(traits)
                .isNotNull()
                .isNotEmpty()
                .hasSizeGreaterThanOrEqualTo(1)
                .containsKeys(SourceTraits.ELEMENT_CAPACITY);
    }

    @Test
    @DisplayName("Checks if the breed method adds fertility at 100 per 1000 with no luck runes.")
    public void breed_should_addFertilityInAbout100Per1000_with_luck0() {
        // Given
        var defaultSourceTraits = SourceTraitTestHelper.getDefaultTraits();

        // When
        var fertileCount = IntStream.range(0, 1000)
                .mapToObj(i -> SourceTraitHelper.breed(random, 0, defaultSourceTraits, defaultSourceTraits))
                .filter(traits -> traits.containsKey(SourceTraits.FERTILITY))
                .count();

        // Then
        ElementalCraftApi.LOGGER.info("Fertile count: {}", fertileCount);
        assertThat(fertileCount).isCloseTo(100L, Offset.offset(50L));
    }

    @Test
    @DisplayName("Checks if the breed method adds fertility at 250 per 1000 with a level 3 luck rune.")
    public void breed_should_addFertilityInAbout250Per1000_with_luck3() {
        // Given
        var defaultSourceTraits = SourceTraitTestHelper.getDefaultTraits();

        // When
        var fertileCount = IntStream.range(0, 1000)
                .mapToObj(i -> SourceTraitHelper.breed(random, 3, defaultSourceTraits, defaultSourceTraits))
                .filter(traits -> traits.containsKey(SourceTraits.FERTILITY))
                .count();

        // Then
        ElementalCraftApi.LOGGER.info("Fertile count: {}", fertileCount);
        assertThat(fertileCount).isCloseTo(250L, Offset.offset(100L));
    }
}
