package sirttas.elementalcraft.item;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import sirttas.elementalcraft.block.ITooltipImageBlock;

import javax.annotation.Nonnull;
import java.util.Optional;

public class TooltipImageBlockItem extends BlockItem {

    public TooltipImageBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    @Nonnull
    public Optional<TooltipComponent> getTooltipImage(@Nonnull ItemStack stack) {
        if (this.getBlock() instanceof ITooltipImageBlock block) {
            return block.getTooltipImage(stack);
        }
        return super.getTooltipImage(stack);
    }
}
