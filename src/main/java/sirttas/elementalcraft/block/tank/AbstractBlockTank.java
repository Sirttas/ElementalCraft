package sirttas.elementalcraft.block.tank;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.block.BlockECTileProvider;
import sirttas.elementalcraft.particle.ParticleHelper;

public abstract class AbstractBlockTank extends BlockECTileProvider {

	public AbstractBlockTank() {
		super(Block.Properties.create(Material.GLASS).hardnessAndResistance(2).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(1).notSolid());
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 1.0F;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
		TileTank tank = (TileTank) world.getTileEntity(pos);
	
		if (tank != null && tank.getElementAmount() > 0 && tank.getElementType() != ElementType.NONE) {
			ParticleHelper.createSourceParticle(tank.getElementType(), world, new Vec3d(pos).add(0, 0.2D, 0), rand);
		}
	}

}