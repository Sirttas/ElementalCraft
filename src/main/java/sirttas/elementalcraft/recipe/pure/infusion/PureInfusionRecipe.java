package sirttas.elementalcraft.recipe.pure.infusion;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlock;
import sirttas.elementalcraft.recipe.ECRecipeBookCategories;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class PureInfusionRecipe implements Recipe<@NotNull PureInfusionRecipeInput> {

	public static final String NAME = "pureinfusion";
	private static final Codec<Map<ElementType, Ingredient>> INGREDIENTS_CODEC = RecordCodecBuilder.create(builder -> builder.group(
			Ingredient.CODEC.fieldOf(PureInfuserBlock.NAME).forGetter(i -> i.get(ElementType.NONE)),
			Ingredient.CODEC.fieldOf(ElementType.FIRE.getSerializedName()).forGetter(i -> i.get(ElementType.FIRE)),
			Ingredient.CODEC.fieldOf(ElementType.WATER.getSerializedName()).forGetter(i -> i.get(ElementType.WATER)),
			Ingredient.CODEC.fieldOf(ElementType.EARTH.getSerializedName()).forGetter(i -> i.get(ElementType.EARTH)),
			Ingredient.CODEC.fieldOf(ElementType.AIR.getSerializedName()).forGetter(i -> i.get(ElementType.AIR))
	).apply(builder, (i, f, w, e, a) -> Map.of(ElementType.NONE, i, ElementType.FIRE, f, ElementType.WATER, w, ElementType.EARTH, e, ElementType.AIR, a)));
	public static final MapCodec<PureInfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo),
			Codec.INT.fieldOf(ECNames.ELEMENT_AMOUNT).forGetter(PureInfusionRecipe::getElementAmount),
			INGREDIENTS_CODEC.fieldOf(ECNames.INGREDIENTS).forGetter(r -> r.ingredients),
            ItemStackTemplate.CODEC.fieldOf(ECNames.RESULT).forGetter(r -> r.result)
	).apply(builder, PureInfusionRecipe::new));

    private static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull Map<ElementType, Ingredient>> INGREDIENTS_STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, i -> i.get(ElementType.NONE),
            Ingredient.CONTENTS_STREAM_CODEC, i -> i.get(ElementType.FIRE),
            Ingredient.CONTENTS_STREAM_CODEC, i -> i.get(ElementType.WATER),
            Ingredient.CONTENTS_STREAM_CODEC, i -> i.get(ElementType.EARTH),
            Ingredient.CONTENTS_STREAM_CODEC, i -> i.get(ElementType.AIR),
            (i, f, w, e, a) -> Map.of(ElementType.NONE, i, ElementType.FIRE, f, ElementType.WATER, w, ElementType.EARTH, e, ElementType.AIR, a));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull PureInfusionRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, r -> r.commonInfo,
            ByteBufCodecs.INT, PureInfusionRecipe::getElementAmount,
            INGREDIENTS_STREAM_CODEC, r -> r.ingredients,
            ItemStackTemplate.STREAM_CODEC, r -> r.result,
            PureInfusionRecipe::new);

    private final CommonInfo commonInfo;
	private final Map<ElementType, Ingredient> ingredients;
	private final ItemStackTemplate result;
	private final int elementAmount;

	public PureInfusionRecipe(CommonInfo commonInfo, int elementAmount, Map<ElementType, Ingredient> ingredients, ItemStackTemplate result) {
        this.commonInfo = commonInfo;
		this.ingredients = Maps.immutableEnumMap(ingredients);
		this.result = result;
		this.elementAmount = elementAmount;
	}

	@Override
	public boolean matches(@Nonnull PureInfusionRecipeInput input, @Nonnull Level level) {
		return ingredients.get(ElementType.NONE).test(input.pureInfuserInput())
				&& ingredients.get(ElementType.FIRE).test(input.getStackInPedestal(ElementType.FIRE))
				&& ingredients.get(ElementType.WATER).test(input.getStackInPedestal(ElementType.WATER))
				&& ingredients.get(ElementType.EARTH).test(input.getStackInPedestal(ElementType.EARTH))
				&& ingredients.get(ElementType.AIR).test(input.getStackInPedestal(ElementType.AIR));
	}

	@Nonnull
	@Override
	public RecipeSerializer<@NotNull PureInfusionRecipe> getSerializer() {
		return ECRecipeSerializers.PURE_INFUSION.get();
	}

	@Nonnull
	@Override
	public RecipeType<@NotNull PureInfusionRecipe> getType() {
		return ECRecipeTypes.PURE_INFUSION.get();
	}

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(List.copyOf(ingredients.values()));
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ECRecipeBookCategories.PURE_INFUSION.get();
    }

    @Override
	public @NotNull ItemStack assemble(@Nonnull PureInfusionRecipeInput input) {
		var result = this.result.create();
		var target = result.getCapability(ElementalCraftCapabilities.ElementStorages.ITEM);

		if (target == null) {
			return result;
		}

		for (var stack : input.getStacksInPedestals()) {
			var storage = stack.getCapability(ElementalCraftCapabilities.ElementStorages.ITEM);

			if (storage != null) {
				storage.transferAll(target);
			}
		}
		return result;
	}

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NotNull String group() {
        return NAME;
    }

    public int getElementAmount() {
		return elementAmount;
	}

    @Override
    public @NotNull List<RecipeDisplay> display() {
        return List.of(new PureInfusionRecipeDisplay(
                getElementAmount(),
                ingredients.get(ElementType.NONE).display(),
                ingredients.get(ElementType.FIRE).display(),
                ingredients.get(ElementType.WATER).display(),
                ingredients.get(ElementType.EARTH).display(),
                ingredients.get(ElementType.AIR).display(),
                new SlotDisplay.ItemStackSlotDisplay(this.result),
                new SlotDisplay.ItemSlotDisplay(ECBlocks.BINDER.get().asItem())));
    }
}
