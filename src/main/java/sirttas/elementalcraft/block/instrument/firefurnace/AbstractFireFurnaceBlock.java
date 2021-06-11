package sirttas.elementalcraft.block.instrument.firefurnace;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
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
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final AbstractFireFurnaceBlockEntity<?> furnace = (AbstractFireFurnaceBlockEntity<?>) world.getBlockEntity(pos);
		IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);
		ItemStack heldItem = player.getItemInHand(hand);
	
		if (furnace != null && (hand == Hand.MAIN_HAND || !heldItem.isEmpty())) {
			if (!inv.getStackInSlot(1).isEmpty()) {
				furnace.dropExperience(player);
				return this.onSlotActivated(inv, player, ItemStack.EMPTY, 1);
			}
			return this.onSlotActivated(inv, player, heldItem, 0);
		}
		return ActionResultType.PASS;
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state.getBlock(), world, pos.below());
	}
}