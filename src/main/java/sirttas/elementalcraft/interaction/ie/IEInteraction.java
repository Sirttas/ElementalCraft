package sirttas.elementalcraft.interaction.ie;

import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import blusunrize.immersiveengineering.common.util.compat.jei.JEIRecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.api.pureore.injector.AbstractPureOreRecipeInjector;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AirMillGrindstoneBlockEntity;
import sirttas.elementalcraft.interaction.ie.injector.ArcFurnacePureOreRecipeInjector;
import sirttas.elementalcraft.interaction.ie.injector.CrusherPureOreRecipeInjector;
import sirttas.elementalcraft.interaction.ie.recipe.IECrusherRecipeWrapper;
import sirttas.elementalcraft.pureore.injector.PureOreRecipeInjectors;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class IEInteraction {

    private IEInteraction() {}

    public static void registerPureOreRecipeInjectors(IForgeRegistry<AbstractPureOreRecipeInjector<?, ? extends Recipe<?>>> registry) {
        PureOreRecipeInjectors.register(registry, new ArcFurnacePureOreRecipeInjector());
        PureOreRecipeInjectors.register(registry, new CrusherPureOreRecipeInjector());
    }

    public static void addAirMillToCrushing(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(ECBlocks.AIR_MILL_GRINDSTONE.get()), JEIRecipeTypes.CRUSHER);
    }

    public static IGrindingRecipe lookupCrusherRecipe(Level level, AirMillGrindstoneBlockEntity airMillGrindstone) {
        return level.getRecipeManager().getRecipeFor(IERecipeTypes.CRUSHER.get(), airMillGrindstone.getInventory(), level)
                .map(IECrusherRecipeWrapper::new)
                .filter(r -> r.matches(airMillGrindstone))
                .orElse(null);
    }

}
