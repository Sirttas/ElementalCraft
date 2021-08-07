package sirttas.elementalcraft.datagen.managed;

import java.io.IOException;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import sirttas.dpanvil.api.data.AbstractManagedDataProvider;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.data.predicate.block.rune.TagHasRunePredicate;
import sirttas.elementalcraft.datagen.ECItemModelProvider;
import sirttas.elementalcraft.tag.ECTags;

public class RunesProvider extends AbstractManagedDataProvider<Rune> {
	
	private ECItemModelProvider itemModelProvider;

	private static final IBlockPosPredicate LUCK_PREDICATE = IBlockPosPredicate.match(ECTags.Blocks.RUNE_AFFECTED_LUCK).and(new TagHasRunePredicate(ECTags.Runes.LUCK_RUNES).not());
	
	public static final ResourceLocation MINOR_SLATE = ElementalCraft.createRL("item/minor_rune_slate");
	public static final ResourceLocation SLATE = ElementalCraft.createRL("item/rune_slate");
	public static final ResourceLocation MAJOR_SLATE = ElementalCraft.createRL("item/major_rune_slate");

	public RunesProvider(DataGenerator generator, ECItemModelProvider itemModelProvider) {
		super(generator, ElementalCraftApi.RUNE_MANAGER);
		this.itemModelProvider = itemModelProvider;
	}

	@Override
	public void run(HashCache cache) throws IOException {
		itemModelProvider.clear();
		addRune(cache, Rune.Builder.create().match(ECTags.Blocks.RUNE_AFFECTED_SPEED).addBonus(BonusType.SPEED, 0.1F).addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F), "wii", MINOR_SLATE);
		addRune(cache, Rune.Builder.create().match(ECTags.Blocks.RUNE_AFFECTED_SPEED).addBonus(BonusType.SPEED, 0.3F).addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F), "fus", SLATE);
		addRune(cache, Rune.Builder.create().match(ECTags.Blocks.RUNE_AFFECTED_SPEED).addBonus(BonusType.SPEED, 0.5F).addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F), "zod", MAJOR_SLATE);
		addRune(cache, Rune.Builder.create().match(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION).addBonus(BonusType.ELEMENT_PRESERVATION, 0.05F).addBonus(BonusType.SPEED, -0.1F), "manx", MINOR_SLATE);
		addRune(cache, Rune.Builder.create().match(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION).addBonus(BonusType.ELEMENT_PRESERVATION, 0.1F).addBonus(BonusType.SPEED, -0.1F), "jita", SLATE);
		addRune(cache, Rune.Builder.create().match(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION).addBonus(BonusType.ELEMENT_PRESERVATION, 0.15F).addBonus(BonusType.SPEED, -0.1F), "tano", MAJOR_SLATE);
		addRune(cache, Rune.Builder.create().predicate(LUCK_PREDICATE).addBonus(BonusType.LUCK, 1).addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F).max(1), "claptrap", MINOR_SLATE);
		addRune(cache, Rune.Builder.create().predicate(LUCK_PREDICATE).addBonus(BonusType.LUCK, 2).addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F).max(1), "bombadil", SLATE);
		addRune(cache, Rune.Builder.create().predicate(LUCK_PREDICATE).addBonus(BonusType.LUCK, 3).addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F).max(1), "tzeentch", MAJOR_SLATE);
		itemModelProvider.generateAll(cache);
	}

	private void addRune(HashCache cache, Rune.Builder builder, String name, ResourceLocation slate) throws IOException {
		String path = Rune.FOLDER + '/' + name;
		ResourceLocation runeTexture = ElementalCraft.createRL(path);

		save(cache, builder.model(itemModelProvider.runeTexture("item/" + path, slate, runeTexture)).sprite(runeTexture), name);
	}

	protected void save(HashCache cache, Rune.Builder builder, String name) throws IOException {
		save(cache, builder.toJson(), ElementalCraft.createRL(name));
	}

	@Override
	public String getName() {
		return "ElementalCraft Spell Properties";
	}
}
