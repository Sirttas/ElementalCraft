package sirttas.elementalcraft.interaction.ie;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import blusunrize.immersiveengineering.api.crafting.IESerializableRecipe;
import blusunrize.immersiveengineering.common.util.compat.jei.JEIRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftInteraction;
import sirttas.elementalcraft.api.pureore.factory.IPureOreRecipeFactoryType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.interaction.ie.injector.ArcFurnacePureOreRecipeFactory;
import sirttas.elementalcraft.interaction.ie.injector.CrusherPureOreRecipeFactory;
import sirttas.elementalcraft.interaction.ie.recipe.IECrusherRecipeWrapper;
import sirttas.elementalcraft.pureore.factory.PureOreRecipeFactoryTypes;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.grinding.GrindingRecipe;

import java.util.function.BiConsumer;

public class IEInteraction implements ElementalCraftInteraction {

    @Override
    public boolean isActive() {
        return ModList.get().isLoaded("immersiveengineering");
    }

    @Override
    public void registerPureOreRecipeInjectors(RegisterEvent.RegisterHelper<@NotNull IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry) {
        register(registry, IERecipeTypes.ARC_FURNACE, ArcFurnacePureOreRecipeFactory::new);
        register(registry, IERecipeTypes.CRUSHER, CrusherPureOreRecipeFactory::new);
    }

    private static <T extends IESerializableRecipe> void register(RegisterEvent.RegisterHelper<@NotNull IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry, IERecipeTypes.TypeWithClass<T> type, IPureOreRecipeFactoryType<RecipeInput, IESerializableRecipe> factory) {
        PureOreRecipeFactoryTypes.register(registry, type.type().getId(), factory);
    }

    @Override
    public <I extends RecipeInput, T extends Recipe<I>> T lookupRecipe(@NotNull Level level, @NotNull RecipeType<T> type, @NotNull I recipeInput) {
        if (type == ECRecipeTypes.GRINDING.get()) {
            return (T) lookupGrindingRecipe(level, (SimpleIOInstrumentRecipeInput) recipeInput);
        }
        return null;
    }

    public GrindingRecipe lookupGrindingRecipe(@NotNull Level level, @NotNull SimpleIOInstrumentRecipeInput recipeInput) {
        var recipeHolder = CrusherRecipe.findRecipe(level, recipeInput.getItem(0));

        if (recipeHolder == null) {
            return null;
        }

        var recipe = new IECrusherRecipeWrapper(recipeHolder.value());

        return recipe.matches(recipeInput, level) ? recipe : null;
    }

    public void addCraftingStations(BiConsumer<Object, ItemStack> consumer) {
        consumer.accept(JEIRecipeTypes.CRUSHER, new ItemStack(ECBlocks.WATER_MILL_GRINDSTONE.get()));
        consumer.accept(JEIRecipeTypes.CRUSHER, new ItemStack(ECBlocks.AIR_MILL_GRINDSTONE.get()));
    }

}
