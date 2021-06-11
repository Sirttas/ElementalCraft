package sirttas.elementalcraft.recipe;

import com.google.gson.JsonObject;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;
import sirttas.elementalcraft.item.spell.FocusItem;
import sirttas.elementalcraft.item.spell.StaffItem;
import sirttas.elementalcraft.spell.SpellHelper;

public class StaffRecipe extends ShapedRecipe implements IECRecipe<CraftingInventory> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + StaffItem.NAME) public static final IRecipeSerializer<ShapedRecipe> SERIALIZER = null;
	
	private StaffRecipe(ShapedRecipe parent) {
		super(parent.getId(), parent.getGroup(), parent.getWidth(), parent.getHeight(), parent.getIngredients(), parent.getResultItem());
	}

	@Override
	public ItemStack assemble(CraftingInventory inv) {
		ItemStack staff = this.getResultItem().copy();
		
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			Item item = stack.getItem();
			
			if (item instanceof FocusItem) {
				SpellHelper.copySpells(stack, staff);
				copyToolInfusion(stack, staff);
			} else if (item instanceof SwordItem) {
				EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(stack), staff);
				if (ToolInfusionHelper.getInfusion(staff) == null) {
					copyToolInfusion(stack, staff);
				}
			}
		}
		return staff;
	}
	
	private void copyToolInfusion(ItemStack source, ItemStack target) {
		ToolInfusion infusion = ToolInfusionHelper.getInfusion(source);
		
		if (infusion != null) {
			ToolInfusionHelper.setInfusion(target, infusion);
		}
	}
	
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
	
	public static class Serializer extends ShapedRecipe.Serializer {

		@Override
		public ShapedRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			return new StaffRecipe(super.fromJson(recipeId, json));
		}

		@Override
		public ShapedRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
			return new StaffRecipe(super.fromNetwork(recipeId, buffer));
		}
	}
}
