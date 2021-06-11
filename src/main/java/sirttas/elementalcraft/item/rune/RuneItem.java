package sirttas.elementalcraft.item.rune;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
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

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();
		PlayerEntity player = context.getPlayer();
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
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	public static Rune getRune(ItemStack stack) {
		CompoundNBT tag = NBTHelper.getECTag(stack);

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
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		Rune rune = getRune(stack);

		if (rune != null) {
			rune.addInformation(tooltip);
		}
	}

	@Override
	public ITextComponent getName(ItemStack stack) {
		Rune rune = getRune(stack);

		if (rune != null) {
			return rune.getDisplayName();
		}
		return super.getName(stack);
	}

	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.allowdedIn(group)) {
			ElementalCraftApi.RUNE_MANAGER.getData().forEach((id, rune) -> items.add(getRuneStack(rune)));
		}
	}
}
