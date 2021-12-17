package sirttas.elementalcraft.datagen.managed;

import java.io.IOException;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import sirttas.dpanvil.api.data.AbstractManagedDataProvider;
import sirttas.dpanvil.api.data.IDataWrapper;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.block.shrine.upgrade.directional.CapacityShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.EfficiencyShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.OptimizationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.RangeShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.SpringalineShrineUpgradeBlock;
import sirttas.elementalcraft.data.predicate.block.shrine.HasShrineUpgradePredicate;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class ShrineUpgradeProvider extends AbstractManagedDataProvider<ShrineUpgrade> {

	private static final IBlockPosPredicate MINING_PREDICATE = IBlockPosPredicate.match(ECBlocks.ORE_SHRINE)
			.or(IBlockPosPredicate.match(ECBlocks.BUDDING_SHRINE).and(new HasShrineUpgradePredicate(ShrineUpgrades.CRYSTAL_HARVEST)));
	
	public ShrineUpgradeProvider(DataGenerator generator) {
		super(generator, ElementalCraft.SHRINE_UPGRADE_MANAGER);
	}

	@Override
	public void run(@Nonnull HashCache cache) throws IOException {
        save(cache, ShrineUpgrade.Builder.create().predicate(MINING_PREDICATE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 2).incompatibleWith(ShrineUpgrades.FORTUNE), ShrineUpgrades.SILK_TOUCH);
        save(cache, ShrineUpgrade.Builder.create().predicate(MINING_PREDICATE).max(3).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.3F).incompatibleWith(ShrineUpgrades.SILK_TOUCH),
                ShrineUpgrades.FORTUNE);
        save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.HARVEST_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 3F), ShrineUpgrades.PLANTING);
        save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.GROWTH_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 5F).addBonus(BonusType.SPEED, 4F)
                .incompatibleWith(ShrineUpgrades.STEM_POLLINATION), ShrineUpgrades.BONELESS_GROWTH);
        save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.VACUUM_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 500F).addBonus(BonusType.SPEED, 20F).addBonus(BonusType.RANGE, 0.5F),
                ShrineUpgrades.PICKUP);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.SWEET_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 5F).addBonus(BonusType.SPEED, 2F)
				.incompatibleWith(ShrineUpgrades.STRENGTH), ShrineUpgrades.NECTAR);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.GROVE_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 2F).addBonus(BonusType.SPEED, 2F), ShrineUpgrades.MYSTICAL_GROVE);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.GROWTH_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 4F).addBonus(BonusType.SPEED, 3F).addBonus(BonusType.RANGE, 0.5F)
				.incompatibleWith(ShrineUpgrades.BONELESS_GROWTH), ShrineUpgrades.STEM_POLLINATION);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.SPRING_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 3F).addBonus(BonusType.SPEED, 2F), ShrineUpgrades.FILLING);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.BUDDING_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 2F).addBonus(BonusType.SPEED, 2F).addBonus(BonusType.CAPACITY, 2F),
				SpringalineShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.BUDDING_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.5F), ShrineUpgrades.CRYSTAL_HARVEST);
		
		save(cache, ShrineUpgrade.Builder.create().predicate(IBlockPosPredicate.match(ECTags.Blocks.SHRINES_UPGRADABLES_ACCELERATION).or(IBlockPosPredicate.match(ECBlocks.VACUUM_SHRINE)
				.and(new HasShrineUpgradePredicate(ShrineUpgrades.PICKUP)))).addBonus(BonusType.SPEED, 0.5F), AccelerationShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES_UPGRADABLES_RANGE).addBonus(BonusType.RANGE, 1.5F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.2F).addBonus(BonusType.SPEED,
				1.2F), RangeShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES_UPGRADABLES_PROTECTION).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 3F), ShrineUpgrades.PROTECTION);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES).max(1).addBonus(BonusType.CAPACITY, 5F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.1F).addBonus(BonusType.SPEED, 1.1F),
				CapacityShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES).addBonus(BonusType.CAPACITY, 0.9F).addBonus(BonusType.ELEMENT_CONSUMPTION, 0.5F), EfficiencyShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().predicate(IBlockPosPredicate.match(ECTags.Blocks.SHRINES_UPGRADABLES_STRENGTH).or(IBlockPosPredicate.match(ECBlocks.SPRING_SHRINE)
				.and(new HasShrineUpgradePredicate(ShrineUpgrades.FILLING)))).addBonus(BonusType.STRENGTH, 2F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.2F)
				.incompatibleWith(ShrineUpgrades.NECTAR), ShrineUpgrades.STRENGTH);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES).max(2).addBonus(BonusType.CAPACITY, 1.25F).addBonus(BonusType.ELEMENT_CONSUMPTION, 0.75F).addBonus(BonusType.SPEED, 0.8F),
				OptimizationShrineUpgradeBlock.NAME);
	}

	protected void save(HashCache cache, ShrineUpgrade.Builder builder, String name) throws IOException {
		save(cache, builder.toJson(), ElementalCraft.createRL(name));
	}
	
	protected void save(HashCache cache, ShrineUpgrade.Builder builder, IDataWrapper<ShrineUpgrade> wrapper) throws IOException {
		save(cache, builder.toJson(), wrapper);
	}
	
	@Nonnull
    @Override
	public String getName() {
		return "ElementalCraft Shrines Upgrades";
	}
}
