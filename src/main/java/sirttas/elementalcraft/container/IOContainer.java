package sirttas.elementalcraft.container;

import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jspecify.annotations.Nullable;
import sirttas.elementalcraft.api.name.ECNames;

public class IOContainer extends AbstractSynchronizableContainer implements WorldlyContainer, ValueIOSerializable {

    private ItemStack input;
    private ItemStack output;

    public IOContainer() {
        this(null);
    }

    public IOContainer(@Nullable Runnable syncCallback) {
        super(syncCallback);
        input = ItemStack.EMPTY;
        output = ItemStack.EMPTY;
    }

    @Override
    public void clearContent() {
        input = ItemStack.EMPTY;
        output = ItemStack.EMPTY;
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return input.isEmpty() && output.isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        if (index == 0) {
            return input;
        } else if (index == 1) {
            return output;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.setItem(slot, itemStack, false);
    }

    @Override
    public void setItem(int index, ItemStack stack, boolean insideTransaction) {
        if (index == 0) {
            this.input = stack;
        } else if (index == 1) {
            this.output = stack;
        }
        if (!insideTransaction) {
            this.setChanged();
        }
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index == 0;
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack value = ContainerHelper.removeItem(Lists.newArrayList(input, output), index, count);

        this.setChanged();
        return value;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack ret = getItem(index);

        setItem(index, ItemStack.EMPTY);
        return ret;
    }

    @Override
    public void serialize(ValueOutput output) {
        if (!input.isEmpty()) {
            output.store(ECNames.INPUT, ItemStack.CODEC, this.input);
        } else {
            output.discard(ECNames.INPUT);
        }
        if (!output.isEmpty()) {
            output.store(ECNames.INPUT, ItemStack.CODEC, this.output);
        }  else {
            output.discard(ECNames.OUTPUT);
        }
    }

    @Override
    public void deserialize(ValueInput input) {
        this.input = input.read(ECNames.INPUT, ItemStack.CODEC).orElse(ItemStack.EMPTY);
        this.output = input.read(ECNames.OUTPUT, ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }


    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return index == 0 || direction == null;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return index == 1 || direction == null;
    }
}
