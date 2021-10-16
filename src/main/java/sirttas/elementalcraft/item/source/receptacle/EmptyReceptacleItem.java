package sirttas.elementalcraft.item.source.receptacle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
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
import sirttas.elementalcraft.item.ECItems;

public class EmptyReceptacleItem extends AbstractReceptacleItem implements ISourceInteractable {

	public static final String NAME = "receptacle_empty";

	public EmptyReceptacleItem() {
	}

	public EmptyReceptacleItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState blockstate = level.getBlockState(pos);
		var player = context.getPlayer();

		if (blockstate.getBlock() == ECBlocks.SOURCE) {
			if (!level.isClientSide) {
				ItemStack stack = ReceptacleHelper.createFrom(context.getItemInHand(), ElementType.getElementType(blockstate));

				BlockEntityHelper.getBlockEntityAs(level, pos, SourceBlockEntity.class).ifPresent(source -> {
					if (source.isStabalized()) {
						source.setStabalized(false);
						level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY() + 0.25, player.getZ(), new ItemStack(ECItems.SOURCE_STABILIZER)));
					}
					source.save(stack.getOrCreateTagElement(ECNames.BLOCK_ENTITY_TAG));
				});
				player.setItemInHand(context.getHand(), stack);
				level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

}
