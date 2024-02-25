package sirttas.elementalcraft.block.shrine.lava;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;
import java.util.Optional;

public class LavaShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(LavaShrineBlock.NAME);

	protected static final List<Direction> UPGRADE_DIRECTIONS = List.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	public LavaShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.LAVA_SHRINE, pos, state, PROPERTIES_KEY);
	}

	private Optional<BlockPos> findRock() {
		return getBlocksInRange()
				.filter(p -> level.getBlockState(p).is(ECTags.Blocks.SHRINES_LAVA_LIQUIFIABLES))
				.findAny();
	}

	public static boolean fill(AbstractShrineBlockEntity shrine, Direction fillingDirection, Fluid fluid) {
		var fluidHandler = shrine.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, shrine.getBlockPos().relative(fillingDirection, 2), fillingDirection.getOpposite());

		return fluidHandler != null && fluidHandler.fill(new FluidStack(fluid, (int) Math.round(shrine.getStrength())), IFluidHandler.FluidAction.EXECUTE) > 0;
	}

	@Override
	protected boolean doPeriod() {
		return findRock()
				.map(p -> melt(p, Fluids.LAVA))
				.orElse(false);
	}

	private boolean melt(BlockPos p, Fluid fluid) {
		var fillingDirection = getUpgradeDirection(ShrineUpgrades.FILLING);

		if (fillingDirection != null && fill(this, fillingDirection, fluid)) {
			level.destroyBlock(p, true);
			return true;
		}
		level.setBlock(p, fluid.defaultFluidState().createLegacyBlock(), 11);
		level.levelEvent(LevelEvent.LAVA_FIZZ, p, 0);
		return true;
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRADE_DIRECTIONS;
	}
}
