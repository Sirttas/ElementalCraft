package sirttas.elementalcraft.datagen.managed;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import sirttas.elementalcraft.datagen.ECItemModelProvider;
import sirttas.elementalcraft.rune.Runes;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class RunesProvider extends AbstractManagedDataBuilderProvider<Rune, Rune.Builder> {

	private final ECItemModelProvider itemModelProvider;

	private static final IBlockPosPredicate SPEED_PREDICATE = matchTagOrElementPump(ECTags.Blocks.RUNE_AFFECTED_SPEED).cache();
	private static final IBlockPosPredicate PRESERVATION_PREDICATE = matchTagOrElementPump(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION).cache();
	private static final IBlockPosPredicate OPTIMIZATION_PREDICATE = matchTagOrElementPump(ECTags.Blocks.RUNE_AFFECTED_OPTIMIZATION).and(IBlockPosPredicate.createOr(
			new HasRunePredicate(Runes.SOARYN),
			new HasRunePredicate(Runes.KAWORU),
			new HasRunePredicate(Runes.MEWTWO)
	).not()).cache();
	private static final IBlockPosPredicate LUCK_PREDICATE = createLuckPredicate(ECTags.Blocks.RUNE_AFFECTED_LUCK);
	private static final IBlockPosPredicate TZEENTCH_PREDICATE = createLuckPredicate(ECTags.Blocks.RUNE_AFFECTED_TZEENTCH);

	public static final ResourceLocation MINOR_SLATE = ElementalCraftApi.createRL("item/minor_rune_slate");
	public static final ResourceLocation SLATE = ElementalCraftApi.createRL("item/rune_slate");
	public static final ResourceLocation MAJOR_SLATE = ElementalCraftApi.createRL("item/major_rune_slate");

	private static IBlockPosPredicate createLuckPredicate(TagKey<Block> tag) {
		return IBlockPosPredicate.match(tag).and(IBlockPosPredicate.createOr(
				new HasRunePredicate(Runes.CLAPTRAP),
				new HasRunePredicate(Runes.BOMBADIL),
				new HasRunePredicate(Runes.TZEENTCH)
		).not()).cache();
	}

	public RunesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries, ECItemModelProvider itemModelProvider) {
		super(packOutput, registries, ElementalCraftApi.RUNE_MANAGER, Rune.Builder.ENCODER);
		this.itemModelProvider = itemModelProvider;
	}

	@Override
	protected void collectBuilders(HolderLookup.Provider registries) {
		builder(Runes.WII, MINOR_SLATE).predicate(SPEED_PREDICATE).addBonus(BonusType.SPEED, 0.1F).addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
		builder(Runes.FUS, SLATE).predicate(SPEED_PREDICATE).addBonus(BonusType.SPEED, 0.3F).addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
		builder(Runes.ZOD, MAJOR_SLATE).predicate(SPEED_PREDICATE).addBonus(BonusType.SPEED, 0.5F).addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
		builder(Runes.MANX, MINOR_SLATE).predicate(PRESERVATION_PREDICATE).addBonus(BonusType.ELEMENT_PRESERVATION, 0.05F).addBonus(BonusType.SPEED, -0.1F);
		builder(Runes.JITA, SLATE).predicate(PRESERVATION_PREDICATE).addBonus(BonusType.ELEMENT_PRESERVATION, 0.1F).addBonus(BonusType.SPEED, -0.1F);
		builder(Runes.TANO, MAJOR_SLATE).predicate(PRESERVATION_PREDICATE).addBonus(BonusType.ELEMENT_PRESERVATION, 0.15F).addBonus(BonusType.SPEED, -0.1F);
		builder(Runes.SOARYN, MINOR_SLATE).predicate(OPTIMIZATION_PREDICATE).addBonus(BonusType.ELEMENT_PRESERVATION, 0.03F).addBonus(BonusType.SPEED, 0.05F).max(1);
		builder(Runes.KAWORU, SLATE).predicate(OPTIMIZATION_PREDICATE).addBonus(BonusType.ELEMENT_PRESERVATION, 0.05F).addBonus(BonusType.SPEED, 0.1F).max(1);
		builder(Runes.MEWTWO, MAJOR_SLATE).predicate(OPTIMIZATION_PREDICATE).addBonus(BonusType.ELEMENT_PRESERVATION, 0.1F).addBonus(BonusType.SPEED, 0.3F).max(1);
		builder(Runes.CLAPTRAP, MINOR_SLATE).predicate(LUCK_PREDICATE).addBonus(BonusType.LUCK, 1).addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F).max(1);
		builder(Runes.BOMBADIL, SLATE).predicate(LUCK_PREDICATE).addBonus(BonusType.LUCK, 2).addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F).max(1);
		builder(Runes.TZEENTCH, MAJOR_SLATE).predicate(TZEENTCH_PREDICATE).addBonus(BonusType.LUCK, 3).addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F).max(1);

		builder(Runes.CREATIVE, MAJOR_SLATE).predicate(SPEED_PREDICATE).addBonus(BonusType.SPEED, 1000000F).addBonus(BonusType.ELEMENT_PRESERVATION, 1000000F).max(1);
	}

	@Nonnull
	@Override
	public CompletableFuture<?> run(@Nonnull CachedOutput cache) {
		itemModelProvider.clear();
		return super.run(cache).thenCompose(v -> itemModelProvider.generateAll(cache));
	}

	private Rune.Builder builder(ResourceKey<Rune> key, ResourceLocation slate) {
		var name = key.location().getPath();
		var path = ElementalCraftApi.RUNE_MANAGER.getFolder() + '/' + name;
		var runeTexture = ElementalCraftApi.createRL(path);
		var builder = Rune.Builder.create().model(itemModelProvider.runeTexture(path, slate, runeTexture)).sprite(runeTexture);

		add(ElementalCraftApi.createRL(name), builder);
		return builder;
	}

	private static IBlockPosPredicate matchTagOrElementPump(TagKey<Block> tag) {
		return IBlockPosPredicate.match(tag).or(new HasPipeUpgrade(PipeUpgradeTypes.ELEMENT_PUMP.get()));
	}

	@Nonnull
	@Override
	public String getName() {
		return "ElementalCraft Runes";
	}
}
