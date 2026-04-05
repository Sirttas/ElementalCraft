package sirttas.elementalcraft.block.synthesizer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.container.IContainerBlockEntity;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractContainerSynthesizerBlockEntity extends AbstractSynthesizerBlockEntity implements IContainerBlockEntity {

    protected AbstractContainerSynthesizerBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, Holder<@NotNull IConfigurableBlockEntityProperties> propertiesHolder, BlockPos pos, BlockState state) {
        super(blockEntityType, propertiesHolder, pos, state);
    }

    @Override
    protected int synthesizeElement() {
        var inventory = getInventory();
        var stack = inventory.getItem(0);

        if (stack.isEmpty()) {
            return 0;
        }

        var amount = getElementAmountForStack(stack);
        var remainder = stack.getCraftingRemainder();

        if (amount <= 0) {
            return 0;
        } else if (remainder != null) {
            inventory.setItem(0, remainder.create());
        } else if (!stack.isEmpty()) {
            stack.shrink(1);
            if (stack.isEmpty()) {
                inventory.setItem(0, ItemStack.EMPTY);
            }
        }
        return amount;
    }

    protected abstract int getElementAmountForStack(ItemStack stack);

    @Override
    public void loadAdditional(@Nonnull ValueInput input) {
        super.loadAdditional(input);
        Container inv = getInventory();

        if (inv instanceof ValueIOSerializable serializable) {
            input.readChild(ECNames.INVENTORY, serializable);
        }
    }

    @Override
    public void saveAdditional(@Nonnull ValueOutput output) {
        super.saveAdditional(output);
        Container inv = getInventory();

        if (inv instanceof ValueIOSerializable serializable) {
            output.putChild(ECNames.INVENTORY, serializable);
        }
    }
}
