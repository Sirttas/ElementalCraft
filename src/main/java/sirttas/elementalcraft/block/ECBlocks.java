package sirttas.elementalcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.extractor.BlockExtractor;
import sirttas.elementalcraft.block.extractor.TileExtractor;
import sirttas.elementalcraft.block.extractor.improved.BlockImprovedExtractor;
import sirttas.elementalcraft.block.extractor.improved.TileImprovedExtractor;
import sirttas.elementalcraft.block.instrument.binder.BlockBinder;
import sirttas.elementalcraft.block.instrument.binder.TileBinder;
import sirttas.elementalcraft.block.instrument.firefurnace.BlockFireFurnace;
import sirttas.elementalcraft.block.instrument.firefurnace.TileFireFurnace;
import sirttas.elementalcraft.block.instrument.infuser.BlockInfuser;
import sirttas.elementalcraft.block.instrument.infuser.TileInfuser;
import sirttas.elementalcraft.block.pipe.BlockElementPipe;
import sirttas.elementalcraft.block.pipe.TileElementPipe;
import sirttas.elementalcraft.block.pureinfuser.BlockPedestal;
import sirttas.elementalcraft.block.pureinfuser.BlockPureInfuser;
import sirttas.elementalcraft.block.pureinfuser.TilePedestal;
import sirttas.elementalcraft.block.pureinfuser.TilePureInfuser;
import sirttas.elementalcraft.block.shrine.firepylon.BlockFirePylon;
import sirttas.elementalcraft.block.shrine.firepylon.TileFirePylon;
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
import sirttas.elementalcraft.block.shrine.vacuum.BlockVacuumShrine;
import sirttas.elementalcraft.block.shrine.vacuum.TileVacuumShrine;
import sirttas.elementalcraft.block.source.BlockSource;
import sirttas.elementalcraft.block.tank.BlockTank;
import sirttas.elementalcraft.block.tank.TileTank;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECBlocks {

	private ECBlocks() {
	}

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTank.NAME) public static BlockTank tank;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockExtractor.NAME) public static BlockExtractor extractor;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockImprovedExtractor.NAME) public static BlockImprovedExtractor improvedExtractor;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockInfuser.NAME) public static BlockInfuser infuser;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBinder.NAME) public static BlockBinder binder;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_FIRE) public static BlockPedestal firePedestal;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_WATER) public static BlockPedestal waterPedestal;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_EARTH) public static BlockPedestal earthPedestal;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_AIR) public static BlockPedestal airPedestal;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPureInfuser.NAME) public static BlockPureInfuser pureInfuser;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFireFurnace.NAME) public static BlockFireFurnace fireFurnace;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME) public static BlockElementPipe elementPipe;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFirePylon.NAME) public static BlockFirePylon firePylon;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockVacuumShrine.NAME) public static BlockVacuumShrine vacuumShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockGrowthShrine.NAME) public static BlockGrowthShrine growthShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockHarvestShrine.NAME) public static BlockHarvestShrine harvestShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockLavaShrine.NAME) public static BlockLavaShrine lavaShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOreShrine.NAME) public static BlockOreShrine oreShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOverloadShrine.NAME) public static BlockOverloadShrine overloadShrine;

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSource.NAME) public static BlockSource source;
	@ObjectHolder(ElementalCraft.MODID + ":crystalore") public static BlockEC crystalOre;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock") public static BlockEC whiteRock;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_slab") public static SlabBlock whiteRockSlab;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_stairs") public static StairsBlock whiteRockStairs;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_wall") public static WallBlock whiteRockWall;
	@ObjectHolder(ElementalCraft.MODID + ":purerock") public static BlockEC pureRock;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_slab") public static SlabBlock pureRockSlab;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_stairs") public static StairsBlock pureRockStairs;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_wall") public static WallBlock pureRockWall;

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();

		RegistryHelper.register(registry, new BlockTank(), BlockTank.NAME);
		RegistryHelper.register(registry, new BlockExtractor(), BlockExtractor.NAME);
		RegistryHelper.register(registry, new BlockImprovedExtractor(), BlockImprovedExtractor.NAME);
		RegistryHelper.register(registry, new BlockInfuser(), BlockInfuser.NAME);
		RegistryHelper.register(registry, new BlockBinder(), BlockBinder.NAME);
		RegistryHelper.register(registry, new BlockPedestal(ElementType.FIRE), BlockPedestal.NAME_FIRE);
		RegistryHelper.register(registry, new BlockPedestal(ElementType.WATER), BlockPedestal.NAME_WATER);
		RegistryHelper.register(registry, new BlockPedestal(ElementType.EARTH), BlockPedestal.NAME_EARTH);
		RegistryHelper.register(registry, new BlockPedestal(ElementType.AIR), BlockPedestal.NAME_AIR);
		RegistryHelper.register(registry, new BlockPureInfuser(), BlockPureInfuser.NAME);
		RegistryHelper.register(registry, new BlockFireFurnace(), BlockFireFurnace.NAME);
		RegistryHelper.register(registry, new BlockElementPipe(), BlockElementPipe.NAME);
		RegistryHelper.register(registry, new BlockFirePylon(), BlockFirePylon.NAME);
		RegistryHelper.register(registry, new BlockVacuumShrine(), BlockVacuumShrine.NAME);
		RegistryHelper.register(registry, new BlockGrowthShrine(), BlockGrowthShrine.NAME);
		RegistryHelper.register(registry, new BlockHarvestShrine(), BlockHarvestShrine.NAME);
		RegistryHelper.register(registry, new BlockLavaShrine(), BlockLavaShrine.NAME);
		RegistryHelper.register(registry, new BlockOreShrine(), BlockOreShrine.NAME);
		RegistryHelper.register(registry, new BlockOverloadShrine(), BlockOverloadShrine.NAME);

		RegistryHelper.register(registry, new BlockSource(), BlockSource.NAME);
		RegistryHelper.register(registry, new BlockEC(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)), "crystalore");
		RegistryHelper.register(registry, new BlockEC(ECProperties.Blocks.WHITEROCK), "whiterock");
		RegistryHelper.register(registry, new SlabBlock(ECProperties.Blocks.WHITEROCK), "whiterock_slab");
		RegistryHelper.register(registry, new StairsBlock(() -> whiteRock.getDefaultState(), ECProperties.Blocks.WHITEROCK), "whiterock_stairs");
		RegistryHelper.register(registry, new WallBlock(ECProperties.Blocks.WHITEROCK), "whiterock_wall");
		RegistryHelper.register(registry, new BlockEC(ECProperties.Blocks.PUREROCK), "purerock");
		RegistryHelper.register(registry, new SlabBlock(ECProperties.Blocks.PUREROCK), "purerock_slab");
		RegistryHelper.register(registry, new StairsBlock(() -> pureRock.getDefaultState(), ECProperties.Blocks.PUREROCK), "purerock_stairs");
		RegistryHelper.register(registry, new WallBlock(ECProperties.Blocks.PUREROCK), "purerock_wall");
	}

	@SubscribeEvent
	public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt) {
		IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();

		RegistryHelper.register(r, TileEntityType.Builder.create(TileTank::new, tank).build(null), BlockTank.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileExtractor::new, extractor).build(null), BlockExtractor.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileImprovedExtractor::new, improvedExtractor).build(null), BlockImprovedExtractor.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileInfuser::new, infuser).build(null), BlockInfuser.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileBinder::new, binder).build(null), BlockBinder.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TilePedestal::new, firePedestal, waterPedestal, earthPedestal, airPedestal).build(null), BlockPedestal.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TilePureInfuser::new, pureInfuser).build(null), BlockPureInfuser.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileFireFurnace::new, fireFurnace).build(null), BlockFireFurnace.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileElementPipe::new, elementPipe).build(null), BlockElementPipe.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileFirePylon::new, firePylon).build(null), BlockFirePylon.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileVacuumShrine::new, vacuumShrine).build(null), BlockVacuumShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileGrowthShrine::new, growthShrine).build(null), BlockGrowthShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileHarvestShrine::new, harvestShrine).build(null), BlockHarvestShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileLavaShrine::new, lavaShrine).build(null), BlockLavaShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileOreShrine::new, oreShrine).build(null), BlockOreShrine.NAME);
		RegistryHelper.register(r, TileEntityType.Builder.create(TileOverloadShrine::new, overloadShrine).build(null), BlockOverloadShrine.NAME);
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		for (Block block : ForgeRegistries.BLOCKS) {
			if (ElementalCraft.MODID.equals(block.getRegistryName().getNamespace())) {
				RegistryHelper.register(registry, new BlockItem(block, ECProperties.Items.DEFAULT_ITEM_PROPERTIES), block.getRegistryName());
			}
		}
	}
}
