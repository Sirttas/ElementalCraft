package sirttas.elementalcraft.item.holder;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.math.NumberUtils;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.api.element.IElementReceiver;
import sirttas.elementalcraft.api.element.IElementSender;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ItemEC;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.property.ECProperties;

public class ItemElementHolder extends ItemEC implements ISourceInteractable {

	public static final String NAME = "element_holder";
	public static final String NAME_FIRE = NAME + "_fire";
	public static final String NAME_WATER = NAME + "_water";
	public static final String NAME_EARTH = NAME + "_earth";
	public static final String NAME_AIR = NAME + "_air";

	private static final String SAVED_POS = "saved_pos";

	private final ElementType elementType;
	private final int elementCapacity;

	public ItemElementHolder(ElementType elementType) {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
		this.elementType = elementType;
		this.elementCapacity = ECConfig.COMMON.elementHolderCapacity.get();
	}

	public ElementType getElementType() {
		return elementType;
	}

	public int getElementCapacity() {
		return elementCapacity;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return ECConfig.COMMON.elementHolderCapacity.get() / ECConfig.COMMON.elementHolderTransferAmount.get();
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.SPEAR;
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
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		ItemStack stack = context.getItem();
		PlayerEntity player = context.getPlayer();
		ActionResultType result = tick(world, player, pos, stack);

		if (result.isSuccessOrConsume()) {
			this.setSavedPos(stack, pos);
			player.setActiveHand(context.getHand());
		}
		return result;
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
		if (!this.tick(player.getEntityWorld(), player, this.getSavedPos(stack), stack).isSuccessOrConsume()) {
			player.stopActiveHand();
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		this.removeSavedPos(stack);
	}

	private ActionResultType tick(World world, LivingEntity entity, BlockPos pos, ItemStack stack) {
		BlockState blockstate = world.getBlockState(pos);

		if (isValidSource(blockstate)) {
			this.inserElement(stack, ECConfig.COMMON.elementHolderTransferAmount.get());
			return ActionResultType.CONSUME;
		}
		if (entity.isSneaking()) {

			return TileEntityHelper.getTileEntityAs(world, pos, IElementSender.class).filter(sender -> sender.getElementType() == elementType).map(sender -> {
				int amount = NumberUtils.min(sender.getElementAmount(), ECConfig.COMMON.elementHolderTransferAmount.get(), this.getElementCapacity() - getElementAmount(stack));

				if (amount > 0) {
					sender.extractElement(amount, elementType, false);
					this.inserElement(stack, amount);
					return ActionResultType.CONSUME;
				}
				return ActionResultType.PASS;
			}).orElse(ActionResultType.PASS);
		}
		return TileEntityHelper.getTileEntityAs(world, pos, IElementReceiver.class).filter(receiver -> receiver.getElementType() == elementType || receiver.getElementType() == ElementType.NONE)
				.map(receiver -> {
					int amount = NumberUtils.min(getElementAmount(stack), ECConfig.COMMON.elementHolderTransferAmount.get(), receiver.getElementCapacity() - receiver.getElementAmount());

					if (amount > 0) {
						this.extractElement(stack, amount);
						receiver.inserElement(amount, elementType, false);
						return ActionResultType.CONSUME;
					}
					return ActionResultType.PASS;
				}).orElse(ActionResultType.PASS);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add((new TranslationTextComponent("tooltip.elementalcraft.percent_full", ItemStack.DECIMALFORMAT.format(getElementAmount(stack) * 100
				/ elementCapacity)))
				.mergeStyle(TextFormatting.GREEN));
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			ItemStack full = new ItemStack(this);

			this.setElementAmount(full, elementCapacity);
			items.add(new ItemStack(this));
			items.add(full);
		}
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 1000 * (elementCapacity - getElementAmount(stack)) / elementCapacity;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return 1000;
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	public int getElementAmount(ItemStack stack) {
		return stack.getTag() == null ? 0 : stack.getTag().getInt(ECNames.ELEMENT_AMOUNT);
	}

	public void setElementAmount(ItemStack stack, int amount) {
		stack.getOrCreateTag().putInt(ECNames.ELEMENT_AMOUNT, Math.min(amount, elementCapacity));
	}

	public BlockPos getSavedPos(ItemStack stack) {
		CompoundNBT tag = stack.getTag();
		
		if (tag != null) {
			CompoundNBT savedPos = tag.getCompound(SAVED_POS);

			if (savedPos != null) {
				return new BlockPos(savedPos.getInt("x"), savedPos.getInt("y"), savedPos.getInt("z"));
			}
		}
		return null;
	}

	public void setSavedPos(ItemStack stack, BlockPos pos) {
		CompoundNBT savedPos = new CompoundNBT();

		savedPos.putInt("x", pos.getX());
		savedPos.putInt("y", pos.getY());
		savedPos.putInt("z", pos.getZ());
		stack.getOrCreateTag().put(SAVED_POS, savedPos);
	}

	public void removeSavedPos(ItemStack stack) {
		CompoundNBT tag = stack.getTag();

		if (tag != null) {
			tag.remove(SAVED_POS);
		}
	}

	public int inserElement(ItemStack stack, int amount) {
		int elementAmount = getElementAmount(stack);
		int newCount = Math.min(elementAmount + amount, elementCapacity);
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
