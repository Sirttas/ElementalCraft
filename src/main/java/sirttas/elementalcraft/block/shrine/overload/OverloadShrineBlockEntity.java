package sirttas.elementalcraft.block.shrine.overload;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;

import java.util.List;
import java.util.stream.Collectors;

public class OverloadShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + OverloadShrineBlock.NAME) public static final BlockEntityType<OverloadShrineBlockEntity> TYPE = null;

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(OverloadShrineBlock.NAME);

	public OverloadShrineBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, PROPERTIES_KEY);
	}

	@Override
	public AABB getRangeBoundingBox() {
		return new AABB(getTargetPos());
	}

	private BlockPos getTargetPos() {
		return worldPosition.relative(this.getBlockState().getValue(OverloadShrineBlock.FACING));
	}

	@Override
	protected boolean doPeriod() {
		var target = getTargetPos();
		TickingBlockEntity ticker = this.level.getChunkAt(target).tickersInLevel.get(target);
		
		if (ticker != null && !ticker.isRemoved() && !(this.level.getBlockEntity(target) instanceof AbstractShrineBlockEntity)) {
			ticker.tick();
			return true;
		}
		return false;
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return DEFAULT_UPGRADE_DIRECTIONS.stream().filter(direction -> direction != this.getBlockState().getValue(OverloadShrineBlock.FACING)).collect(Collectors.toList());
	}
}
