package sirttas.elementalcraft.block.instrument.io.mill;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.IInstrument;
import sirttas.elementalcraft.container.IOContainer;
import sirttas.elementalcraft.recipe.instrument.io.IIOInstrumentRecipe;

import javax.annotation.Nonnull;

public abstract class AbstractMillBlockEntity<T extends IInstrument, R extends IIOInstrumentRecipe<T>> extends AbstractInstrumentBlockEntity<T, R> {

    private final ElementType elementType;
    private final IOContainer inventory;

    protected AbstractMillBlockEntity(Config<T, R> config, ElementType elementType, BlockPos pos, BlockState state) {
        super(config, pos, state);
        this.inventory = new IOContainer(this::setChanged);
        this.elementType = elementType;
    }

    @Nonnull
    @Override
    protected IItemHandler createHandler() {
        return new SidedInvWrapper(inventory, null);
    }

    @Nonnull
    @Override
    public Container getInventory() {
        return inventory;
    }

    @Override
    protected ElementType getRecipeElementType() {
        return recipe == null ? ElementType.NONE : elementType;
    }

    @Override
    public boolean isRecipeAvailable() {
        return this.getContainerElementType() == this.elementType && super.isRecipeAvailable();
    }

    @Override
    public ElementType getElementType() {
        return this.elementType;
    }
}
