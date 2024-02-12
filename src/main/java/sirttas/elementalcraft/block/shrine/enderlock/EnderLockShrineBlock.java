package sirttas.elementalcraft.block.shrine.enderlock;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.AbstractPylonShrineBlock;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.particle.ParticleHelper;

import javax.annotation.Nonnull;

public class EnderLockShrineBlock extends AbstractPylonShrineBlock<EnderLockShrineBlockEntity> {

	public static final String NAME = "enderlockshrine";
	public static final MapCodec<EnderLockShrineBlock> CODEC = simpleCodec(EnderLockShrineBlock::new);

	private static final VoxelShape BASE = Block.box(6D, 12D, 6D, 10D, 16D, 10D);

	private static final VoxelShape IRON_NORTH = Block.box(7D, 12D, 5D, 9D, 14D, 6D);
	private static final VoxelShape IRON_SOUTH = Block.box(7D, 12D, 10D, 9D, 14D, 11D);
	private static final VoxelShape IRON_EAST = Block.box(10D, 12D, 7D, 11D, 14D, 9D);
	private static final VoxelShape IRON_WEST = Block.box(5D, 12D, 7D, 6D, 14D, 9D);

	private static final VoxelShape UPPER_BASE = Block.box(6D, 0D, 6D, 10D, 15D, 10D);
	private static final VoxelShape UPPER_RING_1 = Block.box(5D, 5D, 5D, 11D, 7D, 11D);
	private static final VoxelShape UPPER_RING_2 = Block.box(5D, 8D, 5D, 11D, 10D, 11D);
	private static final VoxelShape UPPER_RING_3 = Block.box(5D, 11D, 5D, 11D, 13D, 11D);

	private static final VoxelShape LOWER_SHAPE = Shapes.or(ECShapes.SHRINE_SHAPE, BASE, IRON_NORTH, IRON_SOUTH, IRON_EAST, IRON_WEST);
	private static final VoxelShape UPPER_SHAPE = Shapes.or(UPPER_BASE, UPPER_RING_1, UPPER_RING_2, UPPER_RING_3);

	public EnderLockShrineBlock(BlockBehaviour.Properties properties) {
		super(ElementType.WATER, properties);
	}

	@Override
	protected @NotNull MapCodec<EnderLockShrineBlock> codec() {
		return CODEC;
	}

	@Override
	public EnderLockShrineBlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? super.newBlockEntity(pos, state) : null;
	}
	
	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? LOWER_SHAPE : UPPER_SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected void doAnimateTick(AbstractShrineBlockEntity shrine, BlockState state, Level world, BlockPos pos, RandomSource rand) {
		ParticleHelper.createEnderParticle(world, Vec3.atLowerCornerOf(pos), 8 + rand.nextInt(5), rand);
	}
}
