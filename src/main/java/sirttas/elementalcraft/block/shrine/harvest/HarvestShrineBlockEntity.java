package sirttas.elementalcraft.block.shrine.harvest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.loot.LootHelper;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;
import java.util.Optional;

public class HarvestShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(HarvestShrineBlock.NAME);

	protected static final List<Direction> UPGRADE_DIRECTIONS = List.of(Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	public HarvestShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.HARVEST_SHRINE, pos, state, PROPERTIES_KEY);
	}

	private Optional<BlockPos> findCrop() {
		return getBlocksInRange()
				.filter(this::canHarvest)
				.findAny();
	}

	private boolean canHarvest(BlockPos pos) {
		var state = level.getBlockState(pos);
		Block block = state.getBlock();

		return (block instanceof CropBlock cropBlock && cropBlock.isMaxAge(state)) || (state.is(ECTags.Blocks.SHRINES_HARVEST_HARVESTABLE_TALL_PLANTS) && level.getBlockState(pos.below()).is(ECTags.Blocks.SHRINES_HARVEST_HARVESTABLE_TALL_PLANTS));
	}

	private void handlePlanting(BlockPos pos, ItemLike provider, List<ItemStack> loots) {
		if (!this.hasUpgrade(ShrineUpgrades.PLANTING) || !(provider.asItem() instanceof BlockItem blockItem)) {
			return;
		}

		loots.stream()
				.filter(stack -> stack.getItem().equals(blockItem))
				.findFirst()
				.ifPresent(seeds -> {
					if (blockItem.place(new DirectionalPlaceContext(this.level, pos, Direction.DOWN, seeds, Direction.UP)).consumesAction()) {
						seeds.shrink(1);
						if (seeds.isEmpty()) {
							loots.remove(seeds);
						}
					}
				});
	}

	@Override
	public AABB getRangeBoundingBox() {
		var range = getRange();

		return ElementalCraftUtils.stitchAABB(new AABB(this.getBlockPos())
				.inflate(range, 0, range)
				.expandTowards(0, -2, 0)
				.move(0, -1, 0));
	}

	@Override
	protected boolean doPeriod() {
		if (level instanceof ServerLevel serverLevel && !level.isClientSide) {
			return findCrop().map(p -> {
				List<ItemStack> loots = LootHelper.getDrops(serverLevel, p);
				Block block = level.getBlockState(p).getBlock();

				level.destroyBlock(p, false);
				handlePlanting(p, block, loots);
				loots.forEach(stack -> Block.popResource(level, p, stack));
				return true;
			}).orElse(false);
		}
		return false;
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRADE_DIRECTIONS;
	}
}
