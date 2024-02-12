package sirttas.elementalcraft.block.shrine.spawning;

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

public class SpawningShrineBlock extends AbstractShrineBlock<SpawningShrineBlockEntity> {

	public static final String NAME = "spawningshrine";
	public static final MapCodec<SpawningShrineBlock> CODEC = simpleCodec(SpawningShrineBlock::new);

	private static final VoxelShape BASE = Block.box(1D, 1D, 1D, 15D, 15D, 15D);

	private static final VoxelShape SHAPE = Shapes.or(ECShapes.SHRINE_SHAPE, BASE);

	public SpawningShrineBlock(BlockBehaviour.Properties properties) {
		super(ElementType.FIRE, properties);
	}

	@Override
	protected @NotNull MapCodec<SpawningShrineBlock> codec() {
		return CODEC;
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
}
