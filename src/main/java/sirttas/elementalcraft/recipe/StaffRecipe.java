package sirttas.elementalcraft.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.spell.SpellHelper;

import javax.annotation.Nonnull;
import java.util.function.Function;

public class StaffRecipe extends ShapedRecipe implements Recipe<@NotNull CraftingInput> {

    public static final MapCodec<StaffRecipe> CODEC = ShapedRecipe.MAP_CODEC.xmap(StaffRecipe::new, Function.identity());
    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull StaffRecipe> STREAM_CODEC = ShapedRecipe.STREAM_CODEC.map(StaffRecipe::new, Function.identity());

	public StaffRecipe(ShapedRecipe parent) {
        super(parent.commonInfo, parent.bookInfo, parent.pattern, parent.result);
	}

	@Nonnull
    @Override
	public ItemStack assemble(@Nonnull CraftingInput input) {
		ItemStack staff = super.assemble(input);
		
		for (int i = 0; i < input.size(); i++) {
			var stack = input.getItem(i);
			
			if (stack.is(ECItems.FOCUS)) {
				SpellHelper.copySpells(stack, staff);
			} else if (stack.is(ItemTags.SWORDS)) {
				EnchantmentHelper.setEnchantments(staff, EnchantmentHelper.getEnchantmentsForCrafting(stack));
			}
		}
		return staff;
	}

}
