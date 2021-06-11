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
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.config.ECConfig;

public class OverloadShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + OverloadShrineBlock.NAME) public static final TileEntityType<OverloadShrineBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.AIR).periode(ECConfig.COMMON.overloadShrinePeriode.get())
			.consumeAmount(ECConfig.COMMON.overloadShrineConsumeAmount.get());

	public OverloadShrineBlockEntity() {
		super(TYPE, PROPERTIES);
	}

	Optional<ITickableTileEntity> getTarget() {
		return BlockEntityHelper.getTileEntityAs(level, getTargetPos(), ITickableTileEntity.class);
	}

	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		return new AxisAlignedBB(getTargetPos());
	}

	private BlockPos getTargetPos() {
		return worldPosition.relative(this.getBlockState().getValue(OverloadShrineBlock.FACING));
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
		return DEFAULT_UPGRRADE_DIRECTIONS.stream().filter(direction -> direction != this.getBlockState().getValue(OverloadShrineBlock.FACING)).collect(Collectors.toList());
	}
}
