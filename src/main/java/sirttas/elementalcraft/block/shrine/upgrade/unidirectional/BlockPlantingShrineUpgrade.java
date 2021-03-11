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

public class BlockPlantingShrineUpgrade extends AbstractBlockShrineUpgrade {

	public static final String NAME = "shrine_upgrade_planting";

	private static final VoxelShape BASE_1 = Block.makeCuboidShape(6D, 14D, 6D, 10D, 16D, 10D);
	private static final VoxelShape BASE_2 = Block.makeCuboidShape(2D, 12D, 2D, 14D, 14D, 14D);
	private static final VoxelShape PIPE_1 = Block.makeCuboidShape(13D, 11D, 1D, 15D, 18D, 3D);
	private static final VoxelShape PIPE_2 = Block.makeCuboidShape(13D, 11D, 13D, 15D, 18D, 15D);
	private static final VoxelShape PIPE_3 = Block.makeCuboidShape(1D, 11D, 13D, 3D, 18D, 15D);
	private static final VoxelShape PIPE_4 = Block.makeCuboidShape(1D, 11D, 1D, 3D, 18D, 3D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE_1, BASE_2, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	@Override
	public Direction getFacing(BlockState state) {
		return Direction.UP;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.planting").mergeStyle(TextFormatting.BLUE));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
