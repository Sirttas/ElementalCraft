package sirttas.elementalcraft.material;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;

public class ECMaterials {

	public static final Material SOURCE = new Material(MaterialColor.NONE, false, false, false, false, false, true, PushReaction.DESTROY);

	private ECMaterials() {}
	
}
