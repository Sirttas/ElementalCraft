package sirttas.elementalcraft.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import sirttas.elementalcraft.item.spell.FocusItem;
import sirttas.elementalcraft.spell.SpellHelper;

import javax.annotation.Nonnull;

public class StaffRecipe extends ShapedRecipe implements IECRecipe<CraftingContainer> {
	
	private StaffRecipe(ShapedRecipe parent) {
		super(parent.getId(), parent.getGroup(), parent.getWidth(), parent.getHeight(), parent.getIngredients(), parent.getResultItem());
	}

	@Nonnull
    @Override
	public ItemStack assemble(CraftingContainer inv) {
		ItemStack staff = this.getResultItem().copy();
		
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			Item item = stack.getItem();
			
			if (item instanceof FocusItem) {
				SpellHelper.copySpells(stack, staff);
			} else if (item instanceof SwordItem) {
				EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(stack), staff);
			}
		}
		return staff;
	}
	
	@Nonnull
    @Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.STAFF.get();
	}
	
	public static class Serializer extends ShapedRecipe.Serializer {

		@Nonnull
        @Override
		public ShapedRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			return new StaffRecipe(super.fromJson(recipeId, json));
		}

		@Override
		public ShapedRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
			return new StaffRecipe(super.fromNetwork(recipeId, buffer));
		}
	}
}
