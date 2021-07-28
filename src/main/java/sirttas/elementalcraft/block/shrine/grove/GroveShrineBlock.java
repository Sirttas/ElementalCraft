package sirttas.elementalcraft.block.shrine.grove;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;

public class GroveShrineBlock extends AbstractShrineBlock {

	public static final String NAME = "groveshrine";

	private static final VoxelShape BASE = Block.box(5D, 12D, 5D, 11D, 14D, 11D);

	private static final VoxelShape PIPE_1 = Block.box(2D, 7D, 2D, 4D, 16D, 4D);
	private static final VoxelShape PIPE_2 = Block.box(12D, 7D, 2D, 14D, 16D, 4D);
	private static final VoxelShape PIPE_3 = Block.box(2D, 7D, 12D, 4D, 16D, 14D);
	private static final VoxelShape PIPE_4 = Block.box(12D, 7D, 12D, 14D, 16D, 14D);
	private static final VoxelShape PIPE_CENTER = Block.box(7D, 15D, 7D, 9D, 16D, 9D);

	private static final VoxelShape PLATE = Block.box(3D, 14D, 3D, 13D, 15D, 13D);

	private static final VoxelShape SHAPE = Shapes.or(ECShapes.SHRINE_SHAPE, BASE, PIPE_1, PIPE_2, PIPE_3, PIPE_4, PIPE_CENTER, PLATE);

	public GroveShrineBlock() {
		super(ElementType.WATER);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GroveShrineBlockEntity(pos, state);
	}

	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createShrineTicker(level, type, GroveShrineBlockEntity.TYPE);
	}
	
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}