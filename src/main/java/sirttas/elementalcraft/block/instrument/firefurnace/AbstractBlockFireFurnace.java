package sirttas.elementalcraft.block.instrument.firefurnace;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import sirttas.elementalcraft.block.BlockECContainer;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public abstract class AbstractBlockFireFurnace extends BlockECContainer {

	public AbstractBlockFireFurnace(Properties properties) {
		super(properties);
	}

	public AbstractBlockFireFurnace() {
		super();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final AbstractTileFireFurnace<?> furnace = (AbstractTileFireFurnace<?>) world.getTileEntity(pos);
		IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);
		ItemStack heldItem = player.getHeldItem(hand);
	
		if (furnace != null) {
			if (!inv.getStackInSlot(1).isEmpty()) {
				furnace.dropExperience(player);
				return this.onSlotActivated(inv, player, ItemStack.EMPTY, 1);
			}
			return this.onSlotActivated(inv, player, heldItem, 0);
		}
		return ActionResultType.PASS;
	}
}