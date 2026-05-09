package sirttas.elementalcraft.test.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import sirttas.elementalcraft.item.DamageableCraftingItem;
import sirttas.elementalcraft.test.annotation.ElementalCraftTest;
import sirttas.elementalcraft.test.annotation.RegistrySource;

import static org.assertj.core.api.Assertions.assertThat;

@ElementalCraftTest
public class DamageableCraftingItemTests {

    @ParameterizedTest
    @RegistrySource(value = "item", ofType = DamageableCraftingItem.class)
    @DisplayName("Returns null when item count is zero")
    void should_returnNull_when_itemCountIsZero(Item item) {
        // Given
        var stack = new ItemStack(item, 0);

        // When
        var result = stack.getCraftingRemainder();

        // Then
        assertThat(result).isNull();
    }

    @ParameterizedTest
    @RegistrySource(value = "item", ofType = DamageableCraftingItem.class)
    @DisplayName("Returns null when item is at last durability (damage = maxDamage - 1)")
    void should_returnNull_when_itemIsAtLastDurability(Item item) {
        // Given
        var stack = new ItemStack(item);
        stack.setDamageValue(stack.getMaxDamage() - 1);

        // When
        var result = stack.getCraftingRemainder();

        // Then
        assertThat(result).isNull();
    }

    @ParameterizedTest
    @RegistrySource(value = "item", ofType = DamageableCraftingItem.class)
    @DisplayName("Returns damaged remainder from an ItemStack with no prior damage")
    void should_returnDamagedRemainder_when_itemStackHasNoDamage(Item item) {
        // Given
        var stack = new ItemStack(item);

        // When
        var result = stack.getCraftingRemainder();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.create().getDamageValue()).isEqualTo(1);
    }

    @ParameterizedTest
    @RegistrySource(value = "item", ofType = DamageableCraftingItem.class)
    @DisplayName("Returns damaged remainder from an ItemStackTemplate")
    void should_returnDamagedRemainder_when_givenItemStackTemplate(Item item) {
        // Given
        var template = ItemStackTemplate.fromNonEmptyStack(new ItemStack(item));

        // When
        var result = template.item().value().getCraftingRemainder(template);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.create().getDamageValue()).isEqualTo(1);
    }

    @ParameterizedTest
    @RegistrySource(value = "item", ofType = DamageableCraftingItem.class)
    @DisplayName("Returns damaged remainder at second-to-last durability (damage = maxDamage - 2)")
    void should_returnDamagedRemainder_when_itemIsAtSecondToLastDurability(Item item) {
        // Given
        var stack = new ItemStack(item);
        int secondToLast = stack.getMaxDamage() - 2;
        stack.setDamageValue(secondToLast);

        // When
        var result = stack.getCraftingRemainder();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.create().getDamageValue()).isEqualTo(secondToLast + 1);
    }

    @ParameterizedTest
    @RegistrySource(value = "item", ofType = DamageableCraftingItem.class)
    @DisplayName("Does not mutate the original ItemStack")
    void should_notMutateOriginalStack_when_computingRemainder(Item item) {
        // Given
        var stack = new ItemStack(item);

        // When
        stack.getCraftingRemainder();

        // Then
        assertThat(stack.getDamageValue()).isEqualTo(0);
    }
}
