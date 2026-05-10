package sirttas.elementalcraft.assertion;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.resource.ResourceStack;
import org.assertj.core.api.AbstractComparableAssert;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.assertion.item.ItemStackAssert;
import sirttas.elementalcraft.assertion.resource.ResourceAssert;
import sirttas.elementalcraft.assertion.resource.ResourceHandlerAssert;
import sirttas.elementalcraft.assertion.resource.ResourceStackAssert;

public class Assertions extends org.assertj.core.api.Assertions {

    private Assertions() {}

    public static ItemStackAssert assertThat(ItemStack itemStack) {
        return ItemStackAssert.assertThat(itemStack);
    }

    public static <T extends Resource> ResourceAssert<T> assertThat(T resource) {
        return ResourceAssert.assertThat(resource);
    }

    public static <T extends Resource> ResourceStackAssert<T> assertThat(ResourceStack<T> resource) {
        return ResourceStackAssert.assertThat(resource);
    }

    public static <T extends Resource> ResourceHandlerAssert<T> assertThat(ResourceHandler<T> resource) {
        return ResourceHandlerAssert.assertThat(resource);
    }

    public static AbstractComparableAssert<?, ElementType> assertThat(ElementType elementType) {
        return org.assertj.core.api.Assertions.assertThat(elementType);
    }
}
