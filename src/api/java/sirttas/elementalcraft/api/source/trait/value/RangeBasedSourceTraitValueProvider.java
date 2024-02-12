package sirttas.elementalcraft.api.source.trait.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.SourceTraitRollContext;

import javax.annotation.Nonnull;
import java.util.List;

public class RangeBasedSourceTraitValueProvider extends LinearSourceTraitValueProvider {

	public static final String NAME = "range_based";
	public static final Codec<RangeBasedSourceTraitValueProvider> CODEC = RecordCodecBuilder.create(builder -> codec(builder)
	        .and(Codec.FLOAT.fieldOf(ECNames.WEIGHT).forGetter(p -> p.weight))
	        .apply(builder, RangeBasedSourceTraitValueProvider::new));
	
	private final float weight;

	public RangeBasedSourceTraitValueProvider(String translationKey, List<SourceTrait.Type> types, float end, float weight) {
		this(translationKey, types, 0, end, weight);
	}

	public RangeBasedSourceTraitValueProvider(String translationKey, List<SourceTrait.Type> types, float start, float end, float weight) {
		this(translationKey, types, start, end, 1, weight);
	}

	public RangeBasedSourceTraitValueProvider(String translationKey, List<SourceTrait.Type> types, float start, float end, float luckRatio, float weight) {
	    super(translationKey, types, start, end, luckRatio);
	    this.weight = weight;
	}
	
	@Override
	public ISourceTraitValue roll(SourceTraitRollContext context, Level level, BlockPos pos) {
	    BlockPos spawn = level instanceof ServerLevel serverLevel ? serverLevel.getSharedSpawnPos() : BlockPos.ZERO;
        var rangeSq = new BlockPos(spawn.getX(), 0, spawn.getZ()).distSqr(new BlockPos(pos.getX(), 0, pos.getZ()));
        var newStart = Math.min(start + (context.luck() * luckRatio), end);

		return createValue(newStart + (float) (rangeSq / (rangeSq + (context.random().nextFloat() * weight * weight))) * (end - newStart));
	}
	
	@Override
	@Nonnull
	public SourceTraitValueProviderType<RangeBasedSourceTraitValueProvider> getType() {
		return SourceTraitValueProviderTypes.RANGE_BASED.get();
	}
}
