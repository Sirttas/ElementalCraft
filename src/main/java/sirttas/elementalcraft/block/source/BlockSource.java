package sirttas.elementalcraft.block.source;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.block.AbstractBlockECTileProvider;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.material.ECMaterials;
import sirttas.elementalcraft.particle.ParticleHelper;

public class BlockSource extends AbstractBlockECTileProvider {

	private static final VoxelShape SHAPE = Block.makeCuboidShape(4D, 0D, 4D, 12D, 8D, 12D);

	public static final String NAME = "source";

	public BlockSource() {
		super(AbstractBlock.Properties.create(ECMaterials.SOURCE).hardnessAndResistance(-1.0F, 3600000.0F).setLightLevel(s -> 7).notSolid().noDrops());
		this.setDefaultState(this.stateContainer.getBaseState().with(ElementType.STATE_PROPERTY, ElementType.NONE));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileSource(ElementType.getElementType(state));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		container.add(ElementType.STATE_PROPERTY);
	}

	@Override
	@Deprecated
	public boolean isTransparent(BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return showShape(state, context) ? SHAPE : VoxelShapes.empty();

	}

	@Override
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.empty();
	}

	private boolean showShape(BlockState state, ISelectionContext context) {
		return Optional.ofNullable(context.getEntity()).filter(LivingEntity.class::isInstance).map(LivingEntity.class::cast)
				.filter(e -> Stream.of(e.getHeldItemMainhand(), e.getHeldItemOffhand())
						.anyMatch(s -> s.getItem() instanceof ISourceInteractable && ((ISourceInteractable) s.getItem()).canIteractWithSource(s, state)))
				.isPresent();
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile
	 * Entity is set
	 */
	@Override
	@Deprecated
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (ElementType.getElementType(state) == ElementType.NONE) {
			worldIn.setBlockState(pos, state.with(ElementType.STATE_PROPERTY, ElementType.random()));
		}
	}

	@Override
	@Deprecated
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) { // TODO 1.17 remove
		if (!TileEntityHelper.getTileEntityAs(world, pos, TileSource.class).isPresent()) {
			ParticleHelper.createSourceParticle(ElementType.getElementType(state), world, Vector3d.copyCentered(pos), rand);
		}
	}
}
