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
import net.neoforged.neoforge.common.Tags;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.loot.LootHelper;
import sirttas.elementalcraft.rune.Runes;
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
	public AABB getRange() {
		var box = super.getRange();

		if (this.hasUpgrade(ShrineUpgrades.CRYSTAL_HARVEST)) {
			return box;
		}

		var minY = level.getMinBuildHeight();
		var maxY = this.getTargetPos().getY();

		return new AABB(box.minX, minY, box.minZ, box.maxX, maxY, box.maxZ);
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
		int fortune = getFortuneLevel(shrine);

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

	private static int getFortuneLevel(AbstractShrineBlockEntity shrine) {
		var fortune = shrine.getUpgradeCount(ShrineUpgrades.FORTUNE) * ECBlocks.FORTUNE_SHRINE_UPGRADE.get().getFortuneLevel();

		var direction = shrine.getUpgradeDirection(ShrineUpgrades.GREATER_FORTUNE);

		if (direction != null) {
			fortune += ECBlocks.GREATER_FORTUNE_SHRINE_UPGRADE.get().getFortuneLevel();

			var runeHandler = shrine.getLevel().getCapability(ElementalCraftCapabilities.RuneHandler.BLOCK, shrine.getBlockPos().relative(direction), null);

			if (runeHandler != null && runeHandler.getRuneCount(Runes.TZEENTCH) > 0) {
				fortune++;
			}
		}
		return fortune;

	}
}
