package sirttas.elementalcraft.item.holder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.tooltip.ElementGaugeTooltip;
import sirttas.elementalcraft.block.source.SourceElementStorage;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.IntSupplier;

public abstract class AbstractElementHolderItem extends Item {

	private final IntSupplier elementCapacity;
	private final IntSupplier transferAmount;
	
	protected AbstractElementHolderItem(IntSupplier elementCapacity, IntSupplier transferAmount, Item.Properties properties) {
		super(properties);
		this.elementCapacity = elementCapacity;
		this.transferAmount = transferAmount;
	}

	public abstract IElementStorage getElementStorage(ItemStack stack);

	@Override
	public int getUseDuration(@Nonnull ItemStack stack, @NotNull LivingEntity entity) {
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
	public ItemUseAnimation getUseAnimation(@Nonnull ItemStack stack) {
		return ItemUseAnimation.BOW;
	}

	protected boolean isValidSource(BlockState state) {
		return state.is(ECTags.Blocks.SOURCES);
	}

	@Nonnull
    @Override
	public InteractionResult useOn(UseOnContext context) {
		var player = context.getPlayer();

		if (player == null) {
			return InteractionResult.PASS;
		}

		var pos = context.getClickedPos();
		var level = context.getLevel();
		var stack = context.getItemInHand();
		var result = tick(level, player, pos, stack);

		if (result.consumesAction()) {
			stack.set(ECDataComponents.TARGET_POS, pos);
			player.startUsingItem(context.getHand());
		}
		return result;
	}

	@Override
	public void onUseTick(@Nonnull Level level, @Nonnull LivingEntity player, @Nonnull ItemStack stack, int count) {
		var pos = stack.get(ECDataComponents.TARGET_POS);

		if (pos == null) {
			return;
		}

		var reachAttribute = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);
		var reach = reachAttribute != null ? reachAttribute.getValue() : 5;

		if (player.blockPosition().distSqr(pos) + 1 > reach * reach || !this.tick(player.level(), player, pos, stack).consumesAction()) {
			player.releaseUsingItem();
		}
	}

	@Override
	public boolean releaseUsing(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entityLiving, int timeLeft) {
		stack.remove(ECDataComponents.TARGET_POS);
        return super.releaseUsing(stack, level, entityLiving, timeLeft);
    }

	protected abstract ElementType getElementType(IElementStorage target, BlockState blockstate);
	
	private InteractionResult tick(Level level, LivingEntity entity, BlockPos pos, ItemStack stack) {
		var amount = this.transferAmount.getAsInt();
		var blockstate = level.getBlockState(pos);
		var storage = level.getCapability(ElementalCraftCapabilities.ElementStorages.BLOCK, pos, null);

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
					ParticleHelper.createElementFlowParticle(elementType, level, Vec3.atCenterOf(pos), entity.getRopeHoldPosition(0), level.getRandom());

					if (isSource && storage.getElementAmount(elementType) <= 0) {
						if (storage instanceof SourceElementStorage sourceStorage && sourceStorage.getSource().isStabilized()) {
							EntityHelper.dropAtFeet(level, entity, new ItemStack(ECItems.SOURCE_STABILIZER));
						}
						level.removeBlock(pos, false);
					}
					return InteractionResult.CONSUME;
				}
				return InteractionResult.PASS;
			}
		} else if (storage.canPipeInsert(elementType, null)) {
			var value = holder.transferTo(storage, elementType, amount);

			if (value > 0) {
				ParticleHelper.createElementFlowParticle(elementType, level, entity.getRopeHoldPosition(0), Vec3.atCenterOf(pos), level.getRandom());
				return InteractionResult.CONSUME;
			}
			return InteractionResult.PASS;
		}
		return InteractionResult.PASS;
	}

	@Nonnull
	@Override
	public Optional<TooltipComponent> getTooltipImage(@Nonnull ItemStack stack) {
		if (stack.isEmpty()) {
			return Optional.empty();
		}

		var storage = stack.getCapability(ElementalCraftCapabilities.ElementStorages.ITEM, null);

		if (storage == null) {
			return Optional.empty();
		}

		return Optional.of(new ElementGaugeTooltip(storage));
	}
}
