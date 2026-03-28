package sirttas.elementalcraft.rune;

import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;

public class Runes {

	public static final ResourceKey<@NotNull Rune> WII = createKey("wii");
	public static final ResourceKey<@NotNull Rune> FUS = createKey("fus");
	public static final ResourceKey<@NotNull Rune> ZOD = createKey("zod");
	public static final ResourceKey<@NotNull Rune> MANX = createKey("manx");
	public static final ResourceKey<@NotNull Rune> JITA = createKey("jita");
	public static final ResourceKey<@NotNull Rune> TANO = createKey("tano");
	public static final ResourceKey<@NotNull Rune> KIRBY = createKey("kirby");
	public static final ResourceKey<@NotNull Rune> WHALE = createKey("whale");
	public static final ResourceKey<@NotNull Rune> TYRIA = createKey("tyria");
	public static final ResourceKey<@NotNull Rune> SOARYN = createKey("soaryn");
	public static final ResourceKey<@NotNull Rune> KAWORU = createKey("kaworu");
	public static final ResourceKey<@NotNull Rune> MEWTWO = createKey("mewtwo");
	public static final ResourceKey<@NotNull Rune> CLAPTRAP = createKey("claptrap");
	public static final ResourceKey<@NotNull Rune> BOMBADIL = createKey("bombadil");
	public static final ResourceKey<@NotNull Rune> TZEENTCH = createKey("tzeentch");

	public static final ResourceKey<@NotNull Rune> CREATIVE = createKey("creative");

	private Runes() {}

	private static ResourceKey<@NotNull Rune> createKey(String name) {
		return IDataManager.createKey(ElementalCraftApi.RUNE_MANAGER_KEY, ElementalCraftApi.createRL(name));
	}
}
