package sirttas.elementalcraft.infusion.tool.effect;

import com.mojang.serialization.Codec;

import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import sirttas.dpanvil.api.codec.ICodecProvider;

public class ToolInfusionEffectType<T extends IToolInfusionEffect> extends ForgeRegistryEntry<ToolInfusionEffectType<?>> implements ICodecProvider<T> {

	public static final IForgeRegistry<ToolInfusionEffectType<?>> REGISTRY = RegistryManager.ACTIVE.getRegistry(ToolInfusionEffectType.class);
	
	private final Codec<T> codec;

	public ToolInfusionEffectType(Codec<T> codec) {
		this.codec = codec;
	}

	@Override
	public Codec<T> getCodec() {
		return codec;
	}
	
}
