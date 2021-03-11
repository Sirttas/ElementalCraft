package sirttas.elementalcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class BlockCrystalOre extends Block {

	public static final String NAME = "crystalore";

	public BlockCrystalOre(Properties properties) {
		super(properties);
	}

	@Override
	public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader reader, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? MathHelper.nextInt(RANDOM, 0, 2) : 0;
	}

}
