package sirttas.elementalcraft.block.instrument.io.mill.woodsaw.water;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.io.mill.AbstractMillBlockEntity;
import sirttas.elementalcraft.recipe.instrument.io.sawing.SawingRecipe;

public class WaterMillWoodSawBlockEntity extends AbstractMillBlockEntity<SawingRecipe> {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(WaterMillWoodSawBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	public WaterMillWoodSawBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.WATER_MILL_WOOD_SAW, PROPERTIES, ElementType.WATER, pos, state);
	}
}
