package sirttas.elementalcraft.datagen.managed;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.data.predicate.block.pipe.HasPipeUpgrade;
import sirttas.elementalcraft.data.predicate.block.rune.HasRunePredicate;
import sirttas.elementalcraft.rune.Runes;
import sirttas.elementalcraft.tag.ECTags;

import java.util.concurrent.CompletableFuture;

public class RunesProvider extends AbstractManagedDataBuilderProvider<Rune, Rune.Builder> {


	private static final IBlockPosPredicate SPEED_PREDICATE = matchTagOrElementPump(ECTags.Blocks.RUNE_AFFECTED_SPEED).cache();
	private static final IBlockPosPredicate PRESERVATION_PREDICATE = matchTagOrElementPump(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION).cache();
	private static final IBlockPosPredicate RANGE_PREDICATE =  IBlockPosPredicate.match(ECTags.Blocks.RUNE_AFFECTED_RANGE)
			.or(new HasPipeUpgrade(PipeUpgradeTypes.ELEMENT_BEAM.get()))
			.cache();
	private static final IBlockPosPredicate OPTIMIZATION_PREDICATE = matchTagOrElementPump(ECTags.Blocks.RUNE_AFFECTED_OPTIMIZATION).and(IBlockPosPredicate.createOr(
			new HasRunePredicate(Runes.SOARYN),
			new HasRunePredicate(Runes.KAWORU),
			new HasRunePredicate(Runes.MEWTWO)
	).not()).cache();
	private static final IBlockPosPredicate LUCK_PREDICATE = createLuckPredicate(ECTags.Blocks.RUNE_AFFECTED_LUCK);
	private static final IBlockPosPredicate TZEENTCH_PREDICATE = createLuckPredicate(ECTags.Blocks.RUNE_AFFECTED_TZEENTCH);

	public RunesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries, ElementalCraftApi.RUNE_MANAGER, Rune.Builder.ENCODER);
	}

	private static IBlockPosPredicate matchTagOrElementPump(TagKey<Block> tag) {
		return IBlockPosPredicate.match(tag).or(new HasPipeUpgrade(PipeUpgradeTypes.ELEMENT_PUMP.get()));
	}

	private static IBlockPosPredicate createLuckPredicate(TagKey<Block> tag) {
		return IBlockPosPredicate.match(tag).and(IBlockPosPredicate.createOr(
				new HasRunePredicate(Runes.CLAPTRAP),
				new HasRunePredicate(Runes.BOMBADIL),
				new HasRunePredicate(Runes.TZEENTCH)
		).not()).cache();
	}

	@Override
	protected void collectBuilders(HolderLookup.Provider registries) {
		generateSpeedRunes();
		generatePreservationRunes();
		generateRangeRunes();
		generateOptimizationRunes();
		generateLuckRunes();

		builder(Runes.CREATIVE)
				.predicate(SPEED_PREDICATE)
				.addBonus(BonusType.SPEED, 1000000F)
				.addBonus(BonusType.ELEMENT_PRESERVATION, 1000000F)
				.max(1);
	}

	private void generateSpeedRunes() {
		builder(Runes.WII)
				.predicate(SPEED_PREDICATE)
				.addBonus(BonusType.SPEED, 0.1F)
				.addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
		builder(Runes.FUS)
				.predicate(SPEED_PREDICATE)
				.addBonus(BonusType.SPEED, 0.3F)
				.addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
		builder(Runes.ZOD)
				.predicate(SPEED_PREDICATE)
				.addBonus(BonusType.SPEED, 0.5F)
				.addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
	}

	private void generatePreservationRunes() {
		builder(Runes.MANX)
				.predicate(PRESERVATION_PREDICATE)
				.addBonus(BonusType.ELEMENT_PRESERVATION, 0.05F)
				.addBonus(BonusType.SPEED, -0.1F);
		builder(Runes.JITA)
				.predicate(PRESERVATION_PREDICATE)
				.addBonus(BonusType.ELEMENT_PRESERVATION, 0.1F)
				.addBonus(BonusType.SPEED, -0.1F);
		builder(Runes.TANO)
				.predicate(PRESERVATION_PREDICATE)
				.addBonus(BonusType.ELEMENT_PRESERVATION, 0.15F)
				.addBonus(BonusType.SPEED, -0.1F);
	}

	private void generateRangeRunes() {
		builder(Runes.KIRBY)
				.predicate(RANGE_PREDICATE)
				.addBonus(BonusType.RANGE, 0.05F)
				.addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
		builder(Runes.WHALE)
				.predicate(RANGE_PREDICATE)
				.addBonus(BonusType.RANGE, 0.1F)
				.addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
		builder(Runes.TYRIA)
				.predicate(RANGE_PREDICATE)
				.addBonus(BonusType.RANGE, 0.15F)
				.addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
	}

	private void generateOptimizationRunes() {
		builder(Runes.SOARYN)
				.predicate(OPTIMIZATION_PREDICATE)
				.addBonus(BonusType.ELEMENT_PRESERVATION, 0.03F)
				.addBonus(BonusType.SPEED, 0.05F).max(1);
		builder(Runes.KAWORU)
				.predicate(OPTIMIZATION_PREDICATE)
				.addBonus(BonusType.ELEMENT_PRESERVATION, 0.05F)
				.addBonus(BonusType.SPEED, 0.1F)
				.max(1);
		builder(Runes.MEWTWO)
				.predicate(OPTIMIZATION_PREDICATE)
				.addBonus(BonusType.ELEMENT_PRESERVATION, 0.1F)
				.addBonus(BonusType.SPEED, 0.3F)
				.max(1);
	}

	private void generateLuckRunes() {
		builder(Runes.CLAPTRAP)
				.predicate(LUCK_PREDICATE)
				.addBonus(BonusType.LUCK, 1)
				.addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F)
				.max(1);
		builder(Runes.BOMBADIL)
				.predicate(LUCK_PREDICATE)
				.addBonus(BonusType.LUCK, 2)
				.addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F)
				.max(1);
		builder(Runes.TZEENTCH)
				.predicate(TZEENTCH_PREDICATE)
				.addBonus(BonusType.LUCK, 3)
				.addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F)
				.max(1);
	}

	private Rune.Builder builder(ResourceKey<Rune> key) {
		var name = key.identifier().getPath();
		var builder = Rune.Builder.create();

		add(ElementalCraftApi.createRL(name), builder);
		return builder;
	}

	@Override
	public String getName() {
		return "ElementalCraft Runes";
	}
}
