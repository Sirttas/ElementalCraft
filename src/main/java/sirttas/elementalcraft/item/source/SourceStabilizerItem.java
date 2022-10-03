package sirttas.elementalcraft.item.source;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.property.ECProperties;

import javax.annotation.Nonnull;

public class SourceStabilizerItem extends ECItem implements ISourceInteractable {

	public static final String NAME = "source_stabilizer";
	
	public SourceStabilizerItem() {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
	}
	
	@Nonnull
    @Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();
		Player player = context.getPlayer();
		
		return BlockEntityHelper.getBlockEntityAs(level, pos, SourceBlockEntity.class)
				.map(source -> {
					if (player != null && !source.isStabilized() && !source.getTraitHolder().isFleeting()) {
						if (!source.isAnalyzed()) {
							player.displayClientMessage(Component.translatable("message.elementalcraft.missing_analysis"), true);
							return InteractionResult.PASS;
						}

						source.setStabilized(true);
						if (!player.isCreative()) {
							stack.shrink(1);
							if (stack.isEmpty()) {
								player.setItemInHand(context.getHand(), ItemStack.EMPTY);
							}
						}
						return InteractionResult.SUCCESS;
					}
					return InteractionResult.PASS;
				}).orElse(InteractionResult.PASS);
	}
	
}
