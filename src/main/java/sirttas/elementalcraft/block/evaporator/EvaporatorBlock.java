package sirttas.elementalcraft.block.evaporator;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.AbstractECContainerBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.item.elemental.ShardItem;
import sirttas.elementalcraft.particle.ParticleHelper;
import sirttas.elementalcraft.tag.ECTags;

public class EvaporatorBlock extends AbstractECContainerBlock {

	public static final String NAME = "evaporator";

	private static final VoxelShape BASE_1 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);
	private static final VoxelShape BASE_2 = Block.box(5D, 1D, 5D, 11D, 7D, 11D);

	private static final VoxelShape PIPE_1 = Block.box(3D, 0D, 3D, 5D, 8D, 5D);
	private static final VoxelShape PIPE_2 = Block.box(11D, 0D, 3D, 13D, 8D, 5D);
	private static final VoxelShape PIPE_3 = Block.box(3D, 0D, 11D, 5D, 8D, 13D);
	private static final VoxelShape PIPE_4 = Block.box(11D, 0D, 11D, 13D, 8D, 13D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	@Override
	public EvaporatorBlockEntity createTileEntity(BlockState state, IBlockReader world) {
		return new EvaporatorBlockEntity();
	}

	@Override
	@Deprecated
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.isEmpty() || getShardElementType(stack) != ElementType.NONE) {
			return onSingleSlotActivated(world, pos, player, hand);
		}
		return ActionResultType.PASS;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
		return BlockEntityHelper.isValidContainer(state.getBlock(), world, pos.below());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		BlockEntityHelper.getTileEntityAs(world, pos, EvaporatorBlockEntity.class).filter(EvaporatorBlockEntity::canExtract)
				.ifPresent(evaporator -> ParticleHelper.createElementFlowParticle(evaporator.getElementStorage().getElementType(), world, Vector3d.atCenterOf(pos.below()), Direction.DOWN, 1, rand));
	}

	public static ElementType getShardElementType(ItemStack stack) {
		if (!stack.isEmpty()) {
			Item item = stack.getItem();

			if (ECTags.Items.SHARDS.contains(item) && item instanceof ShardItem) {
				return ((ShardItem) item).getElementType();
			}
		}
		return ElementType.NONE;
	}
}
