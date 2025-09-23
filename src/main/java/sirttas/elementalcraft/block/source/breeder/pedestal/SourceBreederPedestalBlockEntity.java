package sirttas.elementalcraft.block.source.breeder.pedestal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.block.entity.AbstractIERBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.SingleItemContainer;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SourceBreederPedestalBlockEntity extends AbstractIERBlockEntity implements IElementTypeProvider {

    private final SourceBreederPedestalElementStorage elementStorage;
    private final SingleItemContainer inventory;
    private final RuneHandler runeHandler;

    public SourceBreederPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.SOURCE_BREEDER_PEDESTAL, pos, state);
        elementStorage = new SourceBreederPedestalElementStorage(this);
        inventory = new SourceBreederPedestalContainer(this::setChanged);
        runeHandler = new RuneHandler(ECConfig.SERVER.sourceBreederPedestalMaxRunes.get(), this::setChanged);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SourceBreederPedestalBlockEntity pedestal) {
        pedestal.elementStorage.refreshElement();
    }

    @Override
    @Nonnull
    public ISingleElementStorage getElementStorage() {
        return elementStorage;
    }

    @Override
    public IRuneHandler getRuneHandler() {
        return runeHandler;
    }

    @Nonnull
    @Override
    public Container getInventory() {
        return inventory;
    }

    @Nullable
    public ISourceTraitHolder getTraitHolder() {
        var receptacle = getReceptacle();

        if (receptacle.isEmpty()) {
            return null;
        }
        return receptacle.getCapability(ElementalCraftCapabilities.SourceTraits.ITEM, null);
    }

    @Override
    public @NotNull ElementType getElementType() {
        return ReceptacleHelper.getElementType(getReceptacle());
    }

    public ItemStack getReceptacle() {
        return inventory.getItem(0);
    }

    public boolean hasSource() {
        return !getReceptacle().isEmpty();
    }

    public @NotNull SingleItemSingleElementRecipeInput createRecipeInput() {
        return new SingleItemSingleElementRecipeInput(
                getInventory().getItem(0),
                getElementType(),
                getElementStorage().getElementAmount());
    }

    @Override
    protected void applyImplicitComponents(@NotNull DataComponentInput input) {
        super.applyImplicitComponents(input);
        elementStorage.setElementAmount(input.getOrDefault(ECDataComponents.ELEMENT_AMOUNT, 0));
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
}
