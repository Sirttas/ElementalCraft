package sirttas.elementalcraft.block.shrine.growth;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import java.util.List;
import java.util.Optional;

public class GrowthShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(GrowthShrineBlock.NAME);
	public GrowthShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.GROWTH_SHRINE, pos, state, PROPERTIES_KEY);
	}

	private Optional<BlockPos> findGrowable() {
		List<BlockPos> positions = getBlocksInRange()
				.filter(this::canGrow)
				.toList();

		return positions.isEmpty() ? Optional.empty() : Optional.of(positions.get(this.level.random.nextInt(positions.size())));
	}

	private boolean stemCanGrow(StemBlock stem) {
		if (this.hasUpgrade(ShrineUpgrades.STEM_POLLINATION)) {
			Block crop = stem.getFruit();
			
			return Direction.Plane.HORIZONTAL.stream().map( d -> level.getBlockState(worldPosition.relative(d))).noneMatch(state -> state.is(crop));
		}
		return false;
	}
	
	private boolean canGrow(BlockPos pos) {
		BlockState blockstate = level.getBlockState(pos);
		Block block = blockstate.getBlock();

		if (block instanceof BonemealableBlock growable) {
			return (growable.isValidBonemealTarget(level, pos, blockstate, level.isClientSide) && (growable.isBonemealSuccess(level, level.random, pos, blockstate) || this.hasUpgrade(ShrineUpgrades.BONELESS_GROWTH)))
					|| (block instanceof StemBlock && stemCanGrow((StemBlock) block));
		}
		return false;
	}

	@Override
	public AABB getRangeBoundingBox() {
		int range = getIntegerRange();

		return new AABB(this.getBlockPos()).inflate(range, 0, range).expandTowards(0, 2, 0);
	}

	@Override
	protected boolean doPeriod() {
		if (level instanceof ServerLevel) {
			return findGrowable().map(p -> {
				BlockState blockstate = level.getBlockState(p);

				((BonemealableBlock) blockstate.getBlock()).performBonemeal((ServerLevel) level, level.random, p, blockstate);
				level.levelEvent(2005, p, 0);
				return true;
			}).orElse(false);
		}
		return false;
	}
}
