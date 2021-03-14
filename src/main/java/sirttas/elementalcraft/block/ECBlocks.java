package sirttas.elementalcraft.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DatagenModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.evaporator.BlockEvaporator;
import sirttas.elementalcraft.block.evaporator.TileEvaporator;
import sirttas.elementalcraft.block.extractor.BlockExtractor;
import sirttas.elementalcraft.block.extractor.TileExtractor;
import sirttas.elementalcraft.block.extractor.improved.BlockImprovedExtractor;
import sirttas.elementalcraft.block.instrument.binder.BlockBinder;
import sirttas.elementalcraft.block.instrument.binder.TileBinder;
import sirttas.elementalcraft.block.instrument.binder.improved.BlockImprovedBinder;
import sirttas.elementalcraft.block.instrument.binder.improved.TileImprovedBinder;
import sirttas.elementalcraft.block.instrument.crystallizer.BlockCrystallizer;
import sirttas.elementalcraft.block.instrument.crystallizer.TileCrystallizer;
import sirttas.elementalcraft.block.instrument.firefurnace.BlockFireFurnace;
import sirttas.elementalcraft.block.instrument.firefurnace.TileFireFurnace;
import sirttas.elementalcraft.block.instrument.firefurnace.blast.BlockFireBlastFurnace;
import sirttas.elementalcraft.block.instrument.firefurnace.blast.TileFireBlastFurnace;
import sirttas.elementalcraft.block.instrument.infuser.BlockInfuser;
import sirttas.elementalcraft.block.instrument.infuser.TileInfuser;
import sirttas.elementalcraft.block.instrument.inscriber.BlockInscriber;
import sirttas.elementalcraft.block.instrument.inscriber.TileInscriber;
import sirttas.elementalcraft.block.instrument.mill.BlockAirMill;
import sirttas.elementalcraft.block.instrument.mill.TileAirMill;
import sirttas.elementalcraft.block.instrument.purifier.BlockPurifier;
import sirttas.elementalcraft.block.instrument.purifier.TilePurifier;
import sirttas.elementalcraft.block.pipe.BlockElementPipe;
import sirttas.elementalcraft.block.pipe.TileElementPipe;
import sirttas.elementalcraft.block.pureinfuser.BlockPureInfuser;
import sirttas.elementalcraft.block.pureinfuser.TilePureInfuser;
import sirttas.elementalcraft.block.pureinfuser.pedestal.BlockPedestal;
import sirttas.elementalcraft.block.pureinfuser.pedestal.TilePedestal;
import sirttas.elementalcraft.block.retriever.BlockRetriever;
import sirttas.elementalcraft.block.shrine.breeding.BlockBreedingShrine;
import sirttas.elementalcraft.block.shrine.breeding.TileBreedingShrine;
import sirttas.elementalcraft.block.shrine.enderlock.BlockEnderLockShrine;
import sirttas.elementalcraft.block.shrine.enderlock.TileEnderLockShrine;
import sirttas.elementalcraft.block.shrine.firepylon.BlockFirePylon;
import sirttas.elementalcraft.block.shrine.firepylon.TileFirePylon;
import sirttas.elementalcraft.block.shrine.grove.BlockGroveShrine;
import sirttas.elementalcraft.block.shrine.grove.TileGroveShrine;
import sirttas.elementalcraft.block.shrine.growth.BlockGrowthShrine;
import sirttas.elementalcraft.block.shrine.growth.TileGrowthShrine;
import sirttas.elementalcraft.block.shrine.harvest.BlockHarvestShrine;
import sirttas.elementalcraft.block.shrine.harvest.TileHarvestShrine;
import sirttas.elementalcraft.block.shrine.lava.BlockLavaShrine;
import sirttas.elementalcraft.block.shrine.lava.TileLavaShrine;
import sirttas.elementalcraft.block.shrine.ore.BlockOreShrine;
import sirttas.elementalcraft.block.shrine.ore.TileOreShrine;
import sirttas.elementalcraft.block.shrine.overload.BlockOverloadShrine;
import sirttas.elementalcraft.block.shrine.overload.TileOverloadShrine;
import sirttas.elementalcraft.block.shrine.sweet.BlockSweetShrine;
import sirttas.elementalcraft.block.shrine.sweet.TileSweetShrine;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockCapacityShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockEfficiencyShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockOptimizationShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockRangeShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.BlockStrengthShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.BlockAccelerationShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.directional.acceleration.TileAccelerationShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockFortuneShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockNectarShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.horizontal.BlockSilkTouchShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockBonelessGrowthShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockMysticalGroveShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockPickupShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockPlantingShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.unidirectional.BlockStemPollinationShrineUpgrade;
import sirttas.elementalcraft.block.shrine.vacuum.BlockVacuumShrine;
import sirttas.elementalcraft.block.shrine.vacuum.TileVacuumShrine;
import sirttas.elementalcraft.block.solarsynthesizer.BlockSolarSynthesizer;
import sirttas.elementalcraft.block.solarsynthesizer.TileSolarSynthesizer;
import sirttas.elementalcraft.block.sorter.BlockSorter;
import sirttas.elementalcraft.block.sorter.TileSorter;
import sirttas.elementalcraft.block.source.BlockSource;
import sirttas.elementalcraft.block.source.TileSource;
import sirttas.elementalcraft.block.spelldesk.BlockSpellDesk;
import sirttas.elementalcraft.block.tank.BlockTank;
import sirttas.elementalcraft.block.tank.BlockTankSmall;
import sirttas.elementalcraft.block.tank.TileTank;
import sirttas.elementalcraft.block.tank.creative.BlockTankCreative;
import sirttas.elementalcraft.block.tank.creative.TileTankCreative;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECBlocks {

	public static final AbstractBlock.IPositionPredicate ALWAYS_FALSE = (a, b, c) -> false;

	private ECBlocks() {
	}

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTankSmall.NAME) public static final BlockTankSmall TANK_SMALL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTank.NAME) public static final BlockTank TANK = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTankCreative.NAME) public static final BlockTankCreative TANK_CREATIVE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockExtractor.NAME) public static final BlockExtractor EXTRACTOR = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockImprovedExtractor.NAME) public static final BlockImprovedExtractor EXTRACTOR_IMPROVED = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockEvaporator.NAME) public static final BlockEvaporator EVAPORATOR = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSolarSynthesizer.NAME) public static final BlockSolarSynthesizer SOLAR_SYNTHESIZER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockInfuser.NAME) public static final BlockInfuser INFUSER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBinder.NAME) public static final BlockBinder BINDER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockImprovedBinder.NAME) public static final BlockImprovedBinder BINDER_IMPROVED = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockCrystallizer.NAME) public static final BlockCrystallizer CRYSTALLIZER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockInscriber.NAME) public static final BlockInscriber INSCRIBER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockAirMill.NAME) public static final BlockAirMill AIR_MILL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_FIRE) public static final BlockPedestal FIRE_PEDESTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_WATER) public static final BlockPedestal WATER_PEDESTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_EARTH) public static final BlockPedestal EARTH_PEDESTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_AIR) public static final BlockPedestal AIR_PEDESTAL = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPureInfuser.NAME) public static final BlockPureInfuser PURE_INFUSER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFireFurnace.NAME) public static final BlockFireFurnace FIRE_FURNACE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFireBlastFurnace.NAME) public static final BlockFireBlastFurnace FIRE_BLAST_FURNACE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPurifier.NAME) public static final BlockPurifier PURIFIER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME_IMPAIRED) public static final BlockElementPipe PIPE_IMPAIRED = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME) public static final BlockElementPipe PIPE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME_IMPROVED) public static final BlockElementPipe PIPE_IMPROVED = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockRetriever.NAME) public static final BlockRetriever RETRIEVER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSorter.NAME) public static final BlockSorter SORTER = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSpellDesk.NAME) public static final BlockSpellDesk SPELL_DESK = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFirePylon.NAME) public static final BlockFirePylon FIRE_PYLON = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockVacuumShrine.NAME) public static final BlockVacuumShrine VACUUM_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockGrowthShrine.NAME) public static final BlockGrowthShrine GROWTH_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockHarvestShrine.NAME) public static final BlockHarvestShrine HARVEST_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockLavaShrine.NAME) public static final BlockLavaShrine LAVA_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOreShrine.NAME) public static final BlockOreShrine ORE_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOverloadShrine.NAME) public static final BlockOverloadShrine OVERLOAD_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSweetShrine.NAME) public static final BlockSweetShrine SWEET_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockEnderLockShrine.NAME) public static final BlockEnderLockShrine ENDER_LOCK_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBreedingShrine.NAME) public static final BlockBreedingShrine BREEDING_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockGroveShrine.NAME) public static final BlockGroveShrine GROVE_SHRINE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockAccelerationShrineUpgrade.NAME) public static final BlockAccelerationShrineUpgrade ACCELERATION_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockRangeShrineUpgrade.NAME) public static final BlockRangeShrineUpgrade RANGE_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockCapacityShrineUpgrade.NAME) public static final BlockCapacityShrineUpgrade CAPACITY_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockEfficiencyShrineUpgrade.NAME) public static final BlockEfficiencyShrineUpgrade EFFICIENCY_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockStrengthShrineUpgrade.NAME) public static final BlockStrengthShrineUpgrade STRENGTH_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOptimizationShrineUpgrade.NAME) public static final BlockOptimizationShrineUpgrade OPTIMIZATION_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFortuneShrineUpgrade.NAME) public static final BlockFortuneShrineUpgrade FORTUNE_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSilkTouchShrineUpgrade.NAME) public static final BlockSilkTouchShrineUpgrade SILK_TOUCH_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPlantingShrineUpgrade.NAME) public static final BlockPlantingShrineUpgrade PLANTING_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBonelessGrowthShrineUpgrade.NAME) public static final BlockBonelessGrowthShrineUpgrade BONELESS_GROWTH_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPickupShrineUpgrade.NAME) public static final BlockPickupShrineUpgrade PICKUP_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockNectarShrineUpgrade.NAME) public static final BlockNectarShrineUpgrade NECTAR_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockMysticalGroveShrineUpgrade.NAME) public static final BlockMysticalGroveShrineUpgrade MYSTICAL_GROVE_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockStemPollinationShrineUpgrade.NAME) public static final BlockStemPollinationShrineUpgrade STEM_POLLINATION_SHRINE_UPGRADE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSource.NAME) public static final BlockSource SOURCE = null;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockCrystalOre.NAME) public static final BlockCrystalOre CRYSTAL_ORE = null;
	
	@ObjectHolder(ElementalCraft.MODID + ":whiterock") public static final Block WHITE_ROCK = null;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_slab") public static final SlabBlock WHITE_ROCK_SLAB = null;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_stairs") public static final StairsBlock WHITE_ROCK_STAIRS = null;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_wall") public static final WallBlock WHITE_ROCK_WALL = null;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_fence") public static final FenceBlock WHITE_ROCK_FENCE = null;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_brick") public static final Block WHITE_ROCK_BRICK = null;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_brick_slab") public static final SlabBlock WHITE_ROCK_BRICK_SLAB = null;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_brick_stairs") public static final StairsBlock WHITE_ROCK_BRICK_STAIRS = null;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_brick_wall") public static final WallBlock WHITE_ROCK_BRICK_WALL = null;
	@ObjectHolder(ElementalCraft.MODID + ":purerock") public static final Block PURE_ROCK = null;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_slab") public static final SlabBlock PURE_ROCK_SLAB = null;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_stairs") public static final StairsBlock PURE_ROCK_STAIRS = null;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_wall") public static final WallBlock PURE_ROCK_WALL = null;
	@ObjectHolder(ElementalCraft.MODID + ":burnt_glass") public static final GlassBlock BURNT_GLASS = null;
	@ObjectHolder(ElementalCraft.MODID + ":burnt_glass_pane") public static final PaneBlock BURNT_GLASS_PANE = null;
	@ObjectHolder(ElementalCraft.MODID + ":drenched_iron_block") public static final Block DRENCHED_IRON_BLOCK = null;
	@ObjectHolder(ElementalCraft.MODID + ":swift_alloy_block") public static final Block SWIFT_ALLOY_BLOCK = null;
	@ObjectHolder(ElementalCraft.MODID + ":fireite_block") public static final Block FIREITE_BLOCK = null;
	@ObjectHolder(ElementalCraft.MODID + ":inertcrystal_block") public static final Block INERT_CRYSTAL_BLOCK = null;
	@ObjectHolder(ElementalCraft.MODID + ":firecrystal_block") public static final Block FIRE_CRYSTAL_BLOCK = null;
	@ObjectHolder(ElementalCraft.MODID + ":watercrystal_block") public static final Block WATER_CRYSTAL_BLOCK = null;
	@ObjectHolder(ElementalCraft.MODID + ":earthcrystal_block") public static final Block EARTH_CRYSTAL_BLOCK = null;
	@ObjectHolder(ElementalCraft.MODID + ":aircrystal_block") public static final Block AIR_CRYSTAL_BLOCK = null;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		RegistryHelper.register(registry, new BlockTankSmall(), BlockTankSmall.NAME);
		RegistryHelper.register(registry, new BlockTank(), BlockTank.NAME);
		RegistryHelper.register(registry, new BlockTankCreative(), BlockTankCreative.NAME);
		RegistryHelper.register(registry, new BlockExtractor(), BlockExtractor.NAME);
		RegistryHelper.register(registry, new BlockImprovedExtractor(), BlockImprovedExtractor.NAME);
		RegistryHelper.register(registry, new BlockEvaporator(), BlockEvaporator.NAME);
		RegistryHelper.register(registry, new BlockSolarSynthesizer(), BlockSolarSynthesizer.NAME);
		RegistryHelper.register(registry, new BlockInfuser(), BlockInfuser.NAME);
		RegistryHelper.register(registry, new BlockBinder(), BlockBinder.NAME);
		RegistryHelper.register(registry, new BlockImprovedBinder(), BlockImprovedBinder.NAME);
		RegistryHelper.register(registry, new BlockCrystallizer(), BlockCrystallizer.NAME);
		RegistryHelper.register(registry, new BlockInscriber(), BlockInscriber.NAME);
		RegistryHelper.register(registry, new BlockAirMill(), BlockAirMill.NAME);
		RegistryHelper.register(registry, new BlockPedestal(ElementType.FIRE), BlockPedestal.NAME_FIRE);
		RegistryHelper.register(registry, new BlockPedestal(ElementType.WATER), BlockPedestal.NAME_WATER);
		RegistryHelper.register(registry, new BlockPedestal(ElementType.EARTH), BlockPedestal.NAME_EARTH);
		RegistryHelper.register(registry, new BlockPedestal(ElementType.AIR), BlockPedestal.NAME_AIR);
		RegistryHelper.register(registry, new BlockPureInfuser(), BlockPureInfuser.NAME);
		RegistryHelper.register(registry, new BlockFireFurnace(), BlockFireFurnace.NAME);
		RegistryHelper.register(registry, new BlockFireBlastFurnace(), BlockFireBlastFurnace.NAME);
		RegistryHelper.register(registry, new BlockPurifier(), BlockPurifier.NAME);
		RegistryHelper.register(registry, new BlockElementPipe(ECConfig.COMMON.impairedPipeTransferAmount.get()), BlockElementPipe.NAME_IMPAIRED);
		RegistryHelper.register(registry, new BlockElementPipe(ECConfig.COMMON.pipeTransferAmount.get()), BlockElementPipe.NAME);
		RegistryHelper.register(registry, new BlockElementPipe(ECConfig.COMMON.improvedPipeTransferAmount.get()), BlockElementPipe.NAME_IMPROVED);
		RegistryHelper.register(registry, new BlockRetriever(), BlockRetriever.NAME);
		RegistryHelper.register(registry, new BlockSorter(), BlockSorter.NAME);
		RegistryHelper.register(registry, new BlockSpellDesk(), BlockSpellDesk.NAME);
		RegistryHelper.register(registry, new BlockFirePylon(), BlockFirePylon.NAME);
		RegistryHelper.register(registry, new BlockVacuumShrine(), BlockVacuumShrine.NAME);
		RegistryHelper.register(registry, new BlockGrowthShrine(), BlockGrowthShrine.NAME);
		RegistryHelper.register(registry, new BlockHarvestShrine(), BlockHarvestShrine.NAME);
		RegistryHelper.register(registry, new BlockLavaShrine(), BlockLavaShrine.NAME);
		RegistryHelper.register(registry, new BlockOreShrine(), BlockOreShrine.NAME);
		RegistryHelper.register(registry, new BlockOverloadShrine(), BlockOverloadShrine.NAME);
		RegistryHelper.register(registry, new BlockSweetShrine(), BlockSweetShrine.NAME);
		RegistryHelper.register(registry, new BlockEnderLockShrine(), BlockEnderLockShrine.NAME);
		RegistryHelper.register(registry, new BlockBreedingShrine(), BlockBreedingShrine.NAME);
		RegistryHelper.register(registry, new BlockGroveShrine(), BlockGroveShrine.NAME);
		RegistryHelper.register(registry, new BlockAccelerationShrineUpgrade(), BlockAccelerationShrineUpgrade.NAME);
		RegistryHelper.register(registry, new BlockRangeShrineUpgrade(), BlockRangeShrineUpgrade.NAME);
		RegistryHelper.register(registry, new BlockCapacityShrineUpgrade(), BlockCapacityShrineUpgrade.NAME);
		RegistryHelper.register(registry, new BlockEfficiencyShrineUpgrade(), BlockEfficiencyShrineUpgrade.NAME);
		RegistryHelper.register(registry, new BlockStrengthShrineUpgrade(), BlockStrengthShrineUpgrade.NAME);
		RegistryHelper.register(registry, new BlockOptimizationShrineUpgrade(), BlockOptimizationShrineUpgrade.NAME);
		RegistryHelper.register(registry, new BlockFortuneShrineUpgrade(), BlockFortuneShrineUpgrade.NAME);
		RegistryHelper.register(registry, new BlockSilkTouchShrineUpgrade(), BlockSilkTouchShrineUpgrade.NAME);
		RegistryHelper.register(registry, new BlockPlantingShrineUpgrade(), BlockPlantingShrineUpgrade.NAME);
		RegistryHelper.register(registry, new BlockBonelessGrowthShrineUpgrade(), BlockBonelessGrowthShrineUpgrade.NAME);
		RegistryHelper.register(registry, new BlockPickupShrineUpgrade(), BlockPickupShrineUpgrade.NAME);
		RegistryHelper.register(registry, new BlockNectarShrineUpgrade(), BlockNectarShrineUpgrade.NAME);
		RegistryHelper.register(registry, new BlockStemPollinationShrineUpgrade(), BlockStemPollinationShrineUpgrade.NAME);

		RegistryHelper.register(registry, new BlockSource(), BlockSource.NAME);
		RegistryHelper.register(registry, new BlockCrystalOre(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)), BlockCrystalOre.NAME);
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.WHITEROCK), "whiterock");
		RegistryHelper.register(registry, new SlabBlock(ECProperties.Blocks.WHITEROCK), "whiterock_slab");
		RegistryHelper.register(registry, new StairsBlock(() -> WHITE_ROCK.getDefaultState(), ECProperties.Blocks.WHITEROCK), "whiterock_stairs");
		RegistryHelper.register(registry, new WallBlock(ECProperties.Blocks.WHITEROCK), "whiterock_wall");
		RegistryHelper.register(registry, new FenceBlock(ECProperties.Blocks.WHITEROCK), "whiterock_fence");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.WHITEROCK), "whiterock_brick");
		RegistryHelper.register(registry, new SlabBlock(ECProperties.Blocks.WHITEROCK), "whiterock_brick_slab");
		RegistryHelper.register(registry, new StairsBlock(() -> WHITE_ROCK_BRICK.getDefaultState(), ECProperties.Blocks.WHITEROCK), "whiterock_brick_stairs");
		RegistryHelper.register(registry, new WallBlock(ECProperties.Blocks.WHITEROCK), "whiterock_brick_wall");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.PUREROCK), "purerock");
		RegistryHelper.register(registry, new SlabBlock(ECProperties.Blocks.PUREROCK), "purerock_slab");
		RegistryHelper.register(registry, new StairsBlock(() -> PURE_ROCK.getDefaultState(), ECProperties.Blocks.PUREROCK), "purerock_stairs");
		RegistryHelper.register(registry, new WallBlock(ECProperties.Blocks.PUREROCK), "purerock_wall");
		RegistryHelper.register(registry, new GlassBlock(AbstractBlock.Properties.create(Material.GLASS).hardnessAndResistance(0.7F).sound(SoundType.GLASS).notSolid()
				.setAllowsSpawn((a, b, c, d) -> false).setOpaque(ALWAYS_FALSE).setSuffocates(ALWAYS_FALSE).setBlocksVision(ALWAYS_FALSE)), "burnt_glass");
		RegistryHelper.register(registry, new PaneBlock(AbstractBlock.Properties.create(Material.GLASS).hardnessAndResistance(0.7F).sound(SoundType.GLASS).notSolid()), "burnt_glass_pane");
		RegistryHelper.register(registry, new Block(AbstractBlock.Properties.create(Material.IRON, MaterialColor.IRON).setRequiresTool().hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL)),
				"drenched_iron_block");
		RegistryHelper.register(registry, new Block(AbstractBlock.Properties.create(Material.IRON, MaterialColor.GOLD).setRequiresTool().hardnessAndResistance(3.0F, 6.0F).sound(SoundType.METAL)),
				"swift_alloy_block");
		RegistryHelper.register(registry,
				new Block(AbstractBlock.Properties.create(Material.IRON, MaterialColor.BLACK).setRequiresTool().hardnessAndResistance(50.0F, 1200.0F).sound(SoundType.NETHERITE)), "fireite_block");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES), "inertcrystal_block");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES), "firecrystal_block");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES), "watercrystal_block");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES), "earthcrystal_block");
		RegistryHelper.register(registry, new Block(ECProperties.Blocks.DEFAULT_BLOCK_PROPERTIES), "aircrystal_block");
		
		if (DatagenModLoader.isRunningDataGen() || ECinteractions.isBotaniaActive()) {
			RegistryHelper.register(registry, new BlockMysticalGroveShrineUpgrade(), BlockMysticalGroveShrineUpgrade.NAME);
		}
		
	}

	@SubscribeEvent
	public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt) {
		IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();

		RegistryHelper.register(r, TileEntityType.Builder.create(TileSource::new, SOURCE).build(null), BlockSource.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileTank::new, TANK, TANK_SMALL).build(null), BlockTank.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileTankCreative::new, TANK_CREATIVE).build(null), BlockTankCreative.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileExtractor::new, EXTRACTOR, EXTRACTOR_IMPROVED).build(null), BlockExtractor.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileEvaporator::new, EVAPORATOR).build(null), BlockEvaporator.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileSolarSynthesizer::new, SOLAR_SYNTHESIZER).build(null), BlockSolarSynthesizer.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileInfuser::new, INFUSER).build(null), BlockInfuser.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileBinder::new, BINDER).build(null), BlockBinder.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileImprovedBinder::new, BINDER_IMPROVED).build(null), BlockImprovedBinder.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileCrystallizer::new, CRYSTALLIZER).build(null), BlockCrystallizer.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileInscriber::new, INSCRIBER).build(null), BlockInscriber.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileAirMill::new, AIR_MILL).build(null), BlockAirMill.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TilePedestal::createFire, FIRE_PEDESTAL).build(null), BlockPedestal.NAME_FIRE);
		RegistryHelper.register(r, TileEntityType.Builder.create(TilePedestal::createWater, WATER_PEDESTAL).build(null), BlockPedestal.NAME_WATER);
		RegistryHelper.register(r, TileEntityType.Builder.create(TilePedestal::createEarth, EARTH_PEDESTAL).build(null), BlockPedestal.NAME_EARTH);
		RegistryHelper.register(r, TileEntityType.Builder.create(TilePedestal::createAir, AIR_PEDESTAL).build(null), BlockPedestal.NAME_AIR);
		RegistryHelper.register(r, TileEntityType.Builder.create(TilePureInfuser::new, PURE_INFUSER).build(null), BlockPureInfuser.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileFireFurnace::new, FIRE_FURNACE).build(null), BlockFireFurnace.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileFireBlastFurnace::new, FIRE_BLAST_FURNACE).build(null), BlockFireBlastFurnace.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TilePurifier::new, PURIFIER).build(null), BlockPurifier.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileElementPipe::new, PIPE_IMPAIRED, PIPE, PIPE_IMPROVED).build(null), BlockElementPipe.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileFirePylon::new, FIRE_PYLON).build(null), BlockFirePylon.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileVacuumShrine::new, VACUUM_SHRINE).build(null), BlockVacuumShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileGrowthShrine::new, GROWTH_SHRINE).build(null), BlockGrowthShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileHarvestShrine::new, HARVEST_SHRINE).build(null), BlockHarvestShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileLavaShrine::new, LAVA_SHRINE).build(null), BlockLavaShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileOreShrine::new, ORE_SHRINE).build(null), BlockOreShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileOverloadShrine::new, OVERLOAD_SHRINE).build(null), BlockOverloadShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileSweetShrine::new, SWEET_SHRINE).build(null), BlockSweetShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileEnderLockShrine::new, ENDER_LOCK_SHRINE).build(null), BlockEnderLockShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileBreedingShrine::new, BREEDING_SHRINE).build(null), BlockBreedingShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileGroveShrine::new, GROVE_SHRINE).build(null), BlockGroveShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileAccelerationShrineUpgrade::new, ACCELERATION_SHRINE_UPGRADE).build(null), BlockAccelerationShrineUpgrade.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileSorter::new, SORTER).build(null), BlockSorter.NAME);
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		RegistryHelper.register(registry, new BlockItem(FIREITE_BLOCK, ECProperties.Items.FIREITE), "fireite_block");
		for (Block block : ForgeRegistries.BLOCKS) {
			if (ElementalCraft.MODID.equals(block.getRegistryName().getNamespace()) && !registry.containsKey(block.getRegistryName())) {
				RegistryHelper.register(registry, new BlockItem(block, ECProperties.Items.DEFAULT_ITEM_PROPERTIES), block.getRegistryName());
			}
		}
	}
}
