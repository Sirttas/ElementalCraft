package sirttas.elementalcraft.block.instrument.io.mill.woodsaw;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractMillBlockEntity;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

public class AbstractMillWoodSawBlockEntity extends AbstractMillBlockEntity<AbstractMillWoodSawBlockEntity, SawingRecipe> {

	public AbstractMillWoodSawBlockEntity(Config<AbstractMillWoodSawBlockEntity, SawingRecipe> config, ElementType elementType, BlockPos pos, BlockState state) {
		super(config, elementType, pos, state);
	}
}
