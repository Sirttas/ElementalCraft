package sirttas.elementalcraft.block.sorter.ordered;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.cover.CoverableBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.sorter.ISorterBlock;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.ECContainerHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.List;

public class OrderedSorterBlockEntity extends CoverableBlockEntity {

	private final RuneHandler runeHandler;
	private final NonNullList<@NotNull ItemStack> stacks;
	private int index;
	private float tick;

	public OrderedSorterBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.SORTER, pos, state);
		runeHandler = new RuneHandler(ECConfig.SERVER.sorterMaxRunes.get(), this::setChanged);
		stacks = NonNullList.of(ItemStack.EMPTY);
		index = 0;
		tick = 0;
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, OrderedSorterBlockEntity sorter) {
		if (sorter.isPowered()) {
			return;
		}

        var profiler = Profiler.get();

		profiler.push("elementalcraft:ordered_sorter");

		var speed = sorter.runeHandler.getBonus(Rune.BonusType.SPEED) + 1;
        int cooldown = ECConfig.SERVER.sorterCooldown.get();
        var source = state.getValue(ISorterBlock.SOURCE);
        var target = state.getValue(ISorterBlock.TARGET);
        var sourceInv = ECContainerHelper.getItemHandlerAt(level, pos.relative(source), source.getOpposite());
        var targetInv = ECContainerHelper.getItemHandlerAt(level, pos.relative(target), target.getOpposite());

		sorter.tick += speed;
		while (sorter.tick > cooldown) {
            var transferred = sorter.transfer(sourceInv, targetInv, (int) Math.floor(sorter.tick / cooldown));

            if (transferred == 0) {
                sorter.tick = 0;
                break;
            }
			sorter.tick -= cooldown * transferred;
		}
		profiler.pop();
	}

	public InteractionResult addStack(ItemStack stack) {
		if (!stacks.isEmpty() && stack.isEmpty()) {
			stacks.clear();
			index = 0;
			this.setChanged();
			return InteractionResult.SUCCESS;
		} else if (stacks.size() < ECConfig.SERVER.sorterMaxItem.get()) {
			ItemStack copy = stack.copy();

			copy.setCount(1);
			stacks.add(copy);
			this.setChanged();
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.PASS;
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

	private int transfer(IItemHandler sourceInv, IItemHandler targetInv, int amount) {
		if (stacks.isEmpty()) {
			for (int i = 0; i < sourceInv.getSlots(); i++) {
				if (!sourceInv.getStackInSlot(i).isEmpty()) {
					return doTransfer(sourceInv, targetInv, i, doTransfer(sourceInv, targetInv, i, amount, true), false);
				}
			}
		} else if (!doesTargetUsesSingleSet(this.getBlockState().getValue(ISorterBlock.TARGET)) || index > 0 || ECContainerHelper.isEmpty(targetInv)) {
			ItemStack stack = stacks.get(index).copy();

			for (int i = 0; i < sourceInv.getSlots(); i++) {
				if (ItemStack.isSameItemSameComponents(stack, sourceInv.getStackInSlot(i)) && doTransfer(sourceInv, targetInv, i, 1, true) > 0) {
					doTransfer(sourceInv, targetInv, i, 1, false);
                    moveIndexForward();
                    return 1;
				}
			}
		}
        return 0;
	}

    private boolean doesTargetUsesSingleSet(Direction target) {
		return level.getBlockState(worldPosition.relative(target)).is(ECTags.Blocks.USES_SINGLE_SET_FROM_ORDERED_SORTER);
	}

    private void moveIndexForward() {
        index++;
        if (index >= stacks.size()) {
            index = 0;
        }
    }

	private int doTransfer(IItemHandler sourceInv, IItemHandler targetInv, int i, int amount, boolean simulate) {
		var extracted = sourceInv.extractItem(i, amount, simulate);

		if (extracted.isEmpty()) {
			return 0;
		}

		var stack = ItemHandlerHelper.insertItem(targetInv, extracted, simulate);

		if (!simulate) {
			this.setChanged();
            if (!stack.isEmpty()) {
                if (!ItemHandlerHelper.insertItem(sourceInv, stack, false).isEmpty()) {
                    ElementalCraftApi.LOGGER.error("Couldn't return leftover items to source inventory after failed transfer from ordered sorter");
                    level.addFreshEntity(new ItemEntity(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack));
                }
            }
		}
		return extracted.getCount() - stack.getCount();
	}

	@Override
	public void loadAdditional(@Nonnull ValueInput input) {
		super.loadAdditional(input);
        ContainerHelper.loadAllItems(input, this.stacks);
		index = input.getIntOr(ECNames.INDEX, 0);
		if (index > stacks.size()) {
			index = 0;
		}
        input.readChild(ECNames.RUNE_HANDLER, runeHandler);
	}

    @Override
	public void saveAdditional(@Nonnull ValueOutput output) {
		super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.stacks);
        output.putInt(ECNames.INDEX, index);
        output.putChild(ECNames.RUNE_HANDLER, runeHandler);
    }
}
