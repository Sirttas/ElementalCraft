package sirttas.elementalcraft.item.rune;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.property.ECProperties;

public class RuneItem extends ECItem {

	public static final String NAME = ECNames.RUNE;

	public RuneItem() {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
	}

	@Nonnull
    @Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();
		Player player = context.getPlayer();
		IRuneHandler handler = BlockEntityHelper.getRuneHandlerAt(world, pos);
		Rune rune = getRune(stack);

		if (rune != null && rune.canUpgrade(world, pos, handler)) {
			handler.addRune(rune);
			if (!player.isCreative()) {
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
		ItemStack stack = new ItemStack(this);

		NBTHelper.getOrCreateECTag(stack).putString(ECNames.RUNE, rune.getId().toString());
		return stack;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
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

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			ElementalCraftApi.RUNE_MANAGER.getData().forEach((id, rune) -> items.add(getRuneStack(rune)));
		}
	}
}
