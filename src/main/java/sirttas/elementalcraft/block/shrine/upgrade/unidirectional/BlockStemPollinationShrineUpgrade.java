package sirttas.elementalcraft.block.shrine.upgrade.unidirectional;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
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
import sirttas.elementalcraft.block.shrine.upgrade.AbstractBlockShrineUpgrade;

public class BlockStemPollinationShrineUpgrade extends AbstractBlockShrineUpgrade {

	public static final String NAME = "shrine_upgrade_stem_pollination";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(3D, 3D, 3D, 13D, 5D, 13D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(5D, 5D, 5D, 11D, 8D, 11D);
	private static final VoxelShape BASE_3 = Block.makeCuboidShape(6D, 8D, 6D, 10D, 11D, 10D);
	private static final VoxelShape PIPE_1 = Block.makeCuboidShape(7D, 0D, 7D, 9D, 3D, 9D);
	private static final VoxelShape PIPE_2 = Block.makeCuboidShape(7D, -1D, 4D, 9D, 3D, 6D);
	private static final VoxelShape PIPE_3 = Block.makeCuboidShape(7D, -1D, 10D, 9D, 3D, 12D);
	private static final VoxelShape PIPE_4 = Block.makeCuboidShape(4D, -1D, 7D, 6D, 3D, 9D);
	private static final VoxelShape PIPE_5 = Block.makeCuboidShape(10D, -1D, 7D, 12D, 3D, 9D);
	private static final VoxelShape PIPE_NORTH = Block.makeCuboidShape(7D, 7D, 3D, 9D, 9D, 6D);
	private static final VoxelShape PIPE_SOUTH = Block.makeCuboidShape(7D, 7D, 10D, 9D, 9D, 13D);
	private static final VoxelShape PIPE_WEST = Block.makeCuboidShape(3D, 7D, 7D, 6D, 9D, 9D);
	private static final VoxelShape PIPE_EAST = Block.makeCuboidShape(10D, 7D, 7D, 13D, 9D, 9D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, BASE_3, PIPE_1, PIPE_2, PIPE_3, PIPE_4, PIPE_5, PIPE_NORTH, PIPE_SOUTH, PIPE_WEST, PIPE_EAST);

	@Override
	public Direction getFacing(BlockState state) {
		return Direction.DOWN;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.stem_pollination").mergeStyle(TextFormatting.BLUE));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
