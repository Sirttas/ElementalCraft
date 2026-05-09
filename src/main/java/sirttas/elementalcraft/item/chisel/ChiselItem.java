package sirttas.elementalcraft.item.chisel;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.common.ItemAbility;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.DamageableCraftingItem;
import sirttas.elementalcraft.item.ECItemAbilities;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.pipe.IPipeInteractingItem;

public class ChiselItem extends Item implements IPipeInteractingItem, DamageableCraftingItem {
	
	public static final String NAME_DRENCHED_IRON = "drenched_iron_chisel";
	public static final String NAME_SWIFT_ALLOY = "swift_alloy_chisel";
	public static final String NAME_FIREITE = "fireite_chisel";

	public ChiselItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		return doUse(BlockEntityHelper.getRuneHandlerAt(context.getLevel(), context.getClickedPos(), context.getClickedFace()), context);
	}

	@Override
	public InteractionResult useOnPipe(ElementPipeBlockEntity pipe, UseOnContext context) {
		return doUse(BlockEntityHelper.getCapability(ElementalCraftCapabilities.RuneHandlers.BLOCK, pipe, context.getClickedFace()), context);
	}

	private InteractionResult doUse(@Nullable IRuneHandler handler, UseOnContext context) {
		if (handler == null) {
			return InteractionResult.PASS;
		}

		var level = context.getLevel();
		var player = context.getPlayer();
		var stack = context.getItemInHand();
		var runes = handler.getRunes();

		if (!runes.isEmpty() && player != null && player.isShiftKeyDown()) {
			if (!level.isClientSide()) {
				for (var rune : runes) {
					if (!stack.isEmpty()) {
						stack.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());
						EntityHelper.dropAtFeet(level, player, ECItems.RUNE.get().getRuneStack(rune));
						handler.removeRune(rune);
					}
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

    @Override
    public boolean canPerformAction(ItemInstance stack, ItemAbility itemAbility) {
        return itemAbility == ECItemAbilities.CHISEL_INSCRIBE_RUNE;
    }
}
