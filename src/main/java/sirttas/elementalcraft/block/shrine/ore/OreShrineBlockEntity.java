package sirttas.elementalcraft.block.shrine.ore;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.loot.LootHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nullable;
import java.util.Optional;

public class OreShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(OreShrineBlock.NAME);

	private boolean hasCrystalHarvest = false;

	public OreShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.ORE_SHRINE, pos, state, PROPERTIES_KEY);
	}

	private Optional<BlockPos> findOre() {
		return getBlocksInRange()
				.filter(p -> level.getBlockState(p).is(hasCrystalHarvest ? ECTags.Blocks.SHRINES_ORE_HARVESTABLE_CRYSTALS : Tags.Blocks.ORES))
				.findAny();
	}

	@Override
	public AABB getRangeBoundingBox() {
		if (this.hasUpgrade(ShrineUpgrades.CRYSTAL_HARVEST)) {
			return super.getRangeBoundingBox();
		}

		var range = getRange();
		var height = Math.abs(level.getMinBuildHeight() - worldPosition.getY());

		return ElementalCraftUtils.stitchAABB(new AABB(this.getBlockPos())
				.inflate(range, 0, range)
				.move(0, -1, 0)
				.expandTowards(0, 1D - height, 0));
	}


	@Override
	protected boolean doPeriod() {
		if (level instanceof ServerLevel serverLevel) {
			this.hasCrystalHarvest = this.hasUpgrade(ShrineUpgrades.CRYSTAL_HARVEST);

			return findOre().map(p -> {
				harvest(serverLevel, p, this, hasCrystalHarvest ? Blocks.AIR.defaultBlockState() : Blocks.STONE.defaultBlockState());
				return true;
			}).orElse(false);
		}
		return false;
	}

	public static void harvest(ServerLevel level, BlockPos pos, AbstractShrineBlockEntity shrine, @Nullable BlockState newState) {
		int fortune = shrine.getUpgradeCount(ShrineUpgrades.FORTUNE);

		if (fortune > 0) {
			ItemStack pickaxe = new ItemStack(Items.NETHERITE_PICKAXE);

			pickaxe.enchant(Enchantments.BLOCK_FORTUNE, fortune);
			LootHelper.getDrops(level, pos, pickaxe).forEach(s -> Block.popResource(level, shrine.getBlockPos().above(), s));
		} else {
			LootHelper.getDrops(level, pos, shrine.hasUpgrade(ShrineUpgrades.SILK_TOUCH)).forEach(s -> Block.popResource(level, shrine.getBlockPos().above(), s));
		}
		if (newState != null && !newState.isAir()) {
			level.setBlockAndUpdate(pos, newState);
		} else {
			level.destroyBlock(pos, false);
		}
	}
}
