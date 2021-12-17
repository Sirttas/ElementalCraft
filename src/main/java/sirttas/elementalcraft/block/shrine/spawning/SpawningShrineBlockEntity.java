package sirttas.elementalcraft.block.shrine.spawning;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.entity.EntityHelper;

public class SpawningShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SpawningShrineBlock.NAME) public static final BlockEntityType<SpawningShrineBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.FIRE)
			.period(ECConfig.COMMON.spawningShrinePeriod.get())
			.consumeAmount(ECConfig.COMMON.spawningShrineConsumeAmount.get())
			.range(ECConfig.COMMON.spawningShrineRange.get());

	public SpawningShrineBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, PROPERTIES);
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
