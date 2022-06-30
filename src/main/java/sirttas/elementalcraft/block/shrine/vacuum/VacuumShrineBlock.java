package sirttas.elementalcraft.block.shrine.vacuum;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.particle.ParticleHelper;

import javax.annotation.Nonnull;

public class VacuumShrineBlock extends AbstractShrineBlock<VacuumShrineBlockEntity> {

	public static final String NAME = "vacuumshrine";

	private static final VoxelShape SHAPE = Shapes.or(ECShapes.SHRINE_SHAPE, Block.box(6D, 12D, 6D, 10D, 15D, 10D));

	public VacuumShrineBlock() {
		super(ElementType.AIR);
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	protected void doAnimateTick(AbstractShrineBlockEntity shrine, BlockState state, Level world, BlockPos pos, RandomSource rand) {
		ParticleHelper.createEnderParticle(world, Vec3.atLowerCornerOf(pos), 3, rand);
	}
}
