package sirttas.elementalcraft.block.shrine.lava;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shape.Shapes;
import sirttas.elementalcraft.block.shrine.AbstractBlockShrine;
import sirttas.elementalcraft.block.shrine.AbstractTileShrine;

public class BlockLavaShrine extends AbstractBlockShrine {

	public static final String NAME = "lavashrine";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(3D, 12D, 3D, 13D, 13D, 13D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(0D, 13D, 0D, 16D, 16D, 16D);

	private static final VoxelShape SHAPE = VoxelShapes.or(Shapes.SHRINE_SHAPE, BASE_1, BASE_2);

	public BlockLavaShrine() {
		super(ElementType.FIRE);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileLavaShrine();
	}


	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected void doAnimateTick(AbstractTileShrine shrine, BlockState state, World world, BlockPos pos, Random rand) {
		double x = pos.getX() + (4 + rand.nextDouble() * 7) / 16;
		double y = pos.getY() + 6D / 16;
		double z = pos.getZ() + (4 + rand.nextDouble() * 7) / 16;

		world.addParticle(ParticleTypes.LAVA, x, y, z, 0.0D, 0.0D, 0.0D);
	}
}