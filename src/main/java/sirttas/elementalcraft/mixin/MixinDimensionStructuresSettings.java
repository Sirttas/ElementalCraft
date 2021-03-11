package sirttas.elementalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.ImmutableMap;

import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.world.feature.structure.ECStructures;

@Mixin(DimensionStructuresSettings.class)
public class MixinDimensionStructuresSettings {

	@Shadow @Mutable private static ImmutableMap<Structure<?>, StructureSeparationSettings> field_236191_b_;
	
	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void addMeteoriteSpreadConfig(CallbackInfo ci) {
		field_236191_b_ = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(field_236191_b_)
				.put(ECStructures.SOURCE_ALTAR, new StructureSeparationSettings(ECConfig.COMMON.sourceAltarDistance.get(), 8, 4847339)).build();
	}

}
