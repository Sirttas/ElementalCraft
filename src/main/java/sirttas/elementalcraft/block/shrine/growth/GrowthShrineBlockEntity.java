package sirttas.elementalcraft.block.shrine.growth;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.IPlantable;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.tag.ECTags;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GrowthShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(GrowthShrineBlock.NAME);
	private static final int MAX_TRYS = 100;

	private boolean hasStemPollination = false;

	public GrowthShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.GROWTH_SHRINE, pos, state, PROPERTIES_KEY);
	}

	private Optional<BlockPos> findGrowable() {
		List<BlockPos> positions = getBlocksInRange()
				.filter(this::canGrow)
				.toList();

		return positions.isEmpty() ? Optional.empty() : Optional.of(positions.get(this.level.random.nextInt(positions.size())));
	}

	private boolean stemCanGrow(StemBlock stem, BlockPos pos) {
		if (hasStemPollination) {
			Block crop = stem.getFruit();
			
			return Direction.Plane.HORIZONTAL.stream()
					.map(d -> level.getBlockState(pos.relative(d)))
					.noneMatch(state -> state.is(crop));
		}
		return false;
	}
	
	private boolean canGrow(BlockPos pos) {
		BlockState state = level.getBlockState(pos);

		if (isInBlacklist(state)) {
			return false;
		}

		Block block = state.getBlock();

		if (block instanceof BonemealableBlock growable) {
			return (growable.isValidBonemealTarget(level, pos, state, level.isClientSide) && growable.isBonemealSuccess(level, level.random, pos, state)) || (block instanceof StemBlock stem && stemCanGrow(stem, pos));
		}
		return false;
	}

	private boolean isInBlacklist(BlockState state) {
		return state.is(ECTags.Blocks.SHRINES_GROWTH_BLACKLIST);
	}

	private void addGrowthParticles(BlockPos pos) {
		level.levelEvent(LevelEvent.PARTICLES_PLANT_GROWTH, pos, 0);
	}

	@Override
	public AABB getRangeBoundingBox() {
		if (this.hasUpgrade(ShrineUpgrades.CRYSTAL_GROWTH)) {
			return super.getRangeBoundingBox();
		}

		var range = getRange();

		return ElementalCraftUtils.stitchAABB(new AABB(this.getTargetPos()).inflate(range, 0, range).expandTowards(0, 2, 0));
	}

	private boolean growBoneless() {
		int consumeAmount = this.getConsumeAmount();
		List<BlockPos> positions = getBlocksInRange().toList();

		for (BlockPos pos : positions) {
			BlockState state = level.getBlockState(pos);

			if (isInBlacklist(state)) {
				continue;
			}

			Block block = state.getBlock();

			if ((block instanceof BonemealableBlock || block instanceof IPlantable) && !(block instanceof StemBlock) && block.isRandomlyTicking(state) && this.elementStorage.getElementAmount() >= consumeAmount) {
				state.randomTick((ServerLevel) level, pos, level.random);

				var newState = level.getBlockState(pos);

				if (newState != state) {
					this.consumeElement(consumeAmount);
					addGrowthParticles(pos);
				}
			}
		}
		return false;
	}

	private boolean growStandard() {
		return findGrowable().map(p -> {
			BlockState blockstate = level.getBlockState(p);

			((BonemealableBlock) blockstate.getBlock()).performBonemeal((ServerLevel) level, level.random, p, blockstate);
			addGrowthParticles(p);
			return true;
		}).orElse(false);
	}

	private boolean canClusterGrowAtState(BlockState s) {
		return BuddingAmethystBlock.canClusterGrowAtState(s) || s.is(ECTags.Blocks.BUDS);
	}

	private boolean canGrowCrystal(BlockPos pos) {
		BlockState state = level.getBlockState(pos);

		if (state.is(ECTags.Blocks.BUDDING) && state.isRandomlyTicking()) {
			for (Direction direction : Direction.values()) {
				var offset = pos.relative(direction);
				var s = level.getBlockState(offset);

				if (canClusterGrowAtState(s)) {
					return true;
				}
			}
		}
		return false;
	}


	private boolean growCrystals() {
		List<BlockPos> positions = getBlocksInRange()
				.filter(this::canGrowCrystal)
				.toList();

		if (positions.isEmpty()) {
			return false;
		}

		var pos = positions.get(this.level.random.nextInt(positions.size()));
		var state = level.getBlockState(pos);
		Map<Direction, BlockState> map = new EnumMap<>(Direction.class);

		for (Direction direction : Direction.values()) {
			var offset = pos.relative(direction);
			var s = level.getBlockState(offset);

			if (canClusterGrowAtState(s)) {
				map.put(direction, s);
			}
		}

		for (int tryCount = 0; tryCount < MAX_TRYS; tryCount++) {
			state.randomTick((ServerLevel) level, pos, level.random);
			for (var e : map.entrySet()) {
				var offset = pos.relative(e.getKey());

				if (level.getBlockState(offset) != e.getValue()) {
					this.addGrowthParticles(offset);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected boolean doPeriod() {
		if (level instanceof ServerLevel) {
			hasStemPollination = this.hasUpgrade(ShrineUpgrades.STEM_POLLINATION);

			if (this.hasUpgrade(ShrineUpgrades.CRYSTAL_GROWTH)) {
				return growCrystals();
			} else if (this.hasUpgrade(ShrineUpgrades.BONELESS_GROWTH)) {
				return growBoneless();
			}
			return growStandard();
		}
		return false;
	}
}
