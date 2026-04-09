package sirttas.elementalcraft.pureore;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.item.ECItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PureOreManager {

	private static final PureOreManager INSTANCE = new PureOreManager();

	private final Map<Identifier, PureOre> pureOres = new HashMap<>();

	private PureOreManager() { }

	public static PureOreManager getInstance() {
		return INSTANCE;
	}

	public boolean isValidOre(ItemStack ore) {
		return pureOres.values().stream().anyMatch(pureOre -> pureOre.test(ore));
	}

	public ItemStack createPureOre(Identifier id) {
		if (this.pureOres.containsKey(id)) {
			ItemStack stack = new ItemStack(ECItems.PURE_ORE);
	
			stack.set(ECDataComponents.PURE_ORE, id);
			return stack;
		}
		return ItemStack.EMPTY;
	}

    public ItemStackTemplate createPureOreTemplate(Identifier id, int size) {
        if (this.pureOres.containsKey(id)) {
            return new ItemStackTemplate(ECItems.PURE_ORE, size, DataComponentPatch.builder()
                    .set(ECDataComponents.PURE_ORE.get(), id)
                    .build());
        }
        return null;
    }

	void replacePureOres(Map<Identifier, PureOre> pureOres) {
		this.pureOres.clear();
		this.pureOres.putAll(pureOres);
	}

	Map<Identifier, PureOre> getPureOres() {
		return Map.copyOf(pureOres);
	}

	public List<Identifier> getOres() {
		return List.copyOf(pureOres.keySet());
	}
}
