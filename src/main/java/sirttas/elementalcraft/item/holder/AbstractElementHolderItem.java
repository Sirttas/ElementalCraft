package sirttas.elementalcraft.item.holder;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.property.ECProperties;

public abstract class AbstractElementHolderItem extends ECItem implements ISourceInteractable {
	
	private static final String SAVED_POS = "saved_pos";

	protected final int elementCapacity;
	protected final int transferAmount;
	
	protected AbstractElementHolderItem(int elementCapacity, int transferAmount) {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
		this.elementCapacity = elementCapacity;
		this.transferAmount = transferAmount;
	}

	public abstract IElementStorage getElementStorage(ItemStack stack);

	@Override
	public int getUseDuration(ItemStack stack) {
		return elementCapacity / transferAmount;
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	protected boolean isValidSource(BlockState state) {
		return state.getBlock().equals(ECBlocks.SOURCE);
	}

	@Override
	public boolean canInteractWithSource(ItemStack stack, BlockState state) {
		return isValidSource(state);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		BlockPos pos = context.getClickedPos();
		Level world = context.getLevel();
		ItemStack stack = context.getItemInHand();
		Player player = context.getPlayer();
		InteractionResult result = tick(world, player, pos, stack);

		if (result.consumesAction()) {
			this.setSavedPos(stack, pos);
			player.startUsingItem(context.getHand());
		}
		return result;
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
		BlockPos pos = this.getSavedPos(stack);
		double reach = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
		
		if (player.blockPosition().distSqr(pos) + 1 > reach * reach || !this.tick(player.getCommandSenderWorld(), player, pos, stack).consumesAction()) {
			player.releaseUsingItem();
		}
	}

	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		this.removeSavedPos(stack);
	}

	protected abstract ElementType getElementType(IElementStorage target, BlockState blockstate);
	
	private InteractionResult tick(Level world, LivingEntity entity, BlockPos pos, ItemStack stack) {
		BlockState blockstate = world.getBlockState(pos);
		return BlockEntityHelper.getElementStorageAt(world, pos).map(storage -> {
			IElementStorage holder = getElementStorage(stack);
			boolean isSource = isValidSource(blockstate);
			ElementType elementType = this.getElementType(storage, blockstate);

			if (elementType != ElementType.NONE) {
				if (isSource || entity.isShiftKeyDown()) {
					if (isSource || storage.canPipeExtract(elementType)) {
						return storage.transferTo(holder, elementType, transferAmount) > 0 ? InteractionResult.CONSUME : InteractionResult.PASS;
					}
				} else if (storage.canPipeInsert(elementType)) {
					return holder.transferTo(storage, elementType, transferAmount) > 0 ? InteractionResult.CONSUME : InteractionResult.PASS;
				}
			}
			return InteractionResult.PASS;
		}).orElse(InteractionResult.PASS);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	public BlockPos getSavedPos(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		
		if (tag != null) {
			CompoundTag savedPos = tag.getCompound(SAVED_POS);

			if (savedPos != null) {
				return NbtUtils.readBlockPos(savedPos);
			}
		}
		return null;
	}

	public void setSavedPos(ItemStack stack, BlockPos pos) {
		stack.getOrCreateTag().put(SAVED_POS, NbtUtils.writeBlockPos(pos));
	}

	public void removeSavedPos(ItemStack stack) {
		CompoundTag tag = stack.getTag();

		if (tag != null) {
			tag.remove(SAVED_POS);
		}
	}
}
