package sirttas.elementalcraft.datagen.tag;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import sirttas.dpanvil.api.tag.DataTagsProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.rune.Rune;
import sirttas.elementalcraft.tag.ECTags;

public class RuneTagsProvider extends DataTagsProvider<Rune> {

	public RuneTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, ElementalCraft.RUNE_MANAGER, ElementalCraft.MODID, existingFileHelper);
	}

	@Override
	protected void registerTags() {
		getOrCreateBuilder(ECTags.Runes.SPEED_RUNES).add(ElementalCraft.createRL("wii"), ElementalCraft.createRL("fus"), ElementalCraft.createRL("zod"));
		getOrCreateBuilder(ECTags.Runes.ELEMENT_PRESERVATION_RUNES).add(ElementalCraft.createRL("manx"), ElementalCraft.createRL("jita"), ElementalCraft.createRL("tano"));
		getOrCreateBuilder(ECTags.Runes.LUCK_RUNES).add(ElementalCraft.createRL("claptrap"), ElementalCraft.createRL("bombadil"), ElementalCraft.createRL("tzeentch"));
	}
	
	@Override
	public String getName() {
		return "Elemental Craft Rune tags";
	}
}
