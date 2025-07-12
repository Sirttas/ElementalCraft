package sirttas.elementalcraft.block.instrument.io.mill.grindstone;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractMillBlockEntity;
import sirttas.elementalcraft.interaction.ECInteractions;
import sirttas.elementalcraft.interaction.ie.IEInteraction;
import sirttas.elementalcraft.interaction.mekanism.MekanismInteraction;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import java.util.function.Supplier;

public abstract class AbstractMillGrindstoneBlockEntity extends AbstractMillBlockEntity<IGrindingRecipe> {


    protected AbstractMillGrindstoneBlockEntity(
            Supplier<? extends BlockEntityType<?>> blockEntityType,
            Holder<IConfigurableBlockEntityProperties> properties,
            ElementType elementType,
            BlockPos pos,
            BlockState state) {
        super(blockEntityType, properties, elementType, pos, state);
    }

    @Override
    protected IGrindingRecipe lookupRecipe(@NotNull SimpleIOInstrumentRecipeInput recipeInput) {
        if (getContainerElementType() == ElementType.NONE) {
            return null;
        }

        var recipe = super.lookupRecipe(recipeInput);

        if (recipe == null && ECInteractions.isMekanismActive()) {
            recipe = MekanismInteraction.lookupCrusherRecipe(level, recipeInput);
        }
        if (recipe == null && ECInteractions.isImmersiveEngineeringActive()) {
            recipe = IEInteraction.lookupCrusherRecipe(level, recipeInput);
        }
        return recipe;
    }
}
