package sirttas.elementalcraft.block.shrine.lava;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.tag.ECTags;

public class TileLavaShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockLavaShrine.NAME) public static TileEntityType<TileLavaShrine> TYPE;

	private static final List<Vector3i> RANGE;

	static {
		int range = ECConfig.CONFIG.lavaShrineRange.get();
		RANGE = new ArrayList<>(((range * 2 + 1) ^ 2) * 4);

		IntStream.range(-range, range + 1).forEach(x -> IntStream.range(-range, range + 1).forEach(z -> RANGE.add(new Vector3i(x, 1, z))));
	}

	public TileLavaShrine() {
		super(TYPE, ElementType.FIRE, ECConfig.CONFIG.lavaShrinePeriode.get());
		this.elementMax *= 10;
	}

	@Override
	protected void doTick() {
		int consumeAmount = ECConfig.CONFIG.lavaShrineConsumeAmount.get();

		if (this.getElementAmount() >= consumeAmount) {
			RANGE.forEach(v -> {
				BlockPos p = getPos().add(v);
				BlockState blockstate = world.getBlockState(p);

				if (ECTags.Blocks.LAVASHRINE_LIQUIFIABLES.contains(blockstate.getBlock()) && randomChance(ECConfig.CONFIG.lavaShrineChance.get())
						&& this.getElementAmount() >= consumeAmount) {
					this.consumeElement(consumeAmount);
					world.setBlockState(p, Blocks.LAVA.getDefaultState());
					world.playEvent(1501, p, 0);
				}
			});
		}
	}
}
