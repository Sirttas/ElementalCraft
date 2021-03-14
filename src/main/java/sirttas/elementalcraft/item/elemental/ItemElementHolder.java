package sirttas.elementalcraft.item.elemental;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.StaticElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.property.ECProperties;

public class ItemElementHolder extends ItemElemental implements ISourceInteractable {

	public static final String NAME = "element_holder";
	public static final String NAME_FIRE = NAME + "_fire";
	public static final String NAME_WATER = NAME + "_water";
	public static final String NAME_EARTH = NAME + "_earth";
	public static final String NAME_AIR = NAME + "_air";

	private static final String SAVED_POS = "saved_pos";

	private final int elementCapacity;

	public ItemElementHolder(ElementType elementType) {
		super(ECProperties.Items.ITEM_UNSTACKABLE, elementType);
		this.elementCapacity = ECConfig.COMMON.elementHolderCapacity.get();
	}

	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		ElementStorage storage = new ElementStorage(stack);
		
		if (nbt != null && nbt.contains(ECNames.PARENT)) {
			storage.readNBT(nbt.getCompound(ECNames.PARENT));
		}
		return SingleElementStorage.createProvider(storage);
	}

	public ISingleElementStorage getElementStorage(ItemStack stack) {
		return (ISingleElementStorage) CapabilityElementStorage.get(stack).orElse(new StaticElementStorage(elementType, 0));
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return elementCapacity / ECConfig.COMMON.elementHolderTransferAmount.get();
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	protected boolean isValidSource(BlockState state) {
		return state.getBlock().equals(ECBlocks.SOURCE) && ElementType.getElementType(state) == elementType;
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
		BlockPos pos = this.getSavedPos(stack);
		double reach = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
		
		if (player.getPosition().distanceSq(pos) + 1 > reach * reach || !this.tick(player.getEntityWorld(), player, pos, stack).isSuccessOrConsume()) {
			player.stopActiveHand();
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		this.removeSavedPos(stack);
	}

	private ActionResultType tick(World world, LivingEntity entity, BlockPos pos, ItemStack stack) {
		BlockState blockstate = world.getBlockState(pos);
		return TileEntityHelper.getElementStorageAt(world, pos).map(storage -> {
			ISingleElementStorage holder = getElementStorage(stack);
			boolean isSource = isValidSource(blockstate);

			if (isSource || entity.isSneaking()) {
				if (isSource || storage.canPipeExtract(elementType)) {
					return storage.transferTo(holder, elementType, ECConfig.COMMON.elementHolderTransferAmount.get()) > 0 ? ActionResultType.CONSUME : ActionResultType.PASS;
				}
			} else if (storage.canPipeInsert(elementType)) {
				return holder.transferTo(storage, ECConfig.COMMON.elementHolderTransferAmount.get()) > 0 ? ActionResultType.CONSUME : ActionResultType.PASS;
			}
			return ActionResultType.PASS;
		}).orElse(ActionResultType.PASS);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add((new TranslationTextComponent("tooltip.elementalcraft.percent_full",
				ItemStack.DECIMALFORMAT.format(getElementStorage(stack).getElementAmount() * 100 / elementCapacity)))
				.mergeStyle(TextFormatting.GREEN));
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		if (this.isInGroup(group)) {
			ItemStack full = new ItemStack(this);
			ISingleElementStorage storage = getElementStorage(full);

			storage.insertElement(elementCapacity, false);
			items.add(new ItemStack(this));
			items.add(full);
		}
	}

	@Override
	public int getDamage(ItemStack stack) {
		return 1000 * (elementCapacity - getElementStorage(stack).getElementAmount()) / elementCapacity;
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
	
	private class ElementStorage extends StaticElementStorage {

		private final ItemStack stack;
		
		public ElementStorage(ItemStack stack) {
			super(ItemElementHolder.this.elementType, ItemElementHolder.this.elementCapacity);
			this.stack = stack;
		}


		@Override
		public boolean usableInInventory() {
			return true;
		}
		
		@Override
		public int getElementAmount() {
			refresh();
			return super.getElementAmount();
		}
		
		@Override
		public int insertElement(int count, ElementType type, boolean simulate) {
			refresh();
			
			int value = super.insertElement(count, type, simulate);

			updateAmount();
			return value;
		}

		@Override
		public int extractElement(int count, ElementType type, boolean simulate) {
			refresh();
			
			int value = super.extractElement(count, type, simulate);

			updateAmount();
			return value;
		}
		

		private void refresh() {
			CompoundNBT tag = stack.getTag();
			
			if (tag != null && tag.contains(ECNames.ELEMENT_AMOUNT)) {
				elementAmount = tag.getInt(ECNames.ELEMENT_AMOUNT);
			}
		}
		
		private void updateAmount() {
			stack.getOrCreateTag().putInt(ECNames.ELEMENT_AMOUNT, elementAmount);
		}
	}
}
