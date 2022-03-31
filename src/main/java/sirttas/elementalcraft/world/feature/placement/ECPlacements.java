package sirttas.elementalcraft.world.feature.placement;

import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class ECPlacements {

	public static final PlacementModifierType<SourcePlacement> SOURCE = () -> SourcePlacement.CODEC;

	private ECPlacements() {}

}
