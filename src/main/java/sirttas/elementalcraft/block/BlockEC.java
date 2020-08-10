package sirttas.elementalcraft.block;

import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sirttas.elementalcraft.property.ECProperties;

public class BlockEC extends Block implements IBlockEC {

	public static final float BIT_SIZE = 0.0625f; // 1/16

	public BlockEC() {
		this(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES);
	}

	public BlockEC(Block.Properties properties) {
		super(properties);
	}

	public static <T> Optional<T> getTileEntityAs(World world, BlockPos pos, Class<T> clazz) {
		return Optional.ofNullable(world.getTileEntity(pos)).filter(clazz::isInstance).map(clazz::cast);
	}
}
