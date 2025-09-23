package sirttas.elementalcraft.jewel;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.item.ECItems;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class JewelTestHelper {

    private JewelTestHelper() {}

    public static ItemStack createFullPureHolder() {
        var holder = new ItemStack(ECItems.PURE_HOLDER);

        holder.getCapability(ElementalCraftCapabilities.ElementStorages.ITEM).fill();
        return holder;
    }

    public static ItemStack createWithJewel(ItemLike item, Supplier<? extends Jewel> jewel) {
        var stack = new ItemStack(item);

        JewelHelper.setJewel(stack, jewel.get());
        return stack;
    }

    public static void assertElementUsed(Player player, ElementType elementType) {
        assertElementUsed(player, InteractionHand.OFF_HAND, elementType);
    }

    public static void assertElementUsed(Player player, InteractionHand hand, ElementType elementType) {
        var holder = player.getItemInHand(hand).getCapability(ElementalCraftCapabilities.ElementStorages.ITEM);

        assertThat(holder).isNotNull();
        assertThat(holder.getElementAmount(elementType))
                .describedAs("Element %s should have been used", elementType.getSerializedName())
                .isLessThan(holder.getElementCapacity(elementType));
    }
}
