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
import sirttas.elementalcraft.block.shape.Shapes;
import sirttas.elementalcraft.block.shrine.AbstractPylonShrineBlock;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.particle.ParticleHelper;

public class EnderLockShrineBlock extends AbstractPylonShrineBlock {

	public static final String NAME = "enderlockshrine";

	private static final VoxelShape BASE = Block.box(6D, 12D, 6D, 10D, 16D, 10D);

	private static final VoxelShape IRON_NORTH = Block.box(7D, 12D, 5D, 9D, 14D, 6D);
	private static final VoxelShape IRON_SOUTH = Block.box(7D, 12D, 10D, 9D, 14D, 11D);
	private static final VoxelShape IRON_EAST = Block.box(10D, 12D, 7D, 11D, 14D, 9D);
	private static final VoxelShape IRON_WEST = Block.box(5D, 12D, 7D, 6D, 14D, 9D);

	private static final VoxelShape UPPER_BASE = Block.box(6D, 0D, 6D, 10D, 15D, 10D);
	private static final VoxelShape UPPER_RING_1 = Block.box(5D, 5D, 5D, 11D, 7D, 11D);
	private static final VoxelShape UPPER_RING_2 = Block.box(5D, 8D, 5D, 11D, 10D, 11D);
	private static final VoxelShape UPPER_RING_3 = Block.box(5D, 11D, 5D, 11D, 13D, 11D);

	private static final VoxelShape LOWER_SHAPE = VoxelShapes.or(Shapes.SHRINE_SHAPE, BASE, IRON_NORTH, IRON_SOUTH, IRON_EAST, IRON_WEST);
	private static final VoxelShape UPPER_SHAPE = VoxelShapes.or(UPPER_BASE, UPPER_RING_1, UPPER_RING_2, UPPER_RING_3);

	public EnderLockShrineBlock() {
		super(ElementType.WATER);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER ? new EnderLockShrineBlockEntity() : null;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER ? LOWER_SHAPE : UPPER_SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected void doAnimateTick(AbstractShrineBlockEntity shrine, BlockState state, World world, BlockPos pos, Random rand) {
		ParticleHelper.createEnderParticle(world, Vector3d.atLowerCornerOf(pos), 8 + rand.nextInt(5), rand);
	}
}