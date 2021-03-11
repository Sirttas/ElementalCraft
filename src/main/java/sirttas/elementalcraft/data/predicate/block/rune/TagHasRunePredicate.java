package sirttas.elementalcraft.data.predicate.block.rune;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.tags.ITag;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.rune.Rune;
import sirttas.elementalcraft.rune.handler.IRuneHandler;
import sirttas.elementalcraft.tag.ECTags;

public class TagHasRunePredicate implements IRunePredicate {

	public static final String NAME = "tag_has_rune";
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static final BlockPosPredicateType<TagHasRunePredicate> TYPE = null;
	public static final Codec<TagHasRunePredicate> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ResourceLocation.CODEC.fieldOf(ECNames.TAG).forGetter(p -> p.tagId),
			Codec.INT.optionalFieldOf(ECNames.COUNT, 1).forGetter(p -> p.count)
	).apply(builder, TagHasRunePredicate::new));

	private final int count;
	private final ResourceLocation tagId;
	private ITag<Rune> tag;

	public TagHasRunePredicate(INamedTag<Rune> tag) {
		this(tag, 1);
	}

	public TagHasRunePredicate(INamedTag<Rune> tag, int count) {
		this(tag.getName(), count);
		this.tag = tag;
	}

	public TagHasRunePredicate(ResourceLocation runeId) {
		this(runeId, 1);
	}

	public TagHasRunePredicate(ResourceLocation runeId, int count) {
		this.tagId = runeId;
		this.count = count;
	}

	@Override
	public boolean test(IRuneHandler handler) {
		if (tag == null) {
			tag = ECTags.Runes.RUNE_TAGS.getTag(tagId);
		}
		return tag.getAllElements().stream().mapToInt(handler::getRuneCount).sum() >= count;
	}

	@Override
	public BlockPosPredicateType<TagHasRunePredicate> getType() {
		return TYPE;
	}
}
