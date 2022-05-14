package sirttas.elementalcraft.item.holder;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.api.tooltip.ElementGaugeTooltip;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.property.ECProperties;

import javax.annotation.Nonnull;
import java.util.Optional;

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
	public int getUseDuration(@Nonnull ItemStack stack) {
		return elementCapacity / transferAmount;
	}
	
	@Nonnull
    @Override
	public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
		return UseAnim.BOW;
	}

	protected boolean isValidSource(BlockState state) {
		return state.getBlock().equals(ECBlocks.SOURCE);
	}

	@Override
	public boolean canInteractWithSource(ItemStack stack, BlockState state) {
		return isValidSource(state);
	}

	@Nonnull
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
	public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level worldIn, @Nonnull LivingEntity entityLiving, int timeLeft) {
		this.removeSavedPos(stack);
	}

	protected abstract ElementType getElementType(IElementStorage target, BlockState blockstate);
	
	private InteractionResult tick(Level level, LivingEntity entity, BlockPos pos, ItemStack stack) {
		BlockState blockstate = level.getBlockState(pos);
		return BlockEntityHelper.getElementStorageAt(level, pos).map(storage -> {
			IElementStorage holder = getElementStorage(stack);
			boolean isSource = isValidSource(blockstate);
			ElementType elementType = this.getElementType(storage, blockstate);

			if (elementType != ElementType.NONE) {
				if (isSource || entity.isShiftKeyDown()) {
					if (isSource || storage.canPipeExtract(elementType, null)) {
						var value = storage.transferTo(holder, elementType, transferAmount);

						if (value > 0) {
							ParticleHelper.createElementFlowParticle(elementType, level, Vec3.atCenterOf(pos), entity.getRopeHoldPosition(0), level.random);
							return InteractionResult.CONSUME;
						}
						return InteractionResult.PASS;
					}
				} else if (storage.canPipeInsert(elementType, null)) {
					var value = holder.transferTo(storage, elementType, transferAmount);

					if (value > 0) {
						ParticleHelper.createElementFlowParticle(elementType, level, entity.getRopeHoldPosition(0), Vec3.atCenterOf(pos), level.random);
						return InteractionResult.CONSUME;
					}
					return InteractionResult.PASS;
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

	@Nonnull
	@Override
	public Optional<TooltipComponent> getTooltipImage(@Nonnull ItemStack stack) {
		return CapabilityElementStorage.get(stack).map(ElementGaugeTooltip::new);
	}
}
