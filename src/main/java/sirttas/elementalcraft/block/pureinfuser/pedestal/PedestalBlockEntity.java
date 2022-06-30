package sirttas.elementalcraft.block.pureinfuser.pedestal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.element.storage.single.SingleElementStorage;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.entity.AbstractIERBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.SingleItemContainer;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class PedestalBlockEntity extends AbstractIERBlockEntity implements IElementTypeProvider {
	private final SingleItemContainer inventory;
	private final SingleElementStorage elementStorage;
	private final RuneHandler runeHandler;

	public PedestalBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.PEDESTAL, pos, state);
		inventory = new SingleItemContainer(this::setChanged);
		elementStorage = new PedestalElementStorage(ElementType.getElementType(state), this::setChanged);
		runeHandler = new RuneHandler(ECConfig.COMMON.pedestalMaxRunes.get(), this::setChanged);
	}

	public Direction getPureInfuserDirection() {
		return Stream.of(Direction.values()).filter(d -> d.getAxis().getPlane() == Direction.Plane.HORIZONTAL)
				.filter(d -> this.getLevel().getBlockEntity(worldPosition.relative(d, 3)) instanceof PureInfuserBlockEntity)
				.findAny().orElse(Direction.UP);
	}

	@Override
	public ElementType getElementType() {
		return elementStorage.getElementType();
	}

	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}

	public ItemStack getItem() {
		return inventory.getItem(0);
	}

	@Override
	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

	@Override
	@Nonnull
	public RuneHandler getRuneHandler() {
		return runeHandler;
	}

}
