package sirttas.elementalcraft.block.tank;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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
import net.minecraftforge.common.ToolType;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.BlockECTileProvider;
import sirttas.elementalcraft.particle.ParticleHelper;

public class BlockTank extends BlockECTileProvider {

	public static final String NAME = "tank";

	private static final VoxelShape BASE = Block.makeCuboidShape(0D, 0D, 0D, 16D, 2D, 16D);
	private static final VoxelShape GLASS = Block.makeCuboidShape(2D, 2D, 2D, 14D, 15D, 14D);

	private static final VoxelShape PIPE_1 = Block.makeCuboidShape(1D, 2D, 1D, 3D, 16D, 3D);
	private static final VoxelShape PIPE_2 = Block.makeCuboidShape(13D, 2D, 1D, 15D, 16D, 3D);
	private static final VoxelShape PIPE_3 = Block.makeCuboidShape(1D, 2D, 13D, 3D, 16D, 15D);
	private static final VoxelShape PIPE_4 = Block.makeCuboidShape(13D, 2D, 13D, 15D, 16D, 15D);

	private static final VoxelShape CONNECTOR = Block.makeCuboidShape(6D, 15D, 6D, 10D, 16D, 10D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE, GLASS, PIPE_1, PIPE_2, PIPE_3, PIPE_4, CONNECTOR);

	public BlockTank() {
		super(AbstractBlock.Properties.create(Material.GLASS).hardnessAndResistance(2).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).notSolid());
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileTank();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 1.0F;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
		TileTank tank = (TileTank) world.getTileEntity(pos);

		if (tank != null && tank.getElementAmount() > 0 && tank.getElementType() != ElementType.NONE) {
			ParticleHelper.createSourceParticle(tank.getElementType(), world, Vector3d.func_237491_b_(pos).add(0, 0.2D, 0), rand);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}
