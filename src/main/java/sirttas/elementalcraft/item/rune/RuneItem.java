package sirttas.elementalcraft.item.rune;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.item.pipe.IPipeInteractingItem;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.property.ECProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class RuneItem extends ECItem implements IPipeInteractingItem {

	public static final String NAME = ECNames.RUNE;

	public RuneItem() {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(@Nonnull UseOnContext context) {
		return doUse(BlockEntityHelper.getRuneHandlerAt(context.getLevel(), context.getClickedPos()), context);
	}

	@Nonnull
	@Override
	public InteractionResult useOnPipe(@Nonnull ElementPipeBlockEntity pipe, @Nonnull UseOnContext context) {
		return doUse(BlockEntityHelper.getCapability(ElementalCraftCapabilities.RuneHandler.BLOCK, pipe, context.getClickedFace()), context);
	}

	@Nonnull
	public InteractionResult doUse(IRuneHandler handler, UseOnContext context) {
		if (handler == null) {
			return InteractionResult.PASS;
		}

		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();
		Player player = context.getPlayer();
		Rune rune = getRune(stack);

		if (rune != null && rune.canUpgrade(level, pos, context.getClickedFace(), handler)) {
			handler.addRune(rune);
			if (player != null && !player.getAbilities().instabuild) {
				stack.shrink(1);
				if (stack.isEmpty()) {
					player.setItemInHand(context.getHand(), ItemStack.EMPTY);
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
	}

	public static Rune getRune(ItemStack stack) {
		CompoundTag tag = NBTHelper.getECTag(stack);

		if (tag != null) {
			return ElementalCraftApi.RUNE_MANAGER.get(new ResourceLocation(tag.getString(ECNames.RUNE)));
		}
		return null;
	}

	public ItemStack getRuneStack(Rune rune) {
		return getRuneStack(rune.getId());
	}

	public ItemStack getRuneStack(ResourceLocation rune) {
		ItemStack stack = new ItemStack(this);

		NBTHelper.getOrCreateECTag(stack).putString(ECNames.RUNE, rune.toString());
		return stack;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flag) {
		Rune rune = getRune(stack);

		if (rune != null) {
			rune.addInformation(tooltip);
		}
	}

	@Nonnull
    @Override
	public Component getName(@Nonnull ItemStack stack) {
		Rune rune = getRune(stack);

		if (rune != null) {
			return rune.getDisplayName();
		}
		return super.getName(stack);
	}
}
