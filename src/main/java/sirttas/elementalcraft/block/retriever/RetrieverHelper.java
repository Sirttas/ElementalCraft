package sirttas.elementalcraft.block.retriever;

import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.block.BlockEC;

public class RetrieverHelper {

	private static Optional<IInventory> getTarget(BlockState state, IBlockReader world, BlockPos pos) {
		return BlockEC.getTileEntityAs(world, pos.offset(state.get(BlockRetriever.TARGET)), IInventory.class);
	}

	public static ItemStack retrive(BlockState state, IBlockReader world, BlockPos pos, ItemStack output) {
		return getTarget(state, world, pos).map(t -> HopperTileEntity.putStackInInventoryAllSlots(null, t, output, state.get(BlockRetriever.TARGET).getOpposite())).orElse(output);
	}
}
