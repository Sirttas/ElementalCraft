package sirttas.elementalcraft.block.cover;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.entity.player.ECPlayerHelper;

import javax.annotation.Nonnull;
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
    public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);
        coverState = loadCoverState(compound, provider);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        saveCoverState(compound);
    }
}
