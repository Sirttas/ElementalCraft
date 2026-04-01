package sirttas.elementalcraft.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;

public class SpellCraftRecipe implements Recipe<RecipeInput> {

	public static final String NAME = "spell_craft";
	private static final Ingredient SCROLL_PAPER = Ingredient.of(ECItems.SCROLL_PAPER.get());
    public static final MapCodec<SpellCraftRecipe> CODEC =  RecordCodecBuilder.mapCodec(builder -> builder.group(
            Ingredient.CODEC.fieldOf(ECNames.GEM).forGetter(r -> r.gem),
            Ingredient.CODEC.fieldOf(ECNames.CRYSTAL).forGetter(r -> r.crystal),
            ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(r -> r.output)
    ).apply(builder, SpellCraftRecipe::new));
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull SpellCraftRecipe> STREAM_CODEC = StreamCodec.of(SpellCraftRecipe::toNetwork, SpellCraftRecipe::fromNetwork); // TODO rework recipe stream codecs
	
	private final Ingredient gem;
	private final Ingredient crystal;
	private final ItemStack output;
	
	public SpellCraftRecipe(Ingredient gem, Ingredient crystal, ItemStack output) {
		this.output = output;
		this.gem = gem;
		this.crystal = crystal;
	}
	
	@Override
	public boolean matches(RecipeInput inv, @Nonnull Level level) {
		return SCROLL_PAPER.test(inv.getItem(0)) && gem.test(inv.getItem(1)) && crystal.test(inv.getItem(2));
	}

	@Nonnull
	@Override
	public ItemStack getResultItem(@Nonnull HolderLookup.Provider provider) {
		return output;
	}
	
	@Nonnull
	@Override
	public NonNullList<@NotNull Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, SCROLL_PAPER, gem, crystal);
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

    public static SpellCraftRecipe fromNetwork(@Nonnull RegistryFriendlyByteBuf buffer) {
        var gem = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        var crystal = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        var output = ItemStack.STREAM_CODEC.decode(buffer);

        return new SpellCraftRecipe(gem, crystal, output);
    }

    public static void toNetwork(@Nonnull RegistryFriendlyByteBuf buffer, SpellCraftRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.gem);
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.crystal);
        ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
    }
}
