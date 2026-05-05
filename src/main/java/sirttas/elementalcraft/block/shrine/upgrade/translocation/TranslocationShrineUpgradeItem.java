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
        var pos = getTargetAnchor(player.getMainHandItem());

        if (pos == null) {
            pos = getTargetAnchor(player.getOffhandItem());
        }
        return pos;
    }

    @Nonnull
    @Override
    public InteractionResult useOn(@Nonnull UseOnContext context) {
        var pos = context.getClickedPos();
        var anchors = TranslocationAnchors.get(context.getLevel());

        if (anchors != null && anchors.has(pos)) {
            setTargetAnchor(context.getItemInHand(), pos);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    private void setTargetAnchor(ItemStack stack, BlockPos pos) {
        stack.set(ECDataComponents.TARGET_ANCHOR, pos);
    }

    public static BlockPos getTargetAnchor(ItemStack stack) {
        return stack.get(ECDataComponents.TARGET_ANCHOR);
    }
}
