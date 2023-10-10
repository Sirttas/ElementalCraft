package sirttas.elementalcraft.block.shrine.spring;


import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

public class SpringShrineBlockEntity extends AbstractShrineBlockEntity {


	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(SpringShrineBlock.NAME);

	public SpringShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.SPAWNING_SHRINE, pos, state, PROPERTIES_KEY);
	}

	@Override
	public AABB getRange() {
		return new AABB(this.getTargetPos().above());
	}

	@Override
	protected boolean doPeriod() {
		if (this.hasUpgrade(ShrineUpgrades.FILLING)) {
			return BlockEntityHelper.getBlockEntity(level, worldPosition.above(2))
					.flatMap(entity -> entity.getCapability(ForgeCapabilities.FLUID_HANDLER).resolve())
					.map(fluid -> fluid.fill(new FluidStack(Fluids.WATER, (int) Math.round(this.getStrength())), FluidAction.EXECUTE) > 0)
					.orElse(false);
		}
		return ((BucketItem) Items.WATER_BUCKET).emptyContents(null, level, worldPosition.above(), null);
	}
}
