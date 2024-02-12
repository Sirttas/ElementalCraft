package sirttas.elementalcraft.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.spell.FocusItem;
import sirttas.elementalcraft.spell.SpellHelper;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class StaffRecipe extends ShapedRecipe implements IECRecipe<CraftingContainer> {

	public static final Codec<ShapedRecipe> CODEC = RecipeSerializer.SHAPED_RECIPE.codec().<ShapedRecipe>xmap(StaffRecipe::new, Function.identity()).stable();

	public StaffRecipe(ShapedRecipe parent) {
		super(parent.getGroup(), parent.category(), parent.pattern, new ItemStack(ECItems.STAFF.get()), parent.showNotification());
	}

	@Nonnull
    @Override
	public ItemStack assemble(@Nonnull CraftingContainer inv, @Nonnull RegistryAccess registry) {
		ItemStack staff = this.getResultItem(registry).copy();
		
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

		@Override
		@Nonnull
		public Codec<ShapedRecipe> codec() {
			return CODEC;
		}

		@Override
		public ShapedRecipe fromNetwork(@Nonnull FriendlyByteBuf buffer) {
			return new StaffRecipe(super.fromNetwork(buffer));
		}
	}
}
