package sirttas.elementalcraft.item.holder;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.BlockEC;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.tile.element.IElementReceiver;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.item.receptacle.ISourceInteractable;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.property.ECProperties;

public class ItemElementHolder extends ItemEC implements ISourceInteractable {

	public static final String NAME = "element_holder";
	public static final String NAME_FIRE = NAME + "_fire";
	public static final String NAME_WATER = NAME + "_water";
	public static final String NAME_EARTH = NAME + "_earth";
	public static final String NAME_AIR = NAME + "_air";

	private final ElementType elementType;
	private final int elementAmountMax;

	public ItemElementHolder(ElementType elementType) {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
		this.elementType = elementType;
		this.elementAmountMax = ECConfig.CONFIG.elementHolderMaxAmount.get();
	}

	public ElementType getElementType() {
		return elementType;
	}

	public int getElementAmountMax() {
		return elementAmountMax;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	protected boolean isValidSource(BlockState state) {
		return state.getBlock().equals(ECBlocks.source) && state.get(ECProperties.ELEMENT_TYPE) == elementType;
	}

	@Override
	public boolean canIteractWithSource(ItemStack stack, BlockState state) {
		return isValidSource(state);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		BlockState blockstate = context.getWorld().getBlockState(context.getPos());
		ItemStack stack = context.getItem();

		if (isValidSource(blockstate)) {
			this.inserElement(stack, ECConfig.CONFIG.elementHolderTransferAmount.get());
			return ActionResultType.SUCCESS;
		}
		return BlockEC.getTileEntityAs(context.getWorld(), context.getPos(), IElementReceiver.class).map(r -> {
			int amount = Math.min(getElementAmount(stack), ECConfig.CONFIG.elementHolderTransferAmount.get());

			if (r.getElementType() == elementType) {
				amount -= r.inserElement(amount, elementType, true);

				if (amount > 0) {
					this.extractElement(stack, amount);
					r.inserElement(amount, elementType, false);
					return ActionResultType.SUCCESS;
				}
			}
			return ActionResultType.PASS;
		}).orElse(ActionResultType.PASS);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add((new TranslationTextComponent("tooltip.elementalcraft.percent_full", ItemStack.DECIMALFORMAT.format(getElementAmount(stack) * 100 / elementAmountMax)))
				.mergeStyle(TextFormatting.GREEN));
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			ItemStack full = new ItemStack(this);

			this.setElementAmount(full, elementAmountMax);
			items.add(new ItemStack(this));
			items.add(full);
		}
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 1000 * (elementAmountMax - getElementAmount(stack)) / elementAmountMax;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return 1000;
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	public int getElementAmount(ItemStack stack) {
		return stack.getTag() == null ? 0 : stack.getTag().getInt(ECNames.ELEMENT_AMOUNT);
	}

	public void setElementAmount(ItemStack stack, int amount) {
		stack.getOrCreateTag().putInt(ECNames.ELEMENT_AMOUNT, Math.min(amount, elementAmountMax));
	}

	public int inserElement(ItemStack stack, int amount) {
		int elementAmount = getElementAmount(stack);
		int newCount = Math.min(elementAmount + amount, elementAmountMax);
		int ret = amount - newCount + elementAmount;

		setElementAmount(stack, newCount);
		return ret;
	}

	public int extractElement(ItemStack stack, int amount) {
		int elementAmount = getElementAmount(stack);
		int newCount = Math.max(elementAmount - amount, 0);
		int ret = elementAmount - newCount;

		setElementAmount(stack, newCount);
		return ret;
	}

	public static ItemStack find(PlayerEntity player, ElementType elementType) {
        for(int i = 0; i < player.inventory.getSizeInventory(); ++i) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			Item item = stack.getItem();

			if (item instanceof ItemElementHolder && ((ItemElementHolder) item).getElementType() == elementType) {
				return stack;
            }
         }
			return ItemStack.EMPTY;
	}
}
