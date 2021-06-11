package sirttas.elementalcraft.item.holder;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BOW;
	}

	protected boolean isValidSource(BlockState state) {
		return state.getBlock().equals(ECBlocks.SOURCE);
	}

	@Override
	public boolean canIteractWithSource(ItemStack stack, BlockState state) {
		return isValidSource(state);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		BlockPos pos = context.getClickedPos();
		World world = context.getLevel();
		ItemStack stack = context.getItemInHand();
		PlayerEntity player = context.getPlayer();
		ActionResultType result = tick(world, player, pos, stack);

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
	public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		this.removeSavedPos(stack);
	}

	protected abstract ElementType getElementType(IElementStorage target, BlockState blockstate);
	
	private ActionResultType tick(World world, LivingEntity entity, BlockPos pos, ItemStack stack) {
		BlockState blockstate = world.getBlockState(pos);
		return BlockEntityHelper.getElementStorageAt(world, pos).map(storage -> {
			IElementStorage holder = getElementStorage(stack);
			boolean isSource = isValidSource(blockstate);
			ElementType elementType = this.getElementType(storage, blockstate);

			if (elementType != ElementType.NONE) {
				if (isSource || entity.isShiftKeyDown()) {
					if (isSource || storage.canPipeExtract(elementType)) {
						return storage.transferTo(holder, elementType, transferAmount) > 0 ? ActionResultType.CONSUME : ActionResultType.PASS;
					}
				} else if (storage.canPipeInsert(elementType)) {
					return holder.transferTo(storage, elementType, transferAmount) > 0 ? ActionResultType.CONSUME : ActionResultType.PASS;
				}
			}
			return ActionResultType.PASS;
		}).orElse(ActionResultType.PASS);
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
				return NBTUtil.readBlockPos(savedPos);
			}
		}
		return null;
	}

	public void setSavedPos(ItemStack stack, BlockPos pos) {
		stack.getOrCreateTag().put(SAVED_POS, NBTUtil.writeBlockPos(pos));
	}

	public void removeSavedPos(ItemStack stack) {
		CompoundNBT tag = stack.getTag();

		if (tag != null) {
			tag.remove(SAVED_POS);
		}
	}
}
