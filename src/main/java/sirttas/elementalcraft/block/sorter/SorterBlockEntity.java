package sirttas.elementalcraft.block.sorter;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECTickableBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public class SorterBlockEntity extends AbstractECTickableBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SorterBlock.NAME) public static final TileEntityType<SorterBlockEntity> TYPE = null;
	
	private List<ItemStack> stacks;
	private int index;
	private int tick;
	private boolean alwaysInsert;

	public SorterBlockEntity() {
		super(TYPE);
		stacks = Lists.newArrayList();
		index = 0;
		tick = 0;
		alwaysInsert = false;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!level.isClientSide) {
			tick++;
			if (tick > ECConfig.COMMON.sorterCooldown.get()) {
				if (!this.isPowered()) {
					transfer();
				}
				tick = 0;
			}
		}
	}

	public ActionResultType addStack(ItemStack stack) {
		if (!stacks.isEmpty() && stack.isEmpty()) {
			stacks.clear();
			index = 0;
			this.setChanged();
			return ActionResultType.SUCCESS;
		} else if (stacks.size() < ECConfig.COMMON.sorterMaxItem.get()) {
			ItemStack copy = stack.copy();

			copy.setCount(1);
			stacks.add(copy);
			this.setChanged();
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
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
		IItemHandler sourceInv = ECInventoryHelper.getItemHandlerAt(level, worldPosition.relative(source), source.getOpposite());
		IItemHandler targetInv = ECInventoryHelper.getItemHandlerAt(level, worldPosition.relative(target), target.getOpposite());

		if (stacks.isEmpty()) {
			for (int i = 0; i < sourceInv.getSlots(); i++) {
				ItemStack stack = sourceInv.getStackInSlot(i).copy();

				stack.setCount(1);
				if (!stack.isEmpty() && ItemHandlerHelper.insertItem(targetInv, stack, true).isEmpty()) {
					doTransfer(sourceInv, targetInv, i, stack);
					return;
				}
			}
		} else if (index > 0 || ECInventoryHelper.isEmpty(targetInv) || alwaysInsert) {
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
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		readStacks(compound.getList(ECNames.STACKS, 10));
		index = compound.getInt(ECNames.INDEX);
		if (index > stacks.size()) {
			index = 0;
		}
		alwaysInsert = compound.getBoolean(ECNames.ALWAYSE_INSERT);
	}

	private void readStacks(ListNBT listNbt) {
		stacks.clear();
		for (int i = 0; i < listNbt.size(); ++i) {
			ItemStack itemstack = ItemStack.of(listNbt.getCompound(i));

			if (!itemstack.isEmpty()) {
				stacks.add(itemstack);
			}
		}

	}
	
	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.put(ECNames.STACKS, this.writeStacks());
		compound.putInt(ECNames.INDEX, index);
		compound.putBoolean(ECNames.ALWAYSE_INSERT, alwaysInsert);
		return compound;
	}

	private ListNBT writeStacks() {
		ListNBT listnbt = new ListNBT();

		for (ItemStack itemstack : stacks) {
			if (!itemstack.isEmpty()) {
				listnbt.add(itemstack.save(new CompoundNBT()));
			}
		}
		return listnbt;
	}
}
