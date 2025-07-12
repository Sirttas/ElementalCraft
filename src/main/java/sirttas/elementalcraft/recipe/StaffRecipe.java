package sirttas.elementalcraft.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.spell.FocusItem;
import sirttas.elementalcraft.spell.SpellHelper;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class StaffRecipe extends ShapedRecipe implements IECRecipe<CraftingInput> {


	public StaffRecipe(ShapedRecipe parent) {
		super(parent.getGroup(), parent.category(), parent.pattern, new ItemStack(ECItems.STAFF), parent.showNotification());
	}

	@Nonnull
    @Override
	public ItemStack assemble(@Nonnull CraftingInput input, @Nonnull HolderLookup.Provider provider) {
		ItemStack staff = this.getResultItem(provider).copy();
		
		for (int i = 0; i < input.size(); i++) {
			var stack = input.getItem(i);
			var item = stack.getItem();
			
			if (item instanceof FocusItem) {
				SpellHelper.copySpells(stack, staff);
			} else if (item instanceof SwordItem) {
				EnchantmentHelper.setEnchantments(staff, EnchantmentHelper.getEnchantmentsForCrafting(stack));
			}
		}
		return staff;
	}
	
	@Nonnull
    @Override
	public RecipeSerializer<?> getSerializer() {
		return ECRecipeSerializers.STAFF.get();
	}
	
	public static class Serializer implements RecipeSerializer<StaffRecipe> {

		public static final MapCodec<StaffRecipe> CODEC = RecipeSerializer.SHAPED_RECIPE.codec().xmap(StaffRecipe::new, Function.identity());
		public static final StreamCodec<RegistryFriendlyByteBuf, StaffRecipe> STREAM_CODEC = RecipeSerializer.SHAPED_RECIPE.streamCodec().map(StaffRecipe::new, Function.identity());

		@Override
		@Nonnull
		public MapCodec<StaffRecipe> codec() {
			return CODEC;
		}

		@Override
		public @NotNull StreamCodec<RegistryFriendlyByteBuf, StaffRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
