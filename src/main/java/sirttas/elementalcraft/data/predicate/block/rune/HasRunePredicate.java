package sirttas.elementalcraft.data.predicate.block.rune;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.rune.Rune;
import sirttas.elementalcraft.rune.capability.IRuneHandler;

public class HasRunePredicate implements IRunePredicate {

	public static final String NAME = "has_rune";
	@ObjectHolder(ElementalCraft.MODID + ":" + NAME) public static BlockPosPredicateType<HasRunePredicate> TYPE;
	public static final Codec<HasRunePredicate> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ResourceLocation.CODEC.fieldOf(ECNames.RUNE).forGetter(p -> p.runeId),
			Codec.INT.optionalFieldOf(ECNames.COUNT, 1).forGetter(p -> p.count)
	).apply(builder, HasRunePredicate::new));

	private final int count;
	private final ResourceLocation runeId;
	private Rune rune;

	public HasRunePredicate(Rune upgrade) {
		this(upgrade, 1);
	}

	public HasRunePredicate(Rune upgrade, int count) {
		this(upgrade.getId(), count);
		this.rune = upgrade;
	}

	public HasRunePredicate(ResourceLocation runeId) {
		this(runeId, 1);
	}

	public HasRunePredicate(ResourceLocation runeId, int count) {
		this.runeId = runeId;
		this.count = count;
	}

	@Override
	public boolean test(IRuneHandler handler) {
		if (rune == null) {
			rune = ElementalCraft.RUNE_MANAGER.get(runeId);
		}
		return handler.getRuneCount(rune) >= count;
	}

	@Override
	public BlockPosPredicateType<HasRunePredicate> getType() {
		return TYPE;
	}
}
