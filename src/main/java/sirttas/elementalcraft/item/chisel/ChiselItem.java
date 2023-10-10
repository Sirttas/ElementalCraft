package sirttas.elementalcraft.item.chisel;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandlerHelper;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.pipe.IPipeInteractingItem;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.List;

public class ChiselItem extends ECItem implements IPipeInteractingItem {

	public static final String NAME = "chisel";

	public ChiselItem() {
		super(new Item.Properties().stacksTo(1).durability(250));
	}

	@Override
	public boolean isValidRepairItem(@Nonnull ItemStack toRepair, ItemStack repair) {
		return repair.is(ECTags.Items.INGOTS_SWIFT_ALLOY);
	}

	@Nonnull
    @Override
	public InteractionResult useOn(UseOnContext context) {
		return doUse(BlockEntityHelper.getRuneHandlerAt(context.getLevel(), context.getClickedPos()), context);
	}

	@Nonnull
	@Override
	public InteractionResult useOnPipe(@Nonnull ElementPipeBlockEntity pipe, @Nonnull UseOnContext context) {
		return doUse(RuneHandlerHelper.get(pipe, context.getClickedFace()), context);
	}

	@Nonnull
	public InteractionResult doUse(IRuneHandler handler, UseOnContext context) {
		Level level = context.getLevel();
		Player player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		List<Rune> runes = handler.getRunes();

		if (!runes.isEmpty() && player != null && player.isShiftKeyDown()) {
			if (!level.isClientSide) {
				for (Rune rune : runes) {
					if (!stack.isEmpty()) {
						stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
						level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY() + 0.25, player.getZ(), ECItems.RUNE.get().getRuneStack(rune)));
						handler.removeRune(rune);
					}
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}
}
