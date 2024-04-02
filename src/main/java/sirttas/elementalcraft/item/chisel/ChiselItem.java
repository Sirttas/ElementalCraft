package sirttas.elementalcraft.item.chisel;

import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.pipe.IPipeInteractingItem;

import javax.annotation.Nonnull;
import java.util.List;

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
		return doUse(BlockEntityHelper.getRuneHandlerAt(context.getLevel(), context.getClickedPos(), context.getClickedFace()), context);
	}

	@Nonnull
	@Override
	public InteractionResult useOnPipe(@Nonnull ElementPipeBlockEntity pipe, @Nonnull UseOnContext context) {
		return doUse(BlockEntityHelper.getCapability(ElementalCraftCapabilities.RuneHandler.BLOCK, pipe, context.getClickedFace()), context);
	}

	@Nonnull
	private InteractionResult doUse(IRuneHandler handler, UseOnContext context) {
		if (handler == null) {
			return InteractionResult.PASS;
		}

		Level level = context.getLevel();
		Player player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		List<Rune> runes = handler.getRunes();

		if (!runes.isEmpty() && player != null && player.isShiftKeyDown()) {
			if (!level.isClientSide) {
				for (Rune rune : runes) {
					if (!stack.isEmpty()) {
						stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(context.getHand()));
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
	public boolean hasCraftingRemainingItem(ItemStack stack) {
		return stack.getMaxDamage() - stack.getDamageValue() > 1;
	}

	@Override
	public @NotNull ItemStack getCraftingRemainingItem(ItemStack stack) {
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}

		var result = stack.copy();

		if (result.hurt(1, RandomSource.create(), null)) {
			return ItemStack.EMPTY;
		}
		return result;
	}
}
