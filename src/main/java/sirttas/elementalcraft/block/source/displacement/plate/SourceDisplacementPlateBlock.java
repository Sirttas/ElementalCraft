package sirttas.elementalcraft.block.source.displacement.plate;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shape.ECShapes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SourceDisplacementPlateBlock extends AbstractECEntityBlock implements IElementTypeProvider {

    public static final String NAME = "source_displacement_plate";
    public static final String NAME_FIRE = NAME + "_fire";
    public static final String NAME_WATER = NAME + "_water";
    public static final String NAME_EARTH = NAME + "_earth";
    public static final String NAME_AIR = NAME + "_air";
    private static final VoxelShape SHAPE = Shapes.or(ECShapes.SOURCE_DISPLACEMENT_PLATE_SHAPE, Block.box(5D, 3D, 5D, 11D, 4D, 11D));

    private final ElementType elementType;

    public SourceDisplacementPlateBlock(ElementType elementType) {
        this.elementType = elementType;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new SourceDisplacementPlateBlockEntity(pos, state);
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

    @Nonnull
    @Override
    @Deprecated
    public InteractionResult use(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult hit) {
        SourceDisplacementPlateBlockEntity sourceDisplacementPlate = (SourceDisplacementPlateBlockEntity) level.getBlockEntity(pos);

        if (sourceDisplacementPlate != null) {
            if (!level.isClientSide) {
                sourceDisplacementPlate.start();
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    @Nullable
    public <U extends BlockEntity> BlockEntityTicker<U> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<U> type) {
        return createECTicker(level, type, ECBlockEntityTypes.SOURCE_DISPLACEMENT_PLATE, SourceDisplacementPlateBlockEntity::tick);
    }

    @Override
    public ElementType getElementType() {
        return elementType;
    }
}
