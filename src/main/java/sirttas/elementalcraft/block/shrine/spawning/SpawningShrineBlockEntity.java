package sirttas.elementalcraft.block.shrine.spawning;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

import java.util.concurrent.atomic.AtomicBoolean;

public class SpawningShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SpawningShrineBlock.NAME) public static final BlockEntityType<SpawningShrineBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.FIRE)
			.periode(ECConfig.COMMON.spawningShrinePeriode.get())
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

	private boolean spawnMob(ServerLevel level, BlockPos pos) {
		AtomicBoolean flag = new AtomicBoolean(false);

		NaturalSpawner.spawnCategoryForPosition(MobCategory.MONSTER, level, level.getChunk(pos), pos,
				(m, c, p) -> true,
				(m, c) -> flag.set(true));
		return flag.get();
	}

	@Override
	protected boolean doPeriode() {
		if (this.getLevel() instanceof ServerLevel level) {
			var range = this.getIntegerRange();
			var pos = this.getBlockPos()
					.relative(Direction.Axis.X, level.random.nextInt(-range, range))
					.relative(Direction.Axis.Z, level.random.nextInt(-range, range));

			return spawnMob(level, pos);
		}
		return false;
	}
}
