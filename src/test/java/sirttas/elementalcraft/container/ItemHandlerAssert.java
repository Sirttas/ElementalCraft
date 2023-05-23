package sirttas.elementalcraft.container;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.RegistryObject;
import org.assertj.core.api.AbstractAssert;
import sirttas.elementalcraft.item.ItemStackAssert;

import java.util.function.Consumer;

public class ItemHandlerAssert extends AbstractAssert<ItemHandlerAssert, IItemHandler> {

    private ItemHandlerAssert(IItemHandler handler) {
        super(handler, ItemHandlerAssert.class);
    }

    public static ItemHandlerAssert assertThat(IItemHandler handler) {
        return new ItemHandlerAssert(handler);
    }

    public ItemHandlerAssert isEmpty() {
        isNotNull();
        if (!checkEmpty()) {
            failWithMessage("Expected item handler to be empty but was %s", actual);
        }
        return this;
    }

    public ItemHandlerAssert isNotEmpty() {
        isNotNull();
        if (checkEmpty()) {
            failWithMessage("Expected item handler to not be empty");
        }
        return this;
    }

    public ItemHandlerAssert isEmpty(int slot) {
        isNotNull();
        ItemStackAssert.assertThat(actual.getStackInSlot(slot)).isEmpty();
        return this;
    }

    public ItemHandlerAssert isNotEmpty(int slot) {
        isNotNull();
        ItemStackAssert.assertThat(actual.getStackInSlot(slot)).isNotEmpty();
        return this;
    }

    private boolean checkEmpty() {
        for (int i = 0; i < actual.getSlots(); i++) {
            if (!actual.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public ItemHandlerAssert contains(int slot, ItemLike item) {
        isNotNull();

        var itm = item.asItem();
        var stack = actual.getStackInSlot(slot);

        if (!stack.is(itm)) {
            failWithMessage("Expected item handler to contain %s at slot %d but was %s", itm, slot, stack.getItem());
        }
        return this;
    }

    public ItemHandlerAssert contains(int slot, RegistryObject<? extends ItemLike> item) {
        return contains(slot, item.get());
    }

    public ItemHandlerAssert satisfies(int slot, Consumer<ItemStack> consumer) {
        isNotNull();
        consumer.accept(actual.getStackInSlot(slot));
        return this;
    }
}
