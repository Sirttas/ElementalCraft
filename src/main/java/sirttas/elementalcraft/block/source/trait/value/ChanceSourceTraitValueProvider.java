package sirttas.elementalcraft.block.source.trait.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTraitRollContext;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderType;

import javax.annotation.Nullable;

public class ChanceSourceTraitValueProvider implements ISourceTraitValueProvider {

	public static final String NAME = "chance";
	public static final Codec<ChanceSourceTraitValueProvider> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ISourceTraitValueProvider.CODEC.fieldOf(ECNames.PROVIDER).forGetter(p -> p.provider),
			Codec.FLOAT.fieldOf(ECNames.CHANCE).forGetter(p -> p.chance),
			Codec.FLOAT.optionalFieldOf(ECNames.CHANCE_ON_BRED, -1f).forGetter(p -> p.chanceOnBred)
	).apply(builder, ChanceSourceTraitValueProvider::new));

	private final ISourceTraitValueProvider provider;
	private final float chance;
	private final float chanceOnBred;


	public ChanceSourceTraitValueProvider(ISourceTraitValueProvider provider, float chance, float chanceOnBred) {
		this.provider = provider;
		this.chance = chance;
		this.chanceOnBred = chanceOnBred;
	}
	
	@Override
	public ISourceTraitValue roll(SourceTraitRollContext context, Level level, BlockPos pos) {
		var random = context.random();

		if (random.nextFloat() < chance + getLuckRoll(random, context.luck())) {
			return provider.roll(context, level, pos);
		}
		return null;
	}


	@Nullable
	@Override
	public ISourceTraitValue breed(SourceTraitRollContext context, @Nullable ISourceTraitValue value1, @Nullable ISourceTraitValue value2) {
		var random = context.random();
		var count = (value1 != null ? 1 : 0) + (value2 != null ? 1 : 0);

		if (count >= 2 || random.nextFloat() < getBreedChance(count) + getLuckRoll(random, context.luck())) {
			return provider.breed(context, value1, value2);
		}
		return null;
	}

	private float getLuckRoll(RandomSource random, float luck) {
		return luck > 0 ? (random.nextFloat() * luck) / 4 : 0;
	}

	private float getBreedChance(int count) {
		return (chanceOnBred >= 0 ? chanceOnBred : chance) + (count * 0.5f);
	}

	@Override
	public @NotNull SourceTraitValueProviderType<ChanceSourceTraitValueProvider> getType() {
		return SourceTraitValueProviderTypes.CHANCE.get();
	}
	
	@Override
	public ISourceTraitValue load(Tag tag) {
		return provider.load(tag);
	}

	@Override
	public Tag save(ISourceTraitValue value) {
		return provider.save(value);
	}


}
