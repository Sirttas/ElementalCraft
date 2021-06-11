package sirttas.elementalcraft.block.extractor.improved;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.block.extractor.ExtractorBlock;
import sirttas.elementalcraft.block.extractor.ExtractorBlockEntity;

public class ImprovedExtractorBlock extends ExtractorBlock {

	public static final String NAME = "extractor_improved";

	private static final VoxelShape BASE_1 = Block.box(6D, 0D, 6D, 10D, 4D, 10D);
	private static final VoxelShape BASE_2 = Block.box(0D, 1D, 0D, 16D, 3D, 16D);
	private static final VoxelShape PILLAR = Block.box(7D, 4D, 7D, 9D, 13D, 9D);
	private static final VoxelShape TOP = Block.box(6D, 13D, 6D, 10D, 16D, 10D);

	private static final VoxelShape SIDE_PILLAR_1 = VoxelShapes.or(Block.box(1D, 0D, 1D, 3D, 7D, 3D), Block.box(0D, 7D, 0D, 4D, 10D, 4D));
	private static final VoxelShape SIDE_PILLAR_2 = SIDE_PILLAR_1.move(12D /16, 0D, 0D);
	private static final VoxelShape SIDE_PILLAR_3 = SIDE_PILLAR_1.move(0D, 0D, 12D /16);
	private static final VoxelShape SIDE_PILLAR_4 = SIDE_PILLAR_1.move(12D / 16, 0D, 12D / 16);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, PILLAR, TOP, SIDE_PILLAR_1, SIDE_PILLAR_2, SIDE_PILLAR_3, SIDE_PILLAR_4);

	@Override
	public ExtractorBlockEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ExtractorBlockEntity(true);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
}