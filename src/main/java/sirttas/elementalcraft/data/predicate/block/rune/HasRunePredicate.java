package sirttas.elementalcraft.data.predicate.block.rune;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.data.predicate.block.ECBlockPosPredicateTypes;

import javax.annotation.Nonnull;
import java.util.List;

public class HasRunePredicate implements IRunePredicate {

	public static final String NAME = "has_rune";
	public static final MapCodec<HasRunePredicate> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
			IDataManager.keyCodec(ElementalCraftApi.RUNE_MANAGER_KEY).fieldOf(ECNames.RUNE).forGetter(p -> p.key),
			Codec.INT.optionalFieldOf(ECNames.COUNT, 1).forGetter(p -> p.count)
	).apply(builder, HasRunePredicate::new));

	private final int count;
	private final ResourceKey<Rune> key;


	public HasRunePredicate(ResourceLocation runeId) {
		this(runeId, 1);
	}

	public HasRunePredicate(ResourceLocation runeId, int count) {
		this(IDataManager.createKey(ElementalCraftApi.RUNE_MANAGER_KEY, runeId), count);
	}

	public HasRunePredicate(ResourceKey<Rune> key) {
		this(key, 1);
	}

	public HasRunePredicate(ResourceKey<Rune> key, int count) {
		this.key = key;
		this.count = count;
	}


	@Override
	public boolean test(IRuneHandler handler) {
		return handler.getRuneCount(key) >= count;
	}

	@Override
	public BlockPosPredicateType<HasRunePredicate> getType() {
		return ECBlockPosPredicateTypes.HAS_RUNE.get();
	}

	@Override
	@Nonnull
	public List<Component> getTooltip() {
		var loc = key.location();

		return List.of(Component.translatable("tooltip.elementalcraft.predicate.rune", count, Component.translatable("elementalcraft_rune." + loc.getNamespace() + "." + loc.getPath())));
	}
}
