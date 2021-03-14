package sirttas.elementalcraft.block.shrine.vacuum;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import sirttas.elementalcraft.block.shrine.AbstractBlockShrine;
import sirttas.elementalcraft.block.shrine.AbstractTileShrine;
import sirttas.elementalcraft.particle.ParticleHelper;

public class BlockVacuumShrine extends AbstractBlockShrine {

	public static final String NAME = "vacuumshrine";

	private static final VoxelShape SHAPE = VoxelShapes.or(Shapes.SHRINE_SHAPE, Block.makeCuboidShape(6D, 12D, 6D, 10D, 15D, 10D));

	public BlockVacuumShrine() {
		super(ElementType.AIR);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileVacuumShrine();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected void doAnimateTick(AbstractTileShrine shrine, BlockState state, World world, BlockPos pos, Random rand) {
		ParticleHelper.createEnderParticle(world, Vector3d.copy(pos), 3, rand);
	}
}