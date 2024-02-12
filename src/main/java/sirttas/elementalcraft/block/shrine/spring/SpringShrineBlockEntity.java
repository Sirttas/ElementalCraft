package sirttas.elementalcraft.block.shrine.spring;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
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
			var fluid = level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition.above(2), Direction.DOWN);

			return fluid != null && fluid.fill(new FluidStack(Fluids.WATER, (int) Math.round(this.getStrength())), FluidAction.EXECUTE) > 0;
		}
		return ((BucketItem) Items.WATER_BUCKET).emptyContents(null, level, worldPosition.above(), null);
	}
}
