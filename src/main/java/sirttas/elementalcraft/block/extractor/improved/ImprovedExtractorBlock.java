package sirttas.elementalcraft.block.extractor.improved;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.extractor.ExtractorBlock;
import sirttas.elementalcraft.block.extractor.ExtractorBlockEntity;

import javax.annotation.Nonnull;

public class ImprovedExtractorBlock extends ExtractorBlock {

	public static final String NAME = "extractor_improved";

	private static final VoxelShape BASE_1 = Block.box(6D, 0D, 6D, 10D, 4D, 10D);
	private static final VoxelShape BASE_2 = Block.box(0D, 1D, 0D, 16D, 3D, 16D);
	private static final VoxelShape PILLAR = Block.box(7D, 4D, 7D, 9D, 13D, 9D);
	private static final VoxelShape TOP = Block.box(6D, 13D, 6D, 10D, 16D, 10D);

	private static final VoxelShape SIDE_PILLAR_1 = Shapes.or(Block.box(1D, 0D, 1D, 3D, 7D, 3D), Block.box(0D, 7D, 0D, 4D, 10D, 4D));
	private static final VoxelShape SIDE_PILLAR_2 = SIDE_PILLAR_1.move(12D /16, 0D, 0D);
	private static final VoxelShape SIDE_PILLAR_3 = SIDE_PILLAR_1.move(0D, 0D, 12D /16);
	private static final VoxelShape SIDE_PILLAR_4 = SIDE_PILLAR_1.move(12D / 16, 0D, 12D / 16);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, PILLAR, TOP, SIDE_PILLAR_1, SIDE_PILLAR_2, SIDE_PILLAR_3, SIDE_PILLAR_4);

	@Override
	public ExtractorBlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new ExtractorBlockEntity(pos, state);
	}

	@Nonnull
	@Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
}
