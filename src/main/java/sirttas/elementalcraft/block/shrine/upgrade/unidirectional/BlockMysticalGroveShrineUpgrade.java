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

public class BlockMysticalGroveShrineUpgrade extends AbstractBlockShrineUpgrade {

	public static final String NAME = "shrine_upgrade_mystical_grove";

	private static final VoxelShape BASE = Block.makeCuboidShape(3D, 2D, 3D, 13D, 7D, 3D);
	private static final VoxelShape CONNECTOR = Block.makeCuboidShape(7D, 0D, 7D, 9D, 2D, 9D);
	private static final VoxelShape PIPE_1 = Block.makeCuboidShape(2D, 0D, 3D, 4D, 9D, 4D);
	private static final VoxelShape PIPE_2 = Block.makeCuboidShape(12D, 0D, 2D, 14D, 9D, 4D);
	private static final VoxelShape PIPE_3 = Block.makeCuboidShape(2D, 0D, 12D, 4D, 9D, 14D);
	private static final VoxelShape PIPE_4 = Block.makeCuboidShape(12D, 0D, 12D, 14D, 9D, 14D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE, CONNECTOR, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

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
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.mystical_grove").mergeStyle(TextFormatting.BLUE));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
