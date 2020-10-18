package sirttas.elementalcraft.data.recipe;

import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.data.SingleItemRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.data.recipe.builder.BinderRecipeBuilder;
import sirttas.elementalcraft.data.recipe.builder.InfusionRecipeBuilder;
import sirttas.elementalcraft.data.recipe.builder.PureInfusionRecipeBuilder;
import sirttas.elementalcraft.item.ECItems;

public class ECRecipeProvider extends RecipeProvider {

	private static final String HAS_INERTCRYSTAL = "has_inertcrystal";
	private static final String HAS_CONTAINEDCRYSTAL = "has_containedcrystal";
	private static final String HAS_PURECRYSTAL = "has_purecrystal";
	private static final String HAS_WHITEROCK = "has_whiterock";

	private static final String HAS_DRENCHED_IRON_NUGGET = "has_drenched_iron_nugget";
	private static final String HAS_DRENCHED_IRON_INGOT = "has_drenched_iron_ingot";
	private static final String HAS_SWIFT_IRON_NUGGET = "has_swift_alloy_nugget";
	private static final String HAS_SWIFT_IRON_INGOT = "has_swift_alloy_ingot";

	private ExistingFileHelper existingFileHelper;

	public ECRecipeProvider(DataGenerator generatorIn, ExistingFileHelper exFileHelper) {
		super(generatorIn);
		existingFileHelper = exFileHelper;
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		registerSlabsStairsWalls(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECItems.containedCrystal).key('g', Tags.Items.NUGGETS_GOLD).key('c', ECItems.inertCrystal).patternLine(" g ").patternLine("gcg").patternLine(" g ")
				.addCriterion(HAS_INERTCRYSTAL, hasItem(ECItems.inertCrystal)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.shrineBase).key('w', ECBlocks.whiteRock).key('c', ECItems.inertCrystal).key('p', ECBlocks.elementPipe).patternLine(" p ").patternLine("pcp")
				.patternLine("www").addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.emptyReceptacle).key('c', ECItems.pureCrystal).key('d', Tags.Items.GEMS_DIAMOND).key('g', Tags.Items.INGOTS_GOLD).key('i', ECItems.swiftAlloyIngot)
				.patternLine("gig").patternLine("dcd").patternLine("gig").addCriterion(HAS_PURECRYSTAL, hasItem(ECItems.pureCrystal)).build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECItems.fireElementHolder).key('g', Tags.Items.INGOTS_GOLD).key('e', ECBlocks.extractor).key('t', ECBlocks.tankSmall).key('i', ECItems.drenchedIronIngot)
				.key('c', ECItems.fireCrystal).patternLine("geg").patternLine("iti").patternLine("gcg").addCriterion("has_firecrystal", hasItem(ECItems.fireCrystal)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.waterElementHolder).key('g', Tags.Items.INGOTS_GOLD).key('e', ECBlocks.extractor).key('t', ECBlocks.tankSmall).key('i', ECItems.drenchedIronIngot)
				.key('c', ECItems.waterCrystal).patternLine("geg").patternLine("iti").patternLine("gcg").addCriterion("has_watercrystal", hasItem(ECItems.waterCrystal)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.earthElementHolder).key('g', Tags.Items.INGOTS_GOLD).key('e', ECBlocks.extractor).key('t', ECBlocks.tankSmall).key('i', ECItems.drenchedIronIngot)
				.key('c', ECItems.earthCrystal).patternLine("geg").patternLine("iti").patternLine("gcg").addCriterion("has_earthcrystal", hasItem(ECItems.earthCrystal)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.airElementHolder).key('g', Tags.Items.INGOTS_GOLD).key('e', ECBlocks.extractor).key('t', ECBlocks.tankSmall).key('i', ECItems.drenchedIronIngot)
				.key('c', ECItems.airCrystal).patternLine("geg").patternLine("iti").patternLine("gcg").addCriterion("has_aircrystal", hasItem(ECItems.airCrystal)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.focus).key('d', Tags.Items.GEMS_DIAMOND).key('c', ECItems.containedCrystal).key('s', ECItems.hardenedHandle).key('i', ECItems.swiftAlloyIngot)
				.patternLine(" ic").patternLine(" si").patternLine("d  ").addCriterion(HAS_CONTAINEDCRYSTAL, hasItem(ECItems.containedCrystal)).build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECBlocks.burntGlassPane, 16).key('#', ECBlocks.burntGlass).patternLine("###").patternLine("###").addCriterion("has_burnt_glass", hasItem(ECBlocks.burntGlass))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.drenchedIronIngot).key('#', ECItems.drenchedIronNugget).patternLine("###").patternLine("###").patternLine("###")
				.addCriterion(HAS_DRENCHED_IRON_NUGGET, hasItem(ECItems.drenchedIronNugget)).build(consumer, ElementalCraft.createRL("drenched_iron_ingot_from_nuggets"));
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.drenchedIronNugget, 9).addIngredient(ECItems.drenchedIronIngot).addCriterion(HAS_DRENCHED_IRON_INGOT, hasItem(ECItems.drenchedIronIngot))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.swiftAlloyIngot).key('#', ECItems.swiftAlloyNugget).patternLine("###").patternLine("###").patternLine("###")
				.addCriterion(HAS_SWIFT_IRON_NUGGET, hasItem(ECItems.swiftAlloyNugget)).build(consumer, ElementalCraft.createRL("swift_alloy_ingot_from_nuggets"));
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.swiftAlloyNugget, 9).addIngredient(ECItems.swiftAlloyIngot).addCriterion(HAS_SWIFT_IRON_INGOT, hasItem(ECItems.swiftAlloyIngot)).build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECBlocks.tankSmall).key('g', Tags.Items.GLASS).key('p', ECBlocks.impairedElementPipe).patternLine(" p ").patternLine("pgp").patternLine(" p ")
				.addCriterion(HAS_CONTAINEDCRYSTAL, hasItem(ECItems.containedCrystal)).build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.tank).key('i', ECItems.drenchedIronIngot).key('g', ECBlocks.burntGlass).key('p', ECBlocks.elementPipe).patternLine("ici").patternLine("pgp")
				.patternLine("www").build(consumer);
		prepareInstrumentRecipe(ECBlocks.extractor).key('i', Tags.Items.INGOTS_IRON).patternLine(" c ").patternLine(" i ").patternLine("ici").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.improvedExtractor, ECItems.pureCrystal).key('i', ECItems.swiftAlloyIngot).key('e', ECBlocks.extractor).patternLine(" e ").patternLine("eie")
				.patternLine("wcw").build(consumer);
		prepareInstrumentRecipe(ECBlocks.infuser).key('i', Tags.Items.INGOTS_IRON).key('n', Tags.Items.NUGGETS_IRON).patternLine("n n").patternLine("ici").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.binder).key('i', ECItems.drenchedIronIngot).patternLine("i i").patternLine("wcw").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.pureInfuser).key('i', ECItems.swiftAlloyIngot).key('n', ECBlocks.infuser).patternLine("wnw").patternLine("ici").patternLine("www").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.fireFurnace, ECItems.fireCrystal).key('i', ECItems.drenchedIronIngot).key('f', Blocks.FURNACE).patternLine("www").patternLine("wfw")
				.patternLine("ici").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.fireBlastFurnace, ECItems.fireCrystal).key('i', ECItems.swiftAlloyIngot).key('F', Blocks.BLAST_FURNACE).key('g', ECBlocks.burntGlass)
				.patternLine("www").patternLine("gFg").patternLine("ici").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.purifier, ECItems.pureCrystal).key('i', ECItems.swiftAlloyIngot).key('e', ECItems.earthCrystal).key('g', Tags.Items.INGOTS_GOLD).patternLine("gcg")
				.patternLine("wew").patternLine("ici").build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECBlocks.instrumentRetriever).key('i', ECItems.swiftAlloyIngot).key('h', Blocks.HOPPER).key('d', Blocks.DISPENSER).key('w', ECBlocks.whiteRock)
				.addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock)).patternLine("iw ").patternLine("hdi").patternLine("iw ").build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECBlocks.spellDesk).key('i', ECItems.drenchedIronIngot).key('l', Blocks.LECTERN).key('w', ECBlocks.whiteRock)
				.addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock)).patternLine("wlw").patternLine(" i ").patternLine(" w ").build(consumer);

		prepareInstrumentRecipe(ECBlocks.impairedElementPipe, ECItems.containedCrystal, 4).key('i', Tags.Items.INGOTS_IRON).patternLine("ici").build(consumer);
		prepareInstrumentRecipe(ECBlocks.elementPipe, ECItems.containedCrystal, 4).key('i', ECItems.drenchedIronIngot).patternLine("ici").build(consumer);
		prepareInstrumentRecipe(ECBlocks.improvedElementPipe, ECItems.containedCrystal, 4).key('i', ECItems.swiftAlloyIngot).patternLine("ici").build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.elementPipe).addIngredient(ECBlocks.impairedElementPipe).addIngredient(ECItems.drenchedIronNugget, 5)
				.addCriterion(HAS_DRENCHED_IRON_NUGGET, hasItem(ECItems.drenchedIronNugget)).build(consumer, ElementalCraft.createRL("elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.elementPipe, 4).addIngredient(ECBlocks.impairedElementPipe, 4).addIngredient(ECItems.drenchedIronIngot, 2)
				.addCriterion(HAS_DRENCHED_IRON_INGOT, hasItem(ECItems.drenchedIronIngot)).build(consumer, ElementalCraft.createRL("elementpipe_from_impaired_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.improvedElementPipe).addIngredient(ECBlocks.elementPipe).addIngredient(ECItems.swiftAlloyNugget, 5)
				.addCriterion(HAS_SWIFT_IRON_NUGGET, hasItem(ECItems.swiftAlloyNugget)).build(consumer, ElementalCraft.createRL("improved_elementpipe_from_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.improvedElementPipe, 4).addIngredient(ECBlocks.elementPipe, 4).addIngredient(ECItems.swiftAlloyIngot, 2)
				.addCriterion(HAS_SWIFT_IRON_INGOT, hasItem(ECItems.swiftAlloyIngot)).build(consumer, ElementalCraft.createRL("improved_elementpipe_from_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.improvedElementPipe).addIngredient(ECBlocks.impairedElementPipe).addIngredient(ECItems.swiftAlloyNugget, 5)
				.addCriterion(HAS_SWIFT_IRON_NUGGET, hasItem(ECItems.swiftAlloyNugget)).build(consumer, ElementalCraft.createRL("improved_elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapelessRecipe(ECBlocks.improvedElementPipe, 4).addIngredient(ECBlocks.impairedElementPipe, 4).addIngredient(ECItems.swiftAlloyIngot, 2)
				.addCriterion(HAS_SWIFT_IRON_INGOT, hasItem(ECItems.swiftAlloyIngot)).build(consumer, ElementalCraft.createRL("improved_elementpipe_from_impaired_elementpipe_and_ingot"));

		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.inertCrystal), ECItems.fireCrystal, ElementType.FIRE).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.inertCrystal), ECItems.waterCrystal, ElementType.WATER).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.inertCrystal), ECItems.airCrystal, ElementType.AIR).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.inertCrystal), ECItems.earthCrystal, ElementType.EARTH).build(consumer);

		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(Items.STONE), ECItems.whiteRock, ElementType.EARTH).withDuration(40).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromTag(Tags.Items.INGOTS_IRON), ECItems.drenchedIronIngot, ElementType.WATER).withDuration(40).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromTag(Tags.Items.GLASS), ECBlocks.burntGlass, ElementType.FIRE).withDuration(40).build(consumer);

		BinderRecipeBuilder.binderRecipe(ECItems.firePylon, ElementType.FIRE).addIngredient(ECItems.shrineBase).addIngredient(ECItems.fireCrystal).addIngredient(Items.LAVA_BUCKET)
				.addIngredient(Tags.Items.INGOTS_GOLD).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.vacuumShrine, ElementType.AIR).addIngredient(ECItems.shrineBase).addIngredient(ECItems.airCrystal).addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.HOPPER).addIngredient(Tags.Items.GEMS_DIAMOND).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.growthShrine, ElementType.WATER).addIngredient(ECItems.shrineBase).addIngredient(ECItems.waterCrystal).addIngredient(ECItems.earthCrystal)
				.addIngredient(Items.WHEAT_SEEDS).addIngredient(Items.BONE_MEAL).addIngredient(Tags.Items.GEMS_DIAMOND).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.harvestShrine, ElementType.EARTH).addIngredient(ECItems.growthShrine).addIngredient(ECItems.earthCrystal).addIngredient(Items.DIAMOND_HOE)
				.addIngredient(Items.DIAMOND_AXE).addIngredient(Items.SHEARS).withConsumption(50).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.lavaShrine, ElementType.FIRE).addIngredient(ECItems.firePylon).addIngredient(ECItems.fireCrystal).addIngredient(ECItems.pureCrystal)
				.addIngredient(Blocks.OBSIDIAN).addIngredient(Items.LAVA_BUCKET).addIngredient(Items.BLAZE_ROD).withConsumption(100).withDuration(200).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.oreShrine, ElementType.EARTH).addIngredient(ECItems.shrineBase).addIngredient(ECItems.earthCrystal).addIngredient(ECItems.pureCrystal)
				.addIngredient(Items.DIAMOND_PICKAXE).withConsumption(100).withDuration(200).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.overloadShrine, ElementType.AIR).addIngredient(ECItems.shrineBase).addIngredient(ECItems.airCrystal).addIngredient(ECItems.pureCrystal)
				.addIngredient(Items.CLOCK).addIngredient(Items.ENDER_EYE).withConsumption(100).withDuration(200).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.sweetShrine, ElementType.WATER).addIngredient(ECItems.shrineBase).addIngredient(ECItems.waterCrystal).addIngredient(ECItems.earthCrystal)
				.addIngredient(Items.SUGAR).addIngredient(Items.HONEY_BOTTLE).addIngredient(Items.MILK_BUCKET).withConsumption(50).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.enderLockShrine, ElementType.WATER).addIngredient(ECItems.shrineBase).addIngredient(ECItems.waterCrystal).addIngredient(ECItems.airCrystal)
				.addIngredient(Items.ENDER_EYE).addIngredient(Items.DRAGON_BREATH).addIngredient(Items.OBSIDIAN).withConsumption(50).build(consumer);


		BinderRecipeBuilder.binderRecipe(ECItems.firePedestal, ElementType.FIRE).addIngredient(ECItems.infuser).addIngredient(ECItems.fireCrystal).addIngredient(ECItems.whiteRock)
				.addIngredient(ECItems.whiteRock).withConsumption(50).withDuration(300).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.waterPedestal, ElementType.WATER).addIngredient(ECItems.infuser).addIngredient(ECItems.waterCrystal).addIngredient(ECItems.whiteRock)
				.addIngredient(ECItems.whiteRock).withConsumption(50).withDuration(300).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.earthPedestal, ElementType.EARTH).addIngredient(ECItems.infuser).addIngredient(ECItems.earthCrystal).addIngredient(ECItems.whiteRock)
				.addIngredient(ECItems.whiteRock).withConsumption(50).withDuration(300).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.airPedestal, ElementType.AIR).addIngredient(ECItems.infuser).addIngredient(ECItems.airCrystal).addIngredient(ECItems.whiteRock)
				.addIngredient(ECItems.whiteRock).withConsumption(50).withDuration(300).build(consumer);

		BinderRecipeBuilder.binderRecipe(ECItems.swiftAlloyIngot, ElementType.AIR).addIngredient(Tags.Items.INGOTS_GOLD).addIngredient(ECItems.drenchedIronIngot)
				.addIngredient(Tags.Items.DUSTS_REDSTONE).addIngredient(ECItems.airCrystal).withConsumption(10).withDuration(50).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.hardenedHandle, ElementType.EARTH).addIngredient(Items.STICK).addIngredient(ECBlocks.whiteRock).addIngredient(ECItems.earthCrystal).withConsumption(10)
				.withDuration(50).build(consumer);

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.pureCrystal).setIngredient(Tags.Items.GEMS_DIAMOND).setIngredient(ElementType.WATER, ECItems.waterCrystal)
				.setIngredient(ElementType.FIRE, ECItems.fireCrystal).setIngredient(ElementType.EARTH, ECItems.earthCrystal).setIngredient(ElementType.AIR, ECItems.airCrystal).build(consumer);
		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.pureRock).setIngredient(Items.OBSIDIAN).setIngredient(ElementType.WATER, Items.PRISMARINE)
				.setIngredient(ElementType.FIRE, Blocks.NETHER_BRICKS).setIngredient(ElementType.EARTH, ECItems.whiteRock).setIngredient(ElementType.AIR, Items.PURPUR_BLOCK).build(consumer);

		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ECBlocks.crystalOre), ECItems.inertCrystal, 0.5F, 200).addCriterion("has_crystal_ore", hasItem(ECBlocks.crystalOre)).build(consumer);
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ECBlocks.crystalOre), ECItems.inertCrystal, 0.5F, 100).addCriterion("has_crystal_ore", hasItem(ECBlocks.crystalOre)).build(consumer,
				ElementalCraft.createRL("inertcrystal_from_blasting"));

	}

	private boolean exists(Block block) {
		return existingFileHelper.exists(block.getRegistryName(), ResourcePackType.SERVER_DATA, ".json", "recipes");
	}

	private void registerSlabsStairsWalls(Consumer<IFinishedRecipe> consumer) {
		ForgeRegistries.BLOCKS.forEach(block -> {
			if (ElementalCraft.MODID.equals(block.getRegistryName().getNamespace()) && !exists(block) && (block instanceof SlabBlock || block instanceof StairsBlock || block instanceof WallBlock)) {
				String name = block.getRegistryName().getPath();
				String sourceName = name.substring(0, name.length() - (block instanceof StairsBlock ? 7 : 5));
				IItemProvider source = ForgeRegistries.ITEMS.getValue(ElementalCraft.createRL(sourceName));
				ShapedRecipeBuilder shaped = ShapedRecipeBuilder.shapedRecipe(block, block instanceof StairsBlock ? 4 : 6).key('#', source);

				if (block instanceof SlabBlock) {
					shaped.patternLine("###");
				} else if (block instanceof StairsBlock) {
					shaped.patternLine("#  ").patternLine("## ").patternLine("###");
				} else if (block instanceof WallBlock) {
					shaped.patternLine("###").patternLine("###");
				}
				shaped.addCriterion("has_" + sourceName, hasItem(source)).build(consumer);
				SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(source), block, block instanceof SlabBlock ? 2 : 1).addCriterion("has_" + sourceName, hasItem(source)).build(consumer,
						ElementalCraft.createRL(name + "_from_" + sourceName + "_stonecutting"));
			}
		});
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(IItemProvider result) {
		return prepareInstrumentRecipe(result, ECItems.containedCrystal, 1);
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(IItemProvider result, IItemProvider crystal, int count) {
		return ShapedRecipeBuilder.shapedRecipe(result, count).key('c', crystal).addCriterion("has_" + crystal.asItem().getRegistryName().getPath(), hasItem(crystal));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(IItemProvider result) {
		return prepareInstrumentRecipe(result, ECItems.containedCrystal, 1).key('w', ECBlocks.whiteRock).addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(IItemProvider result, IItemProvider crystal) {
		return prepareInstrumentRecipe(result, crystal, 1).key('w', ECBlocks.whiteRock).addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock));
	}
}
