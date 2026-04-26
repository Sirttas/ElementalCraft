package sirttas.elementalcraft.recipe.spell;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.recipe.ECRecipeBookCategories;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.ECRecipeTypes;

import javax.annotation.Nonnull;
import java.util.List;

public class SpellCraftRecipe implements Recipe<@NotNull RecipeInput> {

	public static final String NAME = "spell_craft";
    public static final MapCodec<SpellCraftRecipe> CODEC =  RecordCodecBuilder.mapCodec(builder -> builder.group(
            CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo),
            Ingredient.CODEC.fieldOf(ECNames.GEM).forGetter(r -> r.gem),
            Ingredient.CODEC.fieldOf(ECNames.CRYSTAL).forGetter(r -> r.crystal),
            ItemStackTemplate.MAP_CODEC.fieldOf(ECNames.RESULT).forGetter(r -> r.result)
    ).apply(builder, SpellCraftRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull SpellCraftRecipe> STREAM_CODEC = StreamCodec.composite(
            CommonInfo.STREAM_CODEC, r -> r.commonInfo,
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.gem,
            Ingredient.CONTENTS_STREAM_CODEC, r -> r.crystal,
            ItemStackTemplate.STREAM_CODEC, r -> r.result,
            SpellCraftRecipe::new);

    private final Recipe.CommonInfo commonInfo;
    private final Ingredient scrollPaper;
	private final Ingredient gem;
	private final Ingredient crystal;
	private final ItemStackTemplate result;

	public SpellCraftRecipe(Recipe.CommonInfo commonInfo, Ingredient gem, Ingredient crystal, ItemStackTemplate result) {
		this.commonInfo = commonInfo;
        this.result = result;
        this.scrollPaper = Ingredient.of(ECItems.SCROLL_PAPER.get());
		this.gem = gem;
		this.crystal = crystal;
	}
	
	@Override
	public boolean matches(RecipeInput inv, @Nonnull Level level) {
		return scrollPaper.test(inv.getItem(0)) && gem.test(inv.getItem(1)) && crystal.test(inv.getItem(2));
	}

    @Override
    public @NotNull ItemStack assemble(RecipeInput input) {
        return result.create();
    }

    @Override
    public boolean showNotification() {
        return commonInfo.showNotification();
    }

    @Override
    public @NotNull String group() {
        return NAME;
    }

	@Nonnull
	@Override
	public RecipeSerializer<@NotNull SpellCraftRecipe> getSerializer() {
		return ECRecipeSerializers.SPELL_CRAFT.get();
	}

	@Nonnull
	@Override
	public RecipeType<@NotNull SpellCraftRecipe> getType() {
		return ECRecipeTypes.SPELL_CRAFT.get();
	}

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(List.of(scrollPaper, gem, crystal));
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return ECRecipeBookCategories.SPELL_CRAFT.get();
    }

    @Override
    public @NotNull List<RecipeDisplay> display() {
        return List.of(new SpellCraftRecipeDisplay(
                gem.display(),
                crystal.display(),
                new SlotDisplay.ItemStackSlotDisplay(this.result),
                new SlotDisplay.ItemSlotDisplay(ECBlocks.BINDER.get().asItem())));
    }
}
