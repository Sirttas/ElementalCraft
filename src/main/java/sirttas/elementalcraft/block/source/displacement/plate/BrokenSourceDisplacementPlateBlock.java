package sirttas.elementalcraft.block.source.displacement.plate;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.shape.ECShapes;

import javax.annotation.Nonnull;

public class BrokenSourceDisplacementPlateBlock extends Block {

    public static final String NAME = "broken_source_displacement_plate";
    public static final MapCodec<BrokenSourceDisplacementPlateBlock> CODEC = simpleCodec(BrokenSourceDisplacementPlateBlock::new);

    public BrokenSourceDisplacementPlateBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<BrokenSourceDisplacementPlateBlock> codec() {
        return CODEC;
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return ECShapes.SOURCE_DISPLACEMENT_PLATE_SHAPE;
    }

    @Override
    @Deprecated
    public boolean canSurvive(@Nonnull BlockState state, LevelReader level, BlockPos pos) {
        var bellow = pos.below();

        return level.getBlockState(bellow).isFaceSturdy(level, bellow, Direction.UP) && super.canSurvive(state, level, pos);
    }

    @Override
    @Deprecated
    public boolean useShapeForLightOcclusion(@Nonnull BlockState state) {
        return true;
    }

}
