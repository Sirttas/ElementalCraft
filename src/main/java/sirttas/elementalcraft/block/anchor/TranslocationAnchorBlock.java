package sirttas.elementalcraft.block.anchor;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sirttas.elementalcraft.block.shape.ECShapes;

import javax.annotation.Nonnull;

public class TranslocationAnchorBlock extends Block {

    public static final String NAME = "translocation_anchor";
    public static final MapCodec<TranslocationAnchorBlock> CODEC = simpleCodec(TranslocationAnchorBlock::new);

    private static final VoxelShape SHAPE = Shapes.or(ECShapes.SOURCE_DISPLACEMENT_PLATE_SHAPE, Block.box(3D, 3D, 3D, 13D, 4D, 13D));

    public TranslocationAnchorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<TranslocationAnchorBlock> codec() {
        return CODEC;
    }

    @Nonnull
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(@Nonnull BlockState state, LevelReader level, BlockPos pos) {
        var bellow = pos.below();

        return level.getBlockState(bellow).isFaceSturdy(level, bellow, Direction.UP) && super.canSurvive(state, level, pos);
    }

    @Override
    public void onPlace(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
        if (!oldState.is(this)) {
            var anchorList = TranslocationAnchorsSaveData.get(level);

            if (anchorList != null) {
                anchorList.addAnchor(pos);
                sendToPlayers(level);
            }
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    protected void affectNeighborsAfterRemoval(@NonNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, boolean movedByPiston) {
        var anchorList = TranslocationAnchorsSaveData.get(level);

        if (anchorList != null) {
            anchorList.removeAnchor(pos);
            sendToPlayers(level);
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    private void sendToPlayers(@Nonnull Level level) {
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersInDimension(serverLevel, TranslocationAnchorListPayload.create(level));
        }

    }

    @Override
    public boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
        return true;
    }
}
