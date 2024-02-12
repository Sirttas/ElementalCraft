package sirttas.elementalcraft.block.instrument.io.mill.grindstone.water;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractMillBlock;
import sirttas.elementalcraft.block.shape.ShapeHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class WaterMillGrindstoneBlock extends AbstractMillBlock {

	public static final String NAME = "water_mill_grindstone";
	public static final MapCodec<WaterMillGrindstoneBlock> CODEC = simpleCodec(WaterMillGrindstoneBlock::new);

	private static final Map<Direction, VoxelShape> SHAPES = ShapeHelper.directionShapes(Direction.NORTH, Shapes.or(SHAPE_NORTH, Block.box(4D, 5D, 4D, 12D, 8D, 12D)));

	public WaterMillGrindstoneBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull MapCodec<WaterMillGrindstoneBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new WaterMillGrindstoneBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> type) {
		return createInstrumentTicker(level, type, ECBlockEntityTypes.WATER_MILL_GRINDSTONE);
	}

	@Nonnull
	@Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPES.get(state.getValue(FACING));
	}
}
