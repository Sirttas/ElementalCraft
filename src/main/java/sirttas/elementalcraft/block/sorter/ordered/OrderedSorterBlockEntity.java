package sirttas.elementalcraft.block.sorter.ordered;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.List;

public class OrderedSorterBlockEntity extends AbstractECBlockEntity {

	private final RuneHandler runeHandler;
	private final List<ItemStack> stacks;
	private int index;
	private float tick;

	public OrderedSorterBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.SORTER, pos, state);
		runeHandler = new RuneHandler(ECConfig.SERVER.sorterMaxRunes.get(), this::setChanged);
		stacks = Lists.newArrayList();
		index = 0;
		tick = 0;
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, OrderedSorterBlockEntity sorter) {
		if (sorter.isPowered()) {
			return;
		}

		var profiler = level.getProfiler();

		profiler.push("elementalcraft:ordered_sorter");

		var speed = sorter.runeHandler.getBonus(Rune.BonusType.SPEED) + 1;
		int cooldown = ECConfig.SERVER.sorterCooldown.get();

		sorter.tick += Math.min(speed, cooldown * 64f); // capped at 1 stack a tick to prevent lag spikes
		while (sorter.tick > cooldown) { // TODO improve performance
			sorter.transfer();
			sorter.tick -= cooldown;
		}
		profiler.pop();
	}

	public ItemInteractionResult addStack(ItemStack stack) {
		if (!stacks.isEmpty() && stack.isEmpty()) {
			stacks.clear();
			index = 0;
			this.setChanged();
			return ItemInteractionResult.SUCCESS;
		} else if (stacks.size() < ECConfig.SERVER.sorterMaxItem.get()) {
			ItemStack copy = stack.copy();

			copy.setCount(1);
			stacks.add(copy);
			this.setChanged();
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	public List<ItemStack> getStacks() {
		return ImmutableList.copyOf(stacks);
	}

	@Nonnull
	public IRuneHandler getRuneHandler() {
		return runeHandler;
	}

	public int getIndex() {
		return index;
	}

	void transfer() {
		BlockState state = this.getBlockState();
		Direction source = state.getValue(ISorterBlock.SOURCE);
		Direction target = state.getValue(ISorterBlock.TARGET);
		IItemHandler sourceInv = ECContainerHelper.getItemHandlerAt(level, worldPosition.relative(source), source.getOpposite());
		IItemHandler targetInv = ECContainerHelper.getItemHandlerAt(level, worldPosition.relative(target), target.getOpposite());

		if (stacks.isEmpty()) {
			for (int i = 0; i < sourceInv.getSlots(); i++) {
				ItemStack stack = sourceInv.getStackInSlot(i).copy();

				stack.setCount(1);
				if (!stack.isEmpty() && doTransfer(sourceInv, targetInv, i, true)) {
					doTransfer(sourceInv, targetInv, i, false);
					return;
				}
			}
		} else if (!doesTargetUsesSingleSet(target) || index > 0 || ECContainerHelper.isEmpty(targetInv)) {
			ItemStack stack = stacks.get(index).copy();

			for (int i = 0; i < sourceInv.getSlots(); i++) {
				if (ItemStack.isSameItemSameComponents(stack, sourceInv.getStackInSlot(i)) && doTransfer(sourceInv, targetInv, i, true)) {
					doTransfer(sourceInv, targetInv, i, false);
					index++;
					if (index >= stacks.size()) {
						index = 0;
					}
					return;
				}
			}
		}
	}

	private boolean doesTargetUsesSingleSet(Direction target) {
		return level.getBlockState(worldPosition.relative(target)).is(ECTags.Blocks.USES_SINGLE_SET_FROM_ORDERED_SORTER);
	}

	private boolean doTransfer(IItemHandler sourceInv, IItemHandler targetInv, int i, boolean simulate) {
		var extracted = sourceInv.extractItem(i, 1, simulate);

		if (extracted.isEmpty()) {
			return false;
		}

		var stack = ItemHandlerHelper.insertItem(targetInv, extracted, simulate);

		if (!simulate) {
			this.setChanged();
		}
		return !stack.equals(extracted);
	}

	@Override
	public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		readStacks(provider, compound.getList(ECNames.STACKS, 10));
		index = compound.getInt(ECNames.INDEX);
		if (index > stacks.size()) {
			index = 0;
		}
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}

	private void readStacks(@Nonnull HolderLookup.Provider provider, ListTag listNbt) {
		stacks.clear();
		for (int i = 0; i < listNbt.size(); ++i) {
			ItemStack itemstack = ItemStack.parseOptional(provider, listNbt.getCompound(i));

			if (!itemstack.isEmpty()) {
				stacks.add(itemstack);
			}
		}

	}

    @Override
	public void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.put(ECNames.STACKS, this.writeStacks(provider));
		compound.putInt(ECNames.INDEX, index);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
	}

	private ListTag writeStacks(@Nonnull HolderLookup.Provider provider) {
		ListTag listTag = new ListTag();

		for (ItemStack itemstack : stacks) {
			if (!itemstack.isEmpty()) {
				listTag.add(itemstack.save(provider));
			}
		}
		return listTag;
	}
}
