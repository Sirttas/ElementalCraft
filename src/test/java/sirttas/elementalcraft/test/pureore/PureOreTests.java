package sirttas.elementalcraft.test.pureore;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.pureore.PureOreManager;
import sirttas.elementalcraft.test.annotation.ElementalCraftTest;
import sirttas.elementalcraft.test.annotation.TagSource;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ElementalCraftTest
public class PureOreTests {

    @ParameterizedTest
    @DisplayName("Check if item is a valid pure ore.")
    @TagSource(registry = "item",
            value = @TagSource.Tag(namespace = ElementalCraftApi.MODID, value = "pure_ores/sources"))
    public void should_bePureOre(Item item) {
        assertThat(PureOreManager.getInstance().isValidOre(new ItemStack(item)))
                .withFailMessage("Item %s is not a valid pure ore", item)
                .isTrue();
    }

    @Test
    @DisplayName("Check if white rock is not a valid pure ore.")
    public void shouldNot_bePureOre() {
        assertThat(PureOreManager.getInstance().isValidOre(new ItemStack(ECBlocks.WHITE_ROCK.get())))
                .withFailMessage("White rock is a valid pure ore")
                .isFalse();
    }
}
