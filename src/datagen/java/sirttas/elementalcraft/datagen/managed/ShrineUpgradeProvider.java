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
import sirttas.elementalcraft.block.shrine.upgrade.directional.CapacityShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.EfficiencyShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.OptimizationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.RangeShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.StrengthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.AccelerationShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.CrystalHarvestShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.FortuneShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.NectarShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.ProtectionShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.SilkTouchShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.SpringalineShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BonelessGrowthShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.FillingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.MysticalGroveShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.PickupShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.PlantingShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.StemPollinationShrineUpgradeBlock;
import sirttas.elementalcraft.data.predicate.block.shrine.HasShrineUpgradePredicate;
import sirttas.elementalcraft.tag.ECTags;

public class ShrineUpgradeProvider extends AbstractManagedDataProvider<ShrineUpgrade> {

	private static final IBlockPosPredicate MINING_PREDICATE = IBlockPosPredicate.match(ECBlocks.ORE_SHRINE)
			.or(IBlockPosPredicate.match(ECBlocks.BUDDING_SHRINE).and(new HasShrineUpgradePredicate(ElementalCraft.createRL(CrystalHarvestShrineUpgradeBlock.NAME))));
	
	public ShrineUpgradeProvider(DataGenerator generator) {
		super(generator, ElementalCraft.SHRINE_UPGRADE_MANAGER);
	}

	@Override
	public void run(HashCache cache) throws IOException {
		save(cache, ShrineUpgrade.Builder.create().predicate(MINING_PREDICATE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 2)
				.incompatibleWith(ElementalCraft.createRL(FortuneShrineUpgradeBlock.NAME)), SilkTouchShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().predicate(MINING_PREDICATE).max(3).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.3F)
				.incompatibleWith(ElementalCraft.createRL(SilkTouchShrineUpgradeBlock.NAME)), FortuneShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.HARVEST_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 3F), PlantingShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.GROWTH_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 5F).addBonus(BonusType.SPEED, 4F)
				.incompatibleWith(ElementalCraft.createRL(StemPollinationShrineUpgradeBlock.NAME)), BonelessGrowthShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.VACUUM_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 500F).addBonus(BonusType.SPEED, 20F).addBonus(BonusType.RANGE,
				0.5F), PickupShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.SWEET_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 5F).addBonus(BonusType.SPEED, 2F)
				.incompatibleWith(ElementalCraft.createRL(StrengthShrineUpgradeBlock.NAME)), NectarShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.GROVE_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 2F).addBonus(BonusType.SPEED, 2F), MysticalGroveShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.GROWTH_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 4F).addBonus(BonusType.SPEED, 3F).addBonus(BonusType.RANGE, 0.5F)
				.incompatibleWith(ElementalCraft.createRL(BonelessGrowthShrineUpgradeBlock.NAME)), StemPollinationShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.SPRING_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 3F).addBonus(BonusType.SPEED, 2F), FillingShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.BUDDING_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 2F).addBonus(BonusType.SPEED, 2F).addBonus(BonusType.CAPACITY, 2F),
				SpringalineShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECBlocks.BUDDING_SHRINE).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.5F), CrystalHarvestShrineUpgradeBlock.NAME);
		
		save(cache, ShrineUpgrade.Builder.create().predicate(IBlockPosPredicate.match(ECTags.Blocks.SHRINES_UPGRADABLES_ACCELERATION).or(IBlockPosPredicate.match(ECBlocks.VACUUM_SHRINE)
				.and(new HasShrineUpgradePredicate(ElementalCraft.createRL(PickupShrineUpgradeBlock.NAME))))).addBonus(BonusType.SPEED, 0.5F), AccelerationShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES_UPGRADABLES_RANGE).addBonus(BonusType.RANGE, 1.5F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.2F).addBonus(BonusType.SPEED,
				1.2F), RangeShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES_UPGRADABLES_PROTECTION).max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 3F), ProtectionShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES).max(1).addBonus(BonusType.CAPACITY, 5F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.1F).addBonus(BonusType.SPEED, 1.1F),
				CapacityShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES).addBonus(BonusType.CAPACITY, 0.9F).addBonus(BonusType.ELEMENT_CONSUMPTION, 0.5F), EfficiencyShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().predicate(IBlockPosPredicate.match(ECTags.Blocks.SHRINES_UPGRADABLES_STRENGTH).or(IBlockPosPredicate.match(ECBlocks.SPRING_SHRINE)
				.and(new HasShrineUpgradePredicate(ElementalCraft.createRL(FillingShrineUpgradeBlock.NAME))))).addBonus(BonusType.STRENGTH, 2F).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.2F)
				.incompatibleWith(ElementalCraft.createRL(NectarShrineUpgradeBlock.NAME)), StrengthShrineUpgradeBlock.NAME);
		save(cache, ShrineUpgrade.Builder.create().match(ECTags.Blocks.SHRINES).max(2).addBonus(BonusType.CAPACITY, 1.25F).addBonus(BonusType.ELEMENT_CONSUMPTION, 0.75F).addBonus(BonusType.SPEED, 0.8F),
				OptimizationShrineUpgradeBlock.NAME);
	}

	protected void save(HashCache cache, ShrineUpgrade.Builder builder, String name) throws IOException {
		save(cache, builder.toJson(), ElementalCraft.createRL(name));
	}
	
	protected void save(HashCache cache, ShrineUpgrade.Builder builder, IDataWrapper<ShrineUpgrade> wrapper) throws IOException {
		save(cache, builder.toJson(), wrapper);
	}
	
	@Override
	public String getName() {
		return "ElementalCraft Shrines Upgrades";
	}
}
