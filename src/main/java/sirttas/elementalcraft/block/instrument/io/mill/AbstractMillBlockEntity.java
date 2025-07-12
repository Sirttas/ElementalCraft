package sirttas.elementalcraft.block.instrument.io.mill;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.io.AbstractIOInstrumentBlockEntity;
import sirttas.elementalcraft.container.IOContainer;
import sirttas.elementalcraft.recipe.instrument.io.IOInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public abstract class AbstractMillBlockEntity<R extends IOInstrumentRecipe<SimpleIOInstrumentRecipeInput>> extends AbstractIOInstrumentBlockEntity<SimpleIOInstrumentRecipeInput, R> {

    private final ElementType elementType;
    private final Container inventory;

    protected AbstractMillBlockEntity(
            Supplier<? extends BlockEntityType<?>> blockEntityType,
            Holder<IConfigurableBlockEntityProperties> properties,
            ElementType elementType,
            BlockPos pos,
            BlockState state) {
        super(blockEntityType, properties, pos, state);
        this.inventory = new IOContainer(this::setChanged);
        this.elementType = elementType;
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
    public @NotNull ElementType getElementType() {
        return this.elementType;
    }

    @NotNull
    @Override
    protected SimpleIOInstrumentRecipeInput createRecipeInput() {
        return createSimpleIORecipeInput();
    }
}
