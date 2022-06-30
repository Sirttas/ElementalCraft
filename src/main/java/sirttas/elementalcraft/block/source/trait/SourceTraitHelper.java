package sirttas.elementalcraft.block.source.trait;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.TreeMap;

public class SourceTraitHelper {

	private SourceTraitHelper() {}
	
	@Nonnull
	public static Map<ResourceKey<SourceTrait>, ISourceTraitValue> loadTraits(@Nullable CompoundTag tag) {
		Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits = new TreeMap<>();
		
		loadTraits(tag, traits);
		return traits;
	}
	
	public static void loadTraits(@Nullable CompoundTag tag, @Nonnull Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits) {
		traits.clear();
		if (tag != null) {
			for (String name : tag.getAllKeys()) {
				var key = SourceTraits.key(name);
				var trait = ElementalCraftApi.SOURCE_TRAIT_MANAGER.get(new ResourceLocation(name));
				
				if (trait != null) {
					var value = trait.load(tag.get(name));
					
					if (value != null) {
						traits.put(key, value);
					}
				}
			}
		}
	}
	
	@Nonnull
	public static CompoundTag saveTraits(@Nonnull Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits) {
		var traitTag = new CompoundTag();

		traits.forEach((key, value) -> {
			var trait = ElementalCraftApi.SOURCE_TRAIT_MANAGER.get(key.location());
			if (trait != null) {
				var tag = trait.save(value);

				if (tag != null) {
					traitTag.put(trait.getId().toString(), tag);
				}
			}
		});
		return traitTag;
	}
	
}
