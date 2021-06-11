package sirttas.elementalcraft.block.evaporator;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.entity.AbstractIERBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.inventory.SingleStackInventory;
import sirttas.elementalcraft.item.elemental.ShardItem;

public class EvaporatorBlockEntity extends AbstractIERBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + EvaporatorBlock.NAME) public static final TileEntityType<EvaporatorBlockEntity> TYPE = null;

	private final SingleStackInventory inventory;
	private final SingleElementStorage elementStorage;
	private final RuneHandler runeHandler;

	public EvaporatorBlockEntity() {
		super(TYPE);
		inventory = new SingleStackInventory(this::setChanged);
		this.elementStorage = new SingleElementStorage(ECConfig.COMMON.shardElementAmount.get() * 20, this::setChanged);
		runeHandler = new RuneHandler(ECConfig.COMMON.evaporatorMaxRunes.get());
	}

	@Override
	public void tick() {
		ItemStack stack = inventory.getItem(0);
		Item item = stack.getItem();
		ElementType type = EvaporatorBlock.getShardElementType(stack);
		float extractionAmount = runeHandler.getTransferSpeed(ECConfig.COMMON.evaporatorExtractionAmount.get());

		super.tick();
		if (type != ElementType.NONE && elementStorage.getElementAmount() <= extractionAmount) {
			elementStorage.insertElement(getShardElementAmount((ShardItem) item), type, false);
			stack.shrink(1);
			if (stack.isEmpty()) {
				inventory.setItem(0, ItemStack.EMPTY);
			}
		}
		if (canExtract()) {
			elementStorage.transferTo(getTank(), extractionAmount, runeHandler.getElementPreservation());
		}
	}

	public boolean canExtract() {
		ISingleElementStorage tank = getTank();

		return !elementStorage.isEmpty() && hasLevel() && tank != null && (tank.getElementAmount() < tank.getElementCapacity() || tank.getElementType() != elementStorage.getElementType());
	}

	private int getShardElementAmount(ShardItem item) {
		return Math.round(ECConfig.COMMON.shardElementAmount.get() * item.getElementAmount() * runeHandler.getElementPreservation());
	}

	@Override
	public IInventory getInventory() {
		return inventory;
	}

	@Override
	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

	@Override
	public RuneHandler getRuneHandler() {
		return runeHandler;
	}
}
