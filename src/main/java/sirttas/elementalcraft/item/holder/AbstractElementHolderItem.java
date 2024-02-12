package sirttas.elementalcraft.item.holder;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.source.ISourceInteractable;
import sirttas.elementalcraft.api.tooltip.ElementGaugeTooltip;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.property.ECProperties;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.IntSupplier;

public abstract class AbstractElementHolderItem extends ECItem implements ISourceInteractable {
	
	private static final String SAVED_POS = "saved_pos";

	private final IntSupplier elementCapacity;
	private final IntSupplier transferAmount;
	
	protected AbstractElementHolderItem(IntSupplier elementCapacity, IntSupplier transferAmount) {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
		this.elementCapacity = elementCapacity;
		this.transferAmount = transferAmount;
	}

	public abstract IElementStorage getElementStorage(ItemStack stack);

	@Override
	public int getUseDuration(@Nonnull ItemStack stack) {
		return getElementCapacity() / getTransferAmount();
	}

	public int getElementCapacity() {
		return elementCapacity.getAsInt();
	}

	public int getTransferAmount() {
		return transferAmount.getAsInt();
	}

	@Nonnull
    @Override
	public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
		return UseAnim.BOW;
	}

	protected boolean isValidSource(BlockState state) {
		return state.getBlock() == ECBlocks.SOURCE.get();
	}

	@Override
	public boolean canInteractWithSource(ItemStack stack, BlockState state) {
		return isValidSource(state);
	}

	@Nonnull
    @Override
	public InteractionResult useOn(UseOnContext context) {
		var pos = context.getClickedPos();
		var level = context.getLevel();
		var stack = context.getItemInHand();
		var player = context.getPlayer();
		var result = tick(level, player, pos, stack);

		if (result.consumesAction()) {
			this.setSavedPos(stack, pos);
			player.startUsingItem(context.getHand());
		}
		return result;
	}

	@Override
	public void onUseTick(@Nonnull Level level, @Nonnull LivingEntity player, @Nonnull ItemStack stack, int count) {
		var pos = this.getSavedPos(stack);
		var reachAttribute = player.getAttribute(NeoForgeMod.ENTITY_REACH.value());
		var reach = reachAttribute != null ? reachAttribute.getValue() : 5;
		
		if (player.blockPosition().distSqr(pos) + 1 > reach * reach || !this.tick(player.level(), player, pos, stack).consumesAction()) {
			player.releaseUsingItem();
		}
	}

	@Override
	public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entityLiving, int timeLeft) {
		this.removeSavedPos(stack);
	}

	protected abstract ElementType getElementType(IElementStorage target, BlockState blockstate);
	
	private InteractionResult tick(Level level, LivingEntity entity, BlockPos pos, ItemStack stack) {
		var amount = this.transferAmount.getAsInt();
		var blockstate = level.getBlockState(pos);
		var storage = level.getCapability(ElementalCraftCapabilities.ElementStorage.BLOCK, pos, null);

		if (storage == null) {
			return InteractionResult.PASS;
		}

		var holder = getElementStorage(stack);
		var isSource = isValidSource(blockstate);
		var elementType = this.getElementType(storage, blockstate);

		if (elementType == ElementType.NONE) {
			return InteractionResult.PASS;
		}

		if (isSource || entity.isShiftKeyDown()) {
			if (isSource || storage.canPipeExtract(elementType, null)) {
				var value = storage.transferTo(holder, elementType, amount);

				if (value > 0) {
					ParticleHelper.createElementFlowParticle(elementType, level, Vec3.atCenterOf(pos), entity.getRopeHoldPosition(0), level.random);
					return InteractionResult.CONSUME;
				}
				return InteractionResult.PASS;
			}
		} else if (storage.canPipeInsert(elementType, null)) {
			var value = holder.transferTo(storage, elementType, amount);

			if (value > 0) {
				ParticleHelper.createElementFlowParticle(elementType, level, entity.getRopeHoldPosition(0), Vec3.atCenterOf(pos), level.random);
				return InteractionResult.CONSUME;
			}
			return InteractionResult.PASS;
		}
		return InteractionResult.PASS;
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
		if (stack.isEmpty()) {
			return Optional.empty();
		}

		var storage = stack.getCapability(ElementalCraftCapabilities.ElementStorage.ITEM, null);

		if (storage == null) {
			return Optional.empty();
		}

		return Optional.of(new ElementGaugeTooltip(storage));
	}
}
