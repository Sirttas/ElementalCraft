package sirttas.elementalcraft.block.instrument.io.firefurnace.blast;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.io.firefurnace.AbstractFireFurnaceBlockEntity;

public class FireBlastFurnaceBlockEntity extends AbstractFireFurnaceBlockEntity<BlastingRecipe> {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(FireBlastFurnaceBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	public FireBlastFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.FIRE_BLAST_FURNACE, PROPERTIES, pos, state);
	}
}
