package sirttas.elementalcraft.loot;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public final class LootHandler {

	private static final List<String> BLACKLIST = ImmutableList.of("dispenser");
	public static final List<String> INJECT_lIST = ImmutableList.<String>builder()
			.addAll(getInjects(EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER, EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.SILVERFISH, EntityType.IRON_GOLEM, EntityType.SKELETON_HORSE,
					EntityType.CREEPER, EntityType.GHAST, EntityType.BLAZE, EntityType.HUSK, EntityType.MAGMA_CUBE, EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOGLIN, EntityType.DROWNED,
					EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN, EntityType.SLIME, EntityType.STRAY, EntityType.SQUID, EntityType.POLAR_BEAR, EntityType.DOLPHIN, EntityType.COD, EntityType.SALMON,
					EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.ENDERMAN, EntityType.SPIDER, EntityType.CAVE_SPIDER, EntityType.PHANTOM, EntityType.SHULKER))
			.build();


	private LootHandler() {}
	
	@SubscribeEvent
	public static void lootLoad(LootTableLoadEvent evt) {
		ResourceLocation name = evt.getName();
		ResourceLocation injectName = ElementalCraft.createRL("inject/" + name.getPath());

		if (INJECT_lIST.contains(name.getPath())) {
			evt.getTable().addPool(getInjectPool(injectName));
		} else if (name.toString().startsWith("minecraft:chests/") && BLACKLIST.stream().anyMatch(name.toString()::contains)) {
			evt.getTable().addPool(getInjectPool(ElementalCraft.createRL("chests/inject")));
		}
	}

	public static LootPool getInjectPool(ResourceLocation name) {
		return LootPool.lootPool().add(LootTableReference.lootTableReference(name).setWeight(1)).bonusRolls(0, 1).name("elementalcraft_inject").build();
	}

	private static List<String> getInjects(EntityType<?>... types) {
		return Stream.of(types).map(type -> type.getDefaultLootTable().getPath()).collect(Collectors.toList());
	}

}