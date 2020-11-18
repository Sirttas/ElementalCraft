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
import sirttas.elementalcraft.block.shrine.upgrade.BlockShrineUpgrade;

public class BlockBonelessGrowthShrineUpgrade extends BlockShrineUpgrade {

	public static final String NAME = "shrine_upgrade_boneless_growth";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(3D, 3D, 3D, 13D, 5D, 13D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(5D, 5D, 5D, 11D, 8D, 11D);
	private static final VoxelShape PIPE_1 = Block.makeCuboidShape(7D, 0D, 7D, 9D, 3D, 9D);
	private static final VoxelShape PIPE_2 = Block.makeCuboidShape(7D, -1D, 4D, 9D, 3D, 6D);
	private static final VoxelShape PIPE_3 = Block.makeCuboidShape(7D, -1D, 10D, 9D, 3D, 12D);
	private static final VoxelShape PIPE_4 = Block.makeCuboidShape(4D, -1D, 7D, 6D, 3D, 9D);
	private static final VoxelShape PIPE_5 = Block.makeCuboidShape(10D, -1D, 7D, 12D, 3D, 9D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, PIPE_1, PIPE_2, PIPE_3, PIPE_4, PIPE_5);

	@Override
	public Direction getFacing(BlockState state) {
		return Direction.DOWN;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.boneless_growth").mergeStyle(TextFormatting.BLUE));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
