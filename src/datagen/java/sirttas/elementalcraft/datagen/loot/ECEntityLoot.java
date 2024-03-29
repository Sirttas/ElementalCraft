package sirttas.elementalcraft.datagen.loot;

import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.entity.ECEntities;
import sirttas.elementalcraft.item.elemental.ElementalItemHelper;
import sirttas.elementalcraft.loot.LootHandler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ECEntityLoot extends EntityLootSubProvider {

	private static final LootPool.Builder FIRE = createShardPool(ElementType.FIRE);
	private static final LootPool.Builder EARTH = createShardPool(ElementType.EARTH);
	private static final LootPool.Builder WATER = createShardPool(ElementType.WATER);
	private static final LootPool.Builder AIR = createShardPool(ElementType.AIR);

	private static final List<EntityType<?>> ENTITIES = Util.make(() -> {
		var list = new ArrayList<EntityType<?>>(LootHandler.INJECT_LIST.size() + 1);

		list.add(ECEntities.THROWN_ELEMENT_CRYSTAL.get());
		list.addAll(LootHandler.INJECT_LIST);
		return List.copyOf(list);
	});

	protected ECEntityLoot() {
		super(FeatureFlags.REGISTRY.allFlags(), FeatureFlags.REGISTRY.subset());
	}

	@Override
	public void generate() {
		addThrownElementCrystal(ElementType.FIRE);
		addThrownElementCrystal(ElementType.WATER);
		addThrownElementCrystal(ElementType.EARTH);
		addThrownElementCrystal(ElementType.AIR);

		addInject(EARTH, EntityType.ZOMBIE);
		addInject(EARTH, EntityType.ZOMBIE_VILLAGER);
		addInject(EARTH, EntityType.SKELETON);
		addInject(EARTH, EntityType.WITHER_SKELETON);
		addInject(EARTH, EntityType.SILVERFISH);
		addInject(EARTH, EntityType.IRON_GOLEM);
		addInject(EARTH, EntityType.SKELETON_HORSE);
		addInject(EARTH, EntityType.GOAT);
		addInject(FIRE, EntityType.CREEPER);
		addInject(FIRE, EntityType.GHAST);
		addInject(FIRE, EntityType.BLAZE);
		addInject(FIRE, EntityType.HUSK);
		addInject(FIRE, EntityType.MAGMA_CUBE);
		addInject(FIRE, EntityType.ZOMBIFIED_PIGLIN);
		addInject(FIRE, EntityType.ZOGLIN);
		addInject(WATER, EntityType.DROWNED);
		addInject(WATER, EntityType.GUARDIAN);
		addInject(WATER, EntityType.ELDER_GUARDIAN);
		addInject(WATER, EntityType.SLIME);
		addInject(WATER, EntityType.STRAY);
		addInject(WATER, EntityType.SQUID);
		addInject(WATER, EntityType.GLOW_SQUID);
		addInject(WATER, EntityType.AXOLOTL);
		addInject(WATER, EntityType.POLAR_BEAR);
		addInject(WATER, EntityType.DOLPHIN);
		addInject(WATER, EntityType.COD);
		addInject(WATER, EntityType.SALMON);
		addInject(WATER, EntityType.TROPICAL_FISH);
		addInject(WATER, EntityType.PUFFERFISH);
		addInject(AIR, EntityType.ENDERMAN);
		addInject(AIR, EntityType.SPIDER);
		addInject(AIR, EntityType.CAVE_SPIDER);
		addInject(AIR, EntityType.PHANTOM);
		addInject(AIR, EntityType.SHULKER);
	}

	private void addThrownElementCrystal(ElementType type) {
		var crystalLocation = BuiltInRegistries.ITEM.getKey(ElementalItemHelper.getCrystalForType(type));

		add(ECEntities.THROWN_ELEMENT_CRYSTAL.get(), new ResourceLocation(crystalLocation.getNamespace(), "entities/thrown_element_crystal/" + crystalLocation.getPath()), LootTable.lootTable().withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(ElementalItemHelper.getShardForType(type)).apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 7))).setWeight(10))
						.add(LootItem.lootTableItem(ElementalItemHelper.getPowerfulShardForType(type))))
				.setParamSet(LootContextParamSets.SELECTOR));
	}

	private static LootPool.Builder createShardPool(ElementType type) {
		return LootPool.lootPool().setRolls(ConstantValue.exactly(1))
				.add(LootItem.lootTableItem(ElementalItemHelper.getShardForType(type)).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))).setWeight(10))
				.add(LootItem.lootTableItem(ElementalItemHelper.getPowerfulShardForType(type)).when(LootItemKilledByPlayerCondition.killedByPlayer()))
				.when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.25F, 0.03F));
	}

	private void addInject(LootPool.Builder pool, EntityType<?> entityType) {
		addInject(entityType, LootTable.lootTable().withPool(pool).setParamSet(LootContextParamSets.ENTITY), ElementalCraftApi.createRL(entityType.getDefaultLootTable().getPath()));
	}

	private void addInject(EntityType<?> entityType, LootTable.Builder builder, ResourceLocation location) {
		if (!LootHandler.INJECT_LIST.contains(entityType)) {
			throw new IllegalStateException(MessageFormat.format("{} is not present in LootHandler.INJECT_LIST and will not be injected at runtime!", location));
		}
		add(entityType, new ResourceLocation(location.getNamespace(), "inject/" + location.getPath()), builder);
	}

	@Override
	protected boolean canHaveLootTable(@NotNull EntityType<?> entityType) {
		return ENTITIES.contains(entityType);
	}
}
