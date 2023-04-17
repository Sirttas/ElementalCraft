package sirttas.elementalcraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;
import org.assertj.core.api.AbstractAssert;

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

    public ItemStackAssert is(RegistryObject<? extends ItemLike> item) {
        return is(item.get());
    }
}
