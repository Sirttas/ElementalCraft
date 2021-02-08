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
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockPickupShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockPlantingShrineUpgrade;
import sirttas.elementalcraft.block.shrine.vacuum.BlockVacuumShrine;
import sirttas.elementalcraft.block.source.BlockSource;
import sirttas.elementalcraft.block.tank.BlockTank;
import sirttas.elementalcraft.block.tank.BlockTankSmall;
import sirttas.elementalcraft.item.chisel.ItemChisel;
import sirttas.elementalcraft.item.holder.ItemElementHolder;
import sirttas.elementalcraft.item.pureore.ItemPureOre;
import sirttas.elementalcraft.item.receptacle.ItemEmptyReceptacle;
import sirttas.elementalcraft.item.receptacle.ItemReceptacle;
import sirttas.elementalcraft.item.receptacle.ReceptacleHelper;
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

	@ObjectHolder(ElementalCraft.MODID + ":" + ItemFocus.NAME) public static ItemFocus focus;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemScroll.NAME) public static ItemScroll scroll;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemSpellBook.NAME) public static ItemSpellBook spellBook;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemReceptacle.NAME) public static ItemReceptacle receptacle;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemEmptyReceptacle.NAME) public static ItemEmptyReceptacle emptyReceptacle;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemElementHolder.NAME_FIRE) public static ItemElementHolder fireElementHolder;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemElementHolder.NAME_WATER) public static ItemElementHolder waterElementHolder;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemElementHolder.NAME_EARTH) public static ItemElementHolder earthElementHolder;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemElementHolder.NAME_AIR) public static ItemElementHolder airElementHolder;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemBossDimKey.NAME) public static ItemBossDimKey bossDimKey;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemPureOre.NAME) public static ItemPureOre pureOre;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemRune.NAME) public static ItemRune rune;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemChisel.NAME) public static ItemChisel chisel;

	@ObjectHolder("patchouli:guide_book") public static Item elementopedia;
	@ObjectHolder(ElementalCraft.MODID + ":inertcrystal") public static ItemEC inertCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":containedcrystal") public static ItemEC containedCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":purecrystal") public static ItemEC pureCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":drenched_iron_ingot") public static ItemEC drenchedIronIngot;
	@ObjectHolder(ElementalCraft.MODID + ":drenched_iron_nugget") public static ItemEC drenchedIronNugget;
	@ObjectHolder(ElementalCraft.MODID + ":swift_alloy_ingot") public static ItemEC swiftAlloyIngot;
	@ObjectHolder(ElementalCraft.MODID + ":swift_alloy_nugget") public static ItemEC swiftAlloyNugget;
	@ObjectHolder(ElementalCraft.MODID + ":hardened_handle") public static ItemEC hardenedHandle;
	@ObjectHolder(ElementalCraft.MODID + ":shrinebase") public static ItemEC shrineBase;
	@ObjectHolder(ElementalCraft.MODID + ":fireite_ingot") public static ItemEC fireiteIngot;
	@ObjectHolder(ElementalCraft.MODID + ":fireite_nugget") public static ItemEC fireiteNugget;
	@ObjectHolder(ElementalCraft.MODID + ":air_silk") public static ItemEC airSilk;
	@ObjectHolder(ElementalCraft.MODID + ":shrine_upgrade_core") public static ItemEC shrineUpgradeCore;
	@ObjectHolder(ElementalCraft.MODID + ":scroll_paper") public static ItemEC scrollPaper;

	@ObjectHolder(ElementalCraft.MODID + ":firecrystal") public static ItemElemental fireCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":watercrystal") public static ItemElemental waterCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":earthcrystal") public static ItemElemental earthCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":aircrystal") public static ItemElemental airCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_FIRE) public static ItemShard fireShard;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_WATER) public static ItemShard waterShard;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_EARTH) public static ItemShard earthShard;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_AIR) public static ItemShard airShard;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_FIRE_POWERFUL) public static ItemShard powerfulFireShard;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_WATER_POWERFUL) public static ItemShard powerfulWaterShard;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_EARTH_POWERFUL) public static ItemShard powerfulEarthShard;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemShard.NAME_AIR_POWERFUL) public static ItemShard powerfulAirShard;
	@ObjectHolder(ElementalCraft.MODID + ":crude_fire_gem") public static ItemElemental crudeFireGem;
	@ObjectHolder(ElementalCraft.MODID + ":crude_water_gem") public static ItemElemental crudeWaterGem;
	@ObjectHolder(ElementalCraft.MODID + ":crude_earth_gem") public static ItemElemental crudeEarthGem;
	@ObjectHolder(ElementalCraft.MODID + ":crude_air_gem") public static ItemElemental crudeAirGem;
	@ObjectHolder(ElementalCraft.MODID + ":fine_fire_gem") public static ItemElemental fineFireGem;
	@ObjectHolder(ElementalCraft.MODID + ":fine_water_gem") public static ItemElemental fineWaterGem;
	@ObjectHolder(ElementalCraft.MODID + ":fine_earth_gem") public static ItemElemental fineEarthGem;
	@ObjectHolder(ElementalCraft.MODID + ":fine_air_gem") public static ItemElemental fineAirGem;
	@ObjectHolder(ElementalCraft.MODID + ":pristine_fire_gem") public static ItemElemental pristineFireGem;
	@ObjectHolder(ElementalCraft.MODID + ":pristine_water_gem") public static ItemElemental pristineWaterGem;
	@ObjectHolder(ElementalCraft.MODID + ":pristine_earth_gem") public static ItemElemental pristineEarthGem;
	@ObjectHolder(ElementalCraft.MODID + ":pristine_air_gem") public static ItemElemental pristineAirGem;
	@ObjectHolder(ElementalCraft.MODID + ":minor_rune_slate") public static ItemEC minorRuneSlate;
	@ObjectHolder(ElementalCraft.MODID + ":rune_slate") public static ItemEC runeSlate;
	@ObjectHolder(ElementalCraft.MODID + ":major_rune_slate") public static ItemEC majorRuneSlate;

	// BLOCKS
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTankSmall.NAME) public static Item tankSmall;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTank.NAME) public static Item tank;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockExtractor.NAME) public static Item extractor;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockImprovedExtractor.NAME) public static Item improvedExtractor;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockEvaporator.NAME) public static Item evaporator;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockInfuser.NAME) public static Item infuser;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBinder.NAME) public static Item binder;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockImprovedBinder.NAME) public static Item improvedBinder;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockCrystallizer.NAME) public static Item crystallizer;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockInscriber.NAME) public static Item inscriber;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_FIRE) public static Item firePedestal;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_WATER) public static Item waterPedestal;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_EARTH) public static Item earthPedestal;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_AIR) public static Item airPedestal;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPureInfuser.NAME) public static Item pureInfuser;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFireFurnace.NAME) public static Item fireFurnace;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFireBlastFurnace.NAME) public static Item fireBlastFurnace;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPurifier.NAME) public static Item purifier;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME_IMPAIRED) public static Item impairedElementPipe;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME) public static Item elementPipe;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME_IMPROVED) public static Item improvedElementPipe;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockRetriever.NAME) public static Item instrumentRetriever;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFirePylon.NAME) public static Item firePylon;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockVacuumShrine.NAME) public static Item vacuumShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockGrowthShrine.NAME) public static Item growthShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockHarvestShrine.NAME) public static Item harvestShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockLavaShrine.NAME) public static Item lavaShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOreShrine.NAME) public static Item oreShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOverloadShrine.NAME) public static Item overloadShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSweetShrine.NAME) public static Item sweetShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockEnderLockShrine.NAME) public static Item enderLockShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBreedingShrine.NAME) public static Item breedingShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockGroveShrine.NAME) public static Item groveShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockAccelerationShrineUpgrade.NAME) public static Item accelerationShrineUpgrade;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockRangeShrineUpgrade.NAME) public static Item rangeShrineUpgrade;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockCapacityShrineUpgrade.NAME) public static Item capacityShrineUpgrade;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockEfficiencyShrineUpgrade.NAME) public static Item efficiencyShrineUpgrade;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockStrengthShrineUpgrade.NAME) public static Item strengthShrineUpgrade;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOptimizationShrineUpgrade.NAME) public static Item optimizationShrineUpgrade;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFortuneShrineUpgrade.NAME) public static Item fortuneShrineUpgrade;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSilkTouchShrineUpgrade.NAME) public static Item silkTouchShrineUpgrade;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPlantingShrineUpgrade.NAME) public static Item plantingShrineUpgrade;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBonelessGrowthShrineUpgrade.NAME) public static Item bonelseeGrowthShrineUpgrade;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPickupShrineUpgrade.NAME) public static Item pickupShrineUpgrade;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockNectarShrineUpgrade.NAME) public static Item nectarShrineUpgrade;

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSource.NAME) public static Item source;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockCrystalOre.NAME) public static Item crystalOre;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock") public static Item whiteRock;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_slab") public static Item whiteRockSlab;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_stairs") public static Item whiteRockStairs;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_wall") public static Item whiteRockWall;
	@ObjectHolder(ElementalCraft.MODID + ":purerock") public static Item pureRock;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_slab") public static Item pureRockSlab;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_stairs") public static Item pureRockStairs;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_wall") public static Item pureRockWall;
	@ObjectHolder(ElementalCraft.MODID + ":burnt_glass") public static Item burntGlass;
	@ObjectHolder(ElementalCraft.MODID + ":burnt_glass_pane") public static Item burntGlassPane;
	@ObjectHolder(ElementalCraft.MODID + ":drenched_iron_block") public static Item drenchedIronBlock;
	@ObjectHolder(ElementalCraft.MODID + ":swift_alloy_block") public static Item swiftAlloyBlock;
	@ObjectHolder(ElementalCraft.MODID + ":fireite_block") public static Item fireiteBlock;

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
		RegistryHelper.register(registry, new ItemEC(), "minor_rune_slate");
		RegistryHelper.register(registry, new ItemEC(), "rune_slate");
		RegistryHelper.register(registry, new ItemEC(), "major_rune_slate");

		// TODO add tools
	}

	public static void replaceModels(ModelBakeEvent event) {
		ModelResourceLocation key = new ModelResourceLocation(ElementalCraft.createRL(ItemRune.NAME), "inventory");
		IBakedModel oldModel = event.getModelRegistry().get(key);
		
		if (oldModel != null) {
			event.getModelRegistry().put(key, new RuneModel(oldModel));
		}
	}

	public static void registerItemColors(ColorHandlerEvent.Item event) {
		event.getItemColors().register((s, l) -> l == 0 ? -1 : ReceptacleHelper.getElementType(s).getColor(), receptacle);
		event.getItemColors().register((s, l) -> l == 0 ? -1 : ElementalCraft.PURE_ORE_MANAGER.getColor(s), pureOre);
		event.getItemColors().register((s, l) -> l == 0 ? -1 : SpellHelper.getSpell(s).getColor(), scroll);
		event.getItemColors().register((s, l) -> l == 0 ? -1 : ((ItemElementHolder) s.getItem()).getElementType().getColor(), 
				fireElementHolder, waterElementHolder, earthElementHolder, airElementHolder);
	}
}
