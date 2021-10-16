package sirttas.elementalcraft.data.predicate.block.rune;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.dpanvil.api.data.IDataWrapper;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;

public class HasRunePredicate implements IRunePredicate {

	public static final String NAME = "has_rune";
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final BlockPosPredicateType<HasRunePredicate> TYPE = null;
	public static final Codec<HasRunePredicate> CODEC = RecordCodecBuilder.create(builder -> builder.group(
	        IDataWrapper.codec(ElementalCraftApi.RUNE_MANAGER).fieldOf(ECNames.RUNE).forGetter(p -> p.rune),
			Codec.INT.optionalFieldOf(ECNames.COUNT, 1).forGetter(p -> p.count)
	).apply(builder, HasRunePredicate::new));

	private final int count;
	private IDataWrapper<Rune> rune;

	public HasRunePredicate(ResourceLocation runeId) {
		this(runeId, 1);
	}

    public HasRunePredicate(ResourceLocation runeId, int count) {
        this(ElementalCraftApi.RUNE_MANAGER.getWrapper(runeId), count);
    }

    public HasRunePredicate(IDataWrapper<Rune> rune) {
        this(rune, 1);
    }

    public HasRunePredicate(IDataWrapper<Rune> rune, int count) {
        this.count = count;
        this.rune = rune;
    }
	
	@Override
	public boolean test(IRuneHandler handler) {
		return rune.isPresent() && handler.getRuneCount(rune.get()) >= count;
	}

	@Override
	public BlockPosPredicateType<HasRunePredicate> getType() {
		return TYPE;
	}
}
