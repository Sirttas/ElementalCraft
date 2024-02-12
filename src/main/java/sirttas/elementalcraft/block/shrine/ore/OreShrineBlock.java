package sirttas.elementalcraft.block.shrine.ore;

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
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;

import javax.annotation.Nonnull;

public class OreShrineBlock extends AbstractShrineBlock<OreShrineBlockEntity> {

	public static final String NAME = "oreshrine";
	public static final MapCodec<OreShrineBlock> CODEC = simpleCodec(OreShrineBlock::new);

	private static final VoxelShape BASE = Block.box(5D, 12D, 5D, 11D, 16D, 11D);

	private static final VoxelShape PIPE_UP_N = Block.box(7D, 13D, 2D, 9D, 15D, 5D);
	private static final VoxelShape PIPE_UP_S = Block.box(7D, 13D, 11D, 9D, 15D, 14D);
	private static final VoxelShape PIPE_UP_E = Block.box(11D, 13D, 7D, 14D, 15D, 9D);
	private static final VoxelShape PIPE_UP_W = Block.box(2D, 13D, 7D, 5D, 15D, 9D);


	private static final VoxelShape SHAPE = Shapes.or(ECShapes.SHRINE_SHAPE, BASE, PIPE_UP_N, PIPE_UP_S, PIPE_UP_E, PIPE_UP_W);

	public OreShrineBlock(BlockBehaviour.Properties properties) {
		super(ElementType.EARTH, properties);
	}

	@Override
	protected @NotNull MapCodec<OreShrineBlock> codec() {
		return CODEC;
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
}
