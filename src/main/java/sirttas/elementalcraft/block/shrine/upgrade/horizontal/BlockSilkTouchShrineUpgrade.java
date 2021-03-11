package sirttas.elementalcraft.block.shrine.upgrade.horizontal;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockSilkTouchShrineUpgrade extends AbstractBlockHorizontalShrineUpgrade {

	public static final String NAME = "shrine_upgrade_silk_touch";

	private static final VoxelShape CORE_NORTH = Block.makeCuboidShape(6D, 6D, 4D, 10D, 16D, 8D);
	private static final VoxelShape PILAR_NORTH = Block.makeCuboidShape(7D, 2D, 5D, 9D, 6D, 7D);
	private static final VoxelShape BASE_1_NORTH = Block.makeCuboidShape(5D, 0D, 3D, 11D, 1D, 9D);
	private static final VoxelShape BASE_2_NORTH = Block.makeCuboidShape(6D, 1D, 4D, 10D, 2D, 8D);
	private static final VoxelShape PIPE_1_NORTH = Block.makeCuboidShape(7D, 7D, 0D, 9D, 9D, 4D);
	private static final VoxelShape PIPE_2_NORTH = Block.makeCuboidShape(7D, 13D, -2D, 9D, 15D, 4D);

	private static final VoxelShape CORE_SOUTH = Block.makeCuboidShape(6D, 6D, 8D, 10D, 16D, 12D);
	private static final VoxelShape PILAR_SOUTH = Block.makeCuboidShape(7D, 2D, 9D, 9D, 6D, 11D);
	private static final VoxelShape BASE_1_SOUTH = Block.makeCuboidShape(5D, 0D, 7D, 11D, 1D, 13D);
	private static final VoxelShape BASE_2_SOUTH = Block.makeCuboidShape(6D, 1D, 8D, 10D, 2D, 12D);
	private static final VoxelShape PIPE_1_SOUTH = Block.makeCuboidShape(7D, 7D, 12D, 9D, 9D, 16D);
	private static final VoxelShape PIPE_2_SOUTH = Block.makeCuboidShape(7D, 13D, 12D, 9D, 15D, 18D);

	private static final VoxelShape CORE_WEST = Block.makeCuboidShape(4D, 6D, 6D, 8D, 16D, 10D);
	private static final VoxelShape PILAR_WEST = Block.makeCuboidShape(5D, 2D, 7D, 7D, 6D, 9D);
	private static final VoxelShape BASE_1_WEST = Block.makeCuboidShape(3D, 0D, 5D, 9D, 1D, 11D);
	private static final VoxelShape BASE_2_WEST = Block.makeCuboidShape(4D, 1D, 6D, 8D, 2D, 10D);
	private static final VoxelShape PIPE_1_WEST = Block.makeCuboidShape(0D, 7D, 7D, 4D, 9D, 9D);
	private static final VoxelShape PIPE_2_WEST = Block.makeCuboidShape(-2D, 13D, 7D, 4D, 15D, 9D);

	private static final VoxelShape CORE_EAST = Block.makeCuboidShape(8D, 6D, 6D, 12D, 16D, 10D);
	private static final VoxelShape PILAR_EAST = Block.makeCuboidShape(9D, 2D, 7D, 11D, 6D, 9D);
	private static final VoxelShape BASE_1_EAST = Block.makeCuboidShape(7D, 0D, 5D, 13D, 1D, 11D);
	private static final VoxelShape BASE_2_EAST = Block.makeCuboidShape(8D, 1D, 6D, 12D, 2D, 10D);
	private static final VoxelShape PIPE_1_EAST = Block.makeCuboidShape(12D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape PIPE_2_EAST = Block.makeCuboidShape(12D, 13D, 7D, 18D, 15D, 9D);

	private static final VoxelShape SHAPE_NORTH = VoxelShapes.or(CORE_NORTH, PILAR_NORTH, BASE_1_NORTH, BASE_2_NORTH, PIPE_1_NORTH, PIPE_2_NORTH);
	private static final VoxelShape SHAPE_SOUTH = VoxelShapes.or(CORE_SOUTH, PILAR_SOUTH, BASE_1_SOUTH, BASE_2_SOUTH, PIPE_1_SOUTH, PIPE_2_SOUTH);
	private static final VoxelShape SHAPE_WEST = VoxelShapes.or(CORE_WEST, PILAR_WEST, BASE_1_WEST, BASE_2_WEST, PIPE_1_WEST, PIPE_2_WEST);
	private static final VoxelShape SHAPE_EAST = VoxelShapes.or(CORE_EAST, PILAR_EAST, BASE_1_EAST, BASE_2_EAST, PIPE_1_EAST, PIPE_2_EAST);

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(FACING)) {
		case SOUTH:
			return SHAPE_SOUTH;
		case WEST:
			return SHAPE_WEST;
		case EAST:
			return SHAPE_EAST;
		default:
			return SHAPE_NORTH;
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("enchantment.minecraft.silk_touch").mergeStyle(TextFormatting.BLUE));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

}
