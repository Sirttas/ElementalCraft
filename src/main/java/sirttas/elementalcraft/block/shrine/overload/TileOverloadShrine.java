package sirttas.elementalcraft.block.shrine.overload;

import java.util.Optional;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.config.ECConfig;

public class TileOverloadShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOverloadShrine.NAME) public static TileEntityType<TileOverloadShrine> TYPE;

	public TileOverloadShrine() {
		super(TYPE, ElementType.AIR, ECConfig.CONFIG.overloadShrinePeriode.get());
	}

	Optional<ITickableTileEntity> getTarget() {
		return TileEntityHelper.getTileEntityAs(world, getTargetPos(), ITickableTileEntity.class);
	}

	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		return new AxisAlignedBB(getTargetPos());
	}

	private BlockPos getTargetPos() {
		return pos.offset(this.getBlockState().get(BlockOverloadShrine.FACING));
	}

	@Override
	protected void doTick() {
		int consumeAmount = ECConfig.CONFIG.overloadShrineConsumeAmount.get();

		if (this.getElementAmount() >= consumeAmount) {
			getTarget().ifPresent(t -> {
				t.tick();
				this.consumeElement(consumeAmount);
			});
		}
	}
}
