package sirttas.elementalcraft.block.source.trait.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValueProvider;
import sirttas.elementalcraft.api.source.trait.value.SourceTraitValueProviderType;

public class RangeBasedSourceTraitValueProvider extends LinearSourceTraitValueProvider {

	public static final String NAME = "range_based";
	@ObjectHolder(ElementalCraftApi.MODID + ":" + NAME) public static final SourceTraitValueProviderType<RangeBasedSourceTraitValueProvider> TYPE = null;
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
	public SourceTraitValueProviderType<? extends ISourceTraitValueProvider> getType() {
		return TYPE;
	}
}
