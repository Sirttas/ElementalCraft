package sirttas.elementalcraft.api.element.storage.single;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.EmptyElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.sync.AbstractSynchronizable;

public class SingleElementStorage extends AbstractSynchronizable implements ISettableSingleElementStorage, ValueIOSerializable {

	protected int elementAmount;
	protected int elementCapacity;
	protected ElementType elementType;

	public SingleElementStorage(ElementType elementType, int elementCapacity) {
		this(elementType, elementCapacity, null);
	}

	public SingleElementStorage(ElementType elementType, int elementCapacity, Runnable syncCallback) {
		this(elementType, 0, elementCapacity, syncCallback);
	}

	public SingleElementStorage(ElementType elementType, int elementAmount, int elementCapacity, Runnable syncCallback) {
		super(syncCallback);
		this.elementType = elementType;
		this.elementCapacity = elementCapacity;
		this.elementAmount = elementAmount;
	}

	@Override
	public @NotNull ElementType getElementType() {
		return elementType;
	}

	@Override
	public int getElementAmount() {
		return elementAmount;
	}

	@Override
	public int getElementCapacity() {
		return elementCapacity;
	}

	@Override
	public void setElementAmount(int newCount) {
		elementAmount = newCount;
		markDirty();
	}

	@Override
	public ISingleElementStorage forElement(ElementType type) {
		if (type != elementType) {
			return EmptyElementStorage.getSingle(type);
		}
		return this;
	}

	@Override
	public String toString() {
		return elementAmount + "/" + elementCapacity + " " + elementType.getSerializedName();
	}

	public CompoundTag serializeNBT(@NotNull HolderLookup.Provider provider) {
		CompoundTag compound = new CompoundTag();

		compound.putString(ECNames.ELEMENT_TYPE, getElementType().getSerializedName());
		compound.putInt(ECNames.ELEMENT_AMOUNT, getElementAmount());
		compound.putInt(ECNames.ELEMENT_CAPACITY, getElementCapacity());
		return compound;
	}

	public void deserializeNBT(@NotNull HolderLookup.Provider provider, @NotNull CompoundTag compound) {
		elementType = ElementType.byName(compound.getString(ECNames.ELEMENT_TYPE).orElse(""));
		elementAmount = compound.getInt(ECNames.ELEMENT_AMOUNT).orElse(0);
		elementCapacity = compound.getInt(ECNames.ELEMENT_CAPACITY).orElse(0);
	}

    @Override
    public void serialize(@NotNull ValueOutput output) {
        output.putString(ECNames.ELEMENT_TYPE, getElementType().getSerializedName());
        output.putInt(ECNames.ELEMENT_AMOUNT, getElementAmount());
        output.putInt(ECNames.ELEMENT_CAPACITY, getElementCapacity());
    }

    @Override
    public void deserialize(@NotNull ValueInput input) {
        input.getString(ECNames.ELEMENT_TYPE).ifPresent(s -> elementType = ElementType.byName(s));
        input.getInt(ECNames.ELEMENT_AMOUNT).ifPresent(s -> elementAmount = s);
        input.getInt(ECNames.ELEMENT_CAPACITY).ifPresent(s -> elementCapacity = s);
    }
}
