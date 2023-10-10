package sirttas.elementalcraft.block.shrine.spawning;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.entity.EntityHelper;

public class SpawningShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(SpawningShrineBlock.NAME);

	public SpawningShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.SPAWNING_SHRINE, pos, state, PROPERTIES_KEY);
	}

	@Override
	protected boolean doPeriod() {
		if (this.getLevel() instanceof ServerLevel serverLevel) {
			var range = this.getRange();
			var pos = new BlockPos(serverLevel.random.nextInt((int) range.minX, (int) range.maxX), this.getTargetPos().getY(), serverLevel.random.nextInt((int) range.minZ, (int) range.maxZ));

			return EntityHelper.spawnMob(serverLevel, pos);
		}
		return false;
	}
}
