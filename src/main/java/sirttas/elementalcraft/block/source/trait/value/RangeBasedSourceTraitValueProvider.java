package sirttas.elementalcraft.block.source.trait.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderType;

public class RangeBasedSourceTraitValueProvider extends LinearSourceTraitValueProvider {

	public static final String NAME = "range_based";
	public static final Codec<RangeBasedSourceTraitValueProvider> CODEC = RecordCodecBuilder.create(builder -> codec(builder)
	        .and(Codec.FLOAT.fieldOf(ECNames.WEIGHT).forGetter(p -> p.weight))
	        .apply(builder, RangeBasedSourceTraitValueProvider::new));
	
	private final float weight;
	
	public RangeBasedSourceTraitValueProvider(String translationKey, float end, float weight) {
		this(translationKey, 0, end, weight);
	}
	
	public RangeBasedSourceTraitValueProvider(String translationKey, float start, float end, float weight) {
	    super(translationKey, start, end);
	    this.weight = weight;
	}
	
	@Override
	public ISourceTraitValue roll(SourceTrait trait, Level level, BlockPos pos) {
	    BlockPos spawn = level instanceof ServerLevel serverLevel ? serverLevel.getSharedSpawnPos() : BlockPos.ZERO;
        var rangeSq = new BlockPos(spawn.getX(), 0, spawn.getZ()).distSqr(new BlockPos(pos.getX(), 0, pos.getZ()));
        
		return createValue(start + (float) (rangeSq / (rangeSq + (level.random.nextFloat() * weight * weight))) * (end - start));
	}
	
	@Override
	public @NotNull SourceTraitValueProviderType<RangeBasedSourceTraitValueProvider> getType() {
		return SourceTraitValueProviderTypes.RANGE_BASED.get();
	}
}
