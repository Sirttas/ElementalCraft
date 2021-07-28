package sirttas.elementalcraft.block.shrine.overload;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;

public class OverloadShrineBlock extends AbstractShrineBlock {

	public static final String NAME = "overloadshrine";

	private static final VoxelShape BASE = Shapes.or(ECShapes.SHRINE_SHAPE, Block.box(5D, 12D, 5D, 11D, 13D, 11D));

	private static final VoxelShape UP_SHAPE = Shapes.or(BASE, Block.box(0D, 13D, 0D, 16D, 16D, 16D));
	private static final VoxelShape NORTH_SHAPE = Shapes.or(BASE, Block.box(0D, 3D, 0D, 16D, 16D, 3D));
	private static final VoxelShape SOUTH_SHAPE = Shapes.or(BASE, Block.box(0D, 3D, 13D, 16D, 16D, 16D));
	private static final VoxelShape WEST_SHAPE = Shapes.or(BASE, Block.box(0D, 3D, 0D, 3D, 16D, 16D));
	private static final VoxelShape EAST_SHAPE = Shapes.or(BASE, Block.box(13D, 3D, 0D, 16D, 16D, 16D));

	public static final DirectionProperty FACING = DirectionProperty.create("facing", d -> d != Direction.DOWN);

	public OverloadShrineBlock() {
		super(ElementType.AIR);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new OverloadShrineBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createShrineTicker(level, type, OverloadShrineBlockEntity.TYPE);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		switch (state.getValue(FACING)) {
		case UP:
			return UP_SHAPE;
		case NORTH:
			return NORTH_SHAPE;
		case SOUTH:
			return SOUTH_SHAPE;
		case WEST:
			return WEST_SHAPE;
		case EAST:
			return EAST_SHAPE;
		default:
			return BASE;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction direction = context.getClickedFace().getOpposite();
		return this.defaultBlockState().setValue(FACING, direction.getAxis() == Direction.Axis.Y ? Direction.UP : direction);
	}
}