package sirttas.elementalcraft.block.instrument.crystallizer;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.IItemHandler;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public class CrystallizerBlock extends AbstractECContainerBlock {

	public static final String NAME = "crystallizer";

	private static final VoxelShape BASE_1 = Block.box(0D, 1D, 0D, 16D, 3D, 16D);

	private static final VoxelShape CONNECTION = Block.box(6D, 0D, 6D, 10D, 1D, 10D);

	private static final VoxelShape BASE_PIPE_1 = Block.box(1D, 0D, 1D, 3D, 5D, 3D);
	private static final VoxelShape BASE_PIPE_2 = Block.box(13D, 0D, 1D, 15D, 5D, 3D);
	private static final VoxelShape BASE_PIPE_3 = Block.box(1D, 0D, 13D, 3D, 5D, 15D);
	private static final VoxelShape BASE_PIPE_4 = Block.box(13D, 0D, 13D, 15D, 5D, 15D);

	private static final VoxelShape TOP_1 = Block.box(4D, 3D, 4D, 12D, 12D, 12D);
	private static final VoxelShape TOP_2 = Block.box(3D, 9D, 3D, 13D, 10D, 13D);
	private static final VoxelShape HOLE_1 = Block.box(6D, 3D, 4D, 10D, 9D, 12D);
	private static final VoxelShape HOLE_2 = Block.box(4D, 3D, 6D, 12D, 9D, 10D);
	private static final VoxelShape TOP = Shapes.join(Shapes.or(TOP_1, TOP_2), Shapes.or(HOLE_1, HOLE_2), BooleanOp.ONLY_FIRST);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, CONNECTION, BASE_PIPE_1, BASE_PIPE_2, BASE_PIPE_3, BASE_PIPE_4, TOP);

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CrystallizerBlockEntity(pos, state);
	}
	
	@Override
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createECTicker(level, type, CrystallizerBlockEntity.TYPE, AbstractInstrumentBlockEntity::tick);
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		final CrystallizerBlockEntity crystallizer = (CrystallizerBlockEntity) world.getBlockEntity(pos);
		ItemStack heldItem = player.getItemInHand(hand);
		IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);

		if (crystallizer != null && (hand == InteractionHand.MAIN_HAND || !heldItem.isEmpty())) {
			if ((crystallizer.isLocked() || heldItem.isEmpty() || player.isShiftKeyDown()) && !crystallizer.getInventory().isEmpty()) {
				for (int i = 0; i < inv.getSlots(); i++) {
					this.onSlotActivated(inv, player, ItemStack.EMPTY, i);
				}
				return InteractionResult.SUCCESS;
			}
			for (int i = 0; i < inv.getSlots(); i++) {
				if (inv.getStackInSlot(i).isEmpty() && this.onSlotActivated(inv, player, heldItem, i).shouldSwing()) {
					return InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state.getBlock(), world, pos.below());
	}
}
