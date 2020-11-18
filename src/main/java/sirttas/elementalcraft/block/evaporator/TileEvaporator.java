package sirttas.elementalcraft.block.evaporator;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.block.tank.TileTank;
import sirttas.elementalcraft.block.tile.TileECContainer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.SingleStackInventory;
import sirttas.elementalcraft.nbt.ECNames;

public class TileEvaporator extends TileECContainer implements IElementStorage {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockEvaporator.NAME) public static TileEntityType<TileEvaporator> TYPE;

	private int elementAmount;
	private ElementType elementType = ElementType.NONE;

	private final SingleStackInventory inventory;

	public TileEvaporator() {
		super(TYPE);
		inventory = new SingleStackInventory(this::forceSync);
	}


	@Override
	public void tick() {
		ItemStack stack = inventory.getStackInSlot(0);
		ElementType type = BlockEvaporator.getTypeFromShard(stack.getItem());
		int shardAmount = ECConfig.COMMON.evaporatorExtractionAmount.get();

		super.tick();
		if (type != ElementType.NONE && this.elementAmount <= 20) {
			if (type == elementType) {
				this.elementAmount += ECConfig.COMMON.shardElementAmount.get();
			} else {
				this.elementAmount = ECConfig.COMMON.shardElementAmount.get() - this.elementAmount;
			}
			elementType = type;
			stack.shrink(1);
			if (stack.isEmpty()) {
				inventory.setInventorySlotContents(0, ItemStack.EMPTY);
			}
			this.forceSync();
		}
		if (canExtract()) {
			getTank().inserElement(shardAmount, elementType, false);
			elementAmount -= shardAmount;
			this.forceSync();
		}
	}

	public boolean canExtract() {
		TileTank tank = getTank();

		return elementAmount > 0 && hasWorld() && tank != null && (tank.getElementAmount() < tank.getElementCapacity() || tank.getElementType() != elementType);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		elementType = ElementType.byName(compound.getString(ECNames.ELEMENT_TYPE));
		elementAmount = compound.getInt(ECNames.ELEMENT_AMOUNT);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putString(ECNames.ELEMENT_TYPE, elementType.getString());
		compound.putInt(ECNames.ELEMENT_AMOUNT, elementAmount);
		return compound;
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	public int getElementCapacity() {
		return ECConfig.COMMON.shardElementAmount.get() * 2;
	}
}
