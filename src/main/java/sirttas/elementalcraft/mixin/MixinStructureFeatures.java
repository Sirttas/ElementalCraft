package sirttas.elementalcraft.mixin;

import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sirttas.elementalcraft.world.feature.ECFeatures;

import java.util.function.BiConsumer;

@Mixin(StructureFeatures.class)
public abstract class MixinStructureFeatures {

    @Inject(method = "registerStructures(Ljava/util/function/BiConsumer;)V", at = @At("RETURN"))
    private static void registerStructuresReturn(BiConsumer<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> consumer, CallbackInfo ci) {
        ECFeatures.registerStructures(consumer);
    }
}
