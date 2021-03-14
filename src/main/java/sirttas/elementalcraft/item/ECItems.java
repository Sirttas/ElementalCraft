package sirttas.elementalcraft.item;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.BlockCrystalOre;
import sirttas.elementalcraft.block.evaporator.BlockEvaporator;
import sirttas.elementalcraft.block.extractor.BlockExtractor;
import sirttas.elementalcraft.block.extractor.improved.BlockImprovedExtractor;
import sirttas.elementalcraft.block.instrument.binder.BlockBinder;
import sirttas.elementalcraft.block.instrument.binder.improved.BlockImprovedBinder;
import sirttas.elementalcraft.block.instrument.crystallizer.BlockCrystallizer;
import sirttas.elementalcraft.block.instrument.firefurnace.BlockFireFurnace;
import sirttas.elementalcraft.block.instrument.firefurnace.blast.BlockFireBlastFurnace;
import sirttas.elementalcraft.block.instrument.infuser.BlockInfuser;
import sirttas.elementalcraft.block.instrument.inscriber.BlockInscriber;
import sirttas.elementalcraft.block.instrument.mill.BlockAirMill;
import sirttas.elementalcraft.block.instrument.purifier.BlockPurifier;
import sirttas.elementalcraft.block.pipe.BlockElementPipe;
import sirttas.elementalcraft.block.pureinfuser.BlockPureInfuser;
import sirttas.elementalcraft.block.pureinfuser.pedestal.BlockPedestal;
import sirttas.elementalcraft.block.retriever.BlockRetriever;
import sirttas.elementalcraft.block.shrine.breeding.BlockBreedingShrine;
import sirttas.elementalcraft.block.shrine.enderlock.BlockEnderLockShrine;
import sirttas.elementalcraft.block.shrine.firepylon.BlockFirePylon;
import sirttas.elementalcraft.block.shrine.grove.BlockGroveShrine;
import sirttas.elementalcraft.block.shrine.growth.BlockGrowthShrine;
import sirttas.elementalcraft.block.shrine.harvest.BlockHarvestShrine;
import sirttas.elementalcraft.block.shrine.lava.BlockLavaShrine;
import sirttas.elementalcraft.block.shrine.ore.BlockOreShrine;
import sirttas.elementalcraft.block.shrine.overload.BlockOverloadShrine;
import sirttas.elementalcraft.block.shrine.sweet.BlockSweetShrine;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockCapacityShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockEfficiencyShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockOptimizationShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockRangeShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockStrengthShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.BlockAccelerationShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockFortuneShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockNectarShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockSilkTouchShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockBonelessGrowthShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockMysticalGroveShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockPickupShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockPlantingShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockStemPollinationShrineUpgrade;
import sirttas.elementalcraft.block.shrine.vacuum.BlockVacuumShrine;
import sirttas.elementalcraft.block.solarsynthesizer.BlockSolarSynthesizer;
import sirttas.elementalcraft.block.source.BlockSource;
import sirttas.elementalcraft.block.tank.BlockTank;
import sirttas.elementalcraft.block.tank.BlockTankSmall;
import sirttas.elementalcraft.block.tank.creative.BlockTankCreative;
import sirttas.elementalcraft.item.chisel.ItemChisel;
import sirttas.elementalcraft.item.elemental.ItemElementHolder;
import sirttas.elementalcraft.item.elemental.ItemElemental;
import sirttas.elementalcraft.item.elemental.ItemLense;
import sirttas.elementalcraft.item.elemental.ItemShard;
import sirttas.elementalcraft.item.pureore.ItemPureOre;
import sirttas.elementalcraft.item.receptacle.ItemEmptyReceptacle;
import sirttas.elementalcraft.item.receptacle.ItemReceptacle;
import sirttas.elementalcraft.item.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.item.receptacle.improved.ItemEmptyImprovedReceptacle;
import sirttas.elementalcraft.item.receptacle.improved.ItemImprovedReceptacle;
import sirttas.elementalcraft.item.rune.ItemRune;
import sirttas.elementalcraft.item.rune.RuneModel;
import sirttas.elementalcraft.item.spell.ItemFocus;
import sirttas.elementalcraft.item.spell.ItemScroll;
import sirttas.elementalcraft.item.spell.book.ItemSpellBook;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.registry.RegistryHelper;
import sirttas.elementalcraft.spell.SpellHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECItems {

	@ObjectHolder(ElementalCraft.MODID + ":" + ItemFocus.NAME) public static final ItemFocus FOCUS = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemScroll.NAME) public static final ItemScroll SCROLL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemSpellBook.NAME) public static final ItemSpellBook SPELL_BOOK = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemReceptacle.NAME) public static final ItemReceptacle RECEPTACLE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemEmptyReceptacle.NAME) public static final ItemEmptyReceptacle EMPTY_RECEPTACLE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemImprovedReceptacle.NAME) public static final ItemImprovedReceptacle RECEPTACLE_IMPROVED = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemEmptyImprovedReceptacle.NAME) public static final ItemEmptyImprovedReceptacle EMPTY_RECEPTACLE_IMPROVED = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemElementHolder.NAME_FIRE) public static final ItemElementHolder FIRE_HOLDER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemElementHolder.NAME_WATER) public static final ItemElementHolder WATER_HOLDER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemElementHolder.NAME_EARTH) public static final ItemElementHolder EARTH_HOLDER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemElementHolder.NAME_AIR) public static final ItemElementHolder AIR_HOLDER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemPureOre.NAME) public static final ItemPureOre PURE_ORE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemRune.NAME) public static final ItemRune RUNE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemChisel.NAME) public static final ItemChisel CHISEL = null;

	@ObjectHolder("patchouli:guide_book") public static final Item ELEMENTOPEDIA = null;
	@ObjectHolder(ElementalCraft.MODID + ":inertcrystal") public static final ItemEC INERT_CRYSTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":containedcrystal") public static final ItemEC CONTAINED_CRYSTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":purecrystal") public static final ItemEC PURE_CRYSTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":drenched_iron_ingot") public static final ItemEC DRENCHED_IRON_INGOT = null;
	@ObjectHolder(ElementalCraft.MODID + ":drenched_iron_nugget") public static final ItemEC DRENCHED_IRON_NUGGET = null;
	@ObjectHolder(ElementalCraft.MODID + ":swift_alloy_ingot") public static final ItemEC SWIFT_ALLOY_INGOT = null;
	@ObjectHolder(ElementalCraft.MODID + ":swift_alloy_nugget") public static final ItemEC SWIFT_ALLOY_NUGGET = null;
	@ObjectHolder(ElementalCraft.MODID + ":hardened_handle") public static final ItemEC HARDENED_HANDLE = null;
	@ObjectHolder(ElementalCraft.MODID + ":shrinebase") public static final ItemEC SHRINE_BASE = null;
	@ObjectHolder(ElementalCraft.MODID + ":fireite_ingot") public static final ItemEC FIREITE_INGOT = null;
	@ObjectHolder(ElementalCraft.MODID + ":fireite_nugget") public static final ItemEC FIREITE_NUGGET = null;
	@ObjectHolder(ElementalCraft.MODID + ":air_silk") public static final ItemEC AIR_SILK = null;
	@ObjectHolder(ElementalCraft.MODID + ":shrine_upgrade_core") public static final ItemEC SHRINE_UPGRADE_CORE = null;
	@ObjectHolder(ElementalCraft.MODID + ":scroll_paper") public static final ItemEC SCROLL_PAPER = null;

	@ObjectHolder(ElementalCraft.MODID + ":firecrystal") public static final ItemElemental FIRE_CRYSTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":watercrystal") public static final ItemElemental WATER_CRYSTAL  = null;
	@ObjectHolder(ElementalCraft.MODID + ":earthcrystal") public static final ItemElemental EARTH_CRYSTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":aircrystal") public static final ItemElemental AIR_CRYSTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_FIRE) public static final ItemShard FIRE_SHARD = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_WATER) public static final ItemShard WATER_SHARD = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_EARTH) public static final ItemShard EARTH_SHARD = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_AIR) public static final ItemShard AIR_SHARD = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_FIRE_POWERFUL) public static final ItemShard POWERFUL_FIRE_SHARD = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_WATER_POWERFUL) public static final ItemShard POWERFUL_WATER_SHARD = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_EARTH_POWERFUL) public static final ItemShard POWERFUL_EARTH_SHARD = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_AIR_POWERFUL) public static final ItemShard POWERFUL_AIR_SHARD = null;
	@ObjectHolder(ElementalCraft.MODID + ":crude_fire_gem") public static final ItemElemental CRUDE_FIRE_GEM = null;
	@ObjectHolder(ElementalCraft.MODID + ":crude_water_gem") public static final ItemElemental CRUDE_WATER_GEM = null;
	@ObjectHolder(ElementalCraft.MODID + ":crude_earth_gem") public static final ItemElemental CRUDE_EARTH_GEM = null;
	@ObjectHolder(ElementalCraft.MODID + ":crude_air_gem") public static final ItemElemental CRUDE_AIR_GEM = null;
	@ObjectHolder(ElementalCraft.MODID + ":fine_fire_gem") public static final ItemElemental FINE_FIRE_GEM = null;
	@ObjectHolder(ElementalCraft.MODID + ":fine_water_gem") public static final ItemElemental FINE_WATER_GEM = null;
	@ObjectHolder(ElementalCraft.MODID + ":fine_earth_gem") public static final ItemElemental FINE_EARTH_GEM = null;
	@ObjectHolder(ElementalCraft.MODID + ":fine_air_gem") public static final ItemElemental FINE_AIR_GEM = null;
	@ObjectHolder(ElementalCraft.MODID + ":pristine_fire_gem") public static final ItemElemental PRISTINE_FIRE_GEM = null;
	@ObjectHolder(ElementalCraft.MODID + ":pristine_water_gem") public static final ItemElemental PRISTINE_WATER_GEM = null;
	@ObjectHolder(ElementalCraft.MODID + ":pristine_earth_gem") public static final ItemElemental PRISTINE_EARTH_GEM = null;
	@ObjectHolder(ElementalCraft.MODID + ":pristine_air_gem") public static final ItemElemental PRISTINE_AIR_GEM = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemLense.NAME_FIRE) public static final ItemLense FIRE_LENSE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemLense.NAME_WATER) public static final ItemLense WATER_LENSE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemLense.NAME_EARTH) public static final ItemLense EARTH_LENSE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemLense.NAME_AIR) public static final ItemLense AIR_LENSE = null;
	@ObjectHolder(ElementalCraft.MODID + ":minor_rune_slate") public static final ItemEC MINOR_RUNE_SLATE = null;
	@ObjectHolder(ElementalCraft.MODID + ":rune_slate") public static final ItemEC RUNE_SLATE = null;
	@ObjectHolder(ElementalCraft.MODID + ":major_rune_slate") public static final ItemEC MAJOR_RUNE_SLATE = null;

	// BLOCKS
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTankSmall.NAME) public static final Item TANK_SMALL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTank.NAME) public static final Item TANK = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTankCreative.NAME) public static final Item TANK_CREATIVE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockExtractor.NAME) public static final Item EXTRACTOR = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockImprovedExtractor.NAME) public static final Item EXTRACTOR_IMPROVED = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockEvaporator.NAME) public static final Item EVAPORATOR = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSolarSynthesizer.NAME) public static final Item SOLAR_SYNTHESIZER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockInfuser.NAME) public static final Item INFUSER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBinder.NAME) public static final Item BINDER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockImprovedBinder.NAME) public static final Item BINDER_IMPROVED = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockCrystallizer.NAME) public static final Item CRYSTALLIZER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockInscriber.NAME) public static final Item INSCRIBER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockAirMill.NAME) public static final Item AIR_MILL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_FIRE) public static final Item FIRE_PEDESTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_WATER) public static final Item WATER_PEDESTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_EARTH) public static final Item EARTH_PEDESTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_AIR) public static final Item AIR_PEDESTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPureInfuser.NAME) public static final Item PURE_INFUSER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFireFurnace.NAME) public static final Item FIRE_FURNACE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFireBlastFurnace.NAME) public static final Item FIRE_BLAST_FURNACE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPurifier.NAME) public static final Item PURIFIER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME_IMPAIRED) public static final Item PIPE_IMPAIRED = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME) public static final Item PIPE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME_IMPROVED) public static final Item PIPE_IMPROVED = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockRetriever.NAME) public static final Item RETRIEVER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFirePylon.NAME) public static final Item FIRE_PYLON = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockVacuumShrine.NAME) public static final Item VACUUM_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockGrowthShrine.NAME) public static final Item GROWTH_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockHarvestShrine.NAME) public static final Item HARVEST_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockLavaShrine.NAME) public static final Item LAVA_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOreShrine.NAME) public static final Item ORE_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOverloadShrine.NAME) public static final Item OVERLOAD_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSweetShrine.NAME) public static final Item SWEET_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockEnderLockShrine.NAME) public static final Item ENDER_LOC_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBreedingShrine.NAME) public static final Item BREEDING_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockGroveShrine.NAME) public static final Item GROVE_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockAccelerationShrineUpgrade.NAME) public static final Item ACCELERATION_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockRangeShrineUpgrade.NAME) public static final Item RANGE_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockCapacityShrineUpgrade.NAME) public static final Item CAPACITY_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockEfficiencyShrineUpgrade.NAME) public static final Item EFFICIENCY_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockStrengthShrineUpgrade.NAME) public static final Item STRENGTH_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOptimizationShrineUpgrade.NAME) public static final Item OPTIMIZATION_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFortuneShrineUpgrade.NAME) public static final Item FORTUNE_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSilkTouchShrineUpgrade.NAME) public static final Item SILK_TOUCH_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPlantingShrineUpgrade.NAME) public static final Item PLANTING_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBonelessGrowthShrineUpgrade.NAME) public static final Item BONELESS_GROWTH_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPickupShrineUpgrade.NAME) public static final Item PICKUP_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockNectarShrineUpgrade.NAME) public static final Item NECTOR_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockMysticalGroveShrineUpgrade.NAME) public static final Item MYSTICAL_GROVE_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockStemPollinationShrineUpgrade.NAME) public static final Item STEM_POLLINATION_SHRINE_UPGRADE = null;


	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSource.NAME) public static final Item SOURCE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockCrystalOre.NAME) public static final Item CRYSTAL_ORE = null;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock") public static final Item WHITE_ROCK = null;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_slab") public static final Item WHITE_ROCK_SLAB = null;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_stairs") public static final Item WHITE_ROCK_STAIRS = null;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_wall") public static final Item WHITE_ROCK_WALL = null;
	@ObjectHolder(ElementalCraft.MODID + ":purerock") public static final Item PURE_ROCK = null;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_slab") public static final Item PURE_ROCK_SLAB = null;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_stairs") public static final Item PURE_ROCK_STAIR = null;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_wall") public static final Item PURE_ROCK_WALL = null;
	@ObjectHolder(ElementalCraft.MODID + ":burnt_glass") public static final Item BURNT_GLASS = null;
	@ObjectHolder(ElementalCraft.MODID + ":burnt_glass_pane") public static final Item BURNT_GLASS_PANE = null;
	@ObjectHolder(ElementalCraft.MODID + ":drenched_iron_block") public static final Item DRENCHED_IRON_BLOCK = null;
	@ObjectHolder(ElementalCraft.MODID + ":swift_alloy_block") public static final Item SWIFT_ALLOY_BLOCK = null;
	@ObjectHolder(ElementalCraft.MODID + ":fireite_block") public static final Item FIREITE_BLOCK = null;

	private ECItems() {

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		RegistryHelper.register(registry, new ItemFocus(), ItemFocus.NAME);
		RegistryHelper.register(registry, new ItemScroll(), ItemScroll.NAME);
		RegistryHelper.register(registry, new ItemSpellBook(), ItemSpellBook.NAME);
		RegistryHelper.register(registry, new ItemReceptacle(), ItemReceptacle.NAME);
		RegistryHelper.register(registry, new ItemEmptyReceptacle(), ItemEmptyReceptacle.NAME);
		RegistryHelper.register(registry, new ItemImprovedReceptacle(), ItemImprovedReceptacle.NAME);
		RegistryHelper.register(registry, new ItemEmptyImprovedReceptacle(), ItemEmptyImprovedReceptacle.NAME);
		RegistryHelper.register(registry, new ItemElementHolder(ElementType.FIRE), ItemElementHolder.NAME_FIRE);
		RegistryHelper.register(registry, new ItemElementHolder(ElementType.WATER), ItemElementHolder.NAME_WATER);
		RegistryHelper.register(registry, new ItemElementHolder(ElementType.EARTH), ItemElementHolder.NAME_EARTH);
		RegistryHelper.register(registry, new ItemElementHolder(ElementType.AIR), ItemElementHolder.NAME_AIR);
		RegistryHelper.register(registry, new ItemPureOre(), ItemPureOre.NAME);
		RegistryHelper.register(registry, new ItemRune(), ItemRune.NAME);
		RegistryHelper.register(registry, new ItemChisel(), ItemChisel.NAME);

		RegistryHelper.register(registry, new ItemEC(), "inertcrystal");
		RegistryHelper.register(registry, new ItemEC(), "containedcrystal");
		RegistryHelper.register(registry, new ItemEC().setEffect(true), "purecrystal");
		RegistryHelper.register(registry, new ItemEC(), "drenched_iron_ingot");
		RegistryHelper.register(registry, new ItemEC(), "drenched_iron_nugget");
		RegistryHelper.register(registry, new ItemEC(), "swift_alloy_ingot");
		RegistryHelper.register(registry, new ItemEC(), "swift_alloy_nugget");
		RegistryHelper.register(registry, new ItemEC(), "hardened_handle");
		RegistryHelper.register(registry, new ItemEC(), "shrinebase");
		RegistryHelper.register(registry, new ItemEC(ECProperties.Items.FIREITE), "fireite_ingot");
		RegistryHelper.register(registry, new ItemEC(ECProperties.Items.FIREITE), "fireite_nugget");
		RegistryHelper.register(registry, new ItemEC(), "air_silk");
		RegistryHelper.register(registry, new ItemEC(), "shrine_upgrade_core");
		RegistryHelper.register(registry, new ItemEC(), "scroll_paper");

		RegistryHelper.register(registry, new ItemElemental(ElementType.FIRE), "firecrystal");
		RegistryHelper.register(registry, new ItemElemental(ElementType.WATER), "watercrystal");
		RegistryHelper.register(registry, new ItemElemental(ElementType.EARTH), "earthcrystal");
		RegistryHelper.register(registry, new ItemElemental(ElementType.AIR), "aircrystal");
		RegistryHelper.register(registry, new ItemShard(ElementType.FIRE), ItemShard.NAME_FIRE);
		RegistryHelper.register(registry, new ItemShard(ElementType.WATER), ItemShard.NAME_WATER);
		RegistryHelper.register(registry, new ItemShard(ElementType.EARTH), ItemShard.NAME_EARTH);
		RegistryHelper.register(registry, new ItemShard(ElementType.AIR), ItemShard.NAME_AIR);
		RegistryHelper.register(registry, new ItemShard(ElementType.FIRE, 9), ItemShard.NAME_FIRE_POWERFUL);
		RegistryHelper.register(registry, new ItemShard(ElementType.WATER, 9), ItemShard.NAME_WATER_POWERFUL);
		RegistryHelper.register(registry, new ItemShard(ElementType.EARTH, 9), ItemShard.NAME_EARTH_POWERFUL);
		RegistryHelper.register(registry, new ItemShard(ElementType.AIR, 9), ItemShard.NAME_AIR_POWERFUL);
		RegistryHelper.register(registry, new ItemElemental(ElementType.FIRE), "crude_fire_gem");
		RegistryHelper.register(registry, new ItemElemental(ElementType.WATER), "crude_water_gem");
		RegistryHelper.register(registry, new ItemElemental(ElementType.EARTH), "crude_earth_gem");
		RegistryHelper.register(registry, new ItemElemental(ElementType.AIR), "crude_air_gem");
		RegistryHelper.register(registry, new ItemElemental(ElementType.FIRE), "fine_fire_gem");
		RegistryHelper.register(registry, new ItemElemental(ElementType.WATER), "fine_water_gem");
		RegistryHelper.register(registry, new ItemElemental(ElementType.EARTH), "fine_earth_gem");
		RegistryHelper.register(registry, new ItemElemental(ElementType.AIR), "fine_air_gem");
		RegistryHelper.register(registry, new ItemElemental(ElementType.FIRE), "pristine_fire_gem");
		RegistryHelper.register(registry, new ItemElemental(ElementType.WATER), "pristine_water_gem");
		RegistryHelper.register(registry, new ItemElemental(ElementType.EARTH), "pristine_earth_gem");
		RegistryHelper.register(registry, new ItemElemental(ElementType.AIR), "pristine_air_gem");
		RegistryHelper.register(registry, new ItemLense(ElementType.FIRE), ItemLense.NAME_FIRE);
		RegistryHelper.register(registry, new ItemLense(ElementType.WATER), ItemLense.NAME_WATER);
		RegistryHelper.register(registry, new ItemLense(ElementType.EARTH), ItemLense.NAME_EARTH);
		RegistryHelper.register(registry, new ItemLense(ElementType.AIR), ItemLense.NAME_AIR);
		RegistryHelper.register(registry, new ItemEC(), "minor_rune_slate");
		RegistryHelper.register(registry, new ItemEC(), "rune_slate");
		RegistryHelper.register(registry, new ItemEC(), "major_rune_slate");
	}

	public static void replaceModels(ModelBakeEvent event) {
		ModelResourceLocation key = new ModelResourceLocation(ElementalCraft.createRL(ItemRune.NAME), "inventory");
		IBakedModel oldModel = event.getModelRegistry().get(key);
		
		if (oldModel != null) {
			event.getModelRegistry().put(key, new RuneModel(oldModel));
		}
	}

	public static void registerItemColors(ColorHandlerEvent.Item event) {
		event.getItemColors().register((s, l) -> l == 0 ? -1 : ReceptacleHelper.getElementType(s).getColor(), RECEPTACLE, RECEPTACLE_IMPROVED);
		event.getItemColors().register((s, l) -> l == 0 ? -1 : ElementalCraft.PURE_ORE_MANAGER.getColor(s), PURE_ORE);
		event.getItemColors().register((s, l) -> l == 0 ? -1 : SpellHelper.getSpell(s).getColor(), SCROLL);
		event.getItemColors().register((s, l) -> l == 0 ? -1 : ((ItemElementHolder) s.getItem()).getElementType().getColor(), 
				FIRE_HOLDER, WATER_HOLDER, EARTH_HOLDER, AIR_HOLDER);
	}
}
