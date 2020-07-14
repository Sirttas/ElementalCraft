package sirttas.elementalcraft.block.shrine.harvest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.config.ECConfig;

public class TileHarvestShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockHarvestShrine.NAME) public static TileEntityType<TileHarvestShrine> TYPE;

	private static final List<Vector3i> RANGE;

	static {
		int range = ECConfig.CONFIG.harvestShrineRange.get();
		RANGE = new ArrayList<>(((range * 2 + 1) ^ 2) * 4);

		IntStream.range(-range, range + 1).forEach(x -> IntStream.range(-range, range + 1).forEach(z -> IntStream.range(-3, 1).forEach(y -> RANGE.add(new Vector3i(x, y, z)))));
	}

	public TileHarvestShrine() {
		super(TYPE, ElementType.EARTH);
	}

	@Override
	public void tick() {
		int consumeAmount = ECConfig.CONFIG.harvestShrineConsumeAmount.get();

		super.tick();
		if (this.hasWorld() && world instanceof ServerWorld && randomChance(0.05D)) {
			RANGE.forEach(v -> {
				BlockPos p = getPos().add(v);
				BlockState blockstate = world.getBlockState(p);

				if (blockstate.getBlock() instanceof CropsBlock && randomChance(ECConfig.CONFIG.harvestShrineChance.get()) && ((CropsBlock) blockstate.getBlock()).isMaxAge(blockstate)
						&& this.consumeElement(consumeAmount) == consumeAmount) {
					world.destroyBlock(p, true);
				}
			});
		}
	}
}
