package sirttas.elementalcraft.block.shrine.lava;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;
import java.util.Optional;

public class LavaShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(LavaShrineBlock.NAME);

	protected static final List<Direction> UPGRADE_DIRECTIONS = List.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	public LavaShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.LAVA_SHRINE, pos, state, PROPERTIES_KEY);
	}

	private Optional<BlockPos> findRock() {
		return getBlocksInRange()
				.filter(p -> level.getBlockState(p).is(ECTags.Blocks.SHRINES_LAVA_LIQUIFIABLES)).findAny();
	}

	@Override
	public AABB getRangeBoundingBox() {
		int range = Math.round(this.getProperties().range());

		return ElementalCraftUtils.stitchAABB(new AABB(this.getBlockPos()).inflate(range, 0, range).move(0, 1, 0));
	}

	@Override
	protected boolean doPeriod() {
		return findRock().map(p -> {
			level.setBlockAndUpdate(p, Blocks.LAVA.defaultBlockState());
			level.levelEvent(LevelEvent.LAVA_FIZZ, p, 0);
			return true;
		}).orElse(false);
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRADE_DIRECTIONS;
	}
}
