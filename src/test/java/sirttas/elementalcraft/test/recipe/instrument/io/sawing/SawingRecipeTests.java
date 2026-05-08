package sirttas.elementalcraft.test.recipe.instrument.io.sawing;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.test.annotation.ElementalCraftTest;
import sirttas.elementalcraft.test.annotation.TagSource;

import java.util.Map;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ElementalCraftTest
public class SawingRecipeTests {

    @ParameterizedTest
    @DisplayName("Check that wood can be sawed.")
    @TagSource(registry = "item",
            value = @TagSource.Tag(value = "logs"))
    public void should_existsForWoods(Item item, MinecraftServer server) {
        // Given
        var input = new SimpleIOInstrumentRecipeInput(new ItemStack(item), ItemStack.EMPTY, RandomSource.create(), ElementType.WATER, 1000000, Map.of());

        // When
        var recipe = server.getRecipeManager().getRecipeFor(ECRecipeTypes.SAWING.get(), input, server.overworld());

        // Then
        assertThat(recipe).isPresent();

    }
}
