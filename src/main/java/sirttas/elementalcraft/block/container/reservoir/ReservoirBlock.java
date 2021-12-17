package sirttas.elementalcraft.block.container.reservoir;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.container.AbstractConnectedElementContainerBlock;
import sirttas.elementalcraft.block.shrine.AbstractPylonShrineBlock;
import sirttas.elementalcraft.config.ECConfig;

import javax.annotation.Nonnull;
import java.util.Random;

public class ReservoirBlock extends AbstractConnectedElementContainerBlock implements IElementTypeProvider {

	public static final String NAME = "reservoir";
	public static final String NAME_FIRE = NAME + "_fire";
	public static final String NAME_WATER = NAME + "_water";
	public static final String NAME_EARTH = NAME + "_earth";
	public static final String NAME_AIR = NAME + "_air";

	private static final VoxelShape UPPER_GLASS = Block.box(2D, 0D, 2D, 14D, 15D, 14D);

	private static final VoxelShape UPPER_PIPE_1 = Block.box(1D, 0D, 1D, 3D, 16D, 3D);
	private static final VoxelShape UPPER_PIPE_2 = Block.box(13D, 0D, 1D, 15D, 16D, 3D);
	private static final VoxelShape UPPER_PIPE_3 = Block.box(1D, 0D, 13D, 3D, 16D, 15D);
	private static final VoxelShape UPPER_PIPE_4 = Block.box(13D, 0D, 13D, 15D, 16D, 15D);

	private static final VoxelShape UPPER_CONNECTOR = Block.box(6D, 15D, 6D, 10D, 16D, 10D);

	private static final VoxelShape UPPER_SHAPE = Shapes.or(UPPER_GLASS, UPPER_PIPE_1, UPPER_PIPE_2, UPPER_PIPE_3, UPPER_PIPE_4, UPPER_CONNECTOR);

	private static final VoxelShape LOWER_GLASS_1 = Block.box(2D, 2D, 2D, 14D, 16D, 14D);
	private static final VoxelShape LOWER_GLASS_2 = Block.box(2D, 0D, 2D, 14D, 2D, 14D);
	
	private static final VoxelShape LOWER_PIPE_1 = Block.box(0D, 0D, 0D, 4D, 16D, 4D);
	private static final VoxelShape LOWER_PIPE_2 = Block.box(12D, 0D, 0D, 16D, 16D, 4D);
	private static final VoxelShape LOWER_PIPE_3 = Block.box(0D, 0D, 12D, 4D, 16D, 16D);
	private static final VoxelShape LOWER_PIPE_4 = Block.box(12D, 0D, 12D, 16D, 16D, 16D);
	
	private static final VoxelShape LOWER_WALL_NORTH = Block.box(4D, 2D, 1D, 12D, 9D, 3D);
	private static final VoxelShape LOWER_WALL_SOUTH = Block.box(4D, 2D, 13D, 12D, 9D, 15D);
	private static final VoxelShape LOWER_WALL_WEST = Block.box(1D, 2D, 4D, 3D, 9D, 12D);
	private static final VoxelShape LOWER_WALL_EAST = Block.box(13D, 2D, 4D, 15D, 9D, 12D);
	
	private static final VoxelShape LOWER_PLATE = Block.box(0D, 0D, 0D, 16D, 2D, 16D);
	
	private static final VoxelShape LOWER_BASE = Shapes.or(LOWER_GLASS_1, LOWER_GLASS_2, LOWER_PIPE_1, LOWER_PIPE_2, LOWER_PIPE_3, LOWER_PIPE_4, LOWER_WALL_NORTH, LOWER_WALL_SOUTH,
			LOWER_WALL_WEST, LOWER_WALL_EAST, LOWER_PLATE);
	
	private static final VoxelShape EARTH_WALL_NORTH = Block.box(4D, 9D, 1D, 12D, 14D, 3D);
	private static final VoxelShape EARTH_WALL_SOUTH = Block.box(4D, 9D, 13D, 12D, 14D, 15D);
	private static final VoxelShape EARTH_WALL_WEST = Block.box(1D, 9D, 4D, 3D, 14D, 12D);
	private static final VoxelShape EARTH_WALL_EAST = Block.box(13D, 9D, 4D, 15D, 14D, 12D);
	
	private static final VoxelShape LOWER_EARTH = Shapes.or(LOWER_BASE, EARTH_WALL_NORTH, EARTH_WALL_SOUTH, EARTH_WALL_WEST, EARTH_WALL_EAST);
	
	private static final VoxelShape AIR_PIPE_1 = Block.box(1D, 5D, 1D, 3D, 16D, 3D);
	private static final VoxelShape AIR_PIPE_2 = Block.box(13D, 5D, 1D, 15D, 16D, 3D);
	private static final VoxelShape AIR_PIPE_3 = Block.box(1D, 5D, 13D, 3D, 16D, 15D);
	private static final VoxelShape AIR_PIPE_4 = Block.box(13D, 5D, 13D, 15D, 16D, 15D);
	
	private static final VoxelShape AIR_PLATE = Block.box(0D, 5D, 0D, 16D, 7D, 16D);
	private static final VoxelShape AIR_PLATE_2 = Block.box(5D, 0D, 5D, 11D, 2D, 11D);
	
	private static final VoxelShape LOWER_AIR = Shapes.or(LOWER_GLASS_1, AIR_PIPE_1, AIR_PIPE_2, AIR_PIPE_3, AIR_PIPE_4, AIR_PLATE, AIR_PLATE_2);
	
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	private final ElementType elementType;
	
	public ReservoirBlock(ElementType elementType) {
		this.elementType = elementType;
		this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false));
	}
	
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new ReservoirBlockEntity(pos, state);
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}
	
	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place
	 * logic
	 */
	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, @Nonnull BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
		worldIn.setBlock(pos.above(), this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER), 3);
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	@Override
	public void playerWillDestroy(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull Player player) {
		AbstractPylonShrineBlock.doubleHalfHarvest(this, level, pos, state, player);
		super.playerWillDestroy(level, pos, state, player);
	}
	
	@Nonnull
	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		VoxelShape shape;

		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			shape = UPPER_SHAPE;
		} else {
			shape = switch (this.getElementType()) {
				case AIR -> LOWER_AIR;
				case EARTH -> LOWER_EARTH;
				default -> LOWER_BASE;
			};
		}
		return Shapes.or(shape, super.getShape(state, level, pos, context));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
		container.add(HALF, NORTH, SOUTH, EAST, WEST);
	}

	@Override
	protected int getDefaultCapacity() {
		return ECConfig.COMMON.reservoirCapacity.get();
	}
	
	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab group, NonNullList<ItemStack> items) {
		ItemStack stack = new ItemStack(this.asItem());
		CompoundTag tag = stack.getOrCreateTagElement(ECNames.BLOCK_ENTITY_TAG);

		tag.put(ECNames.ELEMENT_STORAGE, new SingleElementStorage(this.getElementType(), this.getDefaultCapacity(), this.getDefaultCapacity()).serializeNBT());
		items.add(new ItemStack(this.asItem()));
		items.add(stack);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Random rand) {
		if (state.getValue(ReservoirBlock.HALF) == DoubleBlockHalf.UPPER) {
			super.animateTick(state, level, pos, rand);
		}
	}

}
