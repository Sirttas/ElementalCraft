package sirttas.elementalcraft.block.shrine.upgrade.translocation;


import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.component.ECDataComponents;

import javax.annotation.Nonnull;

public class TranslocationShrineUpgradeBlockItem extends BlockItem {

    public TranslocationShrineUpgradeBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static BlockPos getTargetAnchor(Player player) {
        var pos = getTargetPos(player.getMainHandItem());

        if (pos == null) {
            pos = getTargetPos(player.getOffhandItem());
        }
        return pos;
    }

    @Nonnull
    @Override
    public InteractionResult useOn(@Nonnull UseOnContext context) {
        var pos = context.getClickedPos();

        if (context.getLevel().getBlockState(pos).is(ECBlocks.TRANSLOCATION_ANCHOR.get())) {
            setTargetPos(context.getItemInHand(), pos);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    private void setTargetPos(ItemStack stack, BlockPos pos) {
        stack.set(ECDataComponents.TARGET_POS, pos);
    }

    public static BlockPos getTargetPos(ItemStack stack) {
        return stack.get(ECDataComponents.TARGET_POS);
    }
}
