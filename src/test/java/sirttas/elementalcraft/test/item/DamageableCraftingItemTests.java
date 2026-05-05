package sirttas.elementalcraft.test.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.test.annotation.ElementalCraftTest;

import static org.assertj.core.api.Assertions.assertThat;

@ElementalCraftTest
public class DamageableCraftingItemTests {

    @Test
    @DisplayName("Returns null when item count is zero")
    void should_returnNull_when_itemCountIsZero() {
        // Given
        var stack = new ItemStack(ECItems.FIRE_LENS.get(), 0);

        // When
        var result = stack.getCraftingRemainder();

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Returns null when item is at last durability (damage = maxDamage - 1)")
    void should_returnNull_when_itemIsAtLastDurability() {
        // Given
        var stack = new ItemStack(ECItems.FIRE_LENS.get());
        stack.setDamageValue(stack.getMaxDamage() - 1);

        // When
        var result = stack.getCraftingRemainder();

        // Then
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Returns damaged remainder from an ItemStack with no prior damage")
    void should_returnDamagedRemainder_when_itemStackHasNoDamage() {
        // Given
        var stack = new ItemStack(ECItems.FIRE_LENS.get());

        // When
        var result = stack.getCraftingRemainder();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.create().getDamageValue()).isEqualTo(1);
    }

    @Test
    @DisplayName("Returns damaged remainder from an ItemStackTemplate")
    void should_returnDamagedRemainder_when_givenItemStackTemplate() {
        // Given
        var template = ItemStackTemplate.fromNonEmptyStack(new ItemStack(ECItems.FIRE_LENS.get()));

        // When
        var result = template.item().value().getCraftingRemainder(template);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.create().getDamageValue()).isEqualTo(1);
    }

    @Test
    @DisplayName("Returns damaged remainder at second-to-last durability (damage = maxDamage - 2)")
    void should_returnDamagedRemainder_when_itemIsAtSecondToLastDurability() {
        // Given
        var stack = new ItemStack(ECItems.FIRE_LENS.get());
        int secondToLast = stack.getMaxDamage() - 2;
        stack.setDamageValue(secondToLast);

        // When
        var result = stack.getCraftingRemainder();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.create().getDamageValue()).isEqualTo(secondToLast + 1);
    }

    @Test
    @DisplayName("Does not mutate the original ItemStack")
    void should_notMutateOriginalStack_when_computingRemainder() {
        // Given
        var stack = new ItemStack(ECItems.FIRE_LENS.get());

        // When
        stack.getCraftingRemainder();

        // Then
        assertThat(stack.getDamageValue()).isEqualTo(0);
    }
}
