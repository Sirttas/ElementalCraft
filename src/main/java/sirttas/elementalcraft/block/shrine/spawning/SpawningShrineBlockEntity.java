package sirttas.elementalcraft.block.shrine.spawning;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.entity.EntityHelper;

public class SpawningShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(SpawningShrineBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	public SpawningShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.SPAWNING_SHRINE, PROPERTIES, pos, state);
	}

	@Override
	protected boolean doPeriod() {
		if (this.getLevel() instanceof ServerLevel serverLevel) {
            var random = serverLevel.getRandom();
			var range = this.getRange();
			var pos = new BlockPos(random.nextInt((int) range.minX, (int) range.maxX), this.getTargetPos().getY(), random.nextInt((int) range.minZ, (int) range.maxZ));

			return EntityHelper.spawnMob(serverLevel, pos);
		}
		return false;
	}
}
