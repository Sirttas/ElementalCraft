package sirttas.elementalcraft.datagen.loot;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class PipeUpgradeLoot implements LootTableSubProvider {

	private final Map<ResourceKey<@NotNull LootTable>, Builder> map = Maps.newHashMap();

	protected void generate() {
		add(getKey(PipeUpgradeTypes.ELEMENT_PUMP), createRuneable(PipeUpgradeTypes.ELEMENT_PUMP.get()));
		dropSelf(PipeUpgradeTypes.PIPE_PRIORITY_RINGS.get());
		dropSelf(PipeUpgradeTypes.ELEMENT_VALVE.get());
		dropSelf(PipeUpgradeTypes.ELEMENT_BEAM.get());
	}

	@Override
	public void generate(@NotNull BiConsumer<ResourceKey<@NotNull LootTable>, LootTable.Builder> consumer) {
		map.clear();
		generate();
		map.forEach(consumer);
	}

	protected void dropSelf(PipeUpgradeType<?> type) {
		add(getKey(type), createSingleItemTable(type));
	}

	private ResourceKey<@NotNull LootTable> getKey(Supplier<? extends PipeUpgradeType<?>> type) {
		return getKey(type.get());
	}

	@Nullable
	private static ResourceKey<@NotNull LootTable> getKey(PipeUpgradeType<?> type) {
		var key = PipeUpgradeTypes.REGISTRY.getKey(type);

		return key != null ? ResourceKey.create(Registries.LOOT_TABLE, key.withPrefix(PipeUpgrade.FOLDER + '/')) : null;
	}

	protected void add(ResourceKey<@NotNull LootTable> name, Builder builder) {
		map.put(name, builder);
	}

	protected <T extends ConditionUserBuilder<@NotNull T>> T applyExplosionCondition(ConditionUserBuilder<@NotNull T> conditionBuilder) {
		return conditionBuilder.when(ExplosionCondition.survivesExplosion());
	}

	public LootTable.Builder createSingleItemTable(ItemLike item) {
		return LootTable.lootTable().withPool(this.applyExplosionCondition(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(item))));
	}

	public Builder createRuneable(ItemLike item) {
		return createSingleItemTable(item).withPool(ECBlockLoot.dropRunes());
	}
}
