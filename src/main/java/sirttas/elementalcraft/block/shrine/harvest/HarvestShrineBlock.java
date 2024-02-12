package sirttas.elementalcraft.block.shrine.harvest;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;

import javax.annotation.Nonnull;

public class HarvestShrineBlock extends AbstractShrineBlock<HarvestShrineBlockEntity> {

	public static final String NAME = "harvestshrine";
	public static final MapCodec<HarvestShrineBlock> CODEC = simpleCodec(HarvestShrineBlock::new);

	private static final VoxelShape BASE_1 = Block.box(0D, 13D, 0D, 16D, 16D, 16D);
	private static final VoxelShape BASE_2 = Block.box(1D, 9D, 1D, 15D, 13D, 15D);
	private static final VoxelShape BASE_3 = Block.box(3D, 4D, 3D, 13D, 9D, 13D);

	private static final VoxelShape PIPE_1 = Block.box(1D, 2D, 1D, 3D, 9D, 3D);
	private static final VoxelShape PIPE_2 = Block.box(13D, 2D, 1D, 15D, 9D, 3D);
	private static final VoxelShape PIPE_3 = Block.box(1D, 2D, 13D, 3D, 9D, 15D);
	private static final VoxelShape PIPE_4 = Block.box(13D, 2D, 13D, 15D, 9D, 15D);

	private static final VoxelShape PIPE_UP = Block.box(7D, 0D, 7D, 9D, 4D, 9D);
	private static final VoxelShape PIPE_NORTH = Block.box(7D, 7D, 0D, 9D, 9D, 3D);
	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 7D, 13D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_EAST = Block.box(13D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_WEST = Block.box(0D, 7D, 7D, 3D, 9D, 9D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, BASE_3, PIPE_1, PIPE_2, PIPE_3, PIPE_4, PIPE_UP, PIPE_NORTH, PIPE_SOUTH, PIPE_EAST, PIPE_WEST);

	public HarvestShrineBlock(BlockBehaviour.Properties properties) {
		super(ElementType.EARTH, properties);
	}

	@Override
	protected @NotNull MapCodec<HarvestShrineBlock> codec() {
		return CODEC;
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
}
