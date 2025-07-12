package sirttas.elementalcraft.pureore;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.item.ECItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PureOreManager {

	private static final PureOreManager INSTANCE = new PureOreManager();

	private final Map<ResourceLocation, PureOre> pureOres = new HashMap<>();

	private PureOreManager() { }

	public static PureOreManager getInstance() {
		return INSTANCE;
	}

	public boolean isValidOre(ItemStack ore) {
		return pureOres.values().stream().anyMatch(pureOre -> pureOre.test(ore));
	}

	public ItemStack createPureOre(ResourceLocation id) {
		if (this.pureOres.containsKey(id)) {
			ItemStack stack = new ItemStack(ECItems.PURE_ORE);
	
			stack.set(ECDataComponents.PURE_ORE, id);
			return stack;
		}
		return ItemStack.EMPTY;
	}

	void replacePureOres(Map<ResourceLocation, PureOre> pureOres) {
		this.pureOres.clear();
		this.pureOres.putAll(pureOres);
	}

	Map<ResourceLocation, PureOre> getPureOres() {
		return Map.copyOf(pureOres);
	}

	public List<ResourceLocation> getOres() {
		return List.copyOf(pureOres.keySet());
	}
}
