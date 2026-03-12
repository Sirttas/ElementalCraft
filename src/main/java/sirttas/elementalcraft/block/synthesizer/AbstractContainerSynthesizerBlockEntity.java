package sirttas.elementalcraft.block.synthesizer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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

        if (amount <= 0) {
            return 0;
        } else if (stack.hasCraftingRemainingItem()) {
            inventory.setItem(0, stack.getCraftingRemainingItem());
        } else if (!stack.isEmpty()) {
            stack.shrink(1);
            if (stack.isEmpty()) {
                inventory.setItem(0, stack.getCraftingRemainingItem());
            }
        }
        return amount;
    }

    protected abstract int getElementAmountForStack(ItemStack stack);

    @Override
    public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);
        Container inv = getInventory();

        if (inv instanceof INBTSerializable nbtInv && compound.contains(ECNames.INVENTORY)) {
            nbtInv.deserializeNBT(provider, compound.get(ECNames.INVENTORY));
        }
    }

    @Override
    public void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        Container inv = getInventory();

        if (inv instanceof INBTSerializable<?> nbtInv) {
            compound.put(ECNames.INVENTORY, nbtInv.serializeNBT(provider));
        }
    }
}
