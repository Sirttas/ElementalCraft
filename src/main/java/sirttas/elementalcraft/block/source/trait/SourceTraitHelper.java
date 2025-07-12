package sirttas.elementalcraft.block.source.trait;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class SourceTraitHelper {

	private SourceTraitHelper() {}
	
	@Nonnull
	public static Map<Holder<SourceTrait>, ISourceTraitValue> loadTraits(@Nullable CompoundTag tag) {
		var traits = SourceTraits.createTraitMap();
		
		loadTraits(tag, traits);
		return traits;
	}
	
	public static void loadTraits(@Nullable CompoundTag tag, @Nonnull Map<Holder<SourceTrait>, ISourceTraitValue> traits) {
		traits.clear();
		if (tag != null) {
			for (String name : tag.getAllKeys()) {
				var trait = ElementalCraftApi.SOURCE_TRAIT_MANAGER.getOrCreateHolder(ResourceLocation.parse(name));
				
				if (trait.isBound()) {
					var value = trait.value().load(tag.get(name));
					
					if (value != null) {
						traits.put(trait, value);
					}
				}
  			}
		}
	}
	
	@Nonnull
	public static CompoundTag saveTraits(@Nonnull Map<Holder<SourceTrait>, ISourceTraitValue> traits) {
		var traitTag = new CompoundTag();

		traits.forEach((key, value) -> {
			if (key.isBound()) {
				var trait = key.value();
				var tag = trait.save(value);

				if (tag != null) {
					traitTag.put(trait.getId().toString(), tag);
				}
			}
		});
		return traitTag;
	}

	public static Map<Holder<SourceTrait>, ISourceTraitValue> breed(@Nonnull RandomSource random, float luck, Map<Holder<SourceTrait>, ISourceTraitValue> map1, Map<Holder<SourceTrait>, ISourceTraitValue> map2) {
		var traits = SourceTraits.createTraitMap();

		for (var holder : ElementalCraftApi.SOURCE_TRAIT_MANAGER.holders().toList()) {
			var value = holder.value().breed(random, luck, map1.get(holder), map2.get(holder));

			if (value != null) {
				traits.put(holder, value);
			}
		}
		return traits;
	}
}
