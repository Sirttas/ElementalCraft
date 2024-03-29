package sirttas.elementalcraft.api.source.trait.value;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.registry.ElementalCraftRegistries;
import sirttas.elementalcraft.api.source.trait.SourceTraitRollContext;

import javax.annotation.Nullable;

public interface ISourceTraitValueProvider {

	Codec<ISourceTraitValueProvider> CODEC = ElementalCraftRegistries.SOURCE_TRAIT_VALUE_PROVIDER_TYPE.byNameCodec().dispatch(ISourceTraitValueProvider::getType, SourceTraitValueProviderType::codec);
	
	@NotNull SourceTraitValueProviderType<?> getType();
	
	@Nullable
	ISourceTraitValue roll(SourceTraitRollContext context, Level level, BlockPos pos);

	@Nullable
	ISourceTraitValue breed(SourceTraitRollContext context, @Nullable ISourceTraitValue value1, @Nullable ISourceTraitValue value2);

	ISourceTraitValue load(Tag tag);

	Tag save(ISourceTraitValue value);
	
}
