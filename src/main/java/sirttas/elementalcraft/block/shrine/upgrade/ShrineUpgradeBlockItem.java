package sirttas.elementalcraft.block.shrine.upgrade;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

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

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Item.TooltipContext tooltipContext, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @Nonnull TooltipFlag flag) {
        var upgrade = getUpgrade();

        if (upgrade.isBound()) {
            upgrade.value().addInformation(builder, flag);
        }
    }

    private AbstractShrineUpgradeBlock getShrineUpgradeBlock() {
        return (AbstractShrineUpgradeBlock) getBlock();
    }

    public Holder<@NotNull ShrineUpgrade> getUpgrade() {
        return getShrineUpgradeBlock().getUpgrade();
    }
}
