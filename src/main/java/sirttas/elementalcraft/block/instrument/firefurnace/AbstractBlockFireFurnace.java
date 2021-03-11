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
import sirttas.elementalcraft.block.AbstractBlockECContainer;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public abstract class AbstractBlockFireFurnace extends AbstractBlockECContainer {

	protected AbstractBlockFireFurnace(Properties properties) {
		super(properties);
	}

	protected AbstractBlockFireFurnace() {
		super();
	}

	@Override
	@Deprecated
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final AbstractTileFireFurnace<?> furnace = (AbstractTileFireFurnace<?>) world.getTileEntity(pos);
		IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);
		ItemStack heldItem = player.getHeldItem(hand);
	
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
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return TileEntityHelper.isValidContainer(state.getBlock(), world, pos.down());
	}
}