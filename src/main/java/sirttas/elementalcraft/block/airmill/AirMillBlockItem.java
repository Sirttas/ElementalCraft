package sirttas.elementalcraft.block.airmill;

import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import sirttas.elementalcraft.component.ECDataComponents;

public class AirMillBlockItem extends BlockItem {

    public AirMillBlockItem(Block block, Properties properties) {
        super(block, properties.component(ECDataComponents.AIR_MILL_DAMAGE, 0));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        int damage = getAirMillDamage(stack);

        return damage > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int maxDamage = AirMill.getMaxDamage();
        int damage = getAirMillDamage(stack);

        if (damage > 0) {
            return Math.round((maxDamage - damage) * 13.0F / maxDamage);
        }
        return 0;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int maxDamage = AirMill.getMaxDamage();
        float f = Math.max(0.0F, (maxDamage - (float) getAirMillDamage(stack)) / maxDamage);

        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    private int getAirMillDamage(ItemStack stack) {
        return stack.getComponents().getOrDefault(ECDataComponents.AIR_MILL_DAMAGE.get(), 0);
    }

}
