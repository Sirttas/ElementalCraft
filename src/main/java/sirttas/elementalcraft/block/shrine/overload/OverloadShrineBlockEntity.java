package sirttas.elementalcraft.block.shrine.overload;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;

import java.util.List;
import java.util.stream.Collectors;

public class OverloadShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(OverloadShrineBlock.NAME);

	private TickingBlockEntity ticker;

	public OverloadShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.OVERLOAD_SHRINE, pos, state, PROPERTIES_KEY);
	}

	@Override
	public AABB getRange() {
		return new AABB(getTargetPos());
	}

	@Override
	public BlockPos getTargetPos() {
		return worldPosition.relative(this.getBlockState().getValue(OverloadShrineBlock.FACING));
	}

	@Override
	protected boolean doPeriod() {
		var t = getTicker();
		
		if (t != null) {
			t.tick();
			return true;
		}
		return false;
	}

	private TickingBlockEntity getTicker() {
		if (ticker == null) {
			var target = getTargetPos();

			ticker = this.level.getChunkAt(target).tickersInLevel.get(target);
		}
		if (ticker == null || ticker.isRemoved() || this.level.getBlockEntity(ticker.getPos()) instanceof AbstractShrineBlockEntity) {
			ticker = null;
		}
		return ticker;
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return DEFAULT_UPGRADE_DIRECTIONS.stream().filter(direction -> direction != this.getBlockState().getValue(OverloadShrineBlock.FACING)).collect(Collectors.toList());
	}
}
