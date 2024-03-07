package sirttas.elementalcraft.datagen.managed.shrine;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.dpanvil.api.predicate.block.direction.FacingBlockPredicate;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.data.predicate.block.shrine.HasShrineUpgradePredicate;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ShrineUpgradeProvider extends AbstractManagedDataBuilderProvider<ShrineUpgrade, ShrineUpgrade.Builder> {
	private static final IBlockPosPredicate ACCELERATION_PREDICATE = IBlockPosPredicate.match(ECTags.Blocks.SHRINES_UPGRADABLES_ACCELERATION)
			.or(IBlockPosPredicate.match(ECBlocks.VACUUM_SHRINE.get()).and(new HasShrineUpgradePredicate(ShrineUpgrades.PICKUP)))
			.cache();
	private static final IBlockPosPredicate PROTECTION_PREDICATE = IBlockPosPredicate.match(ECTags.Blocks.SHRINES_UPGRADABLES_PROTECTION)
			.or(IBlockPosPredicate.match(ECBlocks.VACUUM_SHRINE.get()).and(new HasShrineUpgradePredicate(ShrineUpgrades.VORTEX)))
			.cache();
	private static final IBlockPosPredicate STRENGTH_PREDICATE = IBlockPosPredicate.match(ECTags.Blocks.SHRINES_UPGRADABLES_STRENGTH)
			.or(IBlockPosPredicate.match(ECBlocks.SPRING_SHRINE.get(), ECBlocks.LAVA_SHRINE.get()).and(new HasShrineUpgradePredicate(ShrineUpgrades.FILLING)))
			.cache();
	private static final IBlockPosPredicate FILLING_PREDICATE = IBlockPosPredicate.match(ECBlocks.SPRING_SHRINE.get()).and(FacingBlockPredicate.DOWN)
			.or(IBlockPosPredicate.match(ECBlocks.LAVA_SHRINE.get()).and(FacingBlockPredicate.NORTH.or(FacingBlockPredicate.SOUTH).or(FacingBlockPredicate.WEST).or(FacingBlockPredicate.EAST)));

	private static final List<ResourceKey<ShrineUpgrade>> ADVANCED_UPGRADES = List.of(ShrineUpgrades.OVERCLOCKED_ACCELERATION, ShrineUpgrades.TRANSLOCATION, ShrineUpgrades.GREATER_FORTUNE, ShrineUpgrades.OVERWHELMING_STRENGTH);

	public ShrineUpgradeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries, ElementalCraft.SHRINE_UPGRADE_MANAGER, ShrineUpgrade.Builder.ENCODER);
	}

	@Override
	protected void collectBuilders(HolderLookup.Provider registries) {
		shrineUpgrade(ShrineUpgrades.SILK_TOUCH)
				.predicate(miningPredicate(ECTags.Blocks.SHRINES_UPGRADABLES_SILK_TOUCH))
				.max(1)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 2)
				.incompatibleWith(ShrineUpgrades.FORTUNE, ShrineUpgrades.GREATER_FORTUNE);
		shrineUpgrade(ShrineUpgrades.FORTUNE)
				.predicate(miningPredicate(ECTags.Blocks.SHRINES_UPGRADABLES_FORTUNE))
				.max(3)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 1.3F)
				.incompatibleWith(ShrineUpgrades.SILK_TOUCH, ShrineUpgrades.GREATER_FORTUNE);
		shrineUpgrade(ShrineUpgrades.PLANTING)
				.match(ECTags.Blocks.SHRINES_UPGRADABLES_PLANTING)
				.max(1)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 3F);
		shrineUpgrade(ShrineUpgrades.BONELESS_GROWTH)
				.match(ECBlocks.GROWTH_SHRINE.get())
				.max(1)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 1.5F)
				.addBonus(BonusType.SPEED, 10F)
				.addBonus(BonusType.CAPACITY, 1.5F)
				.incompatibleWith(ShrineUpgrades.STEM_POLLINATION, ShrineUpgrades.CRYSTAL_GROWTH);
        shrineUpgrade(ShrineUpgrades.PICKUP)
				.match(ECBlocks.VACUUM_SHRINE.get())
				.max(1)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 100F)
				.addBonus(BonusType.SPEED, 20F)
				.addBonus(BonusType.RANGE, 0.5F)
				.incompatibleWith(ShrineUpgrades.VORTEX, ShrineUpgrades.STRENGTH, ShrineUpgrades.OVERWHELMING_STRENGTH);
		shrineUpgrade(ShrineUpgrades.VORTEX)
				.match(ECBlocks.VACUUM_SHRINE.get())
				.max(1)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 2F)
				.addBonus(BonusType.RANGE, 0.75F)
				.incompatibleWith(ShrineUpgrades.PICKUP);
		shrineUpgrade(ShrineUpgrades.NECTAR)
				.match(ECBlocks.SWEET_SHRINE.get())
				.max(1)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 5F)
				.addBonus(BonusType.SPEED, 2F)
				.incompatibleWith(ShrineUpgrades.STRENGTH, ShrineUpgrades.OVERWHELMING_STRENGTH);
		shrineUpgrade(ShrineUpgrades.MYSTICAL_GROVE)
				.match(ECBlocks.GROVE_SHRINE.get())
				.max(1)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 2F)
				.addBonus(BonusType.SPEED, 2F);
		shrineUpgrade(ShrineUpgrades.STEM_POLLINATION)
				.match(ECBlocks.GROWTH_SHRINE.get())
				.max(1)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 4F)
				.addBonus(BonusType.SPEED, 3F)
				.addBonus(BonusType.RANGE, 0.5F)
				.incompatibleWith(ShrineUpgrades.BONELESS_GROWTH, ShrineUpgrades.CRYSTAL_GROWTH);
		shrineUpgrade(ShrineUpgrades.FILLING)
				.predicate(FILLING_PREDICATE)
				.max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 3F)
				.addBonus(BonusType.SPEED, 2F);
		shrineUpgrade(ShrineUpgrades.SPRINGALINE)
				.match(ECBlocks.BUDDING_SHRINE.get())
				.max(1)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 2F)
				.addBonus(BonusType.SPEED, 2F)
				.addBonus(BonusType.CAPACITY, 2F);
		shrineUpgrade(ShrineUpgrades.CRYSTAL_HARVEST)
				.match(ECTags.Blocks.SHRINES_UPGRADABLES_CRYSTAL_HARVEST)
				.max(1).addBonus(BonusType.ELEMENT_CONSUMPTION, 1.5F)
				.addBonus(BonusType.RANGE, 0.5F);
		shrineUpgrade(ShrineUpgrades.CRYSTAL_GROWTH)
				.match(ECBlocks.GROWTH_SHRINE.get())
				.max(1)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 50F)
				.addBonus(BonusType.SPEED, 30F)
				.incompatibleWith(ShrineUpgrades.BONELESS_GROWTH, ShrineUpgrades.STEM_POLLINATION);

		shrineUpgrade(ShrineUpgrades.ACCELERATION)
				.predicate(ACCELERATION_PREDICATE)
				.addBonus(BonusType.SPEED, 0.5F);
		shrineUpgrade(ShrineUpgrades.RANGE)
				.match(ECTags.Blocks.SHRINES_UPGRADABLES_RANGE)
				.addBonus(BonusType.RANGE, 1.5F)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 1.2F)
				.addBonus(BonusType.SPEED, 1.2F);
		shrineUpgrade(ShrineUpgrades.PROTECTION)
				.predicate(PROTECTION_PREDICATE)
				.max(1)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 3F);
		shrineUpgrade(ShrineUpgrades.CAPACITY)
				.match(ECTags.Blocks.SHRINES)
				.max(1)
				.addBonus(BonusType.CAPACITY, 5F)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 1.1F)
				.addBonus(BonusType.SPEED, 1.1F);
		shrineUpgrade(ShrineUpgrades.EFFICIENCY)
				.match(ECTags.Blocks.SHRINES)
				.addBonus(BonusType.CAPACITY, 0.9F)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 0.5F);
		shrineUpgrade(ShrineUpgrades.STRENGTH)
				.predicate(STRENGTH_PREDICATE)
				.addBonus(BonusType.STRENGTH, 2F)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 1.2F)
				.incompatibleWith(ShrineUpgrades.NECTAR, ShrineUpgrades.PICKUP);
		shrineUpgrade(ShrineUpgrades.OPTIMIZATION)
				.match(ECTags.Blocks.SHRINES)
				.max(2)
				.addBonus(BonusType.CAPACITY, 1.25F)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 0.75F)
				.addBonus(BonusType.SPEED, 0.8F);

		advancedShrineUpgrade(ShrineUpgrades.OVERCLOCKED_ACCELERATION).predicate(ACCELERATION_PREDICATE)
				.addBonus(BonusType.SPEED, 0.25F)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 1.5F);
		advancedShrineUpgrade(ShrineUpgrades.GREATER_FORTUNE)
				.predicate(miningPredicate(ECTags.Blocks.SHRINES_UPGRADABLES_FORTUNE))
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 2.5F)
				.incompatibleWith(ShrineUpgrades.SILK_TOUCH);
		advancedShrineUpgrade(ShrineUpgrades.TRANSLOCATION).match(ECTags.Blocks.SHRINES_UPGRADABLES_RANGE)
				.addBonus(BonusType.RANGE, 0.75F)
				.addBonus(BonusType.CAPACITY, 1.20F)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 2F);
		advancedShrineUpgrade(ShrineUpgrades.OVERWHELMING_STRENGTH)
				.predicate(STRENGTH_PREDICATE)
				.addBonus(BonusType.STRENGTH, 5F)
				.addBonus(BonusType.ELEMENT_CONSUMPTION, 2F)
				.incompatibleWith(ShrineUpgrades.NECTAR, ShrineUpgrades.PICKUP);
	}

	private IBlockPosPredicate miningPredicate(TagKey<Block> tag) {
		return IBlockPosPredicate.match(tag)
				.or(IBlockPosPredicate.match(ECBlocks.BUDDING_SHRINE.get()).and(new HasShrineUpgradePredicate(ShrineUpgrades.CRYSTAL_HARVEST)))
				.cache();
	}

	protected ShrineUpgrade.Builder advancedShrineUpgrade(ResourceKey<ShrineUpgrade> key) {
		return shrineUpgrade(key)
				.max(1)
				.incompatibleWith(ADVANCED_UPGRADES);
	}

	protected ShrineUpgrade.Builder shrineUpgrade(ResourceKey<ShrineUpgrade> key) {
		var builder = ShrineUpgrade.builder(key);
		
		add(key, builder);
		return builder;
	}

	@Nonnull
	@Override
	public String getName() {
		return "ElementalCraft Shrine Upgrades";
	}

}
