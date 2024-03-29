package sirttas.elementalcraft.world.feature.placement;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import sirttas.elementalcraft.api.ElementalCraftApi;

public class ECPlacements {

	private static final DeferredRegister<PlacementModifierType<?>> DEFERRED_REGISTER = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, ElementalCraftApi.MODID);

	public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<SourcePlacement>> SOURCE = register(SourcePlacement.CODEC, SourcePlacement.NAME);

	private ECPlacements() {}

	private static <T extends PlacementModifier> DeferredHolder<PlacementModifierType<?>, PlacementModifierType<T>> register(Codec<T> codec, String name) {
		return DEFERRED_REGISTER.register(name, () -> () -> codec);
	}

	public static void register(IEventBus bus) {
		DEFERRED_REGISTER.register(bus);
	}

}
