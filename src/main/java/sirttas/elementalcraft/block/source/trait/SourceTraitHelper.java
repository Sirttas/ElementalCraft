package sirttas.elementalcraft.block.source.trait;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;

public class SourceTraitHelper {

	private SourceTraitHelper() {}
	
	@Nonnull
	public static Map<SourceTrait, ISourceTraitValue> loadTraits(@Nullable CompoundTag tag) {
		Map<SourceTrait, ISourceTraitValue> traits = new TreeMap<>();
		
		loadTraits(tag, traits);
		return traits;
	}
	
	public static void loadTraits(@Nullable CompoundTag tag, @Nonnull Map<SourceTrait, ISourceTraitValue> traits) {
		traits.clear();
		if (tag != null) {
			for (String key : tag.getAllKeys()) {
				var trait = ElementalCraftApi.SOURCE_TRAIT_MANAGER.get(new ResourceLocation(key));
				
				if (trait != null) {
					var value = trait.load(tag.get(key));
					
					if (value != null) {
						traits.put(trait, value);
					}
				}
			}
		}
	}
	
	@Nonnull
	public static CompoundTag saveTraits(@Nonnull Map<SourceTrait, ISourceTraitValue> traits) {
		var traitTag = new CompoundTag();

		traits.forEach((trait, value) -> {
			var tag = trait.save(value);

			if (tag != null) {
				traitTag.put(trait.getId().toString(), tag);
			}
		});
		return traitTag;
	}
	
}
