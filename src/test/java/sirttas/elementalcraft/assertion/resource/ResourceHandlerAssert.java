package sirttas.elementalcraft.assertion.resource;

import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.resource.ResourceStack;
import org.assertj.core.api.AbstractAssert;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;


public class ResourceHandlerAssert<T extends Resource> extends AbstractAssert<ResourceHandlerAssert<T>, ResourceHandler<T>> {

    private ResourceHandlerAssert(ResourceHandler<T> handler) {
        super(handler, ResourceHandlerAssert.class);
    }

    public static <T extends Resource> ResourceHandlerAssert<T> assertThat(ResourceHandler<T> handler) {
        return new ResourceHandlerAssert<>(handler);
    }

    public ResourceHandlerAssert<T> isEmpty() {
        isNotNull();
        if (!checkEmpty()) {
            failWithMessage("Expected resource handler to be empty but was %s", actual);
        }
        return this;
    }

    public ResourceHandlerAssert<T> isNotEmpty() {
        isNotNull();
        if (checkEmpty()) {
            failWithMessage("Expected resource handler to not be empty");
        }
        return this;
    }

    public ResourceHandlerAssert<T> isEmpty(int slot) {
        isNotNull();
        stackInSlot(slot).isEmpty();
        return this;
    }

    public ResourceHandlerAssert<T> isNotEmpty(int slot) {
        isNotNull();
        stackInSlot(slot).isNotEmpty();
        return this;
    }

    private boolean checkEmpty() {
        for (int i = 0; i < actual.size(); i++) {
            var stack = getStackInSlot(i);

            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public ResourceStackAssert<T> stackInSlot(int slot) {
        var stack = getStackInSlot(slot);

        return ResourceStackAssert.assertThat(stack);
    }

    private @NonNull ResourceStack<T> getStackInSlot(int slot) {
        return new ResourceStack<>(actual.getResource(slot), actual.getAmountAsInt(slot));
    }

    public ResourceHandlerAssert<T> contains(int slot, T resource) {
        isNotNull();

        var actualResource = actual.getResource(slot);

        if (!actualResource.equals(resource)) {
            failWithMessage("Expected resource handler to contain %s at slot %d but was %s", resource, slot, actualResource);
        }
        return this;
    }

    public ResourceHandlerAssert<T> contains(T resource) {
        isNotNull();

        for (int i = 0; i < actual.size(); i++) {
            if (actual.getResource(i).equals(resource)) {
                return this;
            }
        }
        failWithMessage("Expected resource handler to contain %s");
        return this;
    }


    public ResourceHandlerAssert<T> satisfies(int slot, Consumer<ResourceStack<T>> consumer) {
        isNotNull();
        consumer.accept(getStackInSlot(slot));
        return this;
    }
}
