package sirttas.elementalcraft.block.shrine.upgrade.translocation;


import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import sirttas.elementalcraft.block.anchor.TranslocationAnchors;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradeItem;
import sirttas.elementalcraft.component.ECDataComponents;

import javax.annotation.Nonnull;

public class TranslocationShrineUpgradeItem extends ShrineUpgradeItem {

    public TranslocationShrineUpgradeItem(TranslocationShrineUpgradeBlock block, Properties properties) {
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
        var anchors = TranslocationAnchors.get(context.getLevel());

        if (anchors != null && anchors.contains(pos)) {
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
