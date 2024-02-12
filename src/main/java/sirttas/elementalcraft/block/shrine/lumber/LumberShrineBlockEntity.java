package sirttas.elementalcraft.block.shrine.lumber;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IPlantable;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.block.shrine.upgrade.vertical.PlantingShrineUpgradeBlock;
import sirttas.elementalcraft.loot.LootHelper;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;
import java.util.Optional;

public class LumberShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(LumberShrineBlock.NAME);

	public LumberShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.LUMBER_SHRINE, pos, state, PROPERTIES_KEY);
	}

	private Optional<BlockPos> findTreeBlock() {
		if (level == null) {
			return Optional.empty();
		}

		return getBlocksInRange()
				.filter(p -> level.getBlockState(p).is(ECTags.Blocks.TREE_PARTS))
				.findAny();
	}

	private void handlePlanting(BlockPos pos, List<ItemStack> loots) {
		if (this.hasUpgrade(ShrineUpgrades.PLANTING)) {
			var y = this.worldPosition.getY();

			loots.stream()
					.filter(stack -> stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof IPlantable)
					.findFirst()
					.ifPresent(seeds -> {
						var mutablePos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
						var hasPlanted = false;

						do {
							hasPlanted = PlantingShrineUpgradeBlock.plant(seeds, level, mutablePos);
							mutablePos.move(Direction.DOWN);
						} while (!hasPlanted && mutablePos.getY() >= y);

						if (hasPlanted) {
							seeds.shrink(1);
							if (seeds.isEmpty()) {
								loots.remove(seeds);
							}
						}
					});
		}
	}

	@Override
	protected boolean doPeriod() {
		if (level instanceof ServerLevel && !level.isClientSide) {
			return findTreeBlock().map(p -> {
				List<ItemStack> loots = LootHelper.getDrops((ServerLevel) level, p, hasUpgrade(ShrineUpgrades.SILK_TOUCH) ? new ItemStack(Items.SHEARS) : ItemStack.EMPTY);

				level.destroyBlock(p, false);
				handlePlanting(p, loots);
				loots.forEach(stack -> Block.popResource(level, p, stack));
				return true;
			}).orElse(false);
		}
		return false;
	}
}
