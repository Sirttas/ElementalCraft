package sirttas.elementalcraft.item.rune;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.entity.player.ECPlayerHelper;
import sirttas.elementalcraft.item.pipe.IPipeInteractingItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class RuneItem extends Item implements IPipeInteractingItem {

	public static final String NAME = ECNames.RUNE;

	public RuneItem(Item.Properties properties) {
		super(properties);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(@Nonnull UseOnContext context) {
		return doUse(BlockEntityHelper.getRuneHandlerAt(context.getLevel(), context.getClickedPos()), context).result();
	}

	@Nonnull
	@Override
	public ItemInteractionResult useOnPipe(@Nonnull ElementPipeBlockEntity pipe, @Nonnull UseOnContext context) {
		return doUse(BlockEntityHelper.getCapability(ElementalCraftCapabilities.RuneHandlers.BLOCK, pipe, context.getClickedFace()), context);
	}

	@Nonnull
	public ItemInteractionResult doUse(IRuneHandler handler, UseOnContext context) {
		if (handler == null) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		var level = context.getLevel();
		var pos = context.getClickedPos();
		var stack = context.getItemInHand();
		var player = context.getPlayer();
		var rune = getRune(stack);

		if (rune != null && handler.getRuneCount() < handler.getMaxRunes() && rune.value().canUpgrade(level, pos, context.getClickedFace(), handler.getRuneCount(rune.getKey()))) {
			handler.addRune(rune);
			if (player != null) {
				ECPlayerHelper.shrinkItemInHand(player, stack, context.getHand());
			}
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	public static Holder<Rune> getRune(ItemStack stack) {
		return stack.get(ECDataComponents.RUNE);
	}

	public ItemStack getRuneStack(Identifier rune) {
		return getRuneStack(ElementalCraftApi.RUNE_MANAGER.getOrCreateHolder(rune));
	}

	public ItemStack getRuneStack(Holder<Rune> rune) {
		ItemStack stack = new ItemStack(this);

		stack.set(ECDataComponents.RUNE, rune);
		return stack;
	}

	@Override
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Item.TooltipContext tooltipContext, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		var rune = getRune(stack);

		if (rune != null) {
			rune.value().addInformation(tooltip, flag);
		}
	}

	@Nonnull
    @Override
	public Component getName(@Nonnull ItemStack stack) {
		var rune = getRune(stack);
		var id = rune != null ? rune.getKey() : null;

		if (id != null) {
			var location = id.identifier();

			return Component.translatable("elementalcraft.rune." + location.getNamespace() + '.' + location.getPath());
		}
		return super.getName(stack);
	}
}
