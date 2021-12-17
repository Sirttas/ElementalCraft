package sirttas.elementalcraft.datagen.tag;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import sirttas.dpanvil.api.tag.AbstractDataTagsProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class RuneTagsProvider extends AbstractDataTagsProvider<Rune> {

	public RuneTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, ElementalCraftApi.RUNE_MANAGER, ElementalCraftApi.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(ECTags.Runes.SPEED_RUNES).add(ElementalCraft.createRL("wii"), ElementalCraft.createRL("fus"), ElementalCraft.createRL("zod"));
		tag(ECTags.Runes.ELEMENT_PRESERVATION_RUNES).add(ElementalCraft.createRL("manx"), ElementalCraft.createRL("jita"), ElementalCraft.createRL("tano"));
		tag(ECTags.Runes.LUCK_RUNES).add(ElementalCraft.createRL("claptrap"), ElementalCraft.createRL("bombadil"), ElementalCraft.createRL("tzeentch"));
	}
	
	@Nonnull
    @Override
	public String getName() {
		return "Elemental Craft Rune tags";
	}
}
