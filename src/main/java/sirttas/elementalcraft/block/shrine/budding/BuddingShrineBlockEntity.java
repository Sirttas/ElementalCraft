package sirttas.elementalcraft.block.shrine.budding;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.budding.BuddingShrineBudType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.ore.OreShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import java.util.List;

public class BuddingShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(BuddingShrineBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	protected static final List<Direction> UPGRADE_DIRECTIONS = List.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

    private BuddingShrineBudType budType = BuddingShrineBudType.AMETHYST;

	public BuddingShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.BUDDING_SHRINE, PROPERTIES, pos, state);
	}

    public BuddingShrineBudType getBudType() {
        return budType;
    }

	private BlockPos above() {
		return this.getTargetPos().above();
	}

    @Override
    public void refresh() {
        super.refresh();
        budType = ElementalCraftApi.BUD_TYPE_MANAGER.getData().values().stream()
                .filter(b -> this.hasUpgrade(b.requiredUpgrade()))
                .findFirst()
                .orElse(BuddingShrineBudType.AMETHYST);
    }

    @Override
	protected boolean doPeriod() {
		var state = this.level.getBlockState(above());

		if (BuddingAmethystBlock.canClusterGrowAtState(state)) {
			setBud(budType.sequence().getFirst(), state);
			return true;
		}
		var it = budType.sequence().iterator();
		while (it.hasNext()) {
			if (state.is(it.next())) {
				if (it.hasNext()) {
					setBud(it.next(), state);
					return true;
				} else if (this.level instanceof ServerLevel serverLevel && this.hasUpgrade(ShrineUpgrades.CRYSTAL_HARVEST)) {
					var above = worldPosition.above();
					
					OreShrineBlockEntity.harvest(serverLevel, above, this, Blocks.AIR.defaultBlockState());
					return true;
				}
			}
		}
		return false;
	}

	private void setBud(Block block, BlockState state) {
		this.level.setBlockAndUpdate(above(), block.defaultBlockState().setValue(AmethystClusterBlock.FACING, Direction.UP).setValue(AmethystClusterBlock.WATERLOGGED,
				state.getFluidState().getType() == Fluids.WATER));
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRADE_DIRECTIONS;
	}
}
