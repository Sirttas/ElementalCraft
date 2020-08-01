package sirttas.elementalcraft.data.recipe;

import java.util.function.Consumer;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.SingleItemRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
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
		ShapedRecipeBuilder.shapedRecipe(ECItems.emptyReceptacle).key('c', ECItems.pureCrystal).key('d', Items.DIAMOND).key('g', Items.GOLD_INGOT).patternLine("ggg").patternLine("dcd")
				.patternLine("ggg").addCriterion(HAS_PURECRYSTAL, hasItem(ECItems.pureCrystal)).build(consumer);

		prepareInstrumentRecipe(ECBlocks.tank).key('g', Items.GLASS).key('s', Items.STONE).patternLine("ici").patternLine("igi").patternLine("sss").build(consumer);
		prepareInstrumentRecipe(ECBlocks.extractor).patternLine(" c ").patternLine(" i ").patternLine("ici").build(consumer);
		prepareWhitterockInstrumentRecipe(ECBlocks.improvedExtractor, ECItems.pureCrystal).key('e', ECBlocks.extractor).patternLine(" e ").patternLine("eie").patternLine("wcw").build(consumer);
		prepareInstrumentRecipe(ECBlocks.infuser).key('n', Items.IRON_NUGGET).patternLine("n n").patternLine("ici").build(consumer);
		prepareWhitterockInstrumentRecipe(ECBlocks.binder).patternLine("i i").patternLine("wcw").build(consumer);
		prepareWhitterockInstrumentRecipe(ECBlocks.pureInfuser).key('n', ECBlocks.infuser).patternLine("wnw").patternLine("ici").patternLine("www").build(consumer);
		prepareWhitterockInstrumentRecipe(ECBlocks.fireFurnace, ECItems.fireCrystal).key('f', Blocks.FURNACE).patternLine("www").patternLine("wfw").patternLine("ici").build(consumer);
		prepareInstrumentRecipe(ECBlocks.elementPipe, ECItems.containedCrystal, 4).patternLine("ici").build(consumer);
		
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.inertCrystal), ECItems.fireCrystal, ElementType.FIRE).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.inertCrystal), ECItems.waterCrystal, ElementType.WATER).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.inertCrystal), ECItems.airCrystal, ElementType.AIR).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(ECItems.inertCrystal), ECItems.earthCrystal, ElementType.EARTH).build(consumer);

		InfusionRecipeBuilder.infusionRecipe(Ingredient.fromItems(Items.STONE), ECItems.whiteRock, ElementType.EARTH).withDuration(40).build(consumer);
		
		BinderRecipeBuilder.binderRecipe(ECItems.firePylon, ElementType.FIRE).addIngredient(ECItems.shrineBase).addIngredient(ECItems.fireCrystal).addIngredient(Items.LAVA_BUCKET)
				.addIngredient(Items.GOLD_INGOT).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.vacuumShrine, ElementType.AIR).addIngredient(ECItems.shrineBase).addIngredient(ECItems.airCrystal).addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.HOPPER).addIngredient(Items.DIAMOND).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.growthShrine, ElementType.WATER).addIngredient(ECItems.shrineBase).addIngredient(ECItems.waterCrystal).addIngredient(ECItems.earthCrystal)
				.addIngredient(Items.WHEAT_SEEDS).addIngredient(Items.BONE_MEAL).addIngredient(Items.DIAMOND).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.lavaShrine, ElementType.FIRE).addIngredient(ECItems.firePylon).addIngredient(ECItems.fireCrystal).addIngredient(ECItems.fireiteIngot)
				.addIngredient(Blocks.OBSIDIAN).addIngredient(Items.LAVA_BUCKET).addIngredient(Items.BLAZE_ROD).withConsumption(100).withDuration(200).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.harvestShrine, ElementType.EARTH).addIngredient(ECItems.growthShrine).addIngredient(ECItems.earthCrystal).addIngredient(Items.DIAMOND_HOE)
				.addIngredient(Items.DIAMOND_AXE).addIngredient(Items.SHEARS).withConsumption(50).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.oreShrine, ElementType.EARTH).addIngredient(ECItems.shrineBase).addIngredient(ECItems.earthCrystal).addIngredient(ECItems.pureCrystal)
				.addIngredient(Items.DIAMOND_PICKAXE).withConsumption(100).withDuration(200).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.overloadShrine, ElementType.AIR).addIngredient(ECItems.shrineBase).addIngredient(ECItems.airCrystal).addIngredient(ECItems.pureCrystal)
				.addIngredient(Items.CLOCK).addIngredient(Items.ENDER_EYE).withConsumption(100).withDuration(200).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.sweetShrine, ElementType.WATER).addIngredient(ECItems.shrineBase).addIngredient(ECItems.waterCrystal)
				.addIngredient(ECItems.earthCrystal)
				.addIngredient(Items.SUGAR).addIngredient(Items.HONEY_BOTTLE).addIngredient(Items.MILK_BUCKET).withConsumption(50).build(consumer);
		
		BinderRecipeBuilder.binderRecipe(ECItems.firePedestal, ElementType.FIRE).addIngredient(ECItems.infuser).addIngredient(ECItems.fireCrystal).addIngredient(ECItems.whiteRock)
				.addIngredient(ECItems.whiteRock).withConsumption(50).withDuration(300).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.waterPedestal, ElementType.WATER).addIngredient(ECItems.infuser).addIngredient(ECItems.waterCrystal).addIngredient(ECItems.whiteRock)
				.addIngredient(ECItems.whiteRock).withConsumption(50).withDuration(300).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.earthPedestal, ElementType.EARTH).addIngredient(ECItems.infuser).addIngredient(ECItems.earthCrystal).addIngredient(ECItems.whiteRock)
				.addIngredient(ECItems.whiteRock).withConsumption(50).withDuration(300).build(consumer);
		BinderRecipeBuilder.binderRecipe(ECItems.airPedestal, ElementType.AIR).addIngredient(ECItems.infuser).addIngredient(ECItems.airCrystal).addIngredient(ECItems.whiteRock)
				.addIngredient(ECItems.whiteRock).withConsumption(50).withDuration(300).build(consumer);
		
		BinderRecipeBuilder.binderRecipe(ECItems.fireiteIngot, ElementType.FIRE).addIngredient(ECItems.pureCrystal).addIngredient(Items.field_234759_km_ /* NETHERITE_INGOT */).withConsumption(200)
				.build(consumer);

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.pureCrystal).setIngredient(Items.DIAMOND).setIngredient(ElementType.WATER, ECItems.waterCrystal)
				.setIngredient(ElementType.FIRE, ECItems.fireCrystal).setIngredient(ElementType.EARTH, ECItems.earthCrystal).setIngredient(ElementType.AIR, ECItems.airCrystal).build(consumer);
		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.pureRock).setIngredient(Items.OBSIDIAN).setIngredient(ElementType.WATER, Items.PRISMARINE)
				.setIngredient(ElementType.FIRE, ECItems.fireiteIngot).setIngredient(ElementType.EARTH, ECItems.whiteRock).setIngredient(ElementType.AIR, Items.PURPUR_BLOCK)
				.build(consumer);
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
				SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(source), block, block instanceof SlabBlock ? 2 : 1).addCriterion("has_" + sourceName, hasItem(
						source))
						.build(consumer, new ResourceLocation(ElementalCraft.MODID, name + "_from_" + sourceName + "_stonecutting"));
			}
		});
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(IItemProvider result) {
		return prepareInstrumentRecipe(result, ECItems.containedCrystal, 1);
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(IItemProvider result, IItemProvider crystal, int count) {
		return ShapedRecipeBuilder.shapedRecipe(result, count).key('i', Items.IRON_INGOT).key('c', crystal).addCriterion("has_" + crystal.asItem().getRegistryName().getPath(), hasItem(crystal));
	}

	private ShapedRecipeBuilder prepareWhitterockInstrumentRecipe(IItemProvider result) {
		return prepareInstrumentRecipe(result, ECItems.containedCrystal, 1).key('w', ECBlocks.whiteRock).addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock));
	}

	private ShapedRecipeBuilder prepareWhitterockInstrumentRecipe(IItemProvider result, IItemProvider crystal) {
		return prepareInstrumentRecipe(result, crystal, 1).key('w', ECBlocks.whiteRock).addCriterion(HAS_WHITEROCK, hasItem(ECBlocks.whiteRock));
	}
}
