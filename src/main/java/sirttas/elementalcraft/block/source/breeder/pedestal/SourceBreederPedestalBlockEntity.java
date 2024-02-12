package sirttas.elementalcraft.block.source.breeder.pedestal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.block.entity.AbstractIERBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.SingleItemContainer;
import sirttas.elementalcraft.item.source.receptacle.ReceptacleHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SourceBreederPedestalBlockEntity extends AbstractIERBlockEntity implements IElementTypeProvider {

    private final SourceBreederPedestalElementStorage elementStorage;
    private final SingleItemContainer inventory;
    private final RuneHandler runeHandler;

    public SourceBreederPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.SOURCE_BREEDER_PEDESTAL, pos, state);
        elementStorage = new SourceBreederPedestalElementStorage(this);
        inventory = new SourceBreederPedestalItemContainer(this::setChanged);
        runeHandler = new RuneHandler(ECConfig.COMMON.sourceBreederPedestalMaxRunes.get(), this::setChanged);
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
        return receptacle.getCapability(ElementalCraftCapabilities.SourceTrait.ITEM, null);
    }

    @Override
    public ElementType getElementType() {
        return ReceptacleHelper.getElementType(getReceptacle());
    }

    public ItemStack getReceptacle() {
        return inventory.getItem(0);
    }

    public boolean hasSource() {
        return !getReceptacle().isEmpty();
    }
}
