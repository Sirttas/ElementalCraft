package sirttas.elementalcraft.api.source.trait.value;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;

import javax.annotation.Nullable;

public interface ISourceTraitValueProvider {

	Codec<ISourceTraitValueProvider> CODEC = CodecHelper.getRegistryCodec(ElementalCraftApi.SOURCE_TRAIT_VALUE_PROVIDER_TYPE_REGISTRY_KEY).dispatch(ISourceTraitValueProvider::getType, SourceTraitValueProviderType::codec);
	
	@NotNull SourceTraitValueProviderType<?> getType();
	
	@Nullable
	ISourceTraitValue roll(SourceTrait trait, Level level, BlockPos pos);

	@Nullable
	ISourceTraitValue breed(SourceTrait trait, Level level, @Nullable ISourceTraitValue value1, @Nullable ISourceTraitValue value2);

	ISourceTraitValue load(Tag tag);

	Tag save(ISourceTraitValue value);
	
}
