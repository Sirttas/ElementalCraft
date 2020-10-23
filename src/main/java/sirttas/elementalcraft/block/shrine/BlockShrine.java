package sirttas.elementalcraft.block.shrine;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.BlockECTileProvider;
import sirttas.elementalcraft.block.tile.TileEntityHelper;

public abstract class BlockShrine extends BlockECTileProvider {

	private final ElementType elementType;

	public BlockShrine(ElementType elementType) {
		this.elementType = elementType;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final TileShrine shrine = (TileShrine) world.getTileEntity(pos);

		if (shrine != null && player.getHeldItem(hand).isEmpty() && player.isSneaking()) {
			if (world.isRemote) {
				shrine.startShowingRange();
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.consumes", elementType.getDisplayName()).applyTextStyle(TextFormatting.YELLOW));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		TileEntityHelper.getTileEntityAs(world, pos, TileShrine.class).filter(TileShrine::isRunning).ifPresent(s -> this.doAnimateTick(s, state, world, pos, rand));
	}

	@OnlyIn(Dist.CLIENT)
	protected void doAnimateTick(TileShrine shrine, BlockState state, World world, BlockPos pos, Random rand) {
		BoneMealItem.spawnBonemealParticles(world, pos, 1);
	}

}
