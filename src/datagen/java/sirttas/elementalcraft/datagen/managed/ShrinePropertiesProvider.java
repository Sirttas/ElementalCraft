package sirttas.elementalcraft.datagen.managed;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.enderlock.EnderLockShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.firepylon.FirePylonBlockEntity;
import sirttas.elementalcraft.block.shrine.grove.GroveShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.growth.GrowthShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.harvest.HarvestShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.lava.LavaShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.ore.OreShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.spawning.SpawningShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.sweet.SweetShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.vacuum.VacuumShrineBlockEntity;

import javax.annotation.Nonnull;

public class ShrinePropertiesProvider extends AbstractManagedDataBuilderProvider<ShrineProperties, ShrineProperties.Builder> {

	public ShrinePropertiesProvider(DataGenerator generator) {
		super(generator, ElementalCraft.SHRINE_PROPERTIES_MANAGER,  ShrineProperties.Builder.ENCODER);
	}

	@Override
	public void collectBuilders() {
		builder(FirePylonBlockEntity.PROPERTIES_KEY, ElementType.FIRE).consumption(1).range(10).strength(0.1D);
		builder(VacuumShrineBlockEntity.PROPERTIES_KEY, ElementType.AIR).consumption(5).range(10).strength(2);
		builder(GrowthShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER).period(20).consumption(50).range(4);
		builder(HarvestShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH).period(20).consumption(100).range(4);
		builder(LavaShrineBlockEntity.PROPERTIES_KEY, ElementType.FIRE).period(1200).consumption(5000).range(1);
		builder(OreShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH).period(200).consumption(2000).range(12);
		builder(OverloadShrineBlockEntity.PROPERTIES_KEY, ElementType.AIR).period(3).consumption(10);
		builder(SweetShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER).period(40).consumption(500).range(10).strength(1, 0.1);
		builder(SpawningShrineBlockEntity.PROPERTIES_KEY, ElementType.FIRE).period(100).consumption(2000).range(4);
		builder(BreedingShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH).period(200).consumption(2000).range(10);
		builder(EnderLockShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER).consumption(500).range(10);
		builder(GroveShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER).period(200).consumption(500).range(5);
		builder(SpringShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER).period(5).consumption(5).strength(100);
		builder(BuddingShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH).period(600).consumption(2000).strength(100);
	}

	protected ShrineProperties.Builder builder(ResourceKey<ShrineProperties> key, ElementType type) {
		var builder = ShrineProperties.builder(type);

		add(key, builder);
		return builder;
	}

	@Nonnull
    @Override
	public String getName() {
		return "ElementalCraft Shrine Properties";
	}
}
