package sirttas.elementalcraft.item.receptacle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
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
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState blockstate = world.getBlockState(pos);

		if (blockstate.getBlock() == ECBlocks.SOURCE) {
			if (!world.isClientSide) {
				ItemStack stack = ReceptacleHelper.createFrom(context.getItemInHand(), ElementType.getElementType(blockstate));

				BlockEntityHelper.getTileEntityAs(world, pos, SourceBlockEntity.class).ifPresent(tile -> tile.save(stack.getOrCreateTagElement(ECNames.BLOCK_ENTITY_TAG)));
				context.getPlayer().setItemInHand(context.getHand(), stack);
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
