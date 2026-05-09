package sirttas.elementalcraft.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemInstance;

public class ECItemStackHelper {

    private ECItemStackHelper() {}

    public static boolean canBeDamaged(ItemInstance stack) {
        int maxDamage = stack.getOrDefault(DataComponents.MAX_DAMAGE, 0);
        int damage = Mth.clamp(stack.getOrDefault(DataComponents.DAMAGE, 0), 0, maxDamage);

        return stack.count() > 0 && maxDamage - damage > 1;
    }
}
