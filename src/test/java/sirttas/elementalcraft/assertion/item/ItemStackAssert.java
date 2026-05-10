package sirttas.elementalcraft.assertion.item;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

public class ItemStackAssert extends AbstractAssert<ItemStackAssert, ItemStack> {
    private ItemStackAssert(ItemStack itemStack) {
        super(itemStack, ItemStackAssert.class);
    }

    public static ItemStackAssert assertThat(ItemStack itemStack) {
        return new ItemStackAssert(itemStack);
    }

    public ItemStackAssert isEmpty() {
        isNotNull();
        if (!actual.isEmpty()) {
            failWithMessage("Expected item stack to be empty but was %s", actual);
        }
        return this;
    }

    public ItemStackAssert isNotEmpty() {
        isNotNull();
        if (actual.isEmpty()) {
            failWithMessage("Expected item stack to not be empty");
        }
        return this;
    }

    public ItemStackAssert hasCount(int count) {
        isNotEmpty();
        if (actual.getCount() != count) {
            failWithMessage("Expected item stack to have count %d but was %d", count, actual.getCount());
        }
        return this;
    }

    public ItemStackAssert is(Item item) {
        isNotEmpty();
        if (!actual.is(item)) {
            failWithMessage("Expected item stack to be %s but was %s", item, actual.getItem());
        }
        return this;
    }

    public ItemStackAssert is(ItemLike item) {
        return is(item.asItem());
    }

    public ItemStackAssert is(Supplier<? extends ItemLike> item) {
        return is(item.get());
    }

    public ItemStackAssert is(TagKey<Item> tag) {
        isNotEmpty();
        if (!actual.is(tag)) {
            failWithMessage("Expected item stack to be %s but was %s", tag, actual.getItem());
        }
        return this;
    }

    public ItemStackAssert hasDamage(int damage) {
        var actualDamage = actual.getDamageValue();

        if (actualDamage != damage) {
            failWithMessage("Expected item stack to have damage %d but was %d", damage, actualDamage);
        }
        return this;
    }

    public ItemStackAssert hasDamageSatisfying(IntConsumer consumer) {
        consumer.accept(actual.getDamageValue());
        return this;
    }

    public <T> ItemStackAssert hasDataComponent(DataComponentType<T> componentType) {
        isNotEmpty();
        if (actual.get(componentType) == null) {
            failWithMessage("Expected item stack to have component %s", componentType);
        }
        return this;
    }

    public <T> ItemStackAssert hasDataComponent(Supplier<DataComponentType<T>> componentType) {
        return hasDataComponent(componentType.get());
    }

    public <T> ItemStackAssert hasDataComponentSatisfying(DataComponentType<T> componentType, Consumer<T> consumer) {
        hasDataComponent(componentType);
        consumer.accept(actual.get(componentType));
        return this;
    }

    public <T> ItemStackAssert hasDataComponentSatisfying(Supplier<DataComponentType<T>> componentType, Consumer<T> consumer) {
        hasDataComponent(componentType);
        consumer.accept(actual.get(componentType));
        return this;
    }

    public <T> ItemStackAssert hasDataComponentWithValue(Supplier<DataComponentType<T>> componentType, T value) {
        return hasDataComponentSatisfying(componentType, v -> Assertions.assertThat(v).isEqualTo(value));
    }

    public <T> ItemStackAssert hasDataComponentWithValue(DataComponentType<T> componentType, T value) {
        return hasDataComponentSatisfying(componentType, v -> Assertions.assertThat(v).isEqualTo(value));
    }

    public <T> ItemStackAssert doesNotHaveDataComponent(DataComponentType<T> componentType) {
        isNotEmpty();
        if (actual.get(componentType) != null) {
            failWithMessage("Expected item stack to not have component %s", componentType);
        }
        return this;
    }

    public <T> ItemStackAssert doesNotHaveDataComponent(Supplier<DataComponentType<T>> componentType) {
        return doesNotHaveDataComponent(componentType.get());
    }

    public <T, C> ItemStackAssert hasCapability(ItemCapability<T, C> capability, C context) {
        isNotEmpty();
        if (actual.getCapability(capability, context) == null) {
            failWithMessage("Expected item stack to have component %s with context %s", capability, context);
        }
        return this;
    }
    public <T> ItemStackAssert hasCapability(ItemCapability<T, Void> capability) {
        isNotEmpty();
        if (actual.getCapability(capability) == null) {
            failWithMessage("Expected item stack to have component %s", capability);
        }
        return this;
    }

    public <T, C> ItemStackAssert hasCapabilitySatisfying(ItemCapability<T, C> capability, C context, Consumer<T> consumer) {
        hasCapability(capability, context);
        consumer.accept(actual.getCapability(capability, context));
        return this;
    }

    public <T> ItemStackAssert hasCapabilitySatisfying(ItemCapability<T, Void> capability, Consumer<T> consumer) {
        hasCapability(capability);
        consumer.accept(actual.getCapability(capability));
        return this;
    }

    public <T, C> ItemStackAssert hasCapabilityWithValue(ItemCapability<T, C> capability, C context, T value) {
        return hasCapabilitySatisfying(capability, context, v -> Assertions.assertThat(v).isEqualTo(value));
    }


    public <T> ItemStackAssert hasCapabilityWithValue(ItemCapability<T, Void> capability, T value) {
        return hasCapabilitySatisfying(capability, v -> Assertions.assertThat(v).isEqualTo(value));
    }

    public <T, C> ItemStackAssert doesNotHaveCapability(ItemCapability<T, C> capability, C context) {
        isNotEmpty();
        if (actual.getCapability(capability, context) != null) {
            failWithMessage("Expected item stack to not have component %s with context %s", capability, context);
        }
        return this;
    }

    public <T> ItemStackAssert doesNotHaveCapability(ItemCapability<T, Void> capability) {
        isNotEmpty();
        if (actual.getCapability(capability) != null) {
            failWithMessage("Expected item stack to not have component %s", capability);
        }
        return this;
    }
}
