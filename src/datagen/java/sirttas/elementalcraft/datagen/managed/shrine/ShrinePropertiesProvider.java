package sirttas.elementalcraft.datagen.managed.shrine;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
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
import sirttas.elementalcraft.block.shrine.lumber.LumberShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.ore.OreShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.properties.ShrineRange;
import sirttas.elementalcraft.block.shrine.spawning.SpawningShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.sweet.SweetShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.vacuum.VacuumShrineBlockEntity;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class ShrinePropertiesProvider extends AbstractManagedDataBuilderProvider<ShrineProperties, ShrineProperties.Builder> {

	public ShrinePropertiesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries, ElementalCraft.SHRINE_PROPERTIES_MANAGER, ShrineProperties.Builder.ENCODER);
	}

	@Override
	protected void collectBuilders(HolderLookup.Provider registries) {
		builder(FirePylonBlockEntity.PROPERTIES_KEY, ElementType.FIRE)
				.period(20)
				.consumption(1)
				.range(ShrineRange.box(10))
				.strength(1, 2);
		builder(VacuumShrineBlockEntity.PROPERTIES_KEY, ElementType.AIR)
				.consumption(5)
				.range(ShrineRange.box(10))
				.strength(2);
		builder(GrowthShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER)
				.period(20)
				.consumption(50)
				.range(ShrineRange.expendingUp(4, 3).stitch().fixedHeight());
		builder(HarvestShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH)
				.period(20).consumption(100)
				.range(ShrineRange.expendingDown(4, 3).stitch().fixedHeight());
		builder(LumberShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH).period(10)
				.consumption(50)
				.range(ShrineRange.expendingUp(4, 9).stitch());
		builder(LavaShrineBlockEntity.PROPERTIES_KEY, ElementType.FIRE)
				.period(1200)
				.consumption(5000)
				.range(ShrineRange.box(-1, 1, -1, 2, 2, 2).stitch().fixedHeight());
		builder(OreShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH)
				.period(200)
				.consumption(2000)
				.range(ShrineRange.expendingDown(12, 1).stitch());
		builder(OverloadShrineBlockEntity.PROPERTIES_KEY, ElementType.AIR)
				.period(3)
				.consumption(10);
		builder(SweetShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER)
				.period(40).consumption(500)
				.range(ShrineRange.box(10))
				.strength(1, 0.1);
		builder(SpawningShrineBlockEntity.PROPERTIES_KEY, ElementType.FIRE)
				.period(100)
				.consumption(2000)
				.range(ShrineRange.expendingUp(4, 1).stitch().fixedHeight());
		builder(BreedingShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH)
				.period(200)
				.consumption(2000)
				.range(ShrineRange.box(10));
		builder(EnderLockShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER)
				.consumption(500)
				.range(ShrineRange.expendingUp(10, 3).stitch());
		builder(GroveShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER)
				.period(200)
				.consumption(500)
				.range(ShrineRange.box(5, 1).stitch().fixedHeight());
		builder(SpringShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER)
				.period(5)
				.consumption(5)
				.strength(100);
		builder(BuddingShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH)
				.period(1200)
				.consumption(2000)
				.strength(100);
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
