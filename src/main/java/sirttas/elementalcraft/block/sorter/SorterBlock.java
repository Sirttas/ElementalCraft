package sirttas.elementalcraft.block.sorter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import sirttas.elementalcraft.block.AbstractECBlockEntityProviderBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;

public class SorterBlock extends AbstractECBlockEntityProviderBlock implements ISorterBlock {

	public static final String NAME = "sorter";

	private static final VoxelShape CORE_VOID = VoxelShapes.or(Block.box(5D, 6D, 6D, 11D, 10D, 10D), Block.box(6D, 5D, 6D, 10D, 11D, 10D),
			Block.box(6D, 6D, 5D, 10D, 10D, 11D));
	private static final VoxelShape CORE = VoxelShapes.or(VoxelShapes.join(Block.box(5D, 5D, 5D, 11D, 11D, 11D), CORE_VOID, IBooleanFunction.ONLY_FIRST),
			Block.box(6D, 6D, 6D, 10D, 10D, 10D));


	public SorterBlock() {
		this.registerDefaultState(this.stateDefinition.any().setValue(SOURCE, Direction.SOUTH).setValue(TARGET, Direction.NORTH));
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getClickedFace();
		return this.defaultBlockState().setValue(SOURCE, direction.getOpposite()).setValue(TARGET, direction);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new SorterBlockEntity();
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> container) {
		container.add(SOURCE, TARGET);
	}

	@Override
	public VoxelShape getCoreShape(BlockState state) {
		return CORE;
	}
	
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return worldIn instanceof World && ((World) worldIn).isClientSide ? getShape(state, pos, Minecraft.getInstance().hitResult) : getCurentShape(state);
	}
	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return getCurentShape(state);
	}

	@Override
	@Deprecated
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		VoxelShape shape = getShape(state, pos, hit);

		if (CORE.equals(shape)) {
			return BlockEntityHelper.getTileEntityAs(world, pos, SorterBlockEntity.class).map(sorter -> sorter.addStack(player.getItemInHand(hand))).orElse(ActionResultType.PASS);
		} 
		return this.moveIO(state, world, pos, hit, shape);
	}
}
