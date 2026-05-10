package sirttas.elementalcraft.assertion.resource;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.resource.ResourceStack;
import org.assertj.core.api.AbstractAssert;
import sirttas.elementalcraft.assertion.item.ItemStackAssert;


public class ResourceStackAssert<T extends Resource> extends AbstractAssert<ResourceStackAssert<T>, ResourceStack<T>> {

    private ResourceStackAssert(ResourceStack<T> stack) {
        super(stack, ResourceStackAssert.class);
    }

    public static <T extends Resource> ResourceStackAssert<T> assertThat(ResourceStack<T> stack) {
        return new ResourceStackAssert<>(stack);
    }

    public ResourceStackAssert<T> isEmpty() {
        isNotNull();
        if (!actual.isEmpty()) {
            failWithMessage("Expected resource stack to be empty but was %s", actual);
        }
        return this;
    }

    public ResourceStackAssert<T> isNotEmpty() {
        isNotNull();
        if (actual.isEmpty()) {
            failWithMessage("Expected resource stack to not be empty");
        }
        return this;
    }

    public ItemStackAssert isItem() {
        isNotNull();
        if (!(actual.resource() instanceof ItemResource itemResource)) {
            failWithMessage("Expected resource stack to be item but was %s", actual);
            return ItemStackAssert.assertThat(ItemStack.EMPTY); // never called but here so the compiler know we exited the method
        }
        return ItemStackAssert.assertThat(itemResource.toStack(actual.amount()));
    }
}
