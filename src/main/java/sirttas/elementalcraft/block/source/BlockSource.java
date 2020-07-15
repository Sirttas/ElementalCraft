package sirttas.elementalcraft.block.source;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.BlockEC;
import sirttas.elementalcraft.item.receptacle.ISourceInteractable;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.property.ECProperties;

public class BlockSource extends BlockEC {

	private static final VoxelShape SHAPE = Block.makeCuboidShape(4D, 0D, 4D, 12D, 8D, 12D);

	public static final String NAME = "source";

	public BlockSource() {
		super(AbstractBlock.Properties.create(Material.AIR).hardnessAndResistance(-1.0F, 3600000.0F).notSolid().noDrops());
		this.setDefaultState(this.stateContainer.getBaseState().with(ECProperties.ELEMENT_TYPE, ElementType.NONE));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		container.add(ECProperties.ELEMENT_TYPE);
	}

	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return showShape(context) ? SHAPE : VoxelShapes.empty();

	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.empty();
	}

	private boolean showShape(ISelectionContext context) {
		return Optional.ofNullable(context.getEntity()).filter(LivingEntity.class::isInstance).map(LivingEntity.class::cast)
				.filter(e -> Stream.of(e.getHeldItemMainhand(), e.getHeldItemOffhand())
						.anyMatch(s -> s.getItem() instanceof ISourceInteractable && ((ISourceInteractable) s.getItem()).canIteractWithSource(s)))
				.isPresent();
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile
	 * Entity is set
	 */
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (state.get(ECProperties.ELEMENT_TYPE) == ElementType.NONE) {
			worldIn.setBlockState(pos, state.with(ECProperties.ELEMENT_TYPE, ElementType.random()));
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		ParticleHelper.createSourceParticle(ElementType.getElementType(state), world, Vector3d.func_237491_b_(pos), rand);
	}
}
