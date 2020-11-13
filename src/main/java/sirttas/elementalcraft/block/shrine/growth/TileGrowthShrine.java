package sirttas.elementalcraft.block.shrine.growth;

import java.util.Optional;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.config.ECConfig;

public class TileGrowthShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockGrowthShrine.NAME) public static TileEntityType<TileGrowthShrine> TYPE;

	private static final Properties PROPERTIES = Properties.create(ElementType.WATER).periode(ECConfig.COMMON.growthShrinePeriode.get()).consumeAmount(ECConfig.COMMON.growthShrineConsumeAmount.get())
			.range(ECConfig.COMMON.growthShrineRange.get());

	public TileGrowthShrine() {
		super(TYPE, PROPERTIES);
	}

	private Optional<BlockPos> findGrowable() {
		int range = getIntegerRange();

		return IntStream.range(-range, range + 1)
				.mapToObj(x -> IntStream.range(-range, range + 1).mapToObj(z -> IntStream.range(0, 4).mapToObj(y -> new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z))))
				.flatMap(s -> s.flatMap(s2 -> s2)).filter(p -> {
					BlockState blockstate = world.getBlockState(p);
					Block block = blockstate.getBlock();

					if (block instanceof IGrowable) {
						IGrowable igrowable = (IGrowable) block;

						return igrowable.canGrow(world, p, blockstate, world.isRemote) && igrowable.canUseBonemeal(world, world.rand, p, blockstate);
					}
					return false;
				}).findAny();
	}

	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		int range = getIntegerRange();

		return new AxisAlignedBB(this.getPos()).grow(range, 0, range).expand(0, 2, 0);
	}

	@Override
	protected boolean doTick() {
		if (world instanceof ServerWorld) {
			return findGrowable().map(p -> {
				BlockState blockstate = world.getBlockState(p);

				((IGrowable) blockstate.getBlock()).grow((ServerWorld) world, world.rand, p, blockstate);
				world.playEvent(2005, p, 0);
				return true;
			}).orElse(false);
		}
		return false;
	}
}
