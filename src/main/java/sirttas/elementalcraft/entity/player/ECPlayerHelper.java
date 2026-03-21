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
        shrinkItemInHand(player, itemStack, hand, 1);
    }

    public static void shrinkItemInHand(Player player, ItemStack itemStack, InteractionHand hand, int size) {
        if (!player.getAbilities().instabuild) {
            itemStack.shrink(size);
            if (itemStack.isEmpty()) {
                player.setItemInHand(hand, ItemStack.EMPTY);
            }
        }
    }
}
