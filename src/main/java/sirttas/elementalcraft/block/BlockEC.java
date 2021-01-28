package sirttas.elementalcraft.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import sirttas.elementalcraft.property.ECProperties;

public class BlockEC extends Block implements IBlockEC {

	public static final float BIT_SIZE = 0.0625f; // 1/16

	public BlockEC() {
		this(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES);
	}

	public BlockEC(AbstractBlock.Properties properties) {
		super(properties);
	}

	// TODO move to helper
	protected static boolean doesVectorColide(AxisAlignedBB bb, Vector3d vec) {
		return vec.x >= bb.minX && vec.y >= bb.minY && vec.z >= bb.minZ && vec.x <= bb.maxX && vec.y <= bb.maxY && vec.z <= bb.maxZ;
	}
}
