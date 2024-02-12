package sirttas.elementalcraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;

public class SpellCraftRecipe implements IECRecipe<Container> {

	public static final String NAME = "spell_craft";

	public static final Codec<SpellCraftRecipe> CODEC =  RecordCodecBuilder.create(builder -> builder.group(
			Ingredient.CODEC.fieldOf(ECNames.GEM).forGetter(r -> r.gem),
			Ingredient.CODEC.fieldOf(ECNames.CRYSTAL).forGetter(r -> r.crystal),
			ItemStack.CODEC.fieldOf(ECNames.OUTPUT).forGetter(r -> r.output)
	).apply(builder, SpellCraftRecipe::new));

	private static final Ingredient SCROLL_PAPER = Ingredient.of(ECItems.SCROLL_PAPER.get());
	
	private final Ingredient gem;
	private final Ingredient crystal;
	private final ItemStack output;
	
	public SpellCraftRecipe(Ingredient gem, Ingredient crystal, ItemStack output) {
		this.output = output;
		this.gem = gem;
		this.crystal = crystal;
	}
	
	@Override
	public boolean matches(Container inv, @Nonnull Level level) {
		return SCROLL_PAPER.test(inv.getItem(0)) && gem.test(inv.getItem(1)) && crystal.test(inv.getItem(2));
	}

	@Nonnull
	@Override
	public ItemStack getResultItem(@Nonnull RegistryAccess registry) {
		return output;
	}
	
	@Nonnull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, SCROLL_PAPER, gem, crystal);
	}

	@Nonnull
	@Override
	public RecipeSerializer<SpellCraftRecipe> getSerializer() {
		return ECRecipeSerializers.SPELL_CRAFT.get();
	}

	@Nonnull
	@Override
	public RecipeType<SpellCraftRecipe> getType() {
		return ECRecipeTypes.SPELL_CRAFT.get();
	}
	
	public static class Serializer implements RecipeSerializer<SpellCraftRecipe> {

		@Override
		@Nonnull
		public Codec<SpellCraftRecipe> codec() {
			return CODEC;
		}

		@Override
		public SpellCraftRecipe fromNetwork(@Nonnull FriendlyByteBuf buffer) {
			Ingredient gem = Ingredient.fromNetwork(buffer);
			Ingredient crystal = Ingredient.fromNetwork(buffer);
			ItemStack output = buffer.readItem();

			return new SpellCraftRecipe(gem, crystal, output);
		}

		@Override
		public void toNetwork(@Nonnull FriendlyByteBuf buffer, SpellCraftRecipe recipe) {
			recipe.gem.toNetwork(buffer);
			recipe.crystal.toNetwork(buffer);
			buffer.writeItem(recipe.output);
		}
	}

}
