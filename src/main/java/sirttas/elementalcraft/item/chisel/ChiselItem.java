package sirttas.elementalcraft.item.chisel;

import java.util.List;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sirttas.elementalcraft.ElementalCraftTab;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.item.ECItems;

public class ChiselItem extends ECItem {

	public static final String NAME = "chisel";

	public ChiselItem() {
		super(new Item.Properties().tab(ElementalCraftTab.TAB).stacksTo(1).durability(ECConfig.COMMON.chiselDurability.get()));
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return repair.getItem() == ECItems.SWIFT_ALLOY_INGOT;
	}
	
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
		PlayerEntity player = context.getPlayer();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();
		IRuneHandler handler = BlockEntityHelper.getRuneHandlerAt(world, pos);
		List<Rune> runes = handler.getRunes();
		
		if (!runes.isEmpty()) {
			if (!world.isClientSide) {
				for (Rune rune : runes) {
					if (!stack.isEmpty()) {
						stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
						world.addFreshEntity(new ItemEntity(world, player.getX(), player.getY() + 0.25, player.getZ(), ECItems.RUNE.getRuneStack(rune)));
						handler.removeRune(rune);
					}
				}
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
}
