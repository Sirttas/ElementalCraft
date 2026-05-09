package sirttas.elementalcraft.block.sorter.ordered;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
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

import java.util.List;

public class OrderedSorterBlockEntity extends CoverableBlockEntity {

	private final RuneHandler runeHandler;
	private final NonNullList<ItemStack> stacks;
	private int index;
	private float tick;

	public OrderedSorterBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.SORTER, pos, state);
		runeHandler = new RuneHandler(ECConfig.SERVER.sorterMaxRunes.get(), this::setChanged);
		stacks = NonNullList.withSize(ECConfig.SERVER.sorterMaxItem.get(), ItemStack.EMPTY);
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
        var sourceInv = ECContainerHelper.getItemResourceHandlerAt(level, pos.relative(source), source.getOpposite());
        var targetInv = ECContainerHelper.getItemResourceHandlerAt(level, pos.relative(target), target.getOpposite());
		var usesSingleSet = level.getBlockState(pos.relative(target)).is(ECTags.Blocks.USES_SINGLE_SET_FROM_ORDERED_SORTER);
		try (var transaction = Transaction.openRoot()) {
			sorter.tick += speed;
			while (sorter.tick > cooldown) {
				var transferred = sorter.transfer(sourceInv, targetInv, (int) Math.floor(sorter.tick / cooldown), usesSingleSet, transaction);

				if (transferred == 0) {
					sorter.tick = 0;
					break;
				}
				sorter.tick -= cooldown * transferred;
			}
			transaction.commit();
		}
		profiler.pop();
	}

	public InteractionResult addStack(ItemStack stack) {
		if (!stacks.isEmpty() && stack.isEmpty()) {
			stacks.clear();
			index = 0;
			this.setChanged();
			return InteractionResult.SUCCESS;
		}
		for (var i = 0; i < stacks.size(); i++) {
			if (stacks.get(i).isEmpty()) {
				ItemStack copy = stack.copy();

				copy.setCount(1);
				stacks.set(i, copy);
				this.setChanged();
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	public List<ItemStack> getStacks() {
		return ImmutableList.copyOf(stacks);
	}

	public IRuneHandler getRuneHandler() {
		return runeHandler;
	}

	public int getIndex() {
		return index;
	}

	private int transfer(ResourceHandler<ItemResource> sourceInv, ResourceHandler<ItemResource> targetInv, int amount, boolean usesSingleSet, Transaction parent) {
		var unfiltered = stacks.getFirst().isEmpty();

		if (unfiltered || !usesSingleSet || index > 0 || ECContainerHelper.isEmpty(targetInv)) {
			return moveItems(sourceInv, targetInv, unfiltered ? amount : 1, parent);
		}
        return 0;
	}

	private int moveItems(ResourceHandler<ItemResource> sourceInv, ResourceHandler<ItemResource> targetInv, int amount, Transaction parent) {
		try (var transaction = Transaction.open(parent)) {
			var itemResource = ItemResource.of(stacks.get(index));
			var moved = ResourceHandlerUtil.moveStacking(sourceInv, targetInv, r -> itemResource.isEmpty() || r.equals(itemResource), amount, transaction);

			this.setChanged();
			moveIndexForward();
			transaction.commit();
			return moved;
		}
	}

    private void moveIndexForward() {
        index++;
		wrapIndexAroundIfNeeded();
	}

	@Override
	public void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		this.stacks.clear();
        ContainerHelper.loadAllItems(input, this.stacks);
		index = input.getIntOr(ECNames.INDEX, 0);
		wrapIndexAroundIfNeeded();
		input.readChild(ECNames.RUNE_HANDLER, runeHandler);
	}

    @Override
	public void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.stacks);
        output.putInt(ECNames.INDEX, index);
        output.putChild(ECNames.RUNE_HANDLER, runeHandler);
    }

	private void wrapIndexAroundIfNeeded() {
		if (index >= stacks.size() || stacks.get(index).isEmpty()) {
			index = 0;
		}
	}
}
