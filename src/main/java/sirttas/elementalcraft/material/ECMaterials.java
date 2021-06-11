package sirttas.elementalcraft.material;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;

public class ECMaterials {

	public static final Material SOURCE = new Material(MaterialColor.NONE, false, false, false, false, false, true, PushReaction.DESTROY);

	private ECMaterials() {}
	
}
