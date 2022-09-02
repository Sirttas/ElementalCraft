package sirttas.elementalcraft.pureore;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.common.util.Lazy;
import sirttas.dpanvil.api.event.DataPackReloadCompleteEvent;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.ECItem;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.recipe.instrument.io.IPurifierRecipe;
import sirttas.elementalcraft.tag.ECTags;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PureOreManager {

	private static final Lazy<PureOreLoader> ORE_LOADER = Lazy.of(() -> PureOreLoader.create(ECTags.Items.PURE_ORES_ORE_SOURCE)
			.pattern("_?ore$")
			.tagFolder("ores")
			.inputSize(ECConfig.COMMON.pureOreOresInput.get())
			.outputSize(ECConfig.COMMON.pureOreOresOutput.get())
			.luckRatio(ECConfig.COMMON.pureOreOresLuckRatio.get()));
	private static final Lazy<PureOreLoader> RAW_MATERIALS_LOADER = Lazy.of(() -> PureOreLoader.create(ECTags.Items.PURE_ORES_RAW_METAL_SOURCE)
			.pattern("^raw_?")
			.tagFolder("raw_materials")
			.inputSize(ECConfig.COMMON.pureOreRawMaterialsInput.get())
			.outputSize(ECConfig.COMMON.pureOreRawMaterialsOutput.get())
			.luckRatio(ECConfig.COMMON.pureOreRawMaterialsLuckRatio.get()));

	private final Map<ResourceLocation, Entry> pureOres = new HashMap<>();

	public boolean isValidOre(ItemStack ore) {
		return pureOres.values().stream().anyMatch(e -> e.test(ore));
	}
	
	private ResourceLocation getPureOreId(ItemStack stack) {
		var nbt = NBTHelper.getECTag(stack);

		if (nbt != null) {
			return new ResourceLocation(nbt.getString(ECNames.ORE));
		}
		return null;
	}

	public IPurifierRecipe getRecipes(ItemStack ore) {
		return pureOres.values().stream()
				.filter(e -> e.test(ore))
				.<IPurifierRecipe>mapMulti((e, downstream) -> e.getRecipes().forEach(downstream))
				.filter(r -> r.matches(ore))
				.findAny()
				.orElse(null);
	}

	public static Collection<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> getInjectors() {
		return AbstractPureOreRecipeInjector.REGISTRY.getValues();
	}

	public Component getPureOreName(ItemStack stack) {
		var entry = pureOres.get(getPureOreId(stack));
		
		return entry != null ? entry.getDescription() : null;
	}

	public ItemStack createPureOre(ResourceLocation id) {
		if (this.pureOres.containsKey(id)) {
			ItemStack stack = new ItemStack(ECItems.PURE_ORE.get());
	
			NBTHelper.getOrCreateECTag(stack).putString(ECNames.ORE, id.toString());
			return stack;
		}
		return ItemStack.EMPTY;
	}

	@OnlyIn(Dist.CLIENT)
	public int getColor(ItemStack stack) {
		var entry = pureOres.get(getPureOreId(stack));

		return entry != null ? entry.getColor() : -1;
	}

	public List<ResourceLocation> getOres() {
		return new ArrayList<>(pureOres.keySet());
	}

	public List<IPurifierRecipe> getRecipes() {
		return pureOres.values().stream()
				.<IPurifierRecipe>mapMulti((e, downstream) -> e.getRecipes().forEach(downstream))
				.toList();
	}

	public void reload(DataPackReloadCompleteEvent event) {
		var recipeManager = event.getRecipeManager();
		var injectors = getInjectors();

		ElementalCraftApi.LOGGER.info("Pure ore generation started.\n\r\tRecipe Types: {}",
				() -> injectors.stream()
						.map(AbstractPureOreRecipeInjector::toString)
						.collect(Collectors.joining(", ")));
		injectors.forEach(injector -> injector.init(recipeManager));
		this.pureOres.clear();
		ORE_LOADER.get().generate(injectors).forEach(e -> this.pureOres.computeIfAbsent(e.getId(), i -> new Entry()).ore = e);
		RAW_MATERIALS_LOADER.get().generate(injectors).forEach(e -> this.pureOres.computeIfAbsent(e.getId(), i -> new Entry()).rawMaterial = e);

		if (Boolean.TRUE.equals(ECConfig.COMMON.pureOreRecipeInjection.get())) {
			ElementalCraftApi.LOGGER.info("Pure ore recipe injection");
			this.pureOres.values().removeIf(o -> !o.isProcessable());

			var entries = pureOres.values().stream().distinct().toList();
			var recipes = new ArrayList<>(recipeManager.getRecipes());

			injectors.forEach(injector -> inject(injector, recipes, entries));
			recipeManager.replaceRecipes(recipes);
		}

		ElementalCraftApi.LOGGER.info("Pure ore generation ended\r\n\tOres: {}", () -> pureOres.keySet().stream()
				.map(ResourceLocation::toString)
				.collect(Collectors.joining(", ")));
	}

	private <C extends Container, T extends Recipe<C>> void inject(AbstractPureOreRecipeInjector<C, T> injector, Collection<Recipe<?>> recipes, List<Entry> entries) {
		injector.inject(recipes, entries.stream()
				.distinct()
				.<T>mapMulti((entry, downstream) -> {
					if (entry.ore != null) {
						downstream.accept(this.injectEntry(injector, entry.ore));
					}
					if (entry.rawMaterial != null) {
						downstream.accept(this.injectEntry(injector, entry.rawMaterial));
					}
				})
				.filter(Objects::nonNull)
				.filter(ElementalCraftUtils.distinctBy(Recipe::getId))
				.toList());
	}

	private <C extends Container, T extends Recipe<C>> T injectEntry(AbstractPureOreRecipeInjector<C, T> injector, PureOre entry) {
		RecipeType<T> recipeType = injector.getRecipeType();
		try {
			T recipe = entry.getRecipe(recipeType);

			return recipe != null ? injector.build(recipe, StrictNBTIngredient.of(createPureOre(entry.getId()))) : null;
		} catch (Exception e) {
			ElementalCraftApi.LOGGER.error("Error in pure ore recipe injection", e);
			return null;
		}
	}

	private static class Entry {

		private int color = -1;
		private PureOre ore;
		private PureOre rawMaterial;

		public Component getDescription() {
			if (ore != null) {
				return ore.getDescription();
			} else if (rawMaterial != null) {
				return rawMaterial.getDescription();
			}
			return null;
		}

		public boolean test(ItemStack ore) {
			return (this.ore != null && this.ore.getIngredient().test(ore)) || (this.rawMaterial != null && this.rawMaterial.getIngredient().test(ore));
		}

		public boolean isProcessable() {
			return (ore != null && ore.isProcessable() || (rawMaterial != null && rawMaterial.isProcessable()));
		}

		public List<IPurifierRecipe> getRecipes() {
			var list = new ArrayList<IPurifierRecipe>();

			if (ore != null) {
				var recipe = ore.getRecipe();

				if (recipe != null) {
					list.add(recipe);
				}
			}
			if (rawMaterial != null) {
				var recipe = rawMaterial.getRecipe();

				if (recipe != null) {
					list.add(recipe);
				}
			}
			return list;
		}

		@OnlyIn(Dist.CLIENT)
		private int getColor() {
			if (color == -1) {
				if (ore != null) {
					color = ECItem.lookupColor(ore.getResultForColor());
				} else if (rawMaterial != null) {
					color = ECItem.lookupColor(rawMaterial.getResultForColor());
				}
			}
			return color;
		}

	}
}
