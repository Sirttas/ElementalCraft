package sirttas.elementalcraft.block.shrine.growth;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.config.ECConfig;

public class TileGrowthShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockGrowthShrine.NAME) public static TileEntityType<TileGrowthShrine> TYPE;

	private static final List<Vec3i> RANGE;

	static {
		int range = ECConfig.CONFIG.growthShrineRange.get();
		RANGE = new ArrayList<>(((range * 2 + 1) ^ 2) * 4);

		IntStream.range(-range, range + 1).forEach(x -> IntStream.range(-range, range + 1).forEach(z -> IntStream.range(0, 4).forEach(y -> RANGE.add(new Vec3i(x, y, z)))));
	}

	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		int range = ECConfig.CONFIG.growthShrineRange.get();

		return new AxisAlignedBB(this.getPos()).grow(range, 0, range).expand(0, 2, 0);
	}

	public TileGrowthShrine() {
		super(TYPE, ElementType.WATER, ECConfig.CONFIG.growthShrinePeriode.get());
	}

	@Override
	protected void doTick() {
		int consumeAmount = ECConfig.CONFIG.growthShrineConsumeAmount.get();

		if (world instanceof ServerWorld && this.getElementAmount() >= consumeAmount) {
			RANGE.forEach(v -> {
				BlockPos p = getPos().add(v);
				BlockState blockstate = world.getBlockState(p);

				if (blockstate.getBlock() instanceof IGrowable) {
					IGrowable igrowable = (IGrowable) blockstate.getBlock();

					if (randomChance(ECConfig.CONFIG.growthShrineChance.get()) && igrowable.canGrow(getWorld(), p, blockstate, world.isRemote)
							&& igrowable.canUseBonemeal(world, world.rand, p, blockstate) && this.consumeElement(consumeAmount) == consumeAmount) {
						igrowable.grow((ServerWorld) world, world.rand, p, blockstate);
						world.playEvent(2005, p, 0);
					}
				}
			});
		}
	}
}
