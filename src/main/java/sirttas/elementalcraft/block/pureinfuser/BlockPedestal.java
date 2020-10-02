package sirttas.elementalcraft.block.pureinfuser;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.BlockECContainer;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.particle.ParticleHelper;

public class BlockPedestal extends BlockECContainer {

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(0D, 0D, 0D, 16D, 3D, 16D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(2D, 3D, 2D, 14D, 9D, 14D);
	private static final VoxelShape BASE_3 = Block.makeCuboidShape(0D, 9D, 0D, 16D, 12D, 16D);

	private static final VoxelShape BASE = VoxelShapes.or(BASE_1, BASE_2, BASE_3);
	private static final VoxelShape AIR = VoxelShapes.or(Block.makeCuboidShape(5D, 0D, 5D, 11D, 3D, 11D), BASE_2, BASE_3);
	private static final VoxelShape EARTH = VoxelShapes.or(BASE, Block.makeCuboidShape(4D, 3D, 0D, 12D, 8D, 16D), Block.makeCuboidShape(0D, 3D, 4D, 16D, 8D, 12D));

	public static final String NAME = "pedestal";
	public static final String NAME_FIRE = NAME + "_fire";
	public static final String NAME_WATER = NAME + "_water";
	public static final String NAME_EARTH = NAME + "_earth";
	public static final String NAME_AIR = NAME + "_air";

	private ElementType elementType;

	public BlockPedestal(ElementType type) {
		elementType = type;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TilePedestal();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		return onSingleSlotActivated(world, pos, player, hand);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		TileEntityHelper.getTileEntityAs(world, pos, TilePedestal.class).filter(TilePedestal::isPureInfuserRunning).ifPresent(p -> {
			Direction offset = p.getPureInfuserDirection();

			ParticleHelper.createElementFlowParticle(p.getElementType(), world, Vector3d.copy(pos.offset(offset, 2)).add(0, 0.7, 0), offset, 2, rand);
		});
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if (elementType == ElementType.AIR) {
			return AIR;
		} else if (elementType == ElementType.EARTH) {
			return EARTH;
		}
		return BASE;
	}

	public ElementType getElementType() {
		return elementType;
	}
}
