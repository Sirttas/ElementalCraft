package sirttas.elementalcraft.recipe.spell;


import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.recipe.ECRecipeTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID)
public class ClientSpellCraftRecipes {

    private static final List<RecipeHolder<SpellCraftRecipe>> RECIPES = new ArrayList<>();

    @SubscribeEvent
    public static void registerRecipes(RecipesReceivedEvent event) {
        RECIPES.clear();
        RECIPES.addAll(event.getRecipeMap().byType(ECRecipeTypes.SPELL_CRAFT.get()));
    }

    public static Stream<RecipeHolder<SpellCraftRecipe>> getRecipesFor(SpellDeskRecipeInput input, Level level) {
        return input.isEmpty() ? Stream.empty() : RECIPES.stream().filter(r -> r.value().matches(input, level));
    }
}
