package sirttas.elementalcraft.block.cover;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.entity.player.ECPlayerHelper;

import java.util.function.Supplier;

public class CoverableBlockEntity extends AbstractECBlockEntity implements Coverable {

    protected BlockState coverState;

    public CoverableBlockEntity(BlockPos pos, BlockState state) {
        this(ECBlockEntityTypes.COVERABLE, pos, state);
    }

    protected CoverableBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        coverState = Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean hasFrame() {
        return getBlockState().getValue(CoverType.PROPERTY) != CoverType.NONE;
    }

    @Override
    public void putFrame() {
        if (level == null || hasFrame()) {
            return;
        }
        level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(CoverType.PROPERTY, CoverType.FRAME));
    }

    @Override
    public @NotNull BlockState getCoverState() {
        return coverState;
    }

    @Override
    public @NotNull BlockState getUncoveredState() {
        return getBlockState().setValue(CoverType.PROPERTY, CoverType.NONE);
    }

    public InteractionResult putCover(Player player, InteractionHand hand) {
        if (level == null) {
            return InteractionResult.PASS;
        }

        var stack = player.getItemInHand(hand);
        if (stack.isEmpty()) {
            return InteractionResult.PASS;
        }

        var item = stack.getItem();
        if (!(item instanceof BlockItem blockItem)) {
            return InteractionResult.PASS;
        }

        var state = blockItem.getBlock().defaultBlockState();
        if (state == coverState) {
            return InteractionResult.PASS;
        }

        if (!coverState.isAir()) {
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), new ItemStack(coverState.getBlock()));
        }
        coverState = state;
        level.setBlockAndUpdate(getBlockPos(), level.getBlockState(worldPosition).setValue(CoverType.PROPERTY, CoverType.COVERED));

        ECPlayerHelper.shrinkItemInHand(player, stack, hand);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        if (isCovered()) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(getCoverState().getBlock()));
        }
        super.preRemoveSideEffects(pos, state);
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        coverState = loadCoverState(input);
    }

    @Override
    public void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        saveCoverState(output);
    }
}
