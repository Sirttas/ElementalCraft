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
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.tile.TileECCrafting;
import sirttas.elementalcraft.block.tile.TileECTickable;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.ECInventoryHelper;

public class TileSorter extends TileECTickable {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSorter.NAME) public static TileEntityType<TileSorter> TYPE;

	private static final Codec<List<ItemStack>> STACKS_CODEC = ItemStack.CODEC.listOf().fieldOf(ECNames.STACKS).codec();

	private List<ItemStack> stacks;
	private int index;
	private int tick;

	public TileSorter() {
		super(TYPE);
		stacks = Lists.newArrayList();
		index = 0;
		tick = 0;
	}
	
	@Override
	public void tick() {
		super.tick();
		if (!world.isRemote) {
			tick++;
			if (tick > ECConfig.COMMON.sorterCooldown.get()) {
				transfer();
				tick = 0;
			}
		}
	}

	public ActionResultType addStack(ItemStack stack) {
		if (stack.isEmpty() && !stacks.isEmpty()) {
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
		BlockPos targetPos = pos.offset(target);
		
		if (!stacks.isEmpty() && (index > 0 || TileEntityHelper.getTileEntityAs(world, targetPos, TileECCrafting.class).map(TileECCrafting::canSorterInsert).orElse(true))) {
			ItemStack stack = stacks.get(index).copy();
			IItemHandler sourceInv = ECInventoryHelper.getItemHandlerAt(world, pos.offset(source), source.getOpposite());
			IItemHandler targetInv = ECInventoryHelper.getItemHandlerAt(world, targetPos, target.getOpposite());

			for (int i = 0; i < sourceInv.getSlots(); i++) {
				if (ItemHandlerHelper.canItemStacksStack(stack, sourceInv.getStackInSlot(i)) && ItemHandlerHelper.insertItem(targetInv, stack, true).isEmpty()) {
					sourceInv.extractItem(i, 1, false);
					ItemHandlerHelper.insertItem(targetInv, stack, false);
					index++;
					if (index >= stacks.size()) {
						index = 0;
					}
					this.markDirty();
					return;
				}
			}
		}
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		stacks = Lists.newArrayList(CodecHelper.decode(STACKS_CODEC, compound));
		index = compound.getInt(ECNames.INDEX);
		if (index > stacks.size()) {
			index = 0;
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		CompoundNBT value = (CompoundNBT) CodecHelper.handleResult(STACKS_CODEC.encode(stacks, NBTDynamicOps.INSTANCE, compound));

		value.putInt(ECNames.INDEX, index);
		return value;
	}
}
