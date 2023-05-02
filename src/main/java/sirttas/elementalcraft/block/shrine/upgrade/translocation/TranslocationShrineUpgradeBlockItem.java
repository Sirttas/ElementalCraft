package sirttas.elementalcraft.block.shrine.upgrade.translocation;


import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;

import javax.annotation.Nonnull;

public class TranslocationShrineUpgradeBlockItem extends BlockItem {

    public TranslocationShrineUpgradeBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static BlockPos getTargetAnchor(Player player) {
        var main = getTargetPos(player.getMainHandItem());

        if (main != null) {
            return main;
        }
        return getTargetPos(player.getOffhandItem());
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
        stack.getOrCreateTagElement(ECNames.BLOCK_ENTITY_TAG).put(ECNames.TARGET, NbtUtils.writeBlockPos(pos));
    }

    public static BlockPos getTargetPos(ItemStack stack) {
        var tag = stack.getTagElement(ECNames.BLOCK_ENTITY_TAG);

        if (tag == null || !tag.contains(ECNames.TARGET)) {
            return null;
        }
        return NbtUtils.readBlockPos(tag.getCompound(ECNames.TARGET));
    }
}
