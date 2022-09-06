package sirttas.elementalcraft.block.sorter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.ECContainerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SorterBlockEntity extends AbstractECBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SorterBlock.NAME) public static final BlockEntityType<SorterBlockEntity> TYPE = null;
	
	private final List<ItemStack> stacks;
	private int index;
	private float tick;
	private boolean alwaysInsert;

	private final RuneHandler runeHandler;

	public SorterBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
		stacks = Lists.newArrayList();
		index = 0;
		tick = 0;
		alwaysInsert = false;
		runeHandler = new RuneHandler(ECConfig.COMMON.sorterMaxRunes.get(), this::setChanged);
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, SorterBlockEntity sorter) {
		var speed = sorter.runeHandler.getBonus(Rune.BonusType.SPEED) + 1;
		var cooldown = ECConfig.COMMON.sorterCooldown.get();

		sorter.tick += Math.min(speed, cooldown * 64f); // capped at 1 stack a tick to prevent lag spikes
		while (sorter.tick > cooldown) { // TODO improve performance
			if (!sorter.isPowered()) {
				sorter.transfer();
			}
			sorter.tick -= cooldown;
		}
	}

	public InteractionResult addStack(ItemStack stack) {
		if (!stacks.isEmpty() && stack.isEmpty()) {
			stacks.clear();
			index = 0;
			this.setChanged();
			return InteractionResult.SUCCESS;
		} else if (stacks.size() < ECConfig.COMMON.sorterMaxItem.get()) {
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

	private void transfer() {
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
		} else if (alwaysInsert || index > 0 || !(targetInv instanceof InstrumentContainer) || ECContainerHelper.isEmpty(targetInv)) {
			ItemStack stack = stacks.get(index).copy();

			for (int i = 0; i < sourceInv.getSlots(); i++) {
				if (ItemHandlerHelper.canItemStacksStack(stack, sourceInv.getStackInSlot(i)) && doTransfer(sourceInv, targetInv, i, true)) {
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
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		readStacks(compound.getList(ECNames.STACKS, 10));
		index = compound.getInt(ECNames.INDEX);
		if (index > stacks.size()) {
			index = 0;
		}
		alwaysInsert = compound.getBoolean(ECNames.ALWAYS_INSERT);
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			IRuneHandler.readNBT(runeHandler, compound.getList(ECNames.RUNE_HANDLER, 8));
		}
	}

	private void readStacks(ListTag listNbt) {
		stacks.clear();
		for (int i = 0; i < listNbt.size(); ++i) {
			ItemStack itemstack = ItemStack.of(listNbt.getCompound(i));

			if (!itemstack.isEmpty()) {
				stacks.add(itemstack);
			}
		}

	}

    @Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put(ECNames.STACKS, this.writeStacks());
		compound.putInt(ECNames.INDEX, index);
		compound.putBoolean(ECNames.ALWAYS_INSERT, alwaysInsert);
		compound.put(ECNames.RUNE_HANDLER, IRuneHandler.writeNBT(runeHandler));
	}

	private ListTag writeStacks() {
		ListTag listTag = new ListTag();

		for (ItemStack itemstack : stacks) {
			if (!itemstack.isEmpty()) {
				listTag.add(itemstack.save(new CompoundTag()));
			}
		}
		return listTag;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
			return LazyOptional.of(this::getRuneHandler).cast();
		}
		return super.getCapability(cap, side);
	}
}
