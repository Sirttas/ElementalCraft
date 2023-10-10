package sirttas.elementalcraft.loot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.List;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public final class LootHandler {

	private static final List<String> BLACKLIST = List.of("dispenser");
	public static final List<EntityType<?>> INJECT_LIST = List.of(EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.SILVERFISH,
			EntityType.IRON_GOLEM, EntityType.SKELETON_HORSE, EntityType.CREEPER, EntityType.GHAST, EntityType.BLAZE, EntityType.HUSK, EntityType.MAGMA_CUBE, EntityType.ZOMBIFIED_PIGLIN,
			EntityType.ZOGLIN, EntityType.DROWNED, EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN, EntityType.SLIME, EntityType.STRAY, EntityType.SQUID, EntityType.GLOW_SQUID,
			EntityType.GOAT, EntityType.AXOLOTL, EntityType.POLAR_BEAR, EntityType.DOLPHIN,
			EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.ENDERMAN, EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.PHANTOM,
			EntityType.SHULKER);

	private LootHandler() {}
	
	@SubscribeEvent
	public static void lootLoad(LootTableLoadEvent evt) {
		ResourceLocation name = evt.getName();
		ResourceLocation injectName = ElementalCraft.createRL("inject/" + name.getPath());

		if (INJECT_LIST.stream().anyMatch(t -> StringUtils.equals(t.getDefaultLootTable().getPath(), name.getPath()))) {
			evt.getTable().addPool(getInjectPool(injectName));
		} else if (name.toString().startsWith("minecraft:chests/") && BLACKLIST.stream().anyMatch(name.toString()::contains)) {
			evt.getTable().addPool(getInjectPool(ElementalCraft.createRL("chests/inject")));
		}
	}

	public static LootPool getInjectPool(ResourceLocation name) {
		return LootPool.lootPool().add(LootTableReference.lootTableReference(name).setWeight(1)).setBonusRolls(UniformGenerator.between(0, 1)).name("elementalcraft_inject").build();
	}
}
