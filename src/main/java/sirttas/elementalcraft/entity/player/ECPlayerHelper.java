package sirttas.elementalcraft.entity.player;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ECPlayerHelper {

    private ECPlayerHelper() {}

    public static void shrinkItemInHand(Player player, InteractionHand hand) {
        shrinkItemInHand(player, player.getItemInHand(hand), hand);
    }

    public static void shrinkItemInHand(Player player, ItemStack itemStack, InteractionHand hand) {
        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
            if (itemStack.isEmpty()) {
                player.setItemInHand(hand, ItemStack.EMPTY);
            }
        }
    }

    public static ItemStack shrinkItem(ItemStack heldItem) {
        ItemStack copy = heldItem.copy();

        copy.shrink(1);
        if (heldItem.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return copy;
    }
}
