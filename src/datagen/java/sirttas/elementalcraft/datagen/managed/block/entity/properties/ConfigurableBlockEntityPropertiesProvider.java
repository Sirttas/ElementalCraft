package sirttas.elementalcraft.datagen.managed.block.entity.properties;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.range.Range;
import sirttas.elementalcraft.api.range.RangeVariants;
import sirttas.elementalcraft.block.container.ElementContainerBlock;
import sirttas.elementalcraft.block.container.SmallElementContainerBlock;
import sirttas.elementalcraft.block.container.creative.CreativeElementContainerBlock;
import sirttas.elementalcraft.block.container.reservoir.ReservoirBlock;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.binder.BinderBlockEntity;
import sirttas.elementalcraft.block.instrument.binder.improved.ImprovedBinderBlockEntity;
import sirttas.elementalcraft.block.instrument.crystallizer.CrystallizerBlockEntity;
import sirttas.elementalcraft.block.instrument.enchantment.liquefier.EnchantmentLiquefierBlockEntity;
import sirttas.elementalcraft.block.instrument.infuser.InfuserBlockEntity;
import sirttas.elementalcraft.block.instrument.inscriber.InscriberBlockEntity;
import sirttas.elementalcraft.block.instrument.io.firefurnace.FireFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.io.firefurnace.blast.FireBlastFurnaceBlockEntity;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.air.AirMillGrindstoneBlockEntity;
import sirttas.elementalcraft.block.instrument.io.mill.grindstone.water.WaterMillGrindstoneBlockEntity;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.air.AirMillWoodSawBlockEntity;
import sirttas.elementalcraft.block.instrument.io.mill.woodsaw.water.WaterMillWoodSawBlockEntity;
import sirttas.elementalcraft.block.instrument.io.purifier.PurifierBlockEntity;
import sirttas.elementalcraft.block.pureinfuser.PureInfuserBlockEntity;
import sirttas.elementalcraft.block.shrine.breeding.BreedingShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.budding.BuddingShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.enderlock.EnderLockShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.firepylon.FirePylonBlockEntity;
import sirttas.elementalcraft.block.shrine.grove.GroveShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.growth.GrowthShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.harvest.HarvestShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.lumber.LumberShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.melting.MeltingShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.ore.OreShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.overload.OverloadShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.spawning.SpawningShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.spring.SpringShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.sweet.SweetShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.vacuum.VacuumShrineBlockEntity;
import sirttas.elementalcraft.block.source.breeder.SourceBreederBlockEntity;
import sirttas.elementalcraft.block.synthesizer.combustion.CombustionSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.cracking.CrackingSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.cracking.sculk.SculkCrackingSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.culinary.CulinarySynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.draining.DrainingSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.mill.AirMillSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.solar.SolarSynthesizerBlockEntity;
import sirttas.elementalcraft.block.synthesizer.vibration.VibrationSynthesizerBlockEntity;
import sirttas.elementalcraft.range.Ranges;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class ConfigurableBlockEntityPropertiesProvider extends AbstractManagedDataBuilderProvider<IConfigurableBlockEntityProperties, IConfigurableBlockEntityPropertiesBuilder> {

	public ConfigurableBlockEntityPropertiesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries, ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER, IConfigurableBlockEntityPropertiesBuilder.CODEC);
	}

	@Override
	protected void collectBuilders(HolderLookup.Provider registries) {
		collectShrines();
		collectCrafting();
		collectContainers();
		collectSynthesizer();
	}

	private void collectShrines() {
		shrine(FirePylonBlockEntity.PROPERTIES_KEY, ElementType.FIRE)
				.period(20)
				.consumption(1)
				.range(Ranges.BOX_RADIUS_10)
				.strength(1, 2);
		shrine(VacuumShrineBlockEntity.PROPERTIES_KEY, ElementType.AIR)
				.consumption(5)
				.range(Ranges.BOX_RADIUS_10)
				.strength(2);
		shrine(GrowthShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER)
				.period(20)
				.consumption(50)
				.range(Range.builder().expendingUp(4, 3).stitch().fixedHeight())
				.range(GrowthShrineBlockEntity.CRYSTAL_GROWTH_RANGE_KEY, Range.builder().box(4).stitch().fixedHeight());
		shrine(HarvestShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH)
				.period(20).consumption(100)
				.range(Range.builder().expendingDown(4, 3).stitch().fixedHeight());
		shrine(LumberShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH).period(10)
				.consumption(50)
				.range(Range.builder().expendingUp(4, 9).stitch());
		shrine(MeltingShrineBlockEntity.PROPERTIES_KEY, ElementType.FIRE)
				.capacity(50000)
				.period(1)
				.consumption(1)
				.strength(1)
				.range(Ranges.ABOVE);
		shrine(OreShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH)
				.period(200)
				.consumption(2000)
				.range(Range.builder().expendingDown(12, 400).stitch().fixedHeight()) // total world height is 384, so we round up to 400
				.range(OreShrineBlockEntity.CRYSTAL_HARVEST_RANGE_KEY, Range.withParent(Ranges.BOX_RADIUS_10).stitch());
		shrine(OverloadShrineBlockEntity.PROPERTIES_KEY, ElementType.AIR)
				.period(3)
				.consumption(10)
				.range(Direction.UP, Ranges.ABOVE)
				.range(Direction.NORTH, Ranges.NORTH)
				.range(Direction.SOUTH, Ranges.SOUTH)
				.range(Direction.WEST, Ranges.WEST)
				.range(Direction.EAST, Ranges.EAST);
		shrine(SweetShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER)
				.period(40).consumption(500)
				.range(Ranges.BOX_RADIUS_10)
				.strength(1, 0.1);
		shrine(SpawningShrineBlockEntity.PROPERTIES_KEY, ElementType.FIRE)
				.period(100)
				.consumption(2000)
				.range(Range.builder().expendingUp(4, 1).stitch().fixedHeight());
		shrine(BreedingShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH)
				.period(200)
				.consumption(2000)
				.range(Direction.NORTH, Range.builder().boxTowards(Direction.NORTH, 10))
				.range(Direction.SOUTH, Range.builder().boxTowards(Direction.SOUTH, 10))
				.range(Direction.EAST, Range.builder().boxTowards(Direction.EAST, 10))
				.range(Direction.WEST, Range.builder().boxTowards(Direction.WEST, 10))
				.range(RangeVariants.TRANSLOCATION_KEY, Ranges.BOX_RADIUS_10);
		shrine(EnderLockShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER)
				.consumption(500)
				.range(Range.builder().expendingUp(10, 3).stitch());
		shrine(GroveShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER)
				.period(200)
				.consumption(500)
				.range(Range.builder().box(5, 1).stitch().fixedHeight());
		shrine(SpringShrineBlockEntity.PROPERTIES_KEY, ElementType.WATER)
				.period(5)
				.consumption(5)
				.range(Ranges.ABOVE)
				.strength(100);
		shrine(BuddingShrineBlockEntity.PROPERTIES_KEY, ElementType.EARTH)
				.period(1200)
				.consumption(2000)
				.range(Ranges.ABOVE)
				.strength(100);
	}

	private void collectCrafting() {
		crafting(InfuserBlockEntity.PROPERTIES_KEY)
				.recipeType(ECRecipeTypes.INFUSION)
				.transferSpeed(10)
				.maxRunes(1)
				.retrieveAll()
				.lockable();
		crafting(FireFurnaceBlockEntity.PROPERTIES_KEY)
				.recipeType(RecipeType.SMELTING)
				.transferSpeed(10)
				.maxRunes(2)
				.outputSlot(1);
		crafting(FireBlastFurnaceBlockEntity.PROPERTIES_KEY)
				.recipeType(RecipeType.BLASTING)
				.transferSpeed(20)
				.maxRunes(3)
				.outputSlot(1);
		crafting(WaterMillGrindstoneBlockEntity.PROPERTIES_KEY)
				.recipeType(ECRecipeTypes.GRINDING)
				.transferSpeed(5)
				.maxRunes(2)
				.outputSlot(1);
		crafting(WaterMillWoodSawBlockEntity.PROPERTIES_KEY)
				.recipeType(ECRecipeTypes.SAWING)
				.transferSpeed(5)
				.maxRunes(2)
				.outputSlot(1);
		crafting(AirMillGrindstoneBlockEntity.PROPERTIES_KEY)
				.recipeType(ECRecipeTypes.GRINDING)
				.transferSpeed(25)
				.maxRunes(3)
				.outputSlot(1);
		crafting(AirMillWoodSawBlockEntity.PROPERTIES_KEY)
				.recipeType(ECRecipeTypes.SAWING)
				.transferSpeed(25)
				.maxRunes(3)
				.outputSlot(1);
		crafting(BinderBlockEntity.PROPERTIES_KEY)
				.recipeType(ECRecipeTypes.BINDING)
				.transferSpeed(25)
				.maxRunes(2)
				.retrieveAll()
				.lockable();
		crafting(InscriberBlockEntity.PROPERTIES_KEY)
				.recipeType(ECRecipeTypes.INSCRIPTION)
				.transferSpeed(1000)
				.maxRunes(2)
				.retrieveAll()
				.lockable();
		crafting(CrystallizerBlockEntity.PROPERTIES_KEY)
				.recipeType(ECRecipeTypes.CRYSTALLIZATION)
				.transferSpeed(25)
				.maxRunes(3)
				.retrieveAll()
				.lockable();
		crafting(EnchantmentLiquefierBlockEntity.PROPERTIES_KEY)
				.transferSpeed(25)
				.maxRunes(3)
				.outputSlot(1)
				.retrieveAll()
				.lockable();
		crafting(PureInfuserBlockEntity.PROPERTIES_KEY)
				.recipeType(ECRecipeTypes.PURE_INFUSION)
				.transferSpeed(100)
				.maxRunes(3)
				.retrieveAll()
				.lockable();
		crafting(PurifierBlockEntity.PROPERTIES_KEY)
				.recipeType(ECRecipeTypes.ORE_PURIFICATION)
				.transferSpeed(25)
				.maxRunes(3)
				.outputSlot(1);
		crafting(ImprovedBinderBlockEntity.PROPERTIES_KEY)
				.recipeType(ECRecipeTypes.BINDING)
				.transferSpeed(50)
				.maxRunes(3)
				.retrieveAll()
				.lockable();
		crafting(SourceBreederBlockEntity.PROPERTIES_KEY)
				.transferSpeed(500)
				.maxRunes(3)
				.retrieveAll()
				.lockable();
	}

	private void collectContainers() {
		container(SmallElementContainerBlock.PROPERTIES_KEY, createHolderSet(ECTags.Blocks.SMALL_CONTAINER_TOOLS), 2000);
		container(ElementContainerBlock.PROPERTIES_KEY, createHolderSet(ECTags.Blocks.CONTAINER_TOOLS), 100000);
		container(ReservoirBlock.PROPERTIES_KEY_FIRE, createHolderSet(ECTags.Blocks.FIRE_CONTAINER_TOOLS), 5000000);
		container(ReservoirBlock.PROPERTIES_KEY_WATER, createHolderSet(ECTags.Blocks.WATER_CONTAINER_TOOLS), 5000000);
		container(ReservoirBlock.PROPERTIES_KEY_EARTH, createHolderSet(ECTags.Blocks.EARTH_CONTAINER_TOOLS), 5000000);
		container(ReservoirBlock.PROPERTIES_KEY_AIR, createHolderSet(ECTags.Blocks.AIR_CONTAINER_TOOLS), 5000000);
		container(CreativeElementContainerBlock.PROPERTIES_KEY, createHolderSet(ECTags.Blocks.CONTAINER_TOOLS), 1000000);
	}

	private void collectSynthesizer() {
		synthesizer(CrackingSynthesizerBlockEntity.PROPERTIES_KEY)
				.elementType(ElementType.EARTH)
				.transferSpeed(5)
				.bufferCapacity(1000)
				.maxRunes(1)
				.range(Range.builder().expendingDown(5, 2).move(0, -1, 0).stitch().fixedHeight());
		synthesizer(CombustionSynthesizerBlockEntity.PROPERTIES_KEY)
				.elementType(ElementType.FIRE)
				.transferSpeed(5)
				.bufferCapacity(20000)
				.maxRunes(1);
		synthesizer(DrainingSynthesizerBlockEntity.PROPERTIES_KEY)
				.elementType(ElementType.WATER)
				.transferSpeed(5)
				.bufferCapacity(2000)
				.maxRunes(1)
				.synthesisMultiplier(50);
		synthesizer(VibrationSynthesizerBlockEntity.PROPERTIES_KEY)
				.elementType(ElementType.AIR)
				.transferSpeed(5)
				.bufferCapacity(2000)
				.maxRunes(1)
				.range(Ranges.BOX_RADIUS_10)
				.synthesisMultiplier(200);
		synthesizer(SolarSynthesizerBlockEntity.PROPERTIES_KEY)
				.elementType(ElementType.FIRE)
				.transferSpeed(25)
				.bufferCapacity(10000)
				.maxRunes(2)
				.synthesisMultiplier(50);
		synthesizer(CulinarySynthesizerBlockEntity.PROPERTIES_KEY)
				.elementType(ElementType.WATER)
				.transferSpeed(25)
				.bufferCapacity(10000)
				.maxRunes(2)
				.synthesisMultiplier(100);
		synthesizer(SculkCrackingSynthesizerBlockEntity.PROPERTIES_KEY)
				.elementType(ElementType.EARTH)
				.transferSpeed(25)
				.bufferCapacity(10000)
				.maxRunes(2)
				.range(Range.builder().box(8).stitch());
		synthesizer(AirMillSynthesizerBlockEntity.PROPERTIES_KEY)
				.elementType(ElementType.AIR)
				.transferSpeed(25)
				.bufferCapacity(10000)
				.maxRunes(2)
				.synthesisMultiplier(50);
	}

	protected ShrinePropertiesBuilder shrine(ResourceKey<IConfigurableBlockEntityProperties> key, ElementType type) {
		var builder = new ShrinePropertiesBuilder(type);

		add(key, builder);
		return builder;
	}

	protected CraftingBlockEntityPropertiesBuilder crafting(ResourceKey<IConfigurableBlockEntityProperties> key) {
		var builder = new CraftingBlockEntityPropertiesBuilder();

		add(key, builder);
		return builder;
	}

	protected void container(ResourceKey<IConfigurableBlockEntityProperties> key, HolderSet<Block> compatibleTools, int capacity) {
		var builder = new ElementContainerPropertiesBuilder(compatibleTools, capacity);

		add(key, builder);
	}
	protected SynthesizerPropertiesBuilder synthesizer(ResourceKey<IConfigurableBlockEntityProperties> key) {
		var builder = new SynthesizerPropertiesBuilder();

		add(key, builder);
		return builder;
	}


	@Nonnull
	@Override
	public String getName() {
		return "ElementalCraft Shrine Properties";
	}

}
