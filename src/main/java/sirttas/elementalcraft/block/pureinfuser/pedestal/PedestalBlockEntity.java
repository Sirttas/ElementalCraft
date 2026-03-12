package sirttas.elementalcraft.block.pureinfuser.pedestal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.entity.AbstractIERBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.SingleItemContainer;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;

import javax.annotation.Nonnull;

public class PedestalBlockEntity extends AbstractIERBlockEntity implements IElementTypeProvider {
	private final SingleItemContainer inventory;
	private final PedestalElementStorage elementStorage;
	private final RuneHandler runeHandler;

	public PedestalBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.PEDESTAL, pos, state);
		inventory = new SingleItemContainer(this::setChanged);
		elementStorage = new PedestalElementStorage(ElementType.getElementType(state), this::setChanged);
		runeHandler = new RuneHandler(ECConfig.SERVER.pedestalMaxRunes.get(), this::setChanged);
	}

	@Override
	public @NotNull ElementType getElementType() {
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

	@Override
	protected void applyImplicitComponents(@NotNull DataComponentGetter getter) {
		super.applyImplicitComponents(getter);
		elementStorage.setElementAmount(getter.getOrDefault(ECDataComponents.ELEMENT_AMOUNT, 0));
	}

	@Override
	protected void collectImplicitComponents(@NotNull DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(ECDataComponents.ELEMENT_AMOUNT, elementStorage.getElementAmount());
	}

	@Override
	@Deprecated
	public void removeComponentsFromTag(@NotNull CompoundTag tag) {
		super.removeComponentsFromTag(tag);
		tag.remove(ECNames.ELEMENT_STORAGE);
	}

	public SingleItemSingleElementRecipeInput createRecipeInput() {
		return new SingleItemSingleElementRecipeInput(
				getItem(),
				getElementType(),
				getElementStorage().getElementAmount());
	}
}
