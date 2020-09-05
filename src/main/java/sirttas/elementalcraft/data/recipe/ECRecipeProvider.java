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
import net.minecraft.util.ResourceLocation;
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

	private ExistingFileHelper existingFileHelper;

	public ECRecipeProvider(DataGenerator generatorIn, ExistingFileHelper exFileHelper) {
		super(generatorIn);
		existingFileHelper = exFileHelper;
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		registerSlabsStairsWalls(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECItems.containedCrystal).key('g', Items.GOLD_NUGGET).key('c', ECItems.inertCrystal).patternLine("ggg").patternLine("gcg").patternLine("ggg")
				.addCriterion(HAS_INERTCRYSTAL, hasItem(ECItems.inertCrystal)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.shrineBase).key('w', ECBlocks.whiteRock).key('c', ECItems.inertCrystal).key('p', ECBlocks.elementPipe).patternLine(" p ").patternLine("pcp")
				.patternLine("www").addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock)).build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.emptyReceptacle).key('c', ECItems.pureCrystal).key('d', Items.DIAMOND).key('g', Tags.Items.INGOTS_GOLD).patternLine("ggg").patternLine("dcd")
				.patternLine("ggg").addCriterion(HAS_PURECRYSTAL, hasItem(ECItems.pureCrystal)).build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECItems.drenchedIronIngot).key('#', ECItems.drenchedIronNugget).patternLine("###").patternLine("###").patternLine("###")
				.addCriterion("has_drenched_iron_nugget", hasItem(ECItems.drenchedIronNugget)).build(consumer, "drenched_iron_ingot_from_nuggets");
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.drenchedIronNugget, 9).addIngredient(ECItems.drenchedIronIngot).addCriterion("has_drenched_iron_ingot", hasItem(ECItems.drenchedIronIngot))
				.build(consumer);
		ShapedRecipeBuilder.shapedRecipe(ECItems.swiftAlloyIngot).key('#', ECItems.swiftAlloyNugget).patternLine("###").patternLine("###").patternLine("###")
				.addCriterion("has_swift_alloy_nugget", hasItem(ECItems.swiftAlloyNugget)).build(consumer, "swift_alloy_ingot_from_nuggets");
		ShapelessRecipeBuilder.shapelessRecipe(ECItems.swiftAlloyNugget, 9).addIngredient(ECItems.swiftAlloyIngot).addCriterion("has_swift_alloy_ingot", hasItem(ECItems.swiftAlloyIngot))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ECBlocks.tankSmall).key('g', Items.GLASS).key('p', ECBlocks.elementPipe).patternLine(" p ").patternLine("pgp").patternLine(" p ")
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
		prepareWhiterockInstrumentRecipe(ECBlocks.fireBlastFurnace, ECItems.pureCrystal).key('i', ECItems.swiftAlloyIngot).key('F', Blocks.BLAST_FURNACE).key('g', ECBlocks.burntGlass)
				.key('f', ECItems.fireCrystal).patternLine("wcw").patternLine("gFg").patternLine("ifi").build(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.purifier, ECItems.pureCrystal).key('i', ECItems.swiftAlloyIngot).key('e', ECItems.earthCrystal).key('g', Tags.Items.INGOTS_GOLD).patternLine("gcg")
				.patternLine("wew").patternLine("ici").build(consumer);
		prepareInstrumentRecipe(ECBlocks.elementPipe, ECItems.containedCrystal, 4).key('i', Tags.Items.INGOTS_IRON).patternLine("ici").build(consumer);

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
				.addIngredient(Items.HOPPER).addIngredient(Items.DIAMOND).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.growthShrine, ElementType.WATER).addIngredient(ECItems.shrineBase).addIngredient(ECItems.waterCrystal).addIngredient(ECItems.earthCrystal)
				.addIngredient(Items.WHEAT_SEEDS).addIngredient(Items.BONE_MEAL).addIngredient(Items.DIAMOND).build(consumer);
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

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.pureCrystal).setIngredient(Items.DIAMOND).setIngredient(ElementType.WATER, ECItems.waterCrystal)
				.setIngredient(ElementType.FIRE, ECItems.fireCrystal).setIngredient(ElementType.EARTH, ECItems.earthCrystal).setIngredient(ElementType.AIR, ECItems.airCrystal).build(consumer);
		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.pureRock).setIngredient(Items.OBSIDIAN).setIngredient(ElementType.WATER, Items.PRISMARINE)
				.setIngredient(ElementType.FIRE, Blocks.NETHER_BRICKS).setIngredient(ElementType.EARTH, ECItems.whiteRock).setIngredient(ElementType.AIR, Items.PURPUR_BLOCK).build(consumer);

		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ECBlocks.crystalOre), ECItems.inertCrystal, 0.5F, 200).addCriterion("has_crystal_ore", hasItem(ECBlocks.crystalOre)).build(consumer);
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ECBlocks.crystalOre), ECItems.inertCrystal, 0.5F, 100).addCriterion("has_crystal_ore", hasItem(ECBlocks.crystalOre)).build(consumer,
				new ResourceLocation(ElementalCraft.MODID, "inertcrystal_from_blasting"));

	}

	private boolean exists(Block block) {
		return existingFileHelper.exists(block.getRegistryName(), ResourcePackType.SERVER_DATA, ".json", "recipes");
	}

	private void registerSlabsStairsWalls(Consumer<IFinishedRecipe> consumer) {
		ForgeRegistries.BLOCKS.forEach(block -> {
			if (ElementalCraft.MODID.equals(block.getRegistryName().getNamespace()) && !exists(block) && (block instanceof SlabBlock || block instanceof StairsBlock || block instanceof WallBlock)) {
				String name = block.getRegistryName().getPath();
				String sourceName = name.substring(0, name.length() - (block instanceof StairsBlock ? 7 : 5));
				IItemProvider source = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ElementalCraft.MODID, sourceName));
				ShapedRecipeBuilder shaped = ShapedRecipeBuilder.shapedRecipe(block).key('#', source);

				if (block instanceof SlabBlock) {
					shaped.patternLine("###");
				} else if (block instanceof StairsBlock) {
					shaped.patternLine("#  ").patternLine("## ").patternLine("###");
				} else if (block instanceof WallBlock) {
					shaped.patternLine("###").patternLine("###");
				}
				shaped.addCriterion("has_" + sourceName, hasItem(source)).build(consumer);
				SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(source), block, block instanceof SlabBlock ? 2 : 1).addCriterion("has_" + sourceName, hasItem(source)).build(consumer,
						new ResourceLocation(ElementalCraft.MODID, name + "_from_" + sourceName + "_stonecutting"));
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
