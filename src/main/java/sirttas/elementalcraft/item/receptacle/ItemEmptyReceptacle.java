package sirttas.elementalcraft.item.receptacle;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.source.TileSource;
import sirttas.elementalcraft.block.tile.TileEntityHelper;

public class ItemEmptyReceptacle extends AbstractReceptacle implements ISourceInteractable {

	public static final String NAME = "receptacle_empty";

	public ItemEmptyReceptacle() {
	}

	public ItemEmptyReceptacle(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		BlockState blockstate = world.getBlockState(pos);

		if (blockstate.getBlock() == ECBlocks.SOURCE) {
			if (!world.isRemote) {
				ItemStack stack = ReceptacleHelper.createFrom(context.getItem(), ElementType.getElementType(blockstate));

				TileEntityHelper.getTileEntityAs(world, pos, TileSource.class).ifPresent(tile -> tile.write(stack.getOrCreateChildTag(ECNames.BLOCK_ENTITY_TAG)));
				context.getPlayer().setHeldItem(context.getHand(), stack);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

}
