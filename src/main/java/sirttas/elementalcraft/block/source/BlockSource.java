package sirttas.elementalcraft.block.source;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.BlockEC;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.property.ECProperties;

public class BlockSource extends BlockEC {

	public static final String NAME = "source";

	public BlockSource() {
		super(Block.Properties.create(Material.AIR).hardnessAndResistance(-1.0F, 3600000.0F).notSolid().noDrops());
		this.setDefaultState(this.stateContainer.getBaseState().with(ECProperties.ELEMENT_TYPE, ElementType.NONE));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
		container.add(ECProperties.ELEMENT_TYPE);
	}


	@Override
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return false;
	}

	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.empty();

	}

	/**
	 * Called after the block is set in the Chunk data, but before the Tile
	 * Entity is set
	 */
	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (state.get(ECProperties.ELEMENT_TYPE) == ElementType.NONE) {
			worldIn.setBlockState(pos, state.with(ECProperties.ELEMENT_TYPE, ElementType.random()));
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		ParticleHelper.createSourceParticle(ElementType.getElementType(state), world, new Vec3d(pos), rand);
	}
}
