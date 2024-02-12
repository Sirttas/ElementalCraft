package sirttas.elementalcraft.block.instrument.io.mill.grindstone;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractMillBlockEntity;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.ie.IEInteraction;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public abstract class AbstractMillGrindstoneBlockEntity extends AbstractMillBlockEntity<AbstractMillGrindstoneBlockEntity, IGrindingRecipe> {


    protected AbstractMillGrindstoneBlockEntity(Config<AbstractMillGrindstoneBlockEntity, IGrindingRecipe> config, ElementType elementType, BlockPos pos, BlockState state) {
        super(config, elementType, pos, state);
    }

    @Override
    protected IGrindingRecipe lookupRecipe() {
        if (getContainerElementType() == ElementType.NONE) {
            return null;
        }

        var recipe = super.lookupRecipe();

        if (recipe == null && ECinteractions.isImmersiveEngineeringActive()) {
            recipe = IEInteraction.lookupCrusherRecipe(level, this);
        }
        return recipe;
    }
}
