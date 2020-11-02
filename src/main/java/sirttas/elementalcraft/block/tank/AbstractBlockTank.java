package sirttas.elementalcraft.block.tank;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.BlockECTileProvider;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.nbt.ECNames;
import sirttas.elementalcraft.particle.ParticleHelper;

public abstract class AbstractBlockTank extends BlockECTileProvider {

	public AbstractBlockTank() {
		super(Block.Properties.create(Material.GLASS).hardnessAndResistance(2).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).notSolid());
	}

	@Override
	@Deprecated
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
		return TileEntityHelper.getTileEntityAs(world, pos, TileTank.class).map(tank -> tank.getElementAmount() * 15 / tank.getMaxElement()).orElse(0);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 1.0F;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
		TileEntityHelper.getTileEntityAs(world, pos, ITank.class).filter(t -> t.getElementAmount() > 0 && t.getElementType() != ElementType.NONE)
				.ifPresent(t -> ParticleHelper.createSourceParticle(t.getElementType(), world, new Vec3d(pos).add(0, 0.2D, 0), rand));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		CompoundNBT tag = stack.getTag();

		if (tag != null && tag.contains(ECNames.BLOCK_ENTITY_TAG)) {
			CompoundNBT blockNbt = tag.getCompound(ECNames.BLOCK_ENTITY_TAG);
			ElementType elementType = ElementType.byName(blockNbt.getString(ECNames.ELEMENT_TYPE));
			int amount = blockNbt.getInt(ECNames.ELEMENT_AMOUNT);

			if (elementType != ElementType.NONE && amount > 0) {
				tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.contains", elementType.getDisplayName()).applyTextStyle(TextFormatting.GREEN));
				tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.percent_full", ItemStack.DECIMALFORMAT.format(amount * 100 / blockNbt.getInt(ECNames.ELEMENT_MAX)))
						.applyTextStyle(TextFormatting.GREEN));
			}

		}
	}

}