package sirttas.elementalcraft.test.infusion.tool;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.test.annotation.ElementalCraftTest;

import static sirttas.elementalcraft.assertion.Assertions.assertThat;

@ElementalCraftTest
public class ToolInfusionHelperTests {

    @Test
    @DisplayName("Checks that an item doesn't have infusion attribute or infusion enchantments by default.")
    public void shouldNot_haveAttributesOrEnchantments() {
        // Given
        var sword = new ItemStack(Items.DIAMOND_SWORD);

        // When
        var attributes = ToolInfusionHelper.getInfusionAttribute(sword);
        var enchantments = ToolInfusionHelper.getAllInfusionEnchantments(sword);

        // Then
        assertThat(attributes).isEmpty();
        assertThat(enchantments).isEmpty();
    }
}
