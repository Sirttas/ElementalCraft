package sirttas.elementalcraft.block.instrument.io.mill.grindstone;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractMillBlockEntity;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;

import java.util.function.Supplier;

public abstract class AbstractMillGrindstoneBlockEntity extends AbstractMillBlockEntity<GrindingRecipe> {


    protected AbstractMillGrindstoneBlockEntity(
            Supplier<? extends BlockEntityType<?>> blockEntityType,
            Holder<IConfigurableBlockEntityProperties> properties,
            ElementType elementType,
            BlockPos pos,
            BlockState state) {
        super(blockEntityType, properties, elementType, pos, state);
    }

    @Override
    protected GrindingRecipe lookupRecipe(@NotNull ServerLevel level, @NotNull SimpleIOInstrumentRecipeInput recipeInput) {
        if (getContainerElementType() == ElementType.NONE) {
            return null;
        }

        var recipe = super.lookupRecipe(level, recipeInput);

        if (recipe == null) {
            recipe = ElementalCraft.interactions().lookupRecipe(level, recipeInput);
        }
        return recipe;
    }
}
