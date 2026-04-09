package sirttas.elementalcraft.mixin;

import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerContainer;

@Mixin(RecipeManager.class)
public abstract class MixinRecipeManager extends SimplePreparableReloadListener<@NotNull RecipeMap> {

    @Inject(at = @At("RETURN"), method = "finalizeRecipeLoading(Lnet/minecraft/world/flag/FeatureFlagSet;)V")
    private void finalizeRecipeLoading$return(FeatureFlagSet featureFlagSet, CallbackInfo ci) {
        reloadCrystallizerRecipes();
    }

    @Unique
    private void reloadCrystallizerRecipes() {
        CrystallizerContainer.reload((RecipeManager) (Object) this);
    }

}
