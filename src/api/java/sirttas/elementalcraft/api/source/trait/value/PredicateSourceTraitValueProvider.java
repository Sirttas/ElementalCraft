package sirttas.elementalcraft.api.source.trait.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTraitRollContext;

import javax.annotation.Nullable;

public class PredicateSourceTraitValueProvider implements ISourceTraitValueProvider {

	public static final String NAME = "predicate";
	public static final Codec<PredicateSourceTraitValueProvider> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ISourceTraitValueProvider.CODEC.fieldOf(ECNames.PROVIDER).forGetter(p -> p.provider),
			IBlockPosPredicate.CODEC.fieldOf(ECNames.PREDICATE).forGetter(p -> p.predicate)
	).apply(builder, PredicateSourceTraitValueProvider::new));

	private final ISourceTraitValueProvider provider;
	private final IBlockPosPredicate predicate;

	public PredicateSourceTraitValueProvider(ISourceTraitValueProvider provider, IBlockPosPredicate predicate) {
		this.provider = provider;
		this.predicate = predicate;
	}
	
	@Override
	public ISourceTraitValue roll(SourceTraitRollContext context, Level level, BlockPos pos) {
		if (predicate.test(level, pos, null)) {
			return provider.roll(context, level, pos);
		}
		return null;
	}

	@Nullable
	@Override
	public ISourceTraitValue breed(SourceTraitRollContext context, @Nullable ISourceTraitValue value1, @Nullable ISourceTraitValue value2) {
		return provider.breed(context, value1, value2);
	}

	@Override
	public @NotNull SourceTraitValueProviderType<PredicateSourceTraitValueProvider> getType() {
		return SourceTraitValueProviderTypes.PREDICATE.get();
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
