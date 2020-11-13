package sirttas.elementalcraft.block.shrine.enderlock;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.BlockPylonShrine;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.particle.ParticleHelper;

public class BlockEnderLockShrine extends BlockPylonShrine {

	public static final String NAME = "enderlockshrine";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(1D, 3D, 1D, 15D, 7D, 15D);
	private static final VoxelShape BASE_3 = Block.makeCuboidShape(3D, 7D, 3D, 13D, 12D, 13D);
	private static final VoxelShape BASE_4 = Block.makeCuboidShape(6D, 12D, 6D, 10D, 16D, 10D);

	private static final VoxelShape PIPE_NORTH = Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 3D);
	private static final VoxelShape PIPE_SOUTH = Block.makeCuboidShape(7D, 7D, 13D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_EAST = Block.makeCuboidShape(13D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_WEST = Block.makeCuboidShape(0D, 7D, 7D, 3D, 9D, 9D);

	private static final VoxelShape IRON_NORTH = Block.makeCuboidShape(7D, 12D, 5D, 9D, 14D, 6D);
	private static final VoxelShape IRON_SOUTH = Block.makeCuboidShape(7D, 12D, 10D, 9D, 14D, 11D);
	private static final VoxelShape IRON_EAST = Block.makeCuboidShape(10D, 12D, 7D, 11D, 14D, 9D);
	private static final VoxelShape IRON_WEST = Block.makeCuboidShape(5D, 12D, 7D, 6D, 14D, 9D);

	private static final VoxelShape UPPER_BASE = Block.makeCuboidShape(6D, 0D, 6D, 10D, 15D, 10D);
	private static final VoxelShape UPPER_RING_1 = Block.makeCuboidShape(5D, 5D, 5D, 11D, 7D, 11D);
	private static final VoxelShape UPPER_RING_2 = Block.makeCuboidShape(5D, 8D, 5D, 11D, 10D, 11D);
	private static final VoxelShape UPPER_RING_3 = Block.makeCuboidShape(5D, 11D, 5D, 11D, 13D, 11D);

	private static final VoxelShape LOWER_SHAPE = VoxelShapes.or(BASE_1, BASE_2, BASE_3, BASE_4, PIPE_NORTH, PIPE_SOUTH, PIPE_EAST, PIPE_WEST, IRON_NORTH, IRON_SOUTH, IRON_EAST, IRON_WEST);
	private static final VoxelShape UPPER_SHAPE = VoxelShapes.or(UPPER_BASE, UPPER_RING_1, UPPER_RING_2, UPPER_RING_3);

	public BlockEnderLockShrine() {
		super(ElementType.WATER);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return state.get(HALF) == DoubleBlockHalf.LOWER ? new TileEnderLockShrine() : null;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return state.get(HALF) == DoubleBlockHalf.LOWER ? LOWER_SHAPE : UPPER_SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected void doAnimateTick(TileShrine shrine, BlockState state, World world, BlockPos pos, Random rand) {
		ParticleHelper.createEnderParticle(world, Vector3d.copy(pos), 8 + rand.nextInt(5), rand);
	}
}