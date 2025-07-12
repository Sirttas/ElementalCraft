package sirttas.elementalcraft.test.spell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.test.annotation.ElementalCraftTest;
import sirttas.elementalcraft.test.annotation.RegistrySource;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ElementalCraftTest
public class SpellTests {

    @ParameterizedTest
    @DisplayName("Check if spell is valid.")
    @RegistrySource(namespace = ElementalCraftApi.MODID, value = "spell",
            exclude = @RegistrySource.Exclude(namespace = ElementalCraftApi.MODID, value = "none"))
    public void should_beValid(Spell spell) {
        assertThat(spell.isValid())
                .withFailMessage("Spell %s is not valid", spell)
                .isTrue();
    }

    @Test
    @DisplayName("Check if NONE spell is not valid.")
    public void none_shouldNot_beValid() {
        assertThat(Spells.NONE.get().isValid())
                .withFailMessage("Spell NONE is valid")
                .isFalse();
    }

}
