package sirttas.elementalcraft.block.shrine.upgrade.unidirectional;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;

public class MysticalGroveShrineUpgradeBlock extends AbstractShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_mystical_grove";

	private static final VoxelShape BASE = Block.box(3D, 2D, 3D, 13D, 7D, 3D);
	private static final VoxelShape CONNECTOR = Block.box(7D, 0D, 7D, 9D, 2D, 9D);
	private static final VoxelShape PIPE_1 = Block.box(2D, 0D, 3D, 4D, 9D, 4D);
	private static final VoxelShape PIPE_2 = Block.box(12D, 0D, 2D, 14D, 9D, 4D);
	private static final VoxelShape PIPE_3 = Block.box(2D, 0D, 12D, 4D, 9D, 14D);
	private static final VoxelShape PIPE_4 = Block.box(12D, 0D, 12D, 14D, 9D, 14D);

	private static final VoxelShape SHAPE = Shapes.or(BASE, CONNECTOR, PIPE_1, PIPE_2, PIPE_3, PIPE_4);

	public MysticalGroveShrineUpgradeBlock() {
		super(ElementalCraft.createRL(NAME));
	}
	
	@Override
	public Direction getFacing(BlockState state) {
		return Direction.DOWN;
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(new TranslatableComponent("tooltip.elementalcraft.mystical_grove").withStyle(ChatFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
}
