package sirttas.elementalcraft.block.shrine.upgrade.unidirectional;

import java.util.List;

import javax.annotation.Nonnull;
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

public class BonelessGrowthShrineUpgradeBlock extends AbstractShrineUpgradeBlock {

	public static final String NAME = "shrine_upgrade_boneless_growth";

	private static final VoxelShape BASE_1 = Block.box(3D, 3D, 3D, 13D, 5D, 13D);
	private static final VoxelShape BASE_2 = Block.box(5D, 5D, 5D, 11D, 8D, 11D);
	private static final VoxelShape PIPE_1 = Block.box(7D, 0D, 7D, 9D, 3D, 9D);
	private static final VoxelShape PIPE_2 = Block.box(7D, -1D, 4D, 9D, 3D, 6D);
	private static final VoxelShape PIPE_3 = Block.box(7D, -1D, 10D, 9D, 3D, 12D);
	private static final VoxelShape PIPE_4 = Block.box(4D, -1D, 7D, 6D, 3D, 9D);
	private static final VoxelShape PIPE_5 = Block.box(10D, -1D, 7D, 12D, 3D, 9D);

	private static final VoxelShape SHAPE = Shapes.or(BASE_1, BASE_2, PIPE_1, PIPE_2, PIPE_3, PIPE_4, PIPE_5);

	public BonelessGrowthShrineUpgradeBlock() {
		super(ElementalCraft.createRL(NAME));
	}
	
	@Override
	public Direction getFacing(BlockState state) {
		return Direction.DOWN;
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(new TranslatableComponent("tooltip.elementalcraft.boneless_growth").withStyle(ChatFormatting.BLUE));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
}
