package sirttas.elementalcraft.api.infusion.tool.effect;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import sirttas.dpanvil.api.codec.ICodecProvider;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;

public class ToolInfusionEffectType<T extends IToolInfusionEffect> extends ForgeRegistryEntry<ToolInfusionEffectType<?>> implements ICodecProvider<T> {

	public static final IForgeRegistry<ToolInfusionEffectType<?>> REGISTRY = RegistryManager.ACTIVE.getRegistry(new ResourceLocation(ElementalCraftApi.MODID, ECNames.TOOL_INFUSION_TYPE));
	
	private final Codec<T> codec;

	public ToolInfusionEffectType(Codec<T> codec) {
		this.codec = codec;
	}

	@Override
	public Codec<T> getCodec() {
		return codec;
	}
	
}
