package sirttas.elementalcraft.block.evaporator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.tile.AbstractTileECContainer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.SingleStackInventory;
import sirttas.elementalcraft.item.elemental.ItemShard;
import sirttas.elementalcraft.rune.handler.CapabilityRuneHandler;
import sirttas.elementalcraft.rune.handler.RuneHandler;

public class TileEvaporator extends AbstractTileECContainer {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockEvaporator.NAME) public static final TileEntityType<TileEvaporator> TYPE = null;

	private final SingleStackInventory inventory;
	private final SingleElementStorage elementStorage;
	private final RuneHandler runeHandler;

	public TileEvaporator() {
		super(TYPE);
		inventory = new SingleStackInventory(this::markDirty);
		this.elementStorage = new SingleElementStorage(ECConfig.COMMON.shardElementAmount.get() * 20, this::markDirty);
		runeHandler = new RuneHandler(ECConfig.COMMON.evaporatorMaxRunes.get());
	}


	@Override
	public void tick() {
		ItemStack stack = inventory.getStackInSlot(0);
		Item item = stack.getItem();
		ElementType type = BlockEvaporator.getShardElementType(stack);
		float extractionAmount = runeHandler.getTransferSpeed(ECConfig.COMMON.evaporatorExtractionAmount.get());

		super.tick();
		if (type != ElementType.NONE && elementStorage.getElementAmount() <= extractionAmount) {
			elementStorage.insertElement(getShardElementAmount((ItemShard) item), type, false);
			stack.shrink(1);
			if (stack.isEmpty()) {
				inventory.setInventorySlotContents(0, ItemStack.EMPTY);
			}
		}
		if (canExtract()) {
			elementStorage.transferTo(getTank(), extractionAmount, runeHandler.getElementPreservation());
		}
	}

	public boolean canExtract() {
		ISingleElementStorage tank = getTank();

		return !elementStorage.isEmpty() && hasWorld() && tank != null && (tank.getElementAmount() < tank.getElementCapacity() || tank.getElementType() != elementStorage.getElementType());
	}

	private int getShardElementAmount(ItemShard item) {
		return Math.round(ECConfig.COMMON.shardElementAmount.get() * item.getElementAmount() * runeHandler.getElementPreservation());
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.readNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		} else { // TODO 1.17 remove
			elementStorage.readNBT(compound);
		}
		if (compound.contains(ECNames.RUNE_HANDLER)) {
			CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.readNBT(runeHandler, null, compound.get(ECNames.RUNE_HANDLER));
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.writeNBT());
		compound.put(ECNames.RUNE_HANDLER, CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY.writeNBT(runeHandler, null));
		return compound;
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(Capability<U> cap, @Nullable Direction side) {
		if (!this.removed) {
			if (cap == CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY) {
				return LazyOptional.of(elementStorage != null ? () -> elementStorage : null).cast();
			} else if (cap == CapabilityRuneHandler.RUNE_HANDLE_CAPABILITY) {
				return LazyOptional.of(runeHandler != null ? () -> runeHandler : null).cast();
			}
		}
		return super.getCapability(cap, side);
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}

	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

	public RuneHandler getRuneHandler() {
		return runeHandler;
	}
}
