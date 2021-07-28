package sirttas.elementalcraft.block.instrument.firefurnace;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandler;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public abstract class AbstractFireFurnaceBlock extends AbstractECContainerBlock {

	protected AbstractFireFurnaceBlock(Properties properties) {
		super(properties);
	}

	protected AbstractFireFurnaceBlock() {
		super();
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		final AbstractFireFurnaceBlockEntity<?> furnace = (AbstractFireFurnaceBlockEntity<?>) world.getBlockEntity(pos);
		IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);
		ItemStack heldItem = player.getItemInHand(hand);
	
		if (furnace != null && (hand == InteractionHand.MAIN_HAND || !heldItem.isEmpty())) {
			if (!inv.getStackInSlot(1).isEmpty()) {
				furnace.dropExperience(player);
				return this.onSlotActivated(inv, player, ItemStack.EMPTY, 1);
			}
			return this.onSlotActivated(inv, player, heldItem, 0);
		}
		return InteractionResult.PASS;
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state.getBlock(), world, pos.below());
	}
}