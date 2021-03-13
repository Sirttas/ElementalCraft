package sirttas.elementalcraft.block.sorter;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.tile.AbstractTileECTickable;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public class TileSorter extends AbstractTileECTickable {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSorter.NAME) public static final TileEntityType<TileSorter> TYPE = null;

	private static final Codec<List<ItemStack>> STACKS_CODEC = ItemStack.CODEC.listOf().fieldOf(ECNames.STACKS).codec(); // TODO don't use a codec for perf code

	private List<ItemStack> stacks;
	private int index;
	private int tick;
	private boolean alwaysInsert;

	public TileSorter() {
		super(TYPE);
		stacks = Lists.newArrayList();
		index = 0;
		tick = 0;
		alwaysInsert = false;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!world.isRemote) {
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
		if (!stacks.isEmpty()) {
			if (stack.isEmpty()) {
				stacks.clear();
				index = 0;
				this.markDirty();
				return ActionResultType.SUCCESS;
			} else if (stacks.size() < ECConfig.COMMON.sorterMaxItem.get()) {
				ItemStack copy = stack.copy();
	
				copy.setCount(1);
				stacks.add(copy);
				this.markDirty();
				return ActionResultType.SUCCESS;
			}
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
		Direction source = state.get(BlockSorter.SOURCE);
		Direction target = state.get(BlockSorter.TARGET);
		IItemHandler sourceInv = ECInventoryHelper.getItemHandlerAt(world, pos.offset(source), source.getOpposite());
		IItemHandler targetInv = ECInventoryHelper.getItemHandlerAt(world, pos.offset(target), target.getOpposite());

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
		this.markDirty();
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		stacks = Lists.newArrayList(CodecHelper.decode(STACKS_CODEC, compound));
		index = compound.getInt(ECNames.INDEX);
		if (index > stacks.size()) {
			index = 0;
		}
		alwaysInsert = compound.getBoolean(ECNames.ALWAYSE_INSERT);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		CompoundNBT value = (CompoundNBT) CodecHelper.handleResult(STACKS_CODEC.encode(stacks, NBTDynamicOps.INSTANCE, compound));

		compound.putInt(ECNames.INDEX, index);
		compound.putBoolean(ECNames.ALWAYSE_INSERT, alwaysInsert);
		return value;
	}
}
