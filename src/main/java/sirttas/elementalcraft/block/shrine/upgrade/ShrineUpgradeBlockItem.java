package sirttas.elementalcraft.block.shrine.upgrade;

import net.minecraft.core.Holder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;

public class ShrineUpgradeBlockItem extends BlockItem {

    public ShrineUpgradeBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean canPlace(@NotNull BlockPlaceContext context, @NotNull BlockState state) {
        if (!super.canPlace(context, state)) {
            return false;
        }

        var shrineUpgradeBlock = getShrineUpgradeBlock();
        var facing = shrineUpgradeBlock.getFacing(state);
        var shrinePos = context.getClickedPos().relative(facing);

        return BlockEntityHelper.getBlockEntityAs(context.getLevel(), shrinePos, AbstractShrineBlockEntity.class)
                .map(shrine -> {
                    var upgrade = shrineUpgradeBlock.getUpgrade();

                    return shrine.canReceiveUpgrade(facing.getOpposite(), upgrade);
                })
                .orElse(false);
    }

    private AbstractShrineUpgradeBlock getShrineUpgradeBlock() {
        return (AbstractShrineUpgradeBlock) getBlock();
    }

    public Holder<ShrineUpgrade> getUpgrade() {
        return getShrineUpgradeBlock().getUpgrade();
    }
}
