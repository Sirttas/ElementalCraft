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
        var template = createPureOreTemplate(id, 1);

        if (template == null) {
            return null;
        }
        return template.create();
	}

    public ItemStackTemplate createPureOreTemplate(Identifier id, int size) {
        return this.pureOres.containsKey(id) ? new ItemStackTemplate(ECItems.PURE_ORE, size, DataComponentPatch.builder()
                .set(ECDataComponents.PURE_ORE.get(), id)
                .build()) : null;
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
