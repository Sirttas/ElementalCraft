package sirttas.elementalcraft.datagen.managed;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.data.predicate.block.rune.HasRunePredicate;
import sirttas.elementalcraft.datagen.ECItemModelProvider;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.io.IOException;

public class RunesProvider extends AbstractManagedDataBuilderProvider<Rune, Rune.Builder> {
	
	private final ECItemModelProvider itemModelProvider;

	private static final IBlockPosPredicate LUCK_PREDICATE = IBlockPosPredicate.match(ECTags.Blocks.RUNE_AFFECTED_LUCK).and(IBlockPosPredicate.createOr(
			new HasRunePredicate(ElementalCraft.createRL("claptrap")),
			new HasRunePredicate(ElementalCraft.createRL("bombadil")),
			new HasRunePredicate(ElementalCraft.createRL("tzeentch"))).not());
	
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
		builder("wii", MINOR_SLATE).match(ECTags.Blocks.RUNE_AFFECTED_SPEED).addBonus(BonusType.SPEED, 0.1F).addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
		builder("fus", SLATE).match(ECTags.Blocks.RUNE_AFFECTED_SPEED).addBonus(BonusType.SPEED, 0.3F).addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
		builder("zod", MAJOR_SLATE).match(ECTags.Blocks.RUNE_AFFECTED_SPEED).addBonus(BonusType.SPEED, 0.5F).addBonus(BonusType.ELEMENT_PRESERVATION, -0.05F);
		builder("manx", MINOR_SLATE).match(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION).addBonus(BonusType.ELEMENT_PRESERVATION, 0.05F).addBonus(BonusType.SPEED, -0.1F);
		builder("jita", SLATE).match(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION).addBonus(BonusType.ELEMENT_PRESERVATION, 0.1F).addBonus(BonusType.SPEED, -0.1F);
		builder("tano", MAJOR_SLATE).match(ECTags.Blocks.RUNE_AFFECTED_PRESERVATION).addBonus(BonusType.ELEMENT_PRESERVATION, 0.15F).addBonus(BonusType.SPEED, -0.1F);
		builder("claptrap", MINOR_SLATE).predicate(LUCK_PREDICATE).addBonus(BonusType.LUCK, 1).addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F).max(1);
		builder("bombadil", SLATE).predicate(LUCK_PREDICATE).addBonus(BonusType.LUCK, 2).addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F).max(1);
		builder("tzeentch", MAJOR_SLATE).predicate(LUCK_PREDICATE).addBonus(BonusType.LUCK, 3).addBonus(BonusType.ELEMENT_PRESERVATION, -0.1F).max(1);
		
		builder("creative", MAJOR_SLATE).match(ECTags.Blocks.RUNE_AFFECTED).addBonus(BonusType.SPEED, 1000000F).addBonus(BonusType.ELEMENT_PRESERVATION, 1000000F).max(1);
	}

	@Override
	public void run(@Nonnull HashCache cache) throws IOException {
		super.run(cache);
		itemModelProvider.generateAll(cache);
	}

	private Rune.Builder builder(String name, ResourceLocation slate) {
		String path = Rune.FOLDER + '/' + name;
		ResourceLocation runeTexture = ElementalCraft.createRL(path);
		var builder = Rune.Builder.create().model(itemModelProvider.runeTexture("item/" + path, slate, runeTexture)).sprite(runeTexture);

		add(ElementalCraft.createRL(name), builder);
		return builder;
	}

	@Nonnull
    @Override
	public String getName() {
		return "ElementalCraft Spell Properties";
	}
}
