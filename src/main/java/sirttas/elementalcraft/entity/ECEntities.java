package sirttas.elementalcraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.Builder;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.entity.boss.earthgolem.EarthGolemEntity;
import sirttas.elementalcraft.entity.boss.earthgolem.EarthGolemRenderer;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECEntities {

	@SubscribeEvent
	public static void registerEntitiess(RegistryEvent.Register<EntityType<?>> event) {
		IForgeRegistry<EntityType<?>> registry = event.getRegistry();

		register(registry, Builder.create(EarthGolemEntity::new, EntityClassification.MONSTER).immuneToFire().size(7F, 8.5F), EarthGolemEntity.NAME);
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EarthGolemEntity.TYPE, EarthGolemRenderer::new);
		GlobalEntityTypeAttributes.put(EarthGolemEntity.TYPE, EarthGolemEntity.getAttributeModifier().create());
	}

	private static <T extends Entity> void register(IForgeRegistry<EntityType<?>> registry, Builder<T> builder, String name) {
		RegistryHelper.register(registry, builder.build(name), name);
	}
}
