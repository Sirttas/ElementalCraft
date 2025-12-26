package sirttas.elementalcraft.item.chisel;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItemAbilities;
import sirttas.elementalcraft.item.ECItemStackHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.pipe.IPipeInteractingItem;

import javax.annotation.Nonnull;

public class ChiselItem extends TieredItem implements IPipeInteractingItem {
	
	public static final String NAME_DRENCHED_IRON = "drenched_iron_chisel";
	public static final String NAME_SWIFT_ALLOY = "swift_alloy_chisel";
	public static final String NAME_FIREITE = "fireite_chisel";


	public ChiselItem(Tier tier, Properties properties) {
		super(tier, properties);
	}

	@Nonnull
    @Override
	public InteractionResult useOn(@NotNull UseOnContext context) {
		return doUse(BlockEntityHelper.getRuneHandlerAt(context.getLevel(), context.getClickedPos(), context.getClickedFace()), context).result();
	}

	@Nonnull
	@Override
	public ItemInteractionResult useOnPipe(@Nonnull ElementPipeBlockEntity pipe, @Nonnull UseOnContext context) {
		return doUse(BlockEntityHelper.getCapability(ElementalCraftCapabilities.RuneHandlers.BLOCK, pipe, context.getClickedFace()), context);
	}

	@Nonnull
	private ItemInteractionResult doUse(IRuneHandler handler, UseOnContext context) {
		if (handler == null) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		var level = context.getLevel();
		var player = context.getPlayer();
		var stack = context.getItemInHand();
		var runes = handler.getRunes();

		if (!runes.isEmpty() && player != null && player.isShiftKeyDown()) {
			if (!level.isClientSide) {
				for (var rune : runes) {
					if (!stack.isEmpty()) {
						stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
						EntityHelper.dropAtFeet(level, player, ECItems.RUNE.get().getRuneStack(rune));
						handler.removeRune(rune);
					}
				}
			}
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility itemAbility) {
        return itemAbility == ECItemAbilities.CHISEL_INSCRIBE_RUNE;
    }

    @Override
	public boolean hasCraftingRemainingItem(@NotNull ItemStack stack) {
		return ECItemStackHelper.canBeDamaged(stack);
	}

	@Override
	public @NotNull ItemStack getCraftingRemainingItem(@NotNull ItemStack stack) {
		return ECItemStackHelper.damageItem(stack);
	}
}
