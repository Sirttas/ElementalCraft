package sirttas.elementalcraft.block.instrument.firefurnace;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import sirttas.elementalcraft.block.BlockEC;
import sirttas.elementalcraft.block.BlockECContainer;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.inventory.ECInventoryHelper;
import sirttas.elementalcraft.particle.ParticleHelper;

public abstract class AbstractBlockFireFurnace extends BlockECContainer {

	public AbstractBlockFireFurnace(Properties properties) {
		super(properties);
	}

	public AbstractBlockFireFurnace() {
		super();
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		final AbstractTileFireFurnace<?> furnace = (AbstractTileFireFurnace<?>) world.getTileEntity(pos);
		IItemHandler inv = ECInventoryHelper.getItemHandlerAt(world, pos, null);
	
		if (furnace != null) {
			if (!inv.getStackInSlot(1).isEmpty()) {
				furnace.dropExperience(player);
				return this.onSlotActivated(inv, player, ItemStack.EMPTY, 1);
			}
			return this.onSlotActivated(inv, player, player.getHeldItem(hand), 0);
		}
		return ActionResultType.PASS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		double x = pos.getX() + (5 + rand.nextDouble() * 6) * BlockEC.BIT_SIZE;
		double y = pos.getY() + 6 * BlockEC.BIT_SIZE;
		double z = pos.getZ() + (5 + rand.nextDouble() * 6) * BlockEC.BIT_SIZE;
	
		TileEntityHelper.getTileEntityAs(world, pos, AbstractTileFireFurnace.class).filter(AbstractTileFireFurnace::isRunning).ifPresent(f -> {
			world.addParticle(ParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
			world.addParticle(ParticleTypes.SMOKE, x, y + 0.5D, z, 0.0D, 0.0D, 0.0D);
			ParticleHelper.createElementFlowParticle(f.getTankElementType(), world, Vector3d.copy(pos), Direction.UP, 1, rand);
		});
	}

}