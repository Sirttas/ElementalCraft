package sirttas.elementalcraft.block.sorter;

import java.util.List;

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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.ECContainerHelper;

public class SorterBlockEntity extends AbstractECBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SorterBlock.NAME) public static final BlockEntityType<SorterBlockEntity> TYPE = null;
	
	private List<ItemStack> stacks;
	private int index;
	private int tick;
	private boolean alwaysInsert;

	public SorterBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
		stacks = Lists.newArrayList();
		index = 0;
		tick = 0;
		alwaysInsert = false;
	}
	
	public static void serverTick(Level level, BlockPos pos, BlockState state, SorterBlockEntity sorter) {
		sorter.tick++;
		if (sorter.tick > ECConfig.COMMON.sorterCooldown.get()) {
			if (!sorter.isPowered()) {
				sorter.transfer();
			}
			sorter.tick = 0;
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
				if (!stack.isEmpty() && ItemHandlerHelper.insertItem(targetInv, stack, true).isEmpty()) {
					doTransfer(sourceInv, targetInv, i, stack);
					return;
				}
			}
		} else if (index > 0 || ECContainerHelper.isEmpty(targetInv) || alwaysInsert) {
			ItemStack stack = stacks.get(index).copy();

			for (int i = 0; i < sourceInv.getSlots(); i++) {
				if (ItemHandlerHelper.canItemStacksStack(stack, sourceInv.getStackInSlot(i)) && ItemHandlerHelper.insertItem(targetInv, stack, true).isEmpty()) {
					doTransfer(sourceInv, targetInv, i, stack);
					index++;
					if (index >= stacks.size()) {
						index = 0;
					}
					return;
				}
			}
		}
	}

	private void doTransfer(IItemHandler sourceInv, IItemHandler targetInv, int i, ItemStack stack) {
		sourceInv.extractItem(i, 1, false);
		ItemHandlerHelper.insertItem(targetInv, stack, false);
		this.setChanged();
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
		readStacks(compound.getList(ECNames.STACKS, 10));
		index = compound.getInt(ECNames.INDEX);
		if (index > stacks.size()) {
			index = 0;
		}
		alwaysInsert = compound.getBoolean(ECNames.ALWAYSE_INSERT);
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
	public CompoundTag save(CompoundTag compound) {
		super.save(compound);
		compound.put(ECNames.STACKS, this.writeStacks());
		compound.putInt(ECNames.INDEX, index);
		compound.putBoolean(ECNames.ALWAYSE_INSERT, alwaysInsert);
		return compound;
	}

	private ListTag writeStacks() {
		ListTag listnbt = new ListTag();

		for (ItemStack itemstack : stacks) {
			if (!itemstack.isEmpty()) {
				listnbt.add(itemstack.save(new CompoundTag()));
			}
		}
		return listnbt;
	}
}
