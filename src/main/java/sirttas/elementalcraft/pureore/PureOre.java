package sirttas.elementalcraft.pureore;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.nbt.NBTHelper;
import sirttas.elementalcraft.recipe.instrument.io.IPurifierRecipe;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PureOre {

    private static final String MINECRAFT = "minecraft";
    private static final String DEEPSLATE = "deepslate";

    private static final Comparator<ResourceLocation> MINECRAFT_NAMESPACE_COMPARATOR = (name1, name2) -> {
        if (MINECRAFT.equals(name1.getNamespace()) && !MINECRAFT.equals(name2.getNamespace())) {
            return -1;
        } else if (!MINECRAFT.equals(name1.getNamespace()) && MINECRAFT.equals(name2.getNamespace())) {
            return 1;
        }
        return 0;
    };

    private static final Comparator<ResourceLocation> DEEPSLATE_COMPARATOR = (name1, name2) -> {
        if (name1.getPath().contains(DEEPSLATE) && !name2.getPath().contains(DEEPSLATE)) {
            return 1;
        } else if (!name1.getPath().contains(DEEPSLATE) && name2.getPath().contains(DEEPSLATE)) {
            return -1;
        }
        return 0;
    };

    private static final Comparator<Item> DESCRIPTION_COMPARATOR = Comparator.comparing(BuiltInRegistries.ITEM::getKey, MINECRAFT_NAMESPACE_COMPARATOR.thenComparing(DEEPSLATE_COMPARATOR).thenComparing(ResourceLocation::compareTo));

    private final ResourceLocation id;
    private final Set<Item> ores;
    private final Map<RecipeType<?>, Recipe<?>> recipes;

    private ItemStack resultForColor;
    private Component description;
    private final int elementConsumption;

    private final int inputSize;
    private final int outputSize;
    private final double luckRatio;

    public PureOre(ResourceLocation id, int elementConsumption, int inputSize, int outputSize, double luckRatio) {
        this.id = id;
        this.ores = new HashSet<>();
        recipes = new HashMap<>();
        this.resultForColor = ItemStack.EMPTY;
        this.elementConsumption = elementConsumption;
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.luckRatio = luckRatio;
    }

    public Component getDescription() {
        if (this.description == null) {
            this.description = this.ores.stream()
                    .sorted(DESCRIPTION_COMPARATOR)
                    .map(Item::getDescription)
                    .findFirst()
                    .orElse(Component.literal("ERROR no name"));
        }
        return this.description;
    }

    public Ingredient getIngredient() {
        return Ingredient.of(getOres().stream().map(i -> {
            ItemStack stack = new ItemStack(i);

            stack.setCount(inputSize);
            return stack;
        }));
    }

    public ResourceLocation getId() {
        return id;
    }

    public Set<Item> getOres() {
        return ores;
    }

    public boolean isProcessable() {
        return !ores.isEmpty() && !recipes.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public <C extends Container, T extends Recipe<C>> T getRecipe(RecipeType<T> recipeType) {
        return (T) recipes.get(recipeType);
    }

    public <C extends Container, T extends Recipe<C>> void addRecipe(@Nonnull T recipe, ItemStack output) {
        recipes.computeIfAbsent(recipe.getType(), t -> {
            if (resultForColor.isEmpty()) {
                this.resultForColor = output;
            }
            return recipe;
        });
    }

    public void addTag(TagKey<Item> tag) {
        addTag(ECTags.Items.getTag(tag));
    }

    public void addTag(HolderSet.Named<Item> tag) {
        tag.stream()
                .map(Holder::value)
                .forEach(ores::add);
    }

    public boolean contains(Item item) {
        return ores.contains(item);
    }

    public IPurifierRecipe getRecipe() {
        return new PurifierRecipeImpl();
    }

    public ItemStack getResultForColor() {
        return resultForColor;
    }

    public ItemStack createPureOre() {
        ItemStack stack = new ItemStack(ECItems.PURE_ORE.get());

        NBTHelper.getOrCreateECTag(stack).putString(ECNames.ORE, id.toString());
        return stack;
    }

    private class PurifierRecipeImpl implements IPurifierRecipe {

        @Override
        public int getElementAmount() {
            return elementConsumption;
        }

        @Nonnull
        @Override
        public NonNullList<Ingredient> getIngredients() {
            return NonNullList.of(Ingredient.EMPTY, getIngredient());
        }

        @Nonnull
        @Override
        public ItemStack getResultItem(@Nonnull RegistryAccess access) {
            var result =  createPureOre();

            result.setCount(outputSize);
            return result;
        }

        @Override
        public int getInputSize() {
            return inputSize;
        }

        @Override
        public double getLuckRatio() {
            return luckRatio;
        }
    }
}
