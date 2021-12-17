package sirttas.elementalcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class CrystalOreBlock extends Block {

	public static final String NAME = "inert_crystal_ore";

	public CrystalOreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public int getExpDrop(BlockState state, LevelReader level, BlockPos pos, int fortune, int silkTouch) {
		return silkTouch == 0 ? Mth.nextInt(RANDOM, 0, 2) : 0;
	}

}
