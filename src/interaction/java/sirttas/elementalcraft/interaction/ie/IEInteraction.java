package sirttas.elementalcraft.interaction.ie;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import blusunrize.immersiveengineering.api.crafting.IESerializableRecipe;
import blusunrize.immersiveengineering.common.util.compat.jei.JEIRecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraftInteraction;
import sirttas.elementalcraft.api.pureore.factory.IPureOreRecipeFactoryType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.ie.injector.ArcFurnacePureOreRecipeFactory;
import sirttas.elementalcraft.interaction.ie.injector.CrusherPureOreRecipeFactory;
import sirttas.elementalcraft.interaction.ie.recipe.IECrusherRecipeWrapper;
import sirttas.elementalcraft.pureore.factory.PureOreRecipeFactoryTypes;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;

public class IEInteraction implements ElementalCraftInteraction {

    @Override
    public void registerPureOreRecipeInjectors(RegisterEvent.RegisterHelper<@NotNull IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry) {
        register(registry, IERecipeTypes.ARC_FURNACE, ArcFurnacePureOreRecipeFactory::new);
        register(registry, IERecipeTypes.CRUSHER, CrusherPureOreRecipeFactory::new);
    }

    private static <T extends IESerializableRecipe> void register(RegisterEvent.RegisterHelper<@NotNull IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry, IERecipeTypes.TypeWithClass<T> type, IPureOreRecipeFactoryType<RecipeInput, IESerializableRecipe> factory) {
        PureOreRecipeFactoryTypes.register(registry, type.type().getId(), factory);
    }

    public static void addMillsToCrushing(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(ECBlocks.WATER_MILL_GRINDSTONE.get()), JEIRecipeTypes.CRUSHER);
        registry.addRecipeCatalyst(new ItemStack(ECBlocks.AIR_MILL_GRINDSTONE.get()), JEIRecipeTypes.CRUSHER);
    }

    @Override
    public GrindingRecipe lookupCrusherRecipe(@NotNull Level level, @NotNull SimpleIOInstrumentRecipeInput recipeInput) {
        var recipeHolder = CrusherRecipe.findRecipe(level, recipeInput.getItem(0));

        if (recipeHolder == null) {
            return null;
        }

        var recipe = new IECrusherRecipeWrapper(recipeHolder.value());

        return recipe.matches(recipeInput, level) ? recipe : null;
    }

}
