package sirttas.elementalcraft.block.evaporator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.container.IContainerTopBlockEntity;
import sirttas.elementalcraft.block.entity.AbstractIERBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.SingleStackContainer;
import sirttas.elementalcraft.item.elemental.ShardItem;

import javax.annotation.Nonnull;

public class EvaporatorBlockEntity extends AbstractIERBlockEntity implements IContainerTopBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + EvaporatorBlock.NAME) public static final BlockEntityType<EvaporatorBlockEntity> TYPE = null;

	private final SingleStackContainer inventory;
	private final SingleElementStorage elementStorage;
	private final RuneHandler runeHandler;

	public EvaporatorBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state);
		inventory = new SingleStackContainer(this::setChanged);
		this.elementStorage = new SingleElementStorage(ECConfig.COMMON.shardElementAmount.get() * 20, this::setChanged);
		runeHandler = new RuneHandler(ECConfig.COMMON.evaporatorMaxRunes.get());
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, EvaporatorBlockEntity evaporator) {
		ItemStack stack = evaporator.inventory.getItem(0);
		Item item = stack.getItem();
		ElementType type = EvaporatorBlock.getShardElementType(stack);
		float extractionAmount = evaporator.runeHandler.getTransferSpeed(ECConfig.COMMON.evaporatorExtractionAmount.get());

		if (type != ElementType.NONE && evaporator.elementStorage.getElementAmount() <= extractionAmount) {
			evaporator.elementStorage.insertElement(evaporator.getShardElementAmount((ShardItem) item), type, false);
			stack.shrink(1);
			if (stack.isEmpty()) {
				evaporator.inventory.setItem(0, ItemStack.EMPTY);
			}
		}
		if (evaporator.canExtract()) {
			evaporator.elementStorage.transferTo(evaporator.getContainer(), extractionAmount, evaporator.runeHandler.getElementPreservation());
		}
	}

	public boolean canExtract() {
		ISingleElementStorage container = getContainer();

		return !elementStorage.isEmpty() && hasLevel() && container != null && (container.getElementAmount() < container.getElementCapacity() || container.getElementType() != elementStorage.getElementType());
	}

	private int getShardElementAmount(ShardItem item) {
		return Math.round(ECConfig.COMMON.shardElementAmount.get() * item.getElementAmount() * runeHandler.getElementPreservation());
	}

	@Nonnull
    @Override
	public Container getInventory() {
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
