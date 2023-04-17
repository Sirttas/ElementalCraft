package sirttas.elementalcraft.block.source.breeder.pedestal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.api.source.trait.holder.ISourceTraitHolder;
import sirttas.elementalcraft.api.source.trait.holder.SourceTraitHolderHelper;
import sirttas.elementalcraft.block.entity.AbstractIERBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.source.trait.holder.SourceTraitHolder;
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
        return SourceTraitHolderHelper.get(getReceptacle()).orElseGet(SourceTraitHolder::new);
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

    @Override
    @Nonnull
    public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
        if (!this.remove && cap == ElementalCraftCapabilities.SOURCE_TRAIT_HOLDER) {
            return SourceTraitHolderHelper.get(getReceptacle()).cast();
        }
        return super.getCapability(cap, side);
    }

}
