package sirttas.elementalcraft.interaction.ie;

import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import blusunrize.immersiveengineering.api.crafting.IESerializableRecipe;
import blusunrize.immersiveengineering.common.util.compat.jei.JEIRecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.RegisterEvent;
import sirttas.elementalcraft.api.pureore.factory.IPureOreRecipeFactoryType;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.AbstractMillGrindstoneBlockEntity;
import sirttas.elementalcraft.interaction.ie.injector.ArcFurnacePureOreRecipeFactory;
import sirttas.elementalcraft.interaction.ie.injector.CrusherPureOreRecipeFactory;
import sirttas.elementalcraft.interaction.ie.recipe.IECrusherRecipeWrapper;
import sirttas.elementalcraft.pureore.injector.PureOreRecipeFactoryTypes;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

public class IEInteraction {

    private IEInteraction() {}

    public static void registerPureOreRecipeInjectors(RegisterEvent.RegisterHelper<IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry) {
        IEInteraction.register(registry, IERecipeTypes.ARC_FURNACE, ArcFurnacePureOreRecipeFactory::new);
        register(registry, IERecipeTypes.CRUSHER, CrusherPureOreRecipeFactory::new);
    }

    private static <T extends IESerializableRecipe> void register(RegisterEvent.RegisterHelper<IPureOreRecipeFactoryType<?, ? extends Recipe<?>>> registry, IERecipeTypes.TypeWithClass<T> type, IPureOreRecipeFactoryType<Container, IESerializableRecipe> factory) {
        PureOreRecipeFactoryTypes.register(registry, type.type().getId(), factory);
    }

    public static void addAirMillToCrushing(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(ECBlocks.AIR_MILL_GRINDSTONE.get()), JEIRecipeTypes.CRUSHER);
    }

    public static IGrindingRecipe lookupCrusherRecipe(Level level, AbstractMillGrindstoneBlockEntity millGrindstone) {
        return level.getRecipeManager().getRecipeFor(IERecipeTypes.CRUSHER.get(), millGrindstone.getInventory(), level)
                .map(h -> new IECrusherRecipeWrapper(h.value()))
                .filter(r -> r.matches(millGrindstone, level))
                .orElse(null);
    }

}
