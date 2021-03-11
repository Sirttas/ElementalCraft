package sirttas.elementalcraft.gui;

public class ECColorHelper {

	private ECColorHelper() {}
	
	public static int packColor(int red, int green, int blue) {
		return red << 16 | green << 8 | blue;
	}
}
