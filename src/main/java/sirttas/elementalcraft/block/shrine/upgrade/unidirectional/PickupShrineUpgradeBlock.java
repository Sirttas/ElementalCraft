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
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;

public class PickupShrineUpgradeBlock extends AbstractShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_pickup";

	private static final VoxelShape BASE = Block.box(6D, -1D, 6D, 10D, 10D, 10D);
	private static final VoxelShape PIPE_1 = Block.box(7D, 7D, 3D, 9D, 9D, 6D);
	private static final VoxelShape PIPE_2 = Block.box(7D, 7D, 10D, 9D, 9D, 13D);
	private static final VoxelShape PIPE_3 = Block.box(3D, 7D, 7D, 6D, 9D, 9D);
	private static final VoxelShape PIPE_4 = Block.box(10D, 7D, 7D, 13D, 9D, 9D);

	private static final VoxelShape SHAPE = VoxelShapes.or(BASE, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

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
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.pickup").withStyle(TextFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
}
