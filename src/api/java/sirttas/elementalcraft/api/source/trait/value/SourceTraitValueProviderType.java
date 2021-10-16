package sirttas.elementalcraft.api.source.trait.value;

import com.mojang.serialization.Codec;

import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import sirttas.dpanvil.api.codec.ICodecProvider;

public class SourceTraitValueProviderType<T extends ISourceTraitValueProvider> extends ForgeRegistryEntry<SourceTraitValueProviderType<?>> implements ICodecProvider<T> {
	
	public static final IForgeRegistry<SourceTraitValueProviderType<?>> REGISTRY = RegistryManager.ACTIVE.getRegistry(SourceTraitValueProviderType.class);
	
	private final Codec<T> codec;

	public SourceTraitValueProviderType(Codec<T> codec) {
		this.codec = codec;
	}
	
	@Override
	public Codec<T> getCodec() {
		return codec;
	}

}
