package sirttas.elementalcraft.block.shrine.overload;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractTileShrine;
import sirttas.elementalcraft.block.tile.TileEntityHelper;
import sirttas.elementalcraft.config.ECConfig;

public class TileOverloadShrine extends AbstractTileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOverloadShrine.NAME) public static final TileEntityType<TileOverloadShrine> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.AIR).periode(ECConfig.COMMON.overloadShrinePeriode.get())
			.consumeAmount(ECConfig.COMMON.overloadShrineConsumeAmount.get());

	public TileOverloadShrine() {
		super(TYPE, PROPERTIES);
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
	protected boolean doTick() {
		return getTarget().map(t -> {
			t.tick();
			return true;
		}).orElse(false);
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return DEFAULT_UPGRRADE_DIRECTIONS.stream().filter(direction -> direction != this.getBlockState().get(BlockOverloadShrine.FACING)).collect(Collectors.toList());
	}
}
