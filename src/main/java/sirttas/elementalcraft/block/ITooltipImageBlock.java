package sirttas.elementalcraft.block;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface ITooltipImageBlock {

    @Nonnull
    default Optional<TooltipComponent> getTooltipImage(@Nonnull ItemStack stack) {
        return Optional.empty();
    }
}
