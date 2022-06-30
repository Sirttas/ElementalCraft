package sirttas.elementalcraft.block.source.trait.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderType;

public class ChanceSourceTraitValueProvider implements ISourceTraitValueProvider {

	public static final String NAME = "chance";
	public static final Codec<ChanceSourceTraitValueProvider> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ISourceTraitValueProvider.CODEC.fieldOf(ECNames.PROVIDER).forGetter(p -> p.provider),
			Codec.FLOAT.fieldOf(ECNames.CHANCE).forGetter(p -> p.chance)
	).apply(builder, ChanceSourceTraitValueProvider::new));

	private final ISourceTraitValueProvider provider;
	private final float chance;

	public ChanceSourceTraitValueProvider(ISourceTraitValueProvider provider, float chance) {
		this.provider = provider;
		this.chance = chance;
	}
	
	@Override
	public ISourceTraitValue roll(SourceTrait trait, Level level, BlockPos pos) {
		if (level.random.nextDouble() < chance) {
			return provider.roll(trait, level, pos);
		}
		return null;
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
