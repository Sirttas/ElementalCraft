package sirttas.elementalcraft.world.feature.placement;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECPlacements {

	public static final PlacementModifierType<SourcePlacement> SOURCE = register(ElementalCraft.createRL("source"), SourcePlacement.CODEC);

	private ECPlacements() {}
	
	private static <P extends PlacementModifier> PlacementModifierType<P> register(ResourceLocation name, Codec<P> codec) {
		return Registry.register(Registry.PLACEMENT_MODIFIERS, name, () -> codec);
	}

}
