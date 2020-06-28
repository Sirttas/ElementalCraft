package sirttas.elementalcraft.block;

import net.minecraft.block.Block;
import sirttas.elementalcraft.property.ECProperties;

public class BlockEC extends Block implements IBlockEC {

	public static final float BIT_SIZE = 0.0625f; // 1/16

	public BlockEC() {
		this(ECProperties.DEFAULT_BLOCK_PROPERTIES);
	}

	public BlockEC(Block.Properties properties) {
		super(properties);
	}
}
