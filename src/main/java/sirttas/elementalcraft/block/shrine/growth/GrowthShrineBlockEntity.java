package sirttas.elementalcraft.block.shrine.growth;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.block.StemBlock;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;

public class GrowthShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + GrowthShrineBlock.NAME) public static final TileEntityType<GrowthShrineBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.WATER).periode(ECConfig.COMMON.growthShrinePeriode.get()).consumeAmount(ECConfig.COMMON.growthShrineConsumeAmount.get())
			.range(ECConfig.COMMON.growthShrineRange.get());

	public GrowthShrineBlockEntity() {
		super(TYPE, PROPERTIES);
	}

	private Optional<BlockPos> findGrowable() {
		int range = getIntegerRange();

		List<BlockPos> positions = IntStream.range(-range, range + 1)
				.mapToObj(x -> IntStream.range(-range, range + 1).mapToObj(z -> IntStream.range(0, 4).mapToObj(y -> new BlockPos(worldPosition.getX() + x, worldPosition.getY() + y, worldPosition.getZ() + z))))
				.flatMap(s -> s.flatMap(s2 -> s2)).filter(this::canGrow).collect(Collectors.toList());
		return positions.isEmpty() ? Optional.empty() : Optional.of(positions.get(this.level.random.nextInt(positions.size())));
	}

	private boolean stemCanGrow(StemBlock stem) {
		if (this.hasUpgrade(ShrineUpgrades.STEM_POLLINATION.get())) {
			Block crop = stem.getFruit();
			
			return Direction.Plane.HORIZONTAL.stream().map( d -> level.getBlockState(worldPosition.relative(d))).noneMatch(state -> state.is(crop));
		}
		return false;
	}
	
	private boolean canGrow(BlockPos pos) {
		BlockState blockstate = level.getBlockState(pos);
		Block block = blockstate.getBlock();

		if (block instanceof IGrowable) {
			IGrowable igrowable = (IGrowable) block;

			return (igrowable.isValidBonemealTarget(level, pos, blockstate, level.isClientSide) && (igrowable.isBonemealSuccess(level, level.random, pos, blockstate) || this.hasUpgrade(ShrineUpgrades.BONELESS_GROWTH.get())))
					|| (block instanceof StemBlock && stemCanGrow((StemBlock) block));
		}
		return false;
	}

	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		int range = getIntegerRange();

		return new AxisAlignedBB(this.getBlockPos()).inflate(range, 0, range).expandTowards(0, 2, 0);
	}

	@Override
	protected boolean doTick() {
		if (level instanceof ServerWorld) {
			return findGrowable().map(p -> {
				BlockState blockstate = level.getBlockState(p);

				((IGrowable) blockstate.getBlock()).performBonemeal((ServerWorld) level, level.random, p, blockstate);
				level.levelEvent(2005, p, 0);
				return true;
			}).orElse(false);
		}
		return false;
	}
}
