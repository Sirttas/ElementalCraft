package sirttas.elementalcraft.block.shrine.growth;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
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

	private boolean stemCanGrow(StemBlock stem, BlockPos pos) {
		if (this.hasUpgrade(ShrineUpgrades.STEM_POLLINATION)) {
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
		return state.is(ECTags.Blocks.GROWTHSHRINE_BLACKLIST);
	}

	private void addGrowthParticles(BlockPos pos) {
		level.levelEvent(LevelEvent.PARTICLES_PLANT_GROWTH, pos, 0);
	}

	@Override
	public AABB getRangeBoundingBox() {
		var range = getRange();

		return ElementalCraftUtils.stitchAABB(new AABB(this.getBlockPos()).inflate(range, 0, range).expandTowards(0, 2, 0));
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

	@Override
	protected boolean doPeriod() {
		if (level instanceof ServerLevel) {
			if (this.hasUpgrade(ShrineUpgrades.BONELESS_GROWTH)) {
				return growBoneless();
			}
			return growStandard();
		}
		return false;
	}
}
