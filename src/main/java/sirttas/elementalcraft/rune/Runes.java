package sirttas.elementalcraft.rune;

import net.minecraft.resources.ResourceKey;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;

public class Runes {

	public static final ResourceKey<Rune> WII = createKey("wii");
	public static final ResourceKey<Rune> FUS = createKey("fus");
	public static final ResourceKey<Rune> ZOD = createKey("zod");
	public static final ResourceKey<Rune> MANX = createKey("manx");
	public static final ResourceKey<Rune> JITA = createKey("jita");
	public static final ResourceKey<Rune> TANO = createKey("tano");
	public static final ResourceKey<Rune> KIRBY = createKey("kirby");
	public static final ResourceKey<Rune> WHALE = createKey("whale");
	public static final ResourceKey<Rune> TYRIA = createKey("tyria");
	public static final ResourceKey<Rune> SOARYN = createKey("soaryn");
	public static final ResourceKey<Rune> KAWORU = createKey("kaworu");
	public static final ResourceKey<Rune> MEWTWO = createKey("mewtwo");
	public static final ResourceKey<Rune> CLAPTRAP = createKey("claptrap");
	public static final ResourceKey<Rune> BOMBADIL = createKey("bombadil");
	public static final ResourceKey<Rune> TZEENTCH = createKey("tzeentch");

	public static final ResourceKey<Rune> CREATIVE = createKey("creative");

	private Runes() {}

	private static ResourceKey<Rune> createKey(String name) {
		return IDataManager.createKey(ElementalCraftApi.RUNE_MANAGER_KEY, ElementalCraftApi.identifier(name));
	}
}
