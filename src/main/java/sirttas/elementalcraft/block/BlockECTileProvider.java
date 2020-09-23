package sirttas.elementalcraft.block;

import javax.annotation.Nonnull;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import sirttas.elementalcraft.property.ECProperties;

public abstract class BlockECTileProvider extends BlockEC implements IBlockECTileProvider {

	public BlockECTileProvider(AbstractBlock.Properties properties) {
		super(properties);
	}

	public BlockECTileProvider() {
		this(ECProperties.Blocks.BLOCK_NOT_SOLID);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public abstract TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world);

	@Override
	@Deprecated
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
}
