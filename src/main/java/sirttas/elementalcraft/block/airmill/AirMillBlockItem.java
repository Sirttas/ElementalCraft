package sirttas.elementalcraft.block.airmill;

import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.component.ECDataComponents;

import javax.annotation.Nonnull;

public class AirMillBlockItem extends BlockItem {

    public AirMillBlockItem(Block block, Properties properties) {
        super(block, properties.component(ECDataComponents.AIR_MILL_DAMAGE, 0));
    }

    @Override
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        int damage = getAirMillDamage(stack);

        return damage > 0 && damage < AirMill.getMaxDamage();
    }

    @Override
    public int getBarWidth(@Nonnull ItemStack stack) {
        int maxDamage = AirMill.getMaxDamage();
        int damage = getAirMillDamage(stack);

        if (damage > 0) {
            return Math.round((maxDamage - damage) * 13.0F / maxDamage);
        }
        return 0;
    }

    @Override
    public int getBarColor(@Nonnull ItemStack stack) {
        int maxDamage = AirMill.getMaxDamage();
        float f = Math.max(0.0F, (maxDamage - (float) getAirMillDamage(stack)) / maxDamage);

        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    private int getAirMillDamage(@NotNull ItemStack stack) {
        return stack.getComponents().getOrDefault(ECDataComponents.AIR_MILL_DAMAGE.get(), 0);
    }

}
