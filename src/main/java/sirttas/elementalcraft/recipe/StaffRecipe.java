package sirttas.elementalcraft.recipe;

import com.google.gson.JsonObject;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.spell.FocusItem;
import sirttas.elementalcraft.item.spell.StaffItem;
import sirttas.elementalcraft.spell.SpellHelper;

public class StaffRecipe extends ShapedRecipe implements IECRecipe<CraftingContainer> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + StaffItem.NAME) public static final RecipeSerializer<ShapedRecipe> SERIALIZER = null;
	
	private StaffRecipe(ShapedRecipe parent) {
		super(parent.getId(), parent.getGroup(), parent.getWidth(), parent.getHeight(), parent.getIngredients(), parent.getResultItem());
	}

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
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	public static class Serializer extends ShapedRecipe.Serializer {

		@Override
		public ShapedRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			return new StaffRecipe(super.fromJson(recipeId, json));
		}

		@Override
		public ShapedRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			return new StaffRecipe(super.fromNetwork(recipeId, buffer));
		}
	}
}
