package sirttas.elementalcraft.block.shrine.budding;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;

import javax.annotation.Nonnull;

public class BuddingShrineBlock extends AbstractShrineBlock<BuddingShrineBlockEntity> {

	public static final String NAME = "buddingshrine";
	public static final MapCodec<BuddingShrineBlock> CODEC = simpleCodec(BuddingShrineBlock::new);
	
	private static final VoxelShape BASE_1 = Block.box(2D, 10D, 2D, 14D, 12D, 14D);
	private static final VoxelShape BASE_2 = Block.box(0D, 12D, 0D, 16D, 14D, 16D);
	private static final VoxelShape PLATE = Block.box(1D, 14D, 1D, 15D, 16D, 15D);

	private static final VoxelShape SHAPE = Shapes.or(ECShapes.SHRINE_SHAPE, BASE_1, BASE_2, PLATE);

	public BuddingShrineBlock(BlockBehaviour.Properties properties) {
		super(ElementType.EARTH, properties);
		this.registerDefaultState(this.defaultBlockState()
				.setValue(WATERLOGGED, false));
	}

	@Override
	protected @NotNull MapCodec<BuddingShrineBlock> codec() {
		return CODEC;
	}

	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}
}
