package sirttas.elementalcraft.entity;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.entity.boss.earthgolem.EarthGolemEntity;
import sirttas.elementalcraft.entity.boss.earthgolem.EarthGolemRenderer;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECEntities {

	private ECEntities() {}
	
	@SubscribeEvent
	public static void registerEntitiess(RegistryEvent.Register<EntityType<?>> event) {
		IForgeRegistry<EntityType<?>> registry = event.getRegistry();

		register(registry, Builder.of(EarthGolemEntity::new, MobCategory.MONSTER).fireImmune().sized(7F, 8.5F), EarthGolemEntity.NAME, EarthGolemEntity.getAttributeModifier().build());
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerRenderers() {
		EntityRenderers.register(EarthGolemEntity.TYPE, EarthGolemRenderer::new);
	}

	private static <T extends Entity> EntityType<T> register(IForgeRegistry<EntityType<?>> registry, Builder<T> builder, String name) {
		EntityType<T> entityType = builder.build(ElementalCraft.createRL(name).toString());

		RegistryHelper.register(registry, entityType, name);
		return entityType;
	}

	private static <T extends LivingEntity> EntityType<T> register(IForgeRegistry<EntityType<?>> registry, Builder<T> builder, String name, AttributeSupplier map) {
		EntityType<T> entityType = register(registry, builder, name);

		// TODO DefaultAttributes.put(entityType, map);
		return entityType;
	}
}
