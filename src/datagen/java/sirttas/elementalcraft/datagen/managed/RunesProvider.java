package sirttas.elementalcraft.datagen.managed;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.data.predicate.block.pipe.HasPipeUpgrade;
import sirttas.elementalcraft.data.predicate.block.rune.HasRunePredicate;
import sirttas.elementalcraft.datagen.ECItemModelProvider;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.io.IOException;

public class RunesProvider extends AbstractManagedDataBuilderProvider<Rune, Rune.Builder> {

	private final ECItemModelProvider itemModelProvider;

	private static final IBlockPosPredicate SPEED_PREDICATE = matchTagOrElementPump(ECTags.Blocks.RUNE_AFFECTED_SPEED).cache();
	private static final IBlockPosPredicate PRESERVATION_PREDICATE = matchTagOrElementPump(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION).cache();
	private static final IBlockPosPredicate OPTIMIZATION_PREDICATE = matchTagOrElementPump(ECTags.Blocks.RUNE_AFFECTED_OPTIMIZATION).and(IBlockPosPredicate.createOr(
			new HasRunePredicate(ElementalCraft.createRL("soaryn")),
			new HasRunePredicate(ElementalCraft.createRL("kaworu")),
			new HasRunePredicate(ElementalCraft.createRL("mewtwo"))).not()).cache();
	private static final IBlockPosPredicate LUCK_PREDICATE = IBlockPosPredicate.match(ECTags.Blocks.RUNE_AFFECTED_LUCK).and(IBlockPosPredicate.createOr(
			new HasRunePredicate(ElementalCraft.createRL("claptrap")),
			new HasRunePredicate(ElementalCraft.createRL("bombadil")),
			new HasRunePredicate(ElementalCraft.createRL("tzeentch"))).not()).cache();

	public static final ResourceLocation MINOR_SLATE = ElementalCraft.createRL("item/minor_rune_slate");
	public static final ResourceLocation SLATE = ElementalCraft.createRL("item/rune_slate");
	public static final ResourceLocation MAJOR_SLATE = ElementalCraft.createRL("item/major_rune_slate");

	public RunesProvider(DataGenerator generator, ECItemModelProvider itemModelProvider) {
		super(generator, ElementalCraftApi.RUNE_MANAGER, Rune.Builder.ENCODER);
		this.itemModelProvider = itemModelProvider;
	}

	@Override
	public void collectBuilders() {
		itemModelProvider.clear();
		builder("wii", MINOR_SLATE).predicate(SPEED_PREDICATE).addBonus(BonusType.SPEED, 0.1F).addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
		builder("fus", SLATE).predicate(SPEED_PREDICATE).addBonus(BonusType.SPEED, 0.3F).addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
		builder("zod", MAJOR_SLATE).predicate(SPEED_PREDICATE).addBonus(BonusType.SPEED, 0.5F).addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
		builder("manx", MINOR_SLATE).predicate(PRESERVATION_PREDICATE).addBonus(BonusType.ELEMENT_PRESERVATION, 0.05F).addBonus(BonusType.SPEED, -0.1F);
		builder("jita", SLATE).predicate(PRESERVATION_PREDICATE).addBonus(BonusType.ELEMENT_PRESERVATION, 0.1F).addBonus(BonusType.SPEED, -0.1F);
		builder("tano", MAJOR_SLATE).predicate(PRESERVATION_PREDICATE).addBonus(BonusType.ELEMENT_PRESERVATION, 0.15F).addBonus(BonusType.SPEED, -0.1F);
		builder("soaryn", MINOR_SLATE).predicate(OPTIMIZATION_PREDICATE).addBonus(BonusType.ELEMENT_PRESERVATION, 0.03F).addBonus(BonusType.SPEED, 0.05F).max(1);
		builder("kaworu", SLATE).predicate(OPTIMIZATION_PREDICATE).addBonus(BonusType.ELEMENT_PRESERVATION, 0.05F).addBonus(BonusType.SPEED, 0.1F).max(1);
		builder("mewtwo", MAJOR_SLATE).predicate(OPTIMIZATION_PREDICATE).addBonus(BonusType.ELEMENT_PRESERVATION, 0.1F).addBonus(BonusType.SPEED, 0.3F).max(1);
		builder("claptrap", MINOR_SLATE).predicate(LUCK_PREDICATE).addBonus(BonusType.LUCK, 1).addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F).max(1);
		builder("bombadil", SLATE).predicate(LUCK_PREDICATE).addBonus(BonusType.LUCK, 2).addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F).max(1);
		builder("tzeentch", MAJOR_SLATE).predicate(LUCK_PREDICATE).addBonus(BonusType.LUCK, 3).addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F).max(1);

		builder("creative", MAJOR_SLATE).predicate(SPEED_PREDICATE).addBonus(BonusType.SPEED, 1000000F).addBonus(BonusType.ELEMENT_PRESERVATION, 1000000F).max(1);
	}

	@Override
	public void run(@Nonnull CachedOutput cache) throws IOException {
		super.run(cache);
		itemModelProvider.generateAll(cache);
	}

	private Rune.Builder builder(String name, ResourceLocation slate) {
		var path = Rune.FOLDER + '/' + name;
		var runeTexture = ElementalCraft.createRL(path);
		var builder = Rune.Builder.create().model(itemModelProvider.runeTexture(path, slate, runeTexture)).sprite(runeTexture);

		add(ElementalCraft.createRL(name), builder);
		return builder;
	}

	private static IBlockPosPredicate matchTagOrElementPump(TagKey<Block> tag) {
		return IBlockPosPredicate.match(tag).or(new HasPipeUpgrade(PipeUpgradeTypes.ELEMENT_PUMP.get()));
	}

	@Nonnull
    @Override
	public String getName() {
		return "ElementalCraft Spell Properties";
	}
}
