package sirttas.elementalcraft.block.source;

import java.util.Optional;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.block.AbstractECBlockEntityProviderBlock;
import sirttas.elementalcraft.material.ECMaterials;

public class SourceBlock extends AbstractECBlockEntityProviderBlock {

	private static final VoxelShape SHAPE = Block.box(4D, 0D, 4D, 12D, 8D, 12D);

	public static final String NAME = "source";

	public SourceBlock() {
		super(AbstractBlock.Properties.of(ECMaterials.SOURCE).strength(-1.0F, 3600000.0F).lightLevel(s -> 7).noOcclusion().noDrops());
		this.registerDefaultState(this.stateDefinition.any().setValue(ElementType.STATE_PROPERTY, ElementType.NONE));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new SourceBlockEntity(ElementType.getElementType(state));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> container) {
		container.add(ElementType.STATE_PROPERTY);
	}

	@Override
	@Deprecated
	public boolean useShapeForLightOcclusion(BlockState state) {
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
				.filter(e -> Stream.of(e.getMainHandItem(), e.getOffhandItem())
						.anyMatch(s -> s.getItem() instanceof ISourceInteractable && ((ISourceInteractable) s.getItem()).canIteractWithSource(s, state)))
				.isPresent();
	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile
	 * Entity is set
	 */
	@Override
	@Deprecated
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (ElementType.getElementType(state) == ElementType.NONE) {
			worldIn.setBlockAndUpdate(pos, state.setValue(ElementType.STATE_PROPERTY, ElementType.random()));
		}
	}

	@Override
	@Deprecated
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}
}
