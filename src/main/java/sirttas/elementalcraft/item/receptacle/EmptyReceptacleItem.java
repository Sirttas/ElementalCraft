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
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.source.SourceBlockEntity;

public class EmptyReceptacleItem extends AbstractReceptacleItem implements ISourceInteractable {

	public static final String NAME = "receptacle_empty";

	public EmptyReceptacleItem() {
	}

	public EmptyReceptacleItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState blockstate = world.getBlockState(pos);

		if (blockstate.getBlock() == ECBlocks.SOURCE) {
			if (!world.isClientSide) {
				ItemStack stack = ReceptacleHelper.createFrom(context.getItemInHand(), ElementType.getElementType(blockstate));

				BlockEntityHelper.getTileEntityAs(world, pos, SourceBlockEntity.class).ifPresent(tile -> tile.save(stack.getOrCreateTagElement(ECNames.BLOCK_ENTITY_TAG)));
				context.getPlayer().setItemInHand(context.getHand(), stack);
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

}
