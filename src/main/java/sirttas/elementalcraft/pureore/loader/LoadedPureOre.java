package sirttas.elementalcraft.pureore.loader;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sirttas.elementalcraft.pureore.PureOreManager;
import sirttas.elementalcraft.recipe.instrument.io.purification.OrePurificationRecipe;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoadedPureOre {

    private static final String MINECRAFT = "minecraft";
    private static final String DEEPSLATE = "deepslate";

    private static final Comparator<Identifier> MINECRAFT_NAMESPACE_COMPARATOR = (name1, name2) -> {
        if (MINECRAFT.equals(name1.getNamespace()) && !MINECRAFT.equals(name2.getNamespace())) {
            return -1;
        } else if (!MINECRAFT.equals(name1.getNamespace()) && MINECRAFT.equals(name2.getNamespace())) {
            return 1;
        }
        return 0;
    };

    private static final Comparator<Identifier> DEEPSLATE_COMPARATOR = (name1, name2) -> {
        if (name1.getPath().contains(DEEPSLATE) && !name2.getPath().contains(DEEPSLATE)) {
            return 1;
        } else if (!name1.getPath().contains(DEEPSLATE) && name2.getPath().contains(DEEPSLATE)) {
            return -1;
        }
        return 0;
    };

    private static final Comparator<Item> DESCRIPTION_COMPARATOR = Comparator.comparing(BuiltInRegistries.ITEM::getKey, MINECRAFT_NAMESPACE_COMPARATOR.thenComparing(DEEPSLATE_COMPARATOR).thenComparing(Identifier::compareTo));

    private final Identifier id;
    private final Set<Holder<@NotNull Item>> ores;
    private final Map<RecipeType<?>, Recipe<?>> recipes;

    private ItemStack resultForColor;
    private final int elementConsumption;

    private final int inputSize;
    private final int outputSize;
    private final double luckRatio;

    public LoadedPureOre(Identifier id, int elementConsumption, int inputSize, int outputSize, double luckRatio) {
        this.id = id;
        this.ores = new HashSet<>();
        recipes = new HashMap<>();
        this.resultForColor = ItemStack.EMPTY;
        this.elementConsumption = elementConsumption;
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.luckRatio = luckRatio;
    }

    public Ingredient getInput() {
        return Ingredient.of(getOres().stream()
                .map(Holder::value));
    }

    private ItemStackTemplate getOutput() {
        return PureOreManager.getInstance().createPureOreTemplate(id, outputSize);
    }

    public Identifier getId() {
        return id;
    }

    public Set<Holder<@NotNull Item>> getOres() {
        return ores;
    }

    public boolean isProcessable() {
        return !ores.isEmpty() && !recipes.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <C extends RecipeInput, T extends Recipe<@NotNull C>> T getRecipe(RecipeType<@NotNull T> recipeType) {
        return (T) recipes.get(recipeType);
    }

    public <C extends RecipeInput, T extends Recipe<@NotNull C>> void addRecipe(@Nonnull T recipe, ItemStack output) {
        recipes.computeIfAbsent(recipe.getType(), t -> {
            if (resultForColor.isEmpty()) {
                this.resultForColor = output;
            }
            return recipe;
        });
    }

    public void addTag(TagKey<@NotNull Item> tag) {
        addTag(ECTags.Items.getTag(tag));
    }

    public void addTag(HolderSet.Named<@NotNull Item> tag) {
        tag.forEach(ores::add);
    }

    public boolean contains(Holder<@NotNull Item> item) {
        return ores.stream().anyMatch(item::is);
    }

    @Nullable
    public OrePurificationRecipe getOrePurificationRecipe() {
        var result = getOutput();

        if (result == null) {
            return null;
        }
        return new OrePurificationRecipe(new Recipe.CommonInfo(false), elementConsumption, luckRatio, getInput(), inputSize, result);
    }

    public ItemStack getResultForColor() {
        return resultForColor;
    }

}
