package sirttas.elementalcraft.api.source.trait.value;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.elementalcraft.api.source.trait.SourceTrait;

import javax.annotation.Nullable;

public interface ISourceTraitValueProvider {

	Codec<ISourceTraitValueProvider> CODEC = CodecHelper.getRegistryCodec(() -> SourceTraitValueProviderType.REGISTRY).dispatch(ISourceTraitValueProvider::getType, SourceTraitValueProviderType::getCodec);
	
	SourceTraitValueProviderType<? extends ISourceTraitValueProvider> getType();
	
	@Nullable
	ISourceTraitValue roll(SourceTrait trait, Level level, BlockPos pos);
	
	ISourceTraitValue load(Tag tag);

	Tag save(ISourceTraitValue value);
	
}
