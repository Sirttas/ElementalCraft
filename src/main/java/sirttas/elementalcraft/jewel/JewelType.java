package sirttas.elementalcraft.jewel;

import com.mojang.serialization.Codec;

import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import sirttas.dpanvil.api.codec.ICodecProvider;

public class JewelType<T extends AbstractJewel> extends ForgeRegistryEntry<JewelType<?>> implements ICodecProvider<T> {

	public static final IForgeRegistry<JewelType<?>> REGISTRY = RegistryManager.ACTIVE.getRegistry(JewelType.class);
	
	private final Codec<T> codec;
	
	public JewelType(Codec<T> codec) {
		this.codec = codec;
	}
	
	@Override
	public Codec<T> getCodec() {
		return codec;
	}

	
}
