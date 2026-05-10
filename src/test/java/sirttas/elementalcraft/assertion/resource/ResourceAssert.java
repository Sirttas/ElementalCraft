package sirttas.elementalcraft.assertion.resource;

import net.neoforged.neoforge.transfer.resource.Resource;
import org.assertj.core.api.AbstractAssert;


public class ResourceAssert<T extends Resource> extends AbstractAssert<ResourceAssert<T>, T> {

    private ResourceAssert(T resource) {
        super(resource, ResourceAssert.class);
    }

    public static <T extends Resource> ResourceAssert<T> assertThat(T resource) {
        return new ResourceAssert<>(resource);
    }

    public ResourceAssert<T> isEmpty() {
        isNotNull();
        if (!actual.isEmpty()) {
            failWithMessage("Expected resource to be empty but was %s", actual);
        }
        return this;
    }

    public ResourceAssert<T> isNotEmpty() {
        isNotNull();
        if (actual.isEmpty()) {
            failWithMessage("Expected resource to not be empty");
        }
        return this;
    }
}
