package sirttas.elementalcraft.block.airmill;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractAirMillBlock;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;

public interface AirMill {

    int getDamage();

    void setDamage(int damage);

    default boolean isBroken() {
        return getDamage() >= getMaxDamage();
    }

    static int getMaxDamage() {
        var stack = new ItemStack(ECItems.AIR_MILL);

        return stack.getMaxDamage();
    }

    static void renderMillBreaking(Level level, BlockPos pos) {
        BlockEntityHelper.renderItemBreaking(level, pos.above(), new ItemStack(ECItems.AIR_MILL));
    }

    static InteractionResult setMill(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand) {
        var blockEntity = level.getBlockEntity(pos);

        if (!(blockEntity instanceof AirMill mill)) {
            return InteractionResult.PASS;
        }

        var damage = stack.getDamageValue();
        var maxDamage = getMaxDamage();
        var millDamage = mill.getDamage();

        if (millDamage <= 0) {
            return InteractionResult.PASS;
        }

        var repair = Math.min(millDamage, maxDamage - damage);

        if (repair <= 0) {
            return InteractionResult.PASS;
        }

        mill.setDamage(millDamage - repair);
        level.setBlock(pos, state.setValue(AbstractAirMillBlock.BROKEN, false), Block.UPDATE_NEIGHBORS | Block.UPDATE_CLIENTS);

        var copy = stack.copy();

        copy.setDamageValue(damage + repair);
        if (copy.getDamageValue() >= maxDamage) {
            copy.shrink(1);
        }
        return InteractionResult.SUCCESS.heldItemTransformedTo(copy); // FIXME is this necessary
    }
}
