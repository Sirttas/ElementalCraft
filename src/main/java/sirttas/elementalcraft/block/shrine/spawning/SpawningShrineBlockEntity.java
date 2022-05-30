package sirttas.elementalcraft.block.shrine.spawning;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.entity.EntityHelper;

public class SpawningShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SpawningShrineBlock.NAME) public static final BlockEntityType<SpawningShrineBlockEntity> TYPE = null;

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(SpawningShrineBlock.NAME);

	public SpawningShrineBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, PROPERTIES_KEY);
	}

	@Override
	public AABB getRangeBoundingBox() {
		int range = getIntegerRange();

		return new AABB(this.getBlockPos()).inflate(range, 0, range);
	}

	@Override
	protected boolean doPeriod() {
		if (this.getLevel() instanceof ServerLevel level) {
			var range = this.getIntegerRange();
			var pos = this.getBlockPos()
					.relative(Direction.Axis.X, level.random.nextInt(-range, range))
					.relative(Direction.Axis.Z, level.random.nextInt(-range, range));

			return EntityHelper.spawnMob(level, pos);
		}
		return false;
	}
}
