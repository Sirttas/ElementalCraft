package sirttas.elementalcraft.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerContainer;

import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class MixinRecipeManager extends SimpleJsonResourceReloadListener {

    protected MixinRecipeManager(Gson pGson, String pDirectory) {
        super(pGson, pDirectory);
    }

    @Inject(at = @At("RETURN"), method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V")
    private void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        reloadCrystallizerRecipes();
    }

    @Inject(at = @At("RETURN"), method = "replaceRecipes(Ljava/lang/Iterable;)V")
    private void replaceRecipes(Iterable<Recipe<?>> recipes, CallbackInfo ci) {
        reloadCrystallizerRecipes();
    }

    private void reloadCrystallizerRecipes() {
        CrystallizerContainer.reload((RecipeManager) (Object) this);
    }

}
