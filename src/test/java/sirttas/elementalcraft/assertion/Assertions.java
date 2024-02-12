package sirttas.elementalcraft.assertion;

import net.minecraft.gametest.framework.GlobalTestReporter;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import sirttas.elementalcraft.container.ItemHandlerAssert;
import sirttas.elementalcraft.item.ItemStackAssert;

public class Assertions extends org.assertj.core.api.Assertions {

    static {
        GlobalTestReporter.replaceWith(new StackTraceLogTestReporter());
    }

    private Assertions() {}

    public static ItemStackAssert assertThat(ItemStack itemStack) {
        return ItemStackAssert.assertThat(itemStack);
    }

    public static ItemHandlerAssert assertThat(IItemHandler handler) {
        return ItemHandlerAssert.assertThat(handler);
    }
}
