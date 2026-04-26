package sirttas.elementalcraft.item.rune;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;
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
import java.util.function.Consumer;

public class RuneItem extends Item implements IPipeInteractingItem {

	public static final String NAME = ECNames.RUNE;

	public RuneItem(Item.Properties properties) {
		super(properties);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(@Nonnull UseOnContext context) {
		return doUse(BlockEntityHelper.getRuneHandlerAt(context.getLevel(), context.getClickedPos()), context);
	}

	@Nonnull
	@Override
	public InteractionResult useOnPipe(@Nonnull ElementPipeBlockEntity pipe, @Nonnull UseOnContext context) {
		return doUse(BlockEntityHelper.getCapability(ElementalCraftCapabilities.RuneHandlers.BLOCK, pipe, context.getClickedFace()), context);
	}

	@Nonnull
	public InteractionResult doUse(IRuneHandler handler, UseOnContext context) {
		if (handler == null) {
			return InteractionResult.PASS;
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
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	public static Holder<@NotNull Rune> getRune(ItemStack stack) {
		return stack.get(ECDataComponents.RUNE);
	}

	public ItemStackTemplate getRuneStackTemplate(ResourceKey<Rune> rune) {
		return getRuneStackTemplate(rune.identifier());
	}

	public ItemStackTemplate getRuneStackTemplate(Identifier rune) {
		return new ItemStackTemplate(this, DataComponentPatch.builder()
				.set(ECDataComponents.RUNE.get(), ElementalCraftApi.RUNE_MANAGER.getOrCreateHolder(rune))
				.build());
	}

	public ItemStack getRuneStack(Holder<@NotNull Rune> rune) {
		ItemStack stack = new ItemStack(this);

		stack.set(ECDataComponents.RUNE, rune);
		return stack;
	}

    @Override
    @Deprecated
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> builder, @NotNull TooltipFlag tooltipFlag) {
		var rune = getRune(itemStack);

		if (rune != null) {
			rune.value().appendHoverText(builder, tooltipFlag);
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
