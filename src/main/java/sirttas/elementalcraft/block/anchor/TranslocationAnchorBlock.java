package sirttas.elementalcraft.block.anchor;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
    @Deprecated
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return SHAPE;
    }

    @Override
    @Deprecated
    public boolean canSurvive(@Nonnull BlockState state, LevelReader level, BlockPos pos) {
        var bellow = pos.below();

        return level.getBlockState(bellow).isFaceSturdy(level, bellow, Direction.UP) && super.canSurvive(state, level, pos);
    }

    @Override
    @Deprecated
    public void onPlace(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState oldState, boolean isMoving) {
        if (!oldState.is(this)) {
            var anchorList = TranslocationAnchorList.get(level);

            if (anchorList != null) {
                anchorList.addAnchor(pos);
                sendToPlayers(level);
            }
        }
        super.onPlace(state, level, pos, oldState, isMoving);
    }

    @Override
    @Deprecated
    public void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (!newState.is(this)) {
            var anchorList = TranslocationAnchorList.get(level);

            if (anchorList != null) {
                anchorList.removeAnchor(pos);
                sendToPlayers(level);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    private void sendToPlayers(@Nonnull Level level) {
        PacketDistributor.DIMENSION.with(level.dimension()).send(TranslocationAnchorListPayload.create(level));
    }

    @Override
    @Deprecated
    public boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
        return true;
    }
}
