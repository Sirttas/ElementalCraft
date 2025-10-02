package sirttas.elementalcraft.block.shrine.growth;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.Tags;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.tag.ECTags;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GrowthShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(GrowthShrineBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	public static final String CRYSTAL_GROWTH_RANGE_KEY = "crystal_growth";
	private static final int MAX_TRYS = 100;

	private boolean hasStemPollination = false;

	public GrowthShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.GROWTH_SHRINE, PROPERTIES, pos, state);
	}

	private Optional<BlockPos> findGrowable() {
		List<BlockPos> positions = getBlocksInRange()
				.filter(this::canGrow)
				.toList();

		return positions.isEmpty() ? Optional.empty() : Optional.of(positions.get(this.level.random.nextInt(positions.size())));
	}

	private boolean stemCanGrow(StemBlock stem, BlockPos pos) {
		if (hasStemPollination) {
			var fruit = stem.fruit;
			
			return Direction.Plane.HORIZONTAL.stream()
					.map(d -> level.getBlockState(pos.relative(d)))
					.noneMatch(state -> state.is(fruit));
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
			return (growable.isValidBonemealTarget(level, pos, state) && growable.isBonemealSuccess(level, level.random, pos, state)) || (block instanceof StemBlock stem && stemCanGrow(stem, pos));
		}
		return false;
	}

	private boolean isInBlacklist(BlockState state) {
		return state.is(ECTags.Blocks.SHRINES_GROWTH_BLACKLIST);
	}

	private void addGrowthParticles(BlockPos pos) {
		level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 0);
	}

	@Override
	public AABB lookupRange() {
		if (this.hasUpgrade(ShrineUpgrades.CRYSTAL_GROWTH)) {
			return lookupRange(CRYSTAL_GROWTH_RANGE_KEY);
		}
		return super.lookupRange();
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

			// TODO use tag only
			if ((block instanceof BonemealableBlock || state.is(ECTags.Blocks.SHRINES_GROWTH_BONELESS)) && !(block instanceof StemBlock) && state.isRandomlyTicking() && this.elementStorage.getElementAmount() >= consumeAmount) {
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
		return BuddingAmethystBlock.canClusterGrowAtState(s) || s.is(Tags.Blocks.BUDS);
	}

	private boolean canGrowCrystal(BlockPos pos) {
		BlockState state = level.getBlockState(pos);

		if (state.is(Tags.Blocks.BUDDING_BLOCKS) && state.isRandomlyTicking()) {
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
