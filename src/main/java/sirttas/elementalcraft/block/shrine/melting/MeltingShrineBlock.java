package sirttas.elementalcraft.block.shrine.melting;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;

import javax.annotation.Nonnull;

public class MeltingShrineBlock extends AbstractShrineBlock<MeltingShrineBlockEntity> {

	public static final String NAME = "melting_shrine";
	public static final MapCodec<MeltingShrineBlock> CODEC = simpleCodec(MeltingShrineBlock::new);

	private static final VoxelShape BASE_1 = Block.box(3D, 12D, 3D, 13D, 13D, 13D);
	private static final VoxelShape BASE_2 = Block.box(0D, 13D, 0D, 16D, 16D, 16D);

	private static final VoxelShape SHAPE = Shapes.or(ECShapes.SHRINE_SHAPE, BASE_1, BASE_2);

	public MeltingShrineBlock(BlockBehaviour.Properties properties) {
		super(ElementType.FIRE, properties);
	}

	@Override
	protected @NotNull MapCodec<MeltingShrineBlock> codec() {
		return CODEC;
	}

	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected void doAnimateTick(AbstractShrineBlockEntity shrine, BlockState state, Level level, BlockPos pos, RandomSource rand) {
		double x = pos.getX() + (4 + rand.nextDouble() * 7) / 16;
		double y = pos.getY() + 6D / 16;
		double z = pos.getZ() + (4 + rand.nextDouble() * 7) / 16;

		level.addParticle(ParticleTypes.LAVA, x, y, z, 0.0D, 0.0D, 0.0D);
	}
}
