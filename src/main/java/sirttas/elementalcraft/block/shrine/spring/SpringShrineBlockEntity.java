package sirttas.elementalcraft.block.shrine.spring;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;

public class SpringShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SpringShrineBlock.NAME) public static final TileEntityType<SpringShrineBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.WATER).periode(ECConfig.COMMON.springShrinePeriode.get()).consumeAmount(ECConfig.COMMON.springShrineConsumeAmount.get());

	public SpringShrineBlockEntity() {
		super(TYPE, PROPERTIES);
	}

	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		return new AxisAlignedBB(this.worldPosition.above());
	}

	@Override
	protected boolean doTick() {
		if (this.hasUpgrade(ShrineUpgrades.FILLING)) {
			return BlockEntityHelper.getTileEntity(level, worldPosition.above(2))
					.flatMap(entity -> entity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).resolve())
					.map(fluid -> fluid.fill(new FluidStack(Fluids.WATER, (int) Math.round(this.getMultiplier(BonusType.STRENGTH) * ECConfig.COMMON.springShrinefilling.get())), FluidAction.EXECUTE) > 0)
					.orElse(false);
		}
		return ((BucketItem) Items.WATER_BUCKET).emptyBucket(null, level, worldPosition.above(), null);
	}
}
