package sirttas.elementalcraft.datagen.loot;

import com.google.common.collect.Maps;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PipeUpgradeLoot implements Consumer<BiConsumer<ResourceLocation, Builder>> {

	private final Map<ResourceLocation, Builder> map = Maps.newHashMap();

	protected void addTables() {
		dropSelf(PipeUpgradeTypes.ELEMENT_PUMP.get());
		dropSelf(PipeUpgradeTypes.PIPE_PRIORITY_RINGS.get());
		dropSelf(PipeUpgradeTypes.ELEMENT_VALVE.get());
		dropSelf(PipeUpgradeTypes.ELEMENT_BEAM.get());
	}

	protected void dropSelf(PipeUpgradeType<?> type) {
		add(PipeUpgradeTypes.REGISTRY.get().getKey(type), BlockLoot.createSingleItemTable(type));
	}

	protected void add(ResourceLocation name, Builder builder) {
		map.put(name, builder);
	}

	@Override
	public void accept(BiConsumer<ResourceLocation, Builder> consumer) {
		map.clear();
		addTables();
		map.forEach((k, v) -> consumer.accept(new ResourceLocation(k.getNamespace(), PipeUpgrade.FOLDER + k.getPath()), v));
	}
}
