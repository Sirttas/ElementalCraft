package sirttas.elementalcraft.item;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.ElementalCraftTab;
import sirttas.elementalcraft.block.extractor.BlockExtractor;
import sirttas.elementalcraft.block.extractor.improved.BlockImprovedExtractor;
import sirttas.elementalcraft.block.instrument.binder.BlockBinder;
import sirttas.elementalcraft.block.instrument.firefurnace.BlockFireFurnace;
import sirttas.elementalcraft.block.instrument.firefurnace.blast.BlockFireBlastFurnace;
import sirttas.elementalcraft.block.instrument.infuser.BlockInfuser;
import sirttas.elementalcraft.block.instrument.purifier.BlockPurifier;
import sirttas.elementalcraft.block.pipe.BlockElementPipe;
import sirttas.elementalcraft.block.pureinfuser.BlockPedestal;
import sirttas.elementalcraft.block.pureinfuser.BlockPureInfuser;
import sirttas.elementalcraft.block.shrine.firepylon.BlockFirePylon;
import sirttas.elementalcraft.block.shrine.growth.BlockGrowthShrine;
import sirttas.elementalcraft.block.shrine.harvest.BlockHarvestShrine;
import sirttas.elementalcraft.block.shrine.lava.BlockLavaShrine;
import sirttas.elementalcraft.block.shrine.ore.BlockOreShrine;
import sirttas.elementalcraft.block.shrine.overload.BlockOverloadShrine;
import sirttas.elementalcraft.block.shrine.sweet.BlockSweetShrine;
import sirttas.elementalcraft.block.shrine.vacuum.BlockVacuumShrine;
import sirttas.elementalcraft.block.source.BlockSource;
import sirttas.elementalcraft.block.tank.BlockTank;
import sirttas.elementalcraft.block.tank.BlockTankSmall;
import sirttas.elementalcraft.item.pureore.ItemPureOre;
import sirttas.elementalcraft.item.pureore.PureOreHelper;
import sirttas.elementalcraft.item.receptacle.ItemEmptyReceptacle;
import sirttas.elementalcraft.item.receptacle.ItemReceptacle;
import sirttas.elementalcraft.item.receptacle.ReceptacleHelper;
import sirttas.elementalcraft.item.spell.ItemScroll;
import sirttas.elementalcraft.item.tool.ItemFocus;
import sirttas.elementalcraft.registry.RegistryHelper;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECItems {

	@ObjectHolder(ElementalCraft.MODID + ":" + ItemFocus.NAME) public static ItemFocus focus;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemScroll.NAME) public static ItemScroll scroll;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemReceptacle.NAME) public static ItemReceptacle receptacle;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemEmptyReceptacle.NAME) public static ItemEmptyReceptacle emptyReceptacle;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemBossDimKey.NAME) public static ItemBossDimKey bossDimKey;
	@ObjectHolder(ElementalCraft.MODID + ":" + ItemPureOre.NAME) public static ItemPureOre pureOre;

	@ObjectHolder("patchouli:guide_book") public static Item elementScript;
	@ObjectHolder(ElementalCraft.MODID + ":inertcrystal") public static ItemEC inertCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":containedcrystal") public static ItemEC containedCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":firecrystal") public static ItemEC fireCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":watercrystal") public static ItemEC waterCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":earthcrystal") public static ItemEC earthCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":aircrystal") public static ItemEC airCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":purecrystal") public static ItemEC pureCrystal;
	@ObjectHolder(ElementalCraft.MODID + ":drenched_iron_ingot") public static ItemEC drenchedIronIngot;
	@ObjectHolder(ElementalCraft.MODID + ":drenched_iron_nugget") public static ItemEC drenchedIronNugget;
	@ObjectHolder(ElementalCraft.MODID + ":swift_alloy_ingot") public static ItemEC swiftAlloyIngot;
	@ObjectHolder(ElementalCraft.MODID + ":swift_alloy_nugget") public static ItemEC swiftAlloyNugget;
	@ObjectHolder(ElementalCraft.MODID + ":shrinebase") public static ItemEC shrineBase;
	@ObjectHolder(ElementalCraft.MODID + ":fireite_ingot") public static ItemEC fireiteIngot;

	/** BLOCKS */
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTankSmall.NAME) public static Item tankSmall;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockTank.NAME) public static Item tank;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockExtractor.NAME) public static Item extractor;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockImprovedExtractor.NAME) public static Item improvedExtractor;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockInfuser.NAME) public static Item infuser;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBinder.NAME) public static Item binder;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_FIRE) public static Item firePedestal;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_WATER) public static Item waterPedestal;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_EARTH) public static Item earthPedestal;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPedestal.NAME_AIR) public static Item airPedestal;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPureInfuser.NAME) public static Item pureInfuser;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFireFurnace.NAME) public static Item fireFurnace;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFireBlastFurnace.NAME) public static Item fireBlastFurnace;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockPurifier.NAME) public static Item purifier;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockElementPipe.NAME) public static Item elementPipe;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockFirePylon.NAME) public static Item firePylon;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockVacuumShrine.NAME) public static Item vacuumShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockGrowthShrine.NAME) public static Item growthShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockHarvestShrine.NAME) public static Item harvestShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockLavaShrine.NAME) public static Item lavaShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOreShrine.NAME) public static Item oreShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockOverloadShrine.NAME) public static Item overloadShrine;
	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSweetShrine.NAME) public static Item sweetShrine;

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockSource.NAME) public static Item source;
	@ObjectHolder(ElementalCraft.MODID + ":crystalore") public static Item crystalOre;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock") public static Item whiteRock;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_slab") public static Item whiteRockSlab;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_stairs") public static Item whiteRockStairs;
	@ObjectHolder(ElementalCraft.MODID + ":whiterock_wall") public static Item whiteRockWall;
	@ObjectHolder(ElementalCraft.MODID + ":purerock") public static Item pureRock;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_slab") public static Item pureRockSlab;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_stairs") public static Item pureRockStairs;
	@ObjectHolder(ElementalCraft.MODID + ":purerock_wall") public static Item pureRockWall;
	@ObjectHolder(ElementalCraft.MODID + ":burnt_glass") public static Item burntGlass;

	private ECItems() {

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();

		RegistryHelper.register(registry, new ItemFocus(), ItemFocus.NAME);
		RegistryHelper.register(registry, new ItemScroll(), ItemScroll.NAME);
		RegistryHelper.register(registry, new ItemReceptacle(), ItemReceptacle.NAME);
		RegistryHelper.register(registry, new ItemEmptyReceptacle(), ItemEmptyReceptacle.NAME);
		RegistryHelper.register(registry, new ItemPureOre(), ItemPureOre.NAME);

		RegistryHelper.register(registry, new ItemEC(), "inertcrystal");
		RegistryHelper.register(registry, new ItemEC(), "containedcrystal");
		RegistryHelper.register(registry, new ItemEC(), "firecrystal");
		RegistryHelper.register(registry, new ItemEC(), "watercrystal");
		RegistryHelper.register(registry, new ItemEC(), "earthcrystal");
		RegistryHelper.register(registry, new ItemEC(), "aircrystal");
		RegistryHelper.register(registry, new ItemEC().setEffect(true), "purecrystal");
		RegistryHelper.register(registry, new ItemEC(), "drenched_iron_ingot");
		RegistryHelper.register(registry, new ItemEC(), "drenched_iron_nugget");
		RegistryHelper.register(registry, new ItemEC(), "swift_alloy_ingot");
		RegistryHelper.register(registry, new ItemEC(), "swift_alloy_nugget");
		RegistryHelper.register(registry, new ItemEC(), "shrinebase");
		RegistryHelper.register(registry, new ItemEC(new Item.Properties().group(ElementalCraftTab.tabElementalCraft).isBurnable()), "fireite_ingot");

		// TODO add tools
	}

	@SubscribeEvent
	public static void registerItemColors(ColorHandlerEvent.Item event) {
		event.getItemColors().register((s, l) -> l == 0 ? -1 : ReceptacleHelper.getElementType(s).getColor(), receptacle);
		event.getItemColors().register((s, l) -> l == 0 ? -1 : PureOreHelper.getColor(s), pureOre);
	}

}
