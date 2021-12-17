package sirttas.elementalcraft.block.shrine.spring;


import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
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

	@ObjectHolder(ElementalCraftApi.MODID + ":" + SpringShrineBlock.NAME) public static final BlockEntityType<SpringShrineBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.WATER)
			.period(ECConfig.COMMON.springShrinePeriod.get())
			.consumeAmount(ECConfig.COMMON.springShrineConsumeAmount.get());

	public SpringShrineBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, PROPERTIES);
	}

	@Override
	public AABB getRangeBoundingBox() {
		return new AABB(this.worldPosition.above());
	}

	@Override
	protected boolean doPeriod() {
		if (this.hasUpgrade(ShrineUpgrades.FILLING)) {
			return BlockEntityHelper.getBlockEntity(level, worldPosition.above(2))
					.flatMap(entity -> entity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).resolve())
					.map(fluid -> fluid.fill(new FluidStack(Fluids.WATER, (int) Math.round(this.getMultiplier(BonusType.STRENGTH) * ECConfig.COMMON.springShrineFilling.get())), FluidAction.EXECUTE) > 0)
					.orElse(false);
		}
		return ((BucketItem) Items.WATER_BUCKET).emptyContents(null, level, worldPosition.above(), null);
	}
}
