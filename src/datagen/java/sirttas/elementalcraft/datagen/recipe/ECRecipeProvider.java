package sirttas.elementalcraft.datagen.recipe;

import com.google.gson.JsonObject;
import mekanism.api.MekanismAPI;
import mekanism.api.datagen.recipe.builder.ItemStackToItemStackRecipeBuilder;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.StringUtils;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.synthesizer.mana.ManaSynthesizerBlock;
import sirttas.elementalcraft.datagen.recipe.builder.PureInfusionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.SpellCraftRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.BindingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.CrystallizationRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.GrindingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.InscriptionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.SawingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion.InfusionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion.ToolInfusionRecipeBuilder;
import sirttas.elementalcraft.infusion.tool.effect.AutoSmeltToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.DodgeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.FastDrawToolInfusionEffect;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.source.receptacle.NaturalSourceIngredient;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.recipe.ECRecipeSerializers;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.Spells;
import sirttas.elementalcraft.tag.ECTags;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class ECRecipeProvider extends RecipeProvider {

	private static final String HAS_INERTCRYSTAL = "has_inertcrystal";
	private static final String HAS_CONTAINED_CRYSTAL = "has_contained_crystal";
	private static final String HAS_PURECRYSTAL = "has_purecrystal";
	private static final String HAS_WHITEROCK = "has_whiterock";
	private static final String HAS_SHRINE_UPGRADE_CORE = "has_shrine_upgrade_core";
	private static final String HAS_SPRINGALINE_SHARD = "has_springaline_shard";

	private static final String HAS_DRENCHED_IRON_NUGGET = "has_drenched_iron_nugget";
	private static final String HAS_DRENCHED_IRON_INGOT = "has_drenched_iron_ingot";
	private static final String HAS_SWIFT_ALLOY_NUGGET = "has_swift_alloy_nugget";
	private static final String HAS_SWIFT_ALLOY_INGOT = "has_swift_alloy_ingot";
	private static final String HAS_FIREITE_INGOT = "has_fireite_ingot";
	public static final String FROM = "_from_";

	private final ExistingFileHelper existingFileHelper;

	public ECRecipeProvider(DataGenerator generatorIn, ExistingFileHelper exFileHelper) {
		super(generatorIn);
		existingFileHelper = exFileHelper;
	}

	@Override
	protected void buildCraftingRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
		registerSlabsStairsWalls(consumer);
		registerInertCrystal(consumer);
		registerNuggetIngotBlocks(consumer);
		registerMaterials(consumer);
		registerPipes(consumer);
		registerContainers(consumer);
		registerInstruments(consumer);
		registerPureInfuser(consumer);
		registerInfusions(consumer);
		registerSpringaline(consumer);
		registerHolders(consumer);
		registerTools(consumer);
		registerShards(consumer);
		registerLenses(consumer);
		registerShrines(consumer);
		registerShrineUpgrades(consumer);
		registerSourceDisplacementPlates(consumer);
		registerJewels(consumer);
		registerSpells(consumer);
		registerToolInfusions(consumer);
		registerGrinding(consumer);
		registerSawing(consumer);
		registerRunes(consumer);
		registerEmptying(consumer);
		registerCrystallizations(consumer);
		registerDecorative(consumer);
		registerSourceBreeding(consumer);
	}

	private static void registerMaterials(@Nonnull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ECItems.CONTAINED_CRYSTAL.get())
				.define('g', Tags.Items.NUGGETS_GOLD)
				.define('c', ECItems.INERT_CRYSTAL.get())
				.pattern(" g ")
				.pattern("gcg")
				.pattern(" g ")
				.unlockedBy(HAS_INERTCRYSTAL, has(ECItems.INERT_CRYSTAL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('g', ECTags.Items.NUGGETS_SWIFT_ALLOY)
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.pattern("sgs")
				.pattern("gcg")
				.pattern("sgs")
				.unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.SHRINE_BASE.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.INERT_CRYSTAL.get())
				.define('p', ECBlocks.PIPE.get())
				.pattern(" p ")
				.pattern("pcp")
				.pattern("www")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.SHRINE_UPGRADE_CORE.get())
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('r', Tags.Items.DUSTS_REDSTONE)
				.pattern("rir")
				.pattern("ici")
				.pattern("rir")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.SOLAR_PRISM.get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('c', Tags.Items.INGOTS_COPPER)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.pattern(" s ").pattern("cdc")
				.pattern(" s ")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.DRENCHED_SAW_BLADE.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('n', ECTags.Items.NUGGETS_DRENCHED_IRON)
				.define('a', ECItems.AIR_SILK.get())
				.pattern("nin")
				.pattern("iai")
				.pattern("nin")
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.SWIFT_ALLOY_INGOT.get(), ElementType.AIR)
				.addIngredient(Tags.Items.INGOTS_GOLD)
				.addIngredient(ECTags.Items.INGOTS_DRENCHED_IRON)
				.addIngredient(Tags.Items.INGOTS_COPPER)
				.addIngredient(Tags.Items.DUSTS_REDSTONE)
				.addIngredient(ECItems.AIR_CRYSTAL.get())
				.withElementAmount(1250)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.FIREITE_INGOT.get(), ElementType.FIRE)
				.addIngredient(Tags.Items.INGOTS_NETHERITE)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.withElementAmount(30000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.HARDENED_HANDLE.get(), ElementType.EARTH)
				.addIngredient(Tags.Items.RODS_WOODEN)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECItems.AIR_SILK.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.withElementAmount(1250)
				.save(consumer);

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.PURE_CRYSTAL.get())
				.setIngredient(Tags.Items.GEMS_DIAMOND)
				.setIngredient(ElementType.WATER, ECItems.WATER_CRYSTAL.get())
				.setIngredient(ElementType.FIRE, ECItems.FIRE_CRYSTAL.get())
				.setIngredient(ElementType.EARTH, ECItems.EARTH_CRYSTAL.get())
				.setIngredient(ElementType.AIR, ECItems.AIR_CRYSTAL.get())
				.save(consumer);
	}

	private static void registerLenses(@Nonnull Consumer<FinishedRecipe> consumer) {
		BindingRecipeBuilder.bindingRecipe(ECItems.FIRE_LENS.get(), ElementType.FIRE)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE.get())
				.addIngredient(Tags.Items.INGOTS_COPPER)
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.WATER_LENS.get(), ElementType.WATER)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE.get())
				.addIngredient(Tags.Items.INGOTS_COPPER)
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.EARTH_LENS.get(), ElementType.EARTH)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE.get())
				.addIngredient(Tags.Items.INGOTS_COPPER)
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.AIR_LENS.get(), ElementType.AIR)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE.get())
				.addIngredient(Tags.Items.INGOTS_COPPER)
				.addIngredient(ECItems.AIR_CRYSTAL.get())
				.save(consumer);
	}

	private static void registerSpringaline(@Nonnull Consumer<FinishedRecipe> consumer) {
		BindingRecipeBuilder.bindingRecipe(ECItems.SPRINGALINE_SHARD.get(), ElementType.WATER)
				.addIngredient(Items.AMETHYST_SHARD)
				.addIngredient(Tags.Items.GEMS_QUARTZ)
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPRINGALINE_CLUSTER.get(), ElementType.WATER)
				.addIngredient(Items.AMETHYST_BLOCK)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_QUARTZ /* FIXME use all quartz blocks */)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.save(consumer);
	}

	private static void registerTools(@Nonnull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ECItems.SOURCE_STABILIZER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('s', ECBlocks.SPRINGALINE_GLASS.get())
				.pattern("sis")
				.pattern("i i")
				.pattern("sis")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.SOURCE_ANALYSIS_GLASS.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('g', ECBlocks.SPRINGALINE_GLASS.get())
				.define('s', ECItems.AIR_SILK.get())
				.define('h', ECTags.Items.HARDENED_RODS)
				.pattern(" sg")
				.pattern(" is")
				.pattern("h  ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.CHISEL.get())
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern(" i ")
				.pattern(" di")
				.pattern("h  ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.TRANSLOCATION_ANCHOR.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('g', ECTags.Items.PRISTINE_AIR_GEMS)
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('e', Items.ENDER_EYE)
				.pattern(" e ")
				.pattern("fgf")
				.pattern("www")
				.unlockedBy("has_fireite_nugget", has(ECTags.Items.NUGGETS_FIREITE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.RETRIEVER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('h', Blocks.HOPPER)
				.define('d', Blocks.DISPENSER)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.pattern("iw ")
				.pattern("hdi")
				.pattern("iw "
				).save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.SORTER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('h', Blocks.HOPPER)
				.define('d', Blocks.DISPENSER)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.pattern("ii ")
				.pattern("hdi")
				.pattern("ii ")
				.save(consumer);

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECBlocks.PURE_ROCK.get())
				.setIngredient(Items.OBSIDIAN)
				.setIngredient(ElementType.WATER, Items.PRISMARINE)
				.setIngredient(ElementType.FIRE, ECTags.Items.INGOTS_FIREITE)
				.setIngredient(ElementType.EARTH, ECBlocks.WHITE_ROCK.get())
				.setIngredient(ElementType.AIR, Items.PURPUR_BLOCK)
				.save(consumer);
	}

	private void registerInstruments(@Nonnull Consumer<FinishedRecipe> consumer) {
		prepareInstrumentRecipe(ECBlocks.EXTRACTOR)
				.define('i', Tags.Items.INGOTS_IRON)
				.pattern(" c ").pattern(" i ")
				.pattern("ici")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.EXTRACTOR_IMPROVED.get(), ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('e', ECBlocks.EXTRACTOR.get())
				.pattern(" e ")
				.pattern("eie")
				.pattern("wcw")
				.save(consumer);
		prepareInstrumentRecipe(ECBlocks.EVAPORATOR)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('g', Tags.Items.GLASS)
				.pattern("igi")
				.pattern("igi")
				.pattern("ici")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.SOLAR_SYNTHESIZER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('p', ECItems.SOLAR_PRISM.get())
				.pattern("dhd")
				.pattern("ipi")
				.pattern("wcw")
				.save(consumer);
		ConditionalRecipe.builder()
				.addCondition(new ModLoadedCondition(BotaniaAPI.MODID))
				.addRecipe(prepareInstrumentRecipe(ECBlocks.MANA_SYNTHESIZER)
						.define('s', ECBlocks.SOLAR_SYNTHESIZER.get())
						.define('p', BotaniaBlocks.manaPool)
						.define('a', ECTags.Items.INGOTS_SWIFT_ALLOY)
						.define('l', BotaniaBlocks.livingrock)
						.define('m', BotaniaItems.manaDiamond)
						.pattern("msm")
						.pattern("apa")
						.pattern("lcl")::save)
				.build(consumer, ElementalCraft.createRL(ManaSynthesizerBlock.NAME));
		prepareWhiterockInstrumentRecipe(ECBlocks.DIFFUSER.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern(" c ")
				.pattern("did")
				.pattern("wcw")
				.save(consumer);
		prepareInstrumentRecipe(ECBlocks.INFUSER)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('n', Tags.Items.NUGGETS_IRON)
				.pattern("n n")
				.pattern("ici")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.BINDER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("i i")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.BINDER_IMPROVED.get(), ECItems.PURE_CRYSTAL.get())
				.define('s', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('b', ECBlocks.BINDER.get())
				.define('i', ECBlocks.INFUSER.get())
				.pattern("did")
				.pattern("sbs")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.CRYSTALLIZER.get(), ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern("iwi")
				.pattern("i i")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.AIR_MILL_GRINDSTONE.get(), ECItems.AIR_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('p', ItemTags.WOOL_CARPETS)
				.define('g', Items.GRINDSTONE)
				.pattern("pip")
				.pattern("igi")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.WATER_MILL_WOOD_SAW.get(), ECItems.WATER_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('s', ECItems.DRENCHED_SAW_BLADE.get())
				.pattern("www")
				.pattern("isi")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.INSCRIBER.get(), ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.pattern(" wi").pattern("wdi")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.FIRE_FURNACE.get(), ECItems.FIRE_CRYSTAL.get())
				.define('f', Blocks.FURNACE)
				.pattern("www")
				.pattern("wfw")
				.pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.FIRE_BLAST_FURNACE.get(), ECItems.FIRE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('F', Blocks.BLAST_FURNACE)
				.define('g', ECBlocks.BURNT_GLASS.get())
				.pattern("www")
				.pattern("gFg")
				.pattern("ici")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.PURIFIER.get(), ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('e', ECTags.Items.FINE_EARTH_GEMS)
				.define('g', Tags.Items.INGOTS_GOLD)
				.pattern("gig")
				.pattern("wew")
				.pattern("ici")
				.save(consumer);
	}

	private void registerPureInfuser(@Nonnull Consumer<FinishedRecipe> consumer) {
		prepareWhiterockInstrumentRecipe(ECBlocks.PURE_INFUSER.get(), ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('n', ECBlocks.INFUSER.get())
				.pattern("wnw")
				.pattern("ici")
				.pattern("www")
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.FIRE_PEDESTAL.get(), ElementType.FIRE)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECTags.Items.FINE_FIRE_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.WATER_PEDESTAL.get(), ElementType.WATER)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECTags.Items.FINE_WATER_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.EARTH_PEDESTAL.get(), ElementType.EARTH)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECTags.Items.FINE_EARTH_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.AIR_PEDESTAL.get(), ElementType.AIR)
				.addIngredient(ECBlocks.INFUSER.get())
				.addIngredient(ECTags.Items.FINE_AIR_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.addIngredient(ECBlocks.WHITE_ROCK.get())
				.withElementAmount(30000)
				.save(consumer);
	}

	private void registerContainers(@Nonnull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ECBlocks.SMALL_CONTAINER.get())
				.define('g', Tags.Items.GLASS)
				.define('p', ECBlocks.PIPE_IMPAIRED.get())
				.pattern(" p ")
				.pattern("pgp")
				.pattern(" p ")
				.unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL))
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('g', ECBlocks.BURNT_GLASS.get())
				.define('p', ECBlocks.PIPE.get())
				.pattern("ici")
				.pattern("pgp")
				.pattern("www")
				.save(consumer);

		BindingRecipeBuilder.bindingRecipe(ECBlocks.FIRE_RESERVOIR.get(), ElementType.FIRE)
				.addIngredient(ECBlocks.CONTAINER.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(ECTags.Items.PRISTINE_FIRE_GEMS)
				.withElementAmount(10000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.WATER_RESERVOIR.get(), ElementType.WATER)
				.addIngredient(ECBlocks.CONTAINER.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(ECTags.Items.PRISTINE_WATER_GEMS)
				.withElementAmount(10000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.EARTH_RESERVOIR.get(), ElementType.EARTH)
				.addIngredient(ECBlocks.CONTAINER.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(ECTags.Items.PRISTINE_EARTH_GEMS)
				.withElementAmount(10000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.AIR_RESERVOIR.get(), ElementType.AIR)
				.addIngredient(ECBlocks.CONTAINER.get())
				.addIngredient(ECBlocks.SPRINGALINE_GLASS.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(ECTags.Items.PRISTINE_AIR_GEMS)
				.withElementAmount(10000)
				.save(consumer);
	}

	private static void registerShards(@Nonnull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ECItems.POWERFUL_FIRE_SHARD.get())
				.define('#', ECItems.FIRE_SHARD.get())
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_fire_shard", has(ECItems.FIRE_SHARD))
				.save(consumer, ElementalCraft.createRL("powerful_fire_shard_from_fire_shards"));
		ShapelessRecipeBuilder.shapeless(ECItems.FIRE_SHARD.get(), 9)
				.requires(ECItems.POWERFUL_FIRE_SHARD.get())
				.unlockedBy("has_powerful_fire_shard", has(ECItems.POWERFUL_FIRE_SHARD))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.POWERFUL_WATER_SHARD.get())
				.define('#', ECItems.WATER_SHARD.get())
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_water_shard", has(ECItems.WATER_SHARD))
				.save(consumer, ElementalCraft.createRL("powerful_water_shard_from_water_shards"));
		ShapelessRecipeBuilder.shapeless(ECItems.WATER_SHARD.get(), 9)
				.requires(ECItems.POWERFUL_WATER_SHARD.get())
				.unlockedBy("has_powerful_water_shard", has(ECItems.POWERFUL_WATER_SHARD))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.POWERFUL_EARTH_SHARD.get())
				.define('#', ECItems.EARTH_SHARD.get())
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_earth_shard", has(ECItems.EARTH_SHARD))
				.save(consumer, ElementalCraft.createRL("powerful_earth_shard_from_earth_shards"));
		ShapelessRecipeBuilder.shapeless(ECItems.EARTH_SHARD.get(), 9)
				.requires(ECItems.POWERFUL_EARTH_SHARD.get())
				.unlockedBy("has_powerful_earth_shard", has(ECItems.POWERFUL_EARTH_SHARD))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.POWERFUL_AIR_SHARD.get())
				.define('#', ECItems.AIR_SHARD.get())
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_air_shard", has(ECItems.AIR_SHARD))
				.save(consumer, ElementalCraft.createRL("powerful_air_shard_from_air_shards"));
		ShapelessRecipeBuilder.shapeless(ECItems.AIR_SHARD.get(), 9)
				.requires(ECItems.POWERFUL_AIR_SHARD.get())
				.unlockedBy("has_powerful_air_shard", has(ECItems.POWERFUL_AIR_SHARD))
				.save(consumer);
	}

	private static void registerHolders(@Nonnull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ECItems.FIRE_HOLDER.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('e', ECBlocks.EXTRACTOR.get())
				.define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('c', ECItems.FIRE_CRYSTAL.get())
				.pattern("geg")
				.pattern("iti")
				.pattern("gcg")
				.unlockedBy("has_firecrystal", has(ECItems.FIRE_CRYSTAL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.WATER_HOLDER.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('e', ECBlocks.EXTRACTOR.get())
				.define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("geg")
				.pattern("iti")
				.pattern("gcg")
				.unlockedBy("has_watercrystal", has(ECItems.WATER_CRYSTAL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.EARTH_HOLDER.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('e', ECBlocks.EXTRACTOR.get())
				.define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("geg")
				.pattern("iti")
				.pattern("gcg")
				.unlockedBy("has_earthcrystal", has(ECItems.EARTH_CRYSTAL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.AIR_HOLDER.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('e', ECBlocks.EXTRACTOR.get())
				.define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('c', ECItems.AIR_CRYSTAL.get())
				.pattern("geg")
				.pattern("iti")
				.pattern("gcg")
				.unlockedBy("has_aircrystal", has(ECItems.AIR_CRYSTAL))
				.save(consumer);

		ShapedRecipeBuilder.shaped(ECItems.PURE_HOLDER_CORE.get())
				.define('i', ECTags.Items.INGOTS_FIREITE)
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern(" i ")
				.pattern("ici")
				.pattern(" i ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(consumer);
		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.PURE_HOLDER.get())
				.setIngredient(ECItems.PURE_HOLDER_CORE.get()
				).setIngredient(ElementType.WATER, ECItems.WATER_HOLDER.get())
				.setIngredient(ElementType.FIRE, ECItems.FIRE_HOLDER.get())
				.setIngredient(ElementType.EARTH, ECItems.EARTH_HOLDER.get())
				.setIngredient(ElementType.AIR, ECItems.AIR_HOLDER.get())
				.withElementAmount(100000)
				.save(consumer);
	}

	private void registerSlabsStairsWalls(Consumer<FinishedRecipe> consumer) {
		ForgeRegistries.BLOCKS.getEntries().forEach(e -> {
			var block = e.getValue();
			var key = e.getKey().location();

			if (ElementalCraft.owns(key) && !exists(block) && (block instanceof SlabBlock || block instanceof StairBlock || block instanceof WallBlock)) {
				String name = key.getPath();
				String sourceName = name.substring(0, name.length() - (block instanceof StairBlock ? 7 : 5));
				ItemLike source = ForgeRegistries.ITEMS.getValue(ElementalCraft.createRL(sourceName));
				ShapedRecipeBuilder shaped = ShapedRecipeBuilder.shaped(block, block instanceof StairBlock ? 4 : 6).define('#', source);

				if (block instanceof SlabBlock) {
					shaped.pattern("###");
				} else if (block instanceof StairBlock) {
					shaped.pattern("#  ").pattern("## ").pattern("###");
				} else if (block instanceof WallBlock) {
					shaped.pattern("###").pattern("###");
				}
				shaped.unlockedBy("has_" + sourceName, has(source)).save(consumer);
				SingleItemRecipeBuilder.stonecutting(Ingredient.of(source), block, block instanceof SlabBlock ? 2 : 1)
						.unlockedBy("has_" + sourceName, has(source))
						.save(consumer, ElementalCraft.createRL(name + FROM + sourceName + "_stonecutting"));
			}
		});
	}

	private void registerInertCrystal(Consumer<FinishedRecipe> consumer) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ECTags.Items.ORES_INERT_CRYSTAL), ECItems.INERT_CRYSTAL.get(), 0.5F, 200)
				.unlockedBy("has_crystal_ore", has(ECTags.Items.ORES_INERT_CRYSTAL))
				.save(consumer);
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ECTags.Items.ORES_INERT_CRYSTAL), ECItems.INERT_CRYSTAL.get(), 0.5F, 100)
				.unlockedBy("has_crystal_ore", has(ECTags.Items.ORES_INERT_CRYSTAL))
				.save(consumer, ElementalCraft.createRL("inertcrystal_from_blasting"));

		ConditionalRecipe.builder()
				.addCondition(new ModLoadedCondition(MekanismAPI.MEKANISM_MODID))
				.addRecipe(ItemStackToItemStackRecipeBuilder.enriching(IngredientCreatorAccess.item().from(ECTags.Items.ORES_INERT_CRYSTAL), new ItemStack(ECItems.INERT_CRYSTAL.get(), 2))::build)
				.build(consumer, ElementalCraft.createRL("inert_crystal_from_mekanism_enriching"));
	}

	private void registerNuggetIngotBlocks(@Nonnull Consumer<FinishedRecipe> consumer) {
		createNuggetIngotBlock(ECItems.DRENCHED_IRON_NUGGET.get(), ECTags.Items.NUGGETS_DRENCHED_IRON, ECItems.DRENCHED_IRON_INGOT.get(), ECTags.Items.INGOTS_DRENCHED_IRON, ECBlocks.DRENCHED_IRON_BLOCK.get(), ECTags.Items.STORAGE_BLOCKS_DRENCHED_IRON, consumer);
		createNuggetIngotBlock(ECItems.SWIFT_ALLOY_NUGGET.get(), ECTags.Items.NUGGETS_SWIFT_ALLOY, ECItems.SWIFT_ALLOY_INGOT.get(), ECTags.Items.INGOTS_SWIFT_ALLOY, ECBlocks.SWIFT_ALLOY_BLOCK.get(), ECTags.Items.STORAGE_BLOCKS_SWIFT_ALLOY, consumer);
		createNuggetIngotBlock(ECItems.FIREITE_NUGGET.get(), ECTags.Items.NUGGETS_FIREITE, ECItems.FIREITE_INGOT.get(), ECTags.Items.INGOTS_FIREITE, ECBlocks.FIREITE_BLOCK.get(), ECTags.Items.STORAGE_BLOCKS_FIREITE, consumer);

		createStorageBlock(ECItems.INERT_CRYSTAL.get(), ECBlocks.INERT_CRYSTAL_BLOCK.get(), consumer);
		createStorageBlock(ECItems.FIRE_CRYSTAL.get(), ECBlocks.FIRE_CRYSTAL_BLOCK.get(), consumer);
		createStorageBlock(ECItems.WATER_CRYSTAL.get(), ECBlocks.WATER_CRYSTAL_BLOCK.get(), consumer);
		createStorageBlock(ECItems.EARTH_CRYSTAL.get(), ECBlocks.EARTH_CRYSTAL_BLOCK.get(), consumer);
		createStorageBlock(ECItems.AIR_CRYSTAL.get(), ECBlocks.AIR_CRYSTAL_BLOCK.get(), consumer);
	}

	private void registerPipes(Consumer<FinishedRecipe> consumer) {
		prepareInstrumentRecipe(ECBlocks.PIPE_IMPAIRED.get(), ECItems.CONTAINED_CRYSTAL.get(), 4)
				.define('i', Tags.Items.INGOTS_IRON)
				.pattern("ici")
				.save(consumer);
		prepareInstrumentRecipe(ECBlocks.PIPE.get(), ECItems.CONTAINED_CRYSTAL.get(), 4)
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("ici")
				.save(consumer);
		prepareInstrumentRecipe(ECBlocks.PIPE_IMPROVED.get(), ECItems.CONTAINED_CRYSTAL.get(), 4)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern("ici")
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(ECBlocks.PIPE.get())
				.requires(ECBlocks.PIPE_IMPAIRED.get())
				.requires(Ingredient.of(ECTags.Items.NUGGETS_DRENCHED_IRON), 5)
				.unlockedBy(HAS_DRENCHED_IRON_NUGGET, has(ECTags.Items.NUGGETS_DRENCHED_IRON))
				.save(consumer, ElementalCraft.createRL("elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapeless(ECBlocks.PIPE.get(), 4)
				.requires(ECBlocks.PIPE_IMPAIRED.get(), 4)
				.requires(Ingredient.of(ECTags.Items.INGOTS_DRENCHED_IRON), 2)
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(consumer, ElementalCraft.createRL("elementpipe_from_impaired_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapeless(ECBlocks.PIPE_IMPROVED.get())
				.requires(ECBlocks.PIPE.get())
				.requires(Ingredient.of(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.unlockedBy(HAS_SWIFT_ALLOY_NUGGET, has(ECTags.Items.NUGGETS_SWIFT_ALLOY))
				.save(consumer, ElementalCraft.createRL("improved_elementpipe_from_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapeless(ECBlocks.PIPE_IMPROVED.get(), 4)
				.requires(ECBlocks.PIPE.get(), 4)
				.requires(Ingredient.of(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer, ElementalCraft.createRL("improved_elementpipe_from_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapeless(ECBlocks.PIPE_IMPROVED.get())
				.requires(ECBlocks.PIPE_IMPAIRED.get())
				.requires(Ingredient.of(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.unlockedBy(HAS_SWIFT_ALLOY_NUGGET, has(ECTags.Items.NUGGETS_SWIFT_ALLOY))
				.save(consumer, ElementalCraft.createRL("improved_elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapeless(ECBlocks.PIPE_IMPROVED.get(), 4)
				.requires(ECBlocks.PIPE_IMPAIRED.get(), 4)
				.requires(Ingredient.of(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer, ElementalCraft.createRL("improved_elementpipe_from_impaired_elementpipe_and_ingot"));

		shaped(ECItems.COVER_FRAME, 8)
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("iii")
				.pattern("i i")
				.pattern("iii")
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON))
				.save(consumer);

		shaped(ECItems.PIPE_PRIORITY_RINGS, 4)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern(" i ")
				.pattern("ifi")
				.pattern(" i ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);
		shaped(ECItems.ELEMENT_PUMP)
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('n', ECTags.Items.NUGGETS_FIREITE)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern("in ")
				.pattern("cfi")
				.pattern("in ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.NUGGETS_FIREITE))
				.save(consumer);
		shaped(ECItems.ELEMENT_VALVE)
				.define('r', Tags.Items.DUSTS_REDSTONE)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern(" r ")
				.pattern("rfr")
				.pattern(" r ")
				.unlockedBy("has_cover_frame", has(ECItems.COVER_FRAME.get()))
				.save(consumer);
		shaped(ECItems.ELEMENT_BEAM, 2)
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('n', ECTags.Items.NUGGETS_SWIFT_ALLOY)
				.define('f', ECItems.COVER_FRAME.get())
				.pattern("in ")
				.pattern("fic")
				.pattern("in ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);
	}

	private void registerInfusions(@Nonnull Consumer<FinishedRecipe> consumer) {
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL.get()), ECItems.FIRE_CRYSTAL.get(), ElementType.FIRE)
				.save(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL.get()), ECItems.WATER_CRYSTAL.get(), ElementType.WATER)
				.save(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL.get()), ECItems.AIR_CRYSTAL.get(), ElementType.AIR)
				.save(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL.get()), ECItems.EARTH_CRYSTAL.get(), ElementType.EARTH)
				.save(consumer);

		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Items.STONE), ECBlocks.WHITE_ROCK.get(), ElementType.EARTH)
				.withElementAmount(500)
				.save(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Tags.Items.INGOTS_IRON), ECItems.DRENCHED_IRON_INGOT.get(), ElementType.WATER)
				.withElementAmount(500)
				.save(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Tags.Items.GLASS), ECBlocks.BURNT_GLASS.get(), ElementType.FIRE)
				.withElementAmount(500)
				.save(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Tags.Items.STRING), ECItems.AIR_SILK.get(), ElementType.AIR)
				.withElementAmount(500)
				.save(consumer);
	}

	private void registerShrines(Consumer<FinishedRecipe> consumer) {
		BindingRecipeBuilder.bindingRecipe(ECBlocks.FIRE_PYLON.get(), ElementType.FIRE)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.addIngredient(Items.LAVA_BUCKET)
				.addIngredient(Tags.Items.INGOTS_GOLD)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.VACUUM_SHRINE.get(), ElementType.AIR)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.AIR_CRYSTAL.get())
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.HOPPER)
				.addIngredient(Tags.Items.GEMS_DIAMOND)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.GROWTH_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(Items.WHEAT_SEEDS)
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(Tags.Items.GEMS_DIAMOND)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.LAVA_SHRINE.get(), ElementType.FIRE)
				.addIngredient(ECBlocks.FIRE_PYLON.get())
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.addIngredient(ECTags.Items.PRISTINE_FIRE_GEMS)
				.addIngredient(Blocks.OBSIDIAN)
				.addIngredient(Items.LAVA_BUCKET)
				.addIngredient(Items.BLAZE_ROD)
				.withElementAmount(20000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.HARVEST_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECTags.Items.CRUDE_EARTH_GEMS)
				.addIngredient(Items.DIAMOND_HOE)
				.addIngredient(Items.SHEARS)
				.withElementAmount(5000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.LUMBER_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECTags.Items.CRUDE_EARTH_GEMS)
				.addIngredient(ECItems.DRENCHED_SAW_BLADE.get())
				.addIngredient(Items.DIAMOND_AXE)
				.addIngredient(Items.SHEARS)
				.withElementAmount(5000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.ORE_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECTags.Items.PRISTINE_EARTH_GEMS)
				.addIngredient(Items.DIAMOND_PICKAXE)
				.withElementAmount(20000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.OVERLOAD_SHRINE.get(), ElementType.AIR)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.AIR_CRYSTAL.get())
				.addIngredient(ECItems.PURE_CRYSTAL.get())
				.addIngredient(Items.CLOCK)
				.addIngredient(Items.ENDER_EYE)
				.withElementAmount(20000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SWEET_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(Items.SUGAR)
				.addIngredient(Items.HONEY_BOTTLE)
				.addIngredient(Items.MILK_BUCKET)
				.withElementAmount(5000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.ENDER_LOCK_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(ECTags.Items.FINE_AIR_GEMS)
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.DRAGON_BREATH)
				.addIngredient(Items.OBSIDIAN)
				.withElementAmount(5000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.BREEDING_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECTags.Items.CRUDE_WATER_GEMS)
				.addIngredient(Tags.Items.CROPS)
				.addIngredient(Tags.Items.LEATHER)
				.addIngredient(Items.MILK_BUCKET)
				.addIngredient(Tags.Items.GEMS_DIAMOND)
				.withElementAmount(5000)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.GROVE_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(ECTags.Items.CRUDE_EARTH_GEMS)
				.addIngredient(ItemTags.FLOWERS)
				.addIngredient(Tags.Items.SEEDS)
				.addIngredient(Tags.Items.CROPS)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPRING_SHRINE.get(), ElementType.WATER)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.WATER_CRYSTAL.get())
				.addIngredient(Items.BUCKET)
				.addIngredient(ItemTags.FISHES)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.BUDDING_SHRINE.get(), ElementType.EARTH)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.EARTH_CRYSTAL.get())
				.addIngredient(ECTags.Items.CRUDE_WATER_GEMS)
				.addIngredient(Items.AMETHYST_BLOCK)
				.addIngredient(ECItems.SPRINGALINE_SHARD.get())
				.addIngredient(Tags.Items.GEMS_DIAMOND)
				.save(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPAWNING_SHRINE.get(), ElementType.FIRE)
				.addIngredient(ECItems.SHRINE_BASE.get())
				.addIngredient(ECItems.FIRE_CRYSTAL.get())
				.addIngredient(ECTags.Items.FINE_EARTH_GEMS)
				.addIngredient(Items.ROTTEN_FLESH)
				.addIngredient(Items.SPIDER_EYE)
				.addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.DIAMOND)
				.save(consumer);
	}

	private void registerShrineUpgrades(@Nonnull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ECBlocks.ACCELERATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('i', Items.CLOCK)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('r', Tags.Items.DUSTS_REDSTONE)
				.pattern("rir")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.RANGE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', Tags.Items.DUSTS_GLOWSTONE)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("ggg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.CAPACITY_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', ECBlocks.SPRINGALINE_GLASS.get())
				.define('b', Items.BUCKET)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("gbg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.EFFICIENCY_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.FIRE_CRYSTAL.get())
				.pattern("gdg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.STRENGTH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('g', Tags.Items.DUSTS_GLOWSTONE)
				.define('r', Tags.Items.RODS_BLAZE)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.FIRE_CRYSTAL.get())
				.pattern("grg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.OPTIMIZATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('f', ECTags.Items.INGOTS_FIREITE)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("dfd")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.FORTUNE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('l', Tags.Items.GEMS_LAPIS)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("lll")
				.pattern("wCw").
				pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.SILK_TOUCH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', ECItems.AIR_SILK.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("sss")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.PLANTING_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', Tags.Items.SEEDS)
				.define('h', Items.DIAMOND_HOE)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("shs")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.BONELESS_GROWTH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('b', Items.BONE_BLOCK)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("bdb")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.PICKUP_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('e', Items.ENDER_EYE)
				.define('h', Items.HOPPER)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("ehe")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.VORTEX_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('e', Items.ENDER_PEARL)
				.define('h', Items.HOPPER)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.AIR_CRYSTAL.get())
				.pattern("ehe")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.NECTAR_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('h', Items.HONEY_BLOCK)
				.define('s', Items.SUGAR)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("shs")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ConditionalRecipe.builder()
				.addCondition(new ModLoadedCondition(BotaniaAPI.MODID))
				.addRecipe(ShapedRecipeBuilder.shaped(ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE.get())
						.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
						.define('p', BotaniaTags.Items.PETALS)
						.define('m', BotaniaTags.Items.INGOTS_MANASTEEL)
						.define('w', BotaniaBlocks.livingrock)
						.define('c', ECItems.WATER_CRYSTAL.get())
						.pattern("pmp").pattern("wCw")
						.pattern(" c ")
						.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))::save)
				.build(consumer, ForgeRegistries.BLOCKS.getKey(ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE.get()));
		ShapedRecipeBuilder.shaped(ECBlocks.STEM_POLLINATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', Items.PUMPKIN)
				.define('b', Items.BONE_MEAL)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("bpb")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.PROTECTION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', Items.SHIELD)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("isi")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.FILLING_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('b', Items.BUCKET)
				.define('i', Tags.Items.INGOTS_IRON)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("ibi")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.SPRINGALINE_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', Items.PRISMARINE_CRYSTALS)
				.define('s', ECBlocks.SPRINGALINE_BLOCK.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("sps")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.CRYSTAL_HARVEST_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('p', Items.DIAMOND_PICKAXE)
				.define('g', ECBlocks.SPRINGALINE_GLASS.get())
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.pattern("gpg")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.CRYSTAL_GROWTH_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('s', ECBlocks.SPRINGALINE_BLOCK.get())
				.define('e', ECTags.Items.PRISTINE_EARTH_GEMS)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL.get())
				.pattern("ses")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.TRANSLOCATION_SHRINE_UPGRADE.get()).define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('t', createScrollIngredient(Spells.TRANSLOCATION))
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.pattern("ftf")
				.pattern("wCw")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE.get())
				.define('C', ECItems.SHRINE_UPGRADE_CORE.get())
				.define('i', Items.CLOCK)
				.define('p', ECBlocks.PIPE_IMPROVED.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('z', createRuneIngredient("zod"))
				.pattern("ziz")
				.pattern("pCp")
				.pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
	}

	private void registerSourceDisplacementPlates(Consumer<FinishedRecipe> consumer) {
		createSourceDisplacementPlate(ECBlocks.FIRE_SOURCE_DISPLACEMENT_PLATE, ECTags.Items.PRISTINE_FIRE_GEMS, consumer);
		createSourceDisplacementPlate(ECBlocks.EARTH_SOURCE_DISPLACEMENT_PLATE, ECTags.Items.PRISTINE_EARTH_GEMS, consumer);
		createSourceDisplacementPlate(ECBlocks.WATER_SOURCE_DISPLACEMENT_PLATE, ECTags.Items.PRISTINE_WATER_GEMS, consumer);
		createSourceDisplacementPlate(ECBlocks.AIR_SOURCE_DISPLACEMENT_PLATE, ECTags.Items.PRISTINE_AIR_GEMS, consumer);
	}

	private void registerJewels(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ECItems.UNSET_JEWEL.get())
				.pattern("sis")
				.pattern("idi")
				.pattern("sis")
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', Tags.Items.GEMS_DIAMOND)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY))
				.save(consumer);

		createJewelRecipe(Jewels.SALMON, b -> b
				.pattern(" w ")
				.pattern("cUc")
				.pattern(" a ")
				.define('w', ECTags.Items.FINE_WATER_GEMS)
				.define('a', ECTags.Items.CRUDE_AIR_GEMS)
				.define('c', ECItems.WATER_CRYSTAL.get()), consumer);
		createJewelRecipe(Jewels.DOLPHIN, b -> b
				.pattern(" w ")
				.pattern("cUc")
				.pattern(" w ")
				.define('w', ECTags.Items.CRUDE_WATER_GEMS)
				.define('c', ECItems.WATER_CRYSTAL.get()), consumer);
		createJewelRecipe(Jewels.LEOPARD, b -> b
				.pattern(" a ")
				.pattern("cUc")
				.pattern(" e ")
				.define('a', ECTags.Items.FINE_AIR_GEMS)
				.define('e', ECTags.Items.FINE_EARTH_GEMS)
				.define('c', ECItems.AIR_CRYSTAL.get()), consumer);
		createJewelRecipe(Jewels.PHOENIX, b -> b
				.pattern("fgf")
				.pattern("bUb")
				.pattern("fpf")
				.define('g', ECTags.Items.PRISTINE_FIRE_GEMS)
				.define('b', Tags.Items.RODS_BLAZE)
				.define('f', Tags.Items.FEATHERS)
				.define('p', ECItems.PURE_CRYSTAL.get()), consumer);
		createJewelRecipe(Jewels.TORTOISE, b -> b
				.pattern("ses")
				.pattern("gUg")
				.pattern("ses")
				.define('g', Tags.Items.GRAVEL)
				.define('e', ECTags.Items.CRUDE_EARTH_GEMS)
				.define('s', Items.SCUTE), consumer);
		createJewelRecipe(Jewels.DEMIGOD, b -> b
				.pattern("tat")
				.pattern("cUc")
				.pattern("tet")
				.define('e', ECTags.Items.PRISTINE_EARTH_GEMS)
				.define('a', ECTags.Items.PRISTINE_AIR_GEMS)
				.define('t', Items.TOTEM_OF_UNDYING)
				.define('c', ECItems.PURE_CRYSTAL.get()), consumer);
		createJewelRecipe(Jewels.MOLE, b -> b
				.pattern(" e ")
				.pattern("sUa")
				.pattern(" p ")
				.define('e', ECTags.Items.PRISTINE_EARTH_GEMS)
				.define('a', Items.DIAMOND_PICKAXE)
				.define('s', Items.DIAMOND_SHOVEL)
				.define('p', ECItems.PURE_CRYSTAL.get()), consumer);
		createJewelRecipe(Jewels.TIGER, b -> b
				.pattern("sas")
				.pattern("cUc")
				.pattern("sfs")
				.define('a', ECTags.Items.PRISTINE_AIR_GEMS)
				.define('c', ECItems.AIR_CRYSTAL.get())
				.define('f', ECTags.Items.FINE_AIR_GEMS)
				.define('s', Items.SUGAR), consumer);
		createJewelRecipe(Jewels.BEAR, b -> b
				.pattern("heh")
				.pattern("cUc")
				.pattern("hfh")
				.define('e', ECTags.Items.PRISTINE_EARTH_GEMS)
				.define('c', ECItems.EARTH_CRYSTAL.get())
				.define('f', ECTags.Items.FINE_EARTH_GEMS)
				.define('h', Items.HONEY_BOTTLE), consumer);
		createJewelRecipe(Jewels.VIPER, b -> b
				.pattern(" w ")
				.pattern("sUs")
				.pattern(" e ")
				.define('e', ECTags.Items.FINE_EARTH_GEMS)
				.define('w', ECTags.Items.FINE_WATER_GEMS)
				.define('s', Items.SPIDER_EYE), consumer);
		createJewelRecipe(Jewels.HAWK, b -> b
				.pattern("gag")
				.pattern("cUc")
				.pattern("gag")
				.define('a', ECTags.Items.FINE_AIR_GEMS)
				.define('g', Tags.Items.DUSTS_GLOWSTONE)
				.define('c', ECItems.AIR_CRYSTAL.get()), consumer);
		createJewelRecipe(Jewels.KIRIN, b -> b
				.pattern("sfs")
				.pattern("cUc")
				.pattern("sas")
				.define('f', ECTags.Items.PRISTINE_FIRE_GEMS)
				.define('a', ECTags.Items.PRISTINE_AIR_GEMS)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('c', ECItems.PURE_CRYSTAL.get()), consumer);
		createJewelRecipe(Jewels.ARCTIC_HARES, b -> b
				.pattern(" w ")
				.pattern("fUf")
				.pattern(" a ")
				.define('w', ECTags.Items.FINE_WATER_GEMS)
				.define('a', ECTags.Items.CRUDE_AIR_GEMS)
				.define('f', Items.RABBIT_FOOT), consumer);
		createJewelRecipe(Jewels.STRIDER, b -> b
				.pattern(" f ")
				.pattern("bUb")
				.pattern(" f ")
				.define('f', ECTags.Items.FINE_FIRE_GEMS)
				.define('b', Items.LAVA_BUCKET), consumer);
		createJewelRecipe(Jewels.WATER_STRIDER, b -> b
				.pattern(" w ")
				.pattern("bUb")
				.pattern(" w ")
				.define('w', ECTags.Items.FINE_WATER_GEMS)
				.define('b', Items.WATER_BUCKET), consumer);
		createJewelRecipe(Jewels.BASILISK, b -> b
				.pattern("fwf")
				.pattern("cUc")
				.pattern("sws")
				.define('w', ECTags.Items.PRISTINE_WATER_GEMS)
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('f', Items.FERMENTED_SPIDER_EYE)
				.define('s', Items.SCUTE), consumer);
		createJewelRecipe(Jewels.PIGLIN, b -> b
				.pattern("gfg")
				.pattern("pUp")
				.pattern("gcg")
				.define('c', ECItems.FIRE_CRYSTAL.get())
				.define('f', ECTags.Items.CRUDE_FIRE_GEMS)
				.define('g', Tags.Items.INGOTS_GOLD)
				.define('p', Items.PORKCHOP), consumer);
	}

	private void registerSpells(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(ECItems.SCROLL_PAPER.get())
				.requires(ECItems.AIR_SILK.get())
				.requires(Items.PAPER)
				.requires(Items.INK_SAC)
				.unlockedBy("has_air_silk", has(ECItems.AIR_SILK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.SPELL_DESK.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.define('l', Blocks.LECTERN)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.pattern("wlw")
				.pattern(" i ")
				.pattern(" w ")
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.FOCUS.get())
				.define('d', Tags.Items.GEMS_DIAMOND)
				.define('c', ECItems.CONTAINED_CRYSTAL.get())
				.define('s', ECTags.Items.HARDENED_RODS)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.pattern(" ic").pattern(" si")
				.pattern("d  ")
				.unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.STAFF.get())
				.define('s', ECTags.Items.STAFF_CRAFT_SWORD)
				.define('f', ECItems.FOCUS.get())
				.define('h', ECTags.Items.HARDENED_RODS)
				.define('i', ECTags.Items.INGOTS_FIREITE)
				.pattern(" if")
				.pattern("ihi")
				.pattern("si ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(mapToStaff(consumer));
		ShapedRecipeBuilder.shaped(ECItems.SPELL_BOOK.get())
				.define('c', ECItems.PURE_CRYSTAL.get())
				.define('s', ECItems.AIR_SILK.get())
				.define('l', Tags.Items.LEATHER)
				.define('p', ECItems.SCROLL_PAPER.get())
				.pattern("slp")
				.pattern("clp").pattern("slp")
				.unlockedBy(HAS_PURECRYSTAL, has(ECItems.PURE_CRYSTAL))
				.save(consumer);

		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.GRAVEL_FALL)
				.setGem(ECTags.Items.INPUT_EARTH_GEMS)
				.setCrystal(ECItems.EARTH_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.STONE_WALL)
				.setGem(ECTags.Items.INPUT_EARTH_GEMS)
				.setCrystal(ECItems.EARTH_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.FIRE_BALL)
				.setGem(ECTags.Items.INPUT_FIRE_GEMS)
				.setCrystal(ECItems.FIRE_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.ITEM_PULL)
				.setGem(ECTags.Items.FINE_AIR_GEMS)
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.ENDER_STRIKE)
				.setGem(ECTags.Items.CRUDE_AIR_GEMS)
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.ANIMAL_GROWTH)
				.setGem(ECTags.Items.CRUDE_WATER_GEMS)
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.TREE_FALL)
				.setGem(ECTags.Items.FINE_EARTH_GEMS)
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.PURIFICATION)
				.setGem(ECTags.Items.CRUDE_WATER_GEMS)
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.RIPENING)
				.setGem(ECTags.Items.INPUT_WATER_GEMS)
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.FLAME_CLEAVE)
				.setGem(ECTags.Items.CRUDE_FIRE_GEMS)
				.setCrystal(ECItems.FIRE_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.INFERNO)
				.setGem(ECTags.Items.INPUT_FIRE_GEMS)
				.setCrystal(ECItems.FIRE_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.DASH)
				.setGem(ECTags.Items.FINE_AIR_GEMS)
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.SILK_VEIN)
				.setGem(ECTags.Items.PRISTINE_EARTH_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.TRANSLOCATION)
				.setGem(ECTags.Items.PRISTINE_AIR_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.FEATHER_SPIKES)
				.setGem(ECTags.Items.FINE_AIR_GEMS)
				.setCrystal(ECItems.EARTH_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.HEAL)
				.setGem(ECTags.Items.PRISTINE_WATER_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.SPEED)
				.setGem(ECTags.Items.PRISTINE_AIR_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.SHOCKWAVE)
				.setGem(ECTags.Items.CRUDE_AIR_GEMS)
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.AIR_SHIELD)
				.setGem(ECTags.Items.FINE_AIR_GEMS)
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.save(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(Spells.REPAIR)
				.setGem(ECTags.Items.PRISTINE_FIRE_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.save(consumer);
	}

	private void registerToolInfusions(Consumer<FinishedRecipe> consumer) {
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.MOB_LOOTING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.FIRE_ASPECT).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.SHARPNESS).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, ElementalCraft.createRL("attack_speed")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.BLOCK_FORTUNE).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, ElementalCraft.createRL(AutoSmeltToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.BLOCK_EFFICIENCY).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.BLOCK_FORTUNE).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, ElementalCraft.createRL(AutoSmeltToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.BLOCK_EFFICIENCY).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.BLOCK_FORTUNE).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, ElementalCraft.createRL(AutoSmeltToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.BLOCK_EFFICIENCY).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.MOB_LOOTING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.FIRE_ASPECT).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.SHARPNESS /* TODO cleaving ? */).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.BLOCK_EFFICIENCY).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, Enchantments.BLOCK_FORTUNE).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, ElementalCraft.createRL(AutoSmeltToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PAXELS, Enchantments.BLOCK_EFFICIENCY).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.PUNCH_ARROWS).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.FLAMING_ARROWS).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, ElementalCraft.createRL(FastDrawToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.MULTISHOT).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.PIERCING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.QUICK_CHARGE).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.FISHING_LUCK).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, ElementalCraft.createRL(AutoSmeltToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.FISHING_SPEED).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.LOYALTY).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.IMPALING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.RIPTIDE).save(consumer);

		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.RESPIRATION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.FIRE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.ALL_DAMAGE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.PROJECTILE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.BLAST_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.FIRE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.ALL_DAMAGE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, ElementalCraft.createRL(DodgeToolInfusionEffect.NAME)).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.BLAST_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.FIRE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.ALL_DAMAGE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, ElementalCraft.createRL("movement_speed")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.DEPTH_STRIDER).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.FIRE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.ALL_DAMAGE_PROTECTION).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.FALL_PROTECTION).save(consumer);

		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraft.createRL("fire_reduction")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraft.createRL("water_reduction")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraft.createRL("earth_reduction")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraft.createRL("air_reduction")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraft.createRL("fire_staff")).withElementAmount(5000).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraft.createRL("water_staff")).withElementAmount(5000).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraft.createRL("earth_staff")).withElementAmount(5000).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraft.createRL("air_staff")).withElementAmount(5000).save(consumer);

		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECItems.FIRE_LENS.get(), ElementalCraft.createRL("fire_unbreaking")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECItems.WATER_LENS.get(), ElementalCraft.createRL("water_unbreaking")).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECItems.EARTH_LENS.get(), Enchantments.UNBREAKING).save(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECItems.AIR_LENS.get(), ElementalCraft.createRL("air_unbreaking")).save(consumer);
	}

	private void registerGrinding(Consumer<FinishedRecipe> consumer) {
		GrindingRecipeBuilder.grindingRecipe(Items.COBBLESTONE)
				.withIngredient(Tags.Items.STONE)
				.withLuckRatio(1)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.GRAVEL)
				.withIngredient(Tags.Items.COBBLESTONE)
				.withLuckRatio(2)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.SAND)
				.withIngredient(Tags.Items.GRAVEL)
				.withLuckRatio(5)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.BLAZE_POWDER)
				.withCount(3)
				.withIngredient(Tags.Items.RODS_BLAZE)
				.withLuckRatio(3)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.NETHERITE_SCRAP)
				.withCount(2)
				.withIngredient(Tags.Items.ORES_NETHERITE_SCRAP)
				.withElementAmount(5000)
				.withLuckRatio(1)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(ECItems.INERT_CRYSTAL.get())
				.withCount(2)
				.withIngredient(ECTags.Items.ORES_INERT_CRYSTAL)
				.withLuckRatio(5)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.POINTED_DRIPSTONE)
				.withCount(3)
				.withIngredient(Items.DRIPSTONE_BLOCK)
				.withLuckRatio(1)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.AMETHYST_SHARD)
				.withCount(6)
				.withIngredient(Items.AMETHYST_CLUSTER)
				.withLuckRatio(5)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(ECItems.SPRINGALINE_SHARD.get())
				.withCount(6)
				.withIngredient(ECBlocks.SPRINGALINE_CLUSTER.get())
				.withLuckRatio(5)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.BONE_MEAL)
				.withCount(4)
				.withIngredient(Tags.Items.BONES)
				.withLuckRatio(3)
				.save(consumer);
		GrindingRecipeBuilder.grindingRecipe(Items.STRING)
				.withCount(4)
				.withIngredient(ItemTags.WOOL)
				.save(consumer);

		grindToDye(Items.GREEN_DYE, Items.CACTUS, consumer);
		grindToDye(Items.WHITE_DYE, Items.BONE_MEAL, consumer);
		grindToDye(Items.BLACK_DYE, Items.INK_SAC, consumer);
		grindToDye(Items.BLUE_DYE, Tags.Items.GEMS_LAPIS, consumer);
		grindToDye(Items.RED_DYE, Tags.Items.CROPS_BEETROOT, consumer);
		grindToDye(Items.LIME_DYE, Items.SEA_PICKLE, consumer);
		grindToDye(Items.BROWN_DYE, Items.COCOA_BEANS, consumer);

		grindToDye(Items.WHITE_DYE, ECTags.Items.WHITE_FLOWERS, consumer);
		grindToDye(Items.ORANGE_DYE, ECTags.Items.ORANGE_FLOWERS, consumer);
		grindToDye(Items.MAGENTA_DYE, ECTags.Items.MAGENTA_FLOWERS, consumer);
		grindToDye(Items.LIGHT_BLUE_DYE, ECTags.Items.LIGHT_BLUE_FLOWERS, consumer);
		grindToDye(Items.YELLOW_DYE, ECTags.Items.YELLOW_FLOWERS, consumer);
		grindToDye(Items.LIME_DYE, ECTags.Items.LIME_FLOWERS, consumer);
		grindToDye(Items.PINK_DYE, ECTags.Items.PINK_FLOWERS, consumer);
		grindToDye(Items.GRAY_DYE, ECTags.Items.GRAY_FLOWERS, consumer);
		grindToDye(Items.LIGHT_GRAY_DYE, ECTags.Items.LIGHT_GRAY_FLOWERS, consumer);
		grindToDye(Items.CYAN_DYE, ECTags.Items.CYAN_FLOWERS, consumer);
		grindToDye(Items.PURPLE_DYE, ECTags.Items.PURPLE_FLOWERS, consumer);
		grindToDye(Items.BLUE_DYE, ECTags.Items.BLUE_FLOWERS, consumer);
		grindToDye(Items.BROWN_DYE, ECTags.Items.BROWN_FLOWERS, consumer);
		grindToDye(Items.GREEN_DYE, ECTags.Items.GREEN_FLOWERS, consumer);
		grindToDye(Items.BLACK_DYE, ECTags.Items.BLACK_FLOWERS, consumer);
		grindToDye(Items.RED_DYE, ECTags.Items.RED_FLOWERS, consumer);
	}

	private void grindToDye(ItemLike dye, ItemLike from, Consumer<FinishedRecipe> consumer) {
		GrindingRecipeBuilder.grindingRecipe(dye)
				.withCount(2)
				.withIngredient(from)
				.withLuckRatio(2)
				.save(consumer, ForgeRegistries.ITEMS.getKey(dye.asItem()).getPath() + FROM + ForgeRegistries.ITEMS.getKey(from.asItem()).getPath());
	}

	private void grindToDye(ItemLike dye, TagKey<Item> from, Consumer<FinishedRecipe> consumer) {
		var tagName = from.location();

		ConditionalRecipe.builder()
				.addCondition(new NotCondition(new TagEmptyCondition(tagName)))
				.addRecipe(GrindingRecipeBuilder.grindingRecipe(dye)
						.withCount(2)
						.withIngredient(from)
						.withLuckRatio(2)::save)
				.build(consumer, ElementalCraft.createRL(IGrindingRecipe.NAME + '/' + ForgeRegistries.ITEMS.getKey(dye.asItem()).getPath() + FROM + tagName.getNamespace() + '_' + StringUtils.replaceChars(tagName.getPath(), '/', '_')));
	}

	private void registerSawing(Consumer<FinishedRecipe> consumer) {
		sawingRecipe(Items.STRIPPED_OAK_LOG, Items.STRIPPED_OAK_WOOD, Items.OAK_PLANKS, Items.OAK_LOG, Items.OAK_WOOD, ECTags.Items.STRIPPED_OAK, consumer);
		sawingRecipe(Items.STRIPPED_DARK_OAK_LOG, Items.STRIPPED_DARK_OAK_WOOD, Items.DARK_OAK_PLANKS, Items.DARK_OAK_LOG, Items.DARK_OAK_WOOD, ECTags.Items.STRIPPED_DARK_OAK, consumer);
		sawingRecipe(Items.STRIPPED_BIRCH_LOG, Items.STRIPPED_BIRCH_WOOD, Items.BIRCH_PLANKS, Items.BIRCH_LOG, Items.BIRCH_WOOD, ECTags.Items.STRIPPED_BIRCH, consumer);
		sawingRecipe(Items.STRIPPED_ACACIA_LOG, Items.STRIPPED_ACACIA_WOOD, Items.ACACIA_PLANKS, Items.ACACIA_LOG, Items.ACACIA_WOOD, ECTags.Items.STRIPPED_ACACIA, consumer);
		sawingRecipe(Items.STRIPPED_JUNGLE_LOG, Items.STRIPPED_JUNGLE_WOOD, Items.JUNGLE_PLANKS, Items.JUNGLE_LOG, Items.JUNGLE_WOOD, ECTags.Items.STRIPPED_JUNGLE, consumer);
		sawingRecipe(Items.STRIPPED_SPRUCE_LOG, Items.STRIPPED_SPRUCE_WOOD, Items.SPRUCE_PLANKS, Items.SPRUCE_LOG, Items.SPRUCE_WOOD, ECTags.Items.STRIPPED_SPRUCE, consumer);
		sawingRecipe(Items.STRIPPED_MANGROVE_LOG, Items.STRIPPED_MANGROVE_WOOD, Items.MANGROVE_PLANKS, Items.MANGROVE_LOG, Items.MANGROVE_WOOD, ECTags.Items.STRIPPED_MANGROVE, consumer);
		sawingRecipe(Items.STRIPPED_CRIMSON_STEM, Items.STRIPPED_CRIMSON_HYPHAE, Items.CRIMSON_PLANKS, Items.CRIMSON_STEM, Items.CRIMSON_HYPHAE, ECTags.Items.STRIPPED_CRIMSON, consumer);
		sawingRecipe(Items.STRIPPED_WARPED_STEM, Items.STRIPPED_WARPED_HYPHAE, Items.WARPED_PLANKS, Items.WARPED_STEM, Items.WARPED_HYPHAE, ECTags.Items.STRIPPED_WARPED, consumer);
	}

	private void sawingRecipe(ItemLike stripedLog, ItemLike stripedWood, ItemLike planks, ItemLike log, ItemLike wood, TagKey<Item> stripped, Consumer<FinishedRecipe> consumer) {
		SawingRecipeBuilder.sawingRecipe(stripedLog)
				.withIngredient(log)
				.withElementAmount(250)
				.save(consumer);
		SawingRecipeBuilder.sawingRecipe(stripedWood)
				.withIngredient(wood)
				.withElementAmount(250)
				.save(consumer);
		SawingRecipeBuilder.sawingRecipe(planks)
				.withCount(6)
				.withIngredient(stripped)
				.withLuckRatio(3)
				.save(consumer);
	}

	private void registerRunes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ECItems.MINOR_RUNE_SLATE.get(), 4)
				.pattern("www")
				.pattern("wiw")
				.pattern("www")
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.RUNE_SLATE.get(), 4)
				.pattern("www")
				.pattern("wiw")
				.pattern("www")
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.MAJOR_RUNE_SLATE.get(), 4)
				.pattern("www")
				.pattern("wiw")
				.pattern("www")
				.define('w', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_FIREITE)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK))
				.save(consumer);

		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("wii"), ElementType.AIR)
				.withElementAmount(2000)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.CRUDE_AIR_GEMS)
				.addIngredient(Items.SUGAR)
				.addIngredient(Items.SUGAR)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("fus"), ElementType.AIR)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(ECTags.Items.FINE_AIR_GEMS)
				.addIngredient(Tags.Items.STRING)
				.addIngredient(Tags.Items.STRING)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("zod"), ElementType.AIR)
				.withElementAmount(10000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.PRISTINE_AIR_GEMS)
				.addIngredient(ECItems.AIR_SILK.get())
				.addIngredient(ECItems.AIR_SILK.get())
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("manx"), ElementType.FIRE)
				.withElementAmount(2000)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.CRUDE_FIRE_GEMS)
				.addIngredient(ItemTags.COALS)
				.addIngredient(ItemTags.COALS)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("jita"), ElementType.FIRE)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(ECTags.Items.FINE_FIRE_GEMS)
				.addIngredient(Items.BLAZE_ROD)
				.addIngredient(Items.BLAZE_ROD)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("tano"), ElementType.FIRE)
				.withElementAmount(10000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.PRISTINE_FIRE_GEMS)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_COAL)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_COAL)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("soaryn"), ElementType.EARTH)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.CRUDE_EARTH_GEMS)
				.addIngredient(createRuneIngredient("wii"))
				.addIngredient(createRuneIngredient("manx"))
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("kaworu"), ElementType.EARTH)
				.withElementAmount(10000)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(ECTags.Items.FINE_EARTH_GEMS)
				.addIngredient(createRuneIngredient("fus"))
				.addIngredient(createRuneIngredient("jita"))
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("mewtwo"), ElementType.EARTH)
				.withElementAmount(20000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.PRISTINE_EARTH_GEMS)
				.addIngredient(createRuneIngredient("zod"))
				.addIngredient(createRuneIngredient("tano"))
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("claptrap"), ElementType.WATER)
				.withElementAmount(2000)
				.setSlate(ECItems.MINOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.CRUDE_WATER_GEMS)
				.addIngredient(Tags.Items.GEMS_LAPIS)
				.addIngredient(Tags.Items.GEMS_LAPIS)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("bombadil"), ElementType.WATER)
				.setSlate(ECItems.RUNE_SLATE.get())
				.addIngredient(ECTags.Items.FINE_WATER_GEMS)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_LAPIS)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_LAPIS)
				.save(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("tzeentch"), ElementType.WATER)
				.withElementAmount(10000)
				.setSlate(ECItems.MAJOR_RUNE_SLATE.get())
				.addIngredient(ECTags.Items.PRISTINE_WATER_GEMS)
				.addIngredient(Tags.Items.GEMS_EMERALD)
				.addIngredient(Tags.Items.GEMS_EMERALD)
				.save(consumer);
	}

	private void registerCrystallizations(@Nonnull Consumer<FinishedRecipe> consumer) {
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.FIRE)
				.setGem(ECTags.Items.INPUT_FIRE_GEMS)
				.setCrystal(ECItems.FIRE_CRYSTAL.get())
				.setShard(ECTags.Items.FIRE_SHARDS)
				.addOutput(ECItems.CRUDE_FIRE_GEM.get(), 15, -0.5F)
				.addOutput(ECItems.FINE_FIRE_GEM.get(), 4)
				.addOutput(ECItems.PRISTINE_FIRE_GEM.get(), 1, 2)
				.save(consumer, "fire_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.WATER)
				.setGem(ECTags.Items.INPUT_WATER_GEMS)
				.setCrystal(ECItems.WATER_CRYSTAL.get())
				.setShard(ECTags.Items.WATER_SHARDS)
				.addOutput(ECItems.CRUDE_WATER_GEM.get(), 15, -0.5F)
				.addOutput(ECItems.FINE_WATER_GEM.get(), 4)
				.addOutput(ECItems.PRISTINE_WATER_GEM.get(), 1, 2)
				.save(consumer, "water_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.EARTH)
				.setGem(ECTags.Items.INPUT_EARTH_GEMS)
				.setCrystal(ECItems.EARTH_CRYSTAL.get())
				.setShard(ECTags.Items.EARTH_SHARDS)
				.addOutput(ECItems.CRUDE_EARTH_GEM.get(), 15, -0.5F)
				.addOutput(ECItems.FINE_EARTH_GEM.get(), 4)
				.addOutput(ECItems.PRISTINE_EARTH_GEM.get(), 1, 2)
				.save(consumer, "earth_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.AIR)
				.setGem(ECTags.Items.INPUT_AIR_GEMS)
				.setCrystal(ECItems.AIR_CRYSTAL.get())
				.setShard(ECTags.Items.AIR_SHARDS)
				.addOutput(ECItems.CRUDE_AIR_GEM.get(), 15, -0.5F)
				.addOutput(ECItems.FINE_AIR_GEM.get(), 4)
				.addOutput(ECItems.PRISTINE_AIR_GEM.get(), 1, 2)
				.save(consumer, "air_gem");

		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.FIRE)
				.setGem(ECTags.Items.INPUT_FIRE_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.addOutput(ECItems.PRISTINE_FIRE_GEM.get(), 1)
				.save(consumer, "pristine_fire_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.WATER)
				.setGem(ECTags.Items.INPUT_WATER_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.addOutput(ECItems.PRISTINE_WATER_GEM.get(), 1)
				.save(consumer, "pristine_water_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.EARTH)
				.setGem(ECTags.Items.INPUT_EARTH_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.addOutput(ECItems.PRISTINE_EARTH_GEM.get(), 1)
				.save(consumer, "pristine_earth_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.AIR)
				.setGem(ECTags.Items.INPUT_AIR_GEMS)
				.setCrystal(ECItems.PURE_CRYSTAL.get())
				.addOutput(ECItems.PRISTINE_AIR_GEM.get(), 1)
				.save(consumer, "pristine_air_gem");
	}

	private Ingredient createScrollIngredient(RegistryObject<? extends Spell> spell) {
		var tag = new CompoundTag();
		var ecTag = new CompoundTag();

		tag.put(ECNames.EC_NBT, ecTag);
		ecTag.putString(ECNames.SPELL, spell.getKey().location().toString());
		return PartialNBTIngredient.of(ECItems.SCROLL.get(), tag);
	}

	private Ingredient createRuneIngredient(String name) {
		var tag = new CompoundTag();
		var ecTag = new CompoundTag();

		tag.put(ECNames.EC_NBT, ecTag);
		ecTag.putString(ECNames.RUNE, ElementalCraft.createRL(name).toString());
		return PartialNBTIngredient.of(ECItems.RUNE.get(), tag);
	}

	private void registerEmptying(@Nonnull Consumer<FinishedRecipe> consumer) {
		registerEmptying(ECBlocks.SMALL_CONTAINER.get(), consumer);
		registerEmptying(ECBlocks.CONTAINER.get(), consumer);
		registerEmptying(ECBlocks.FIRE_RESERVOIR.get(), consumer);
		registerEmptying(ECBlocks.WATER_RESERVOIR.get(), consumer);
		registerEmptying(ECBlocks.EARTH_RESERVOIR.get(), consumer);
		registerEmptying(ECBlocks.AIR_RESERVOIR.get(), consumer);
		registerEmptying(ECBlocks.CREATIVE_CONTAINER.get(), consumer);
		registerEmptying(ECItems.FIRE_HOLDER.get(), consumer);
		registerEmptying(ECItems.WATER_HOLDER.get(), consumer);
		registerEmptying(ECItems.EARTH_HOLDER.get(), consumer);
		registerEmptying(ECItems.AIR_HOLDER.get(), consumer);
		registerEmptying(ECItems.PURE_HOLDER.get(), consumer);
	}


	private void registerEmptying(ItemLike item, Consumer<FinishedRecipe> consumer) {
		var name = ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();

		ShapelessRecipeBuilder.shapeless(item)
				.requires(item)
				.unlockedBy("has_" + name, has(item))
				.save(consumer, ElementalCraft.createRL(name + "_emptying"));
	}

	private void registerDecorative(@Nonnull Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ECBlocks.BURNT_GLASS_PANE.get(), 16)
				.define('#', ECBlocks.BURNT_GLASS.get())
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_burnt_glass", has(ECBlocks.BURNT_GLASS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.WHITE_ROCK_FENCE.get(), 16)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.pattern("#i#")
				.pattern("#i#")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.WHITE_ROCK_BRICK.get(), 4)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.pattern("##")
				.pattern("##")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(consumer);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ECBlocks.WHITE_ROCK.get()), ECBlocks.WHITE_ROCK_BRICK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(consumer, ElementalCraft.createRL("whiterock_brick_from_whiterock_stonecutting"));
		ShapedRecipeBuilder.shaped(ECBlocks.MOSSY_WHITE_ROCK.get(), 8)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.define('$', Blocks.MOSS_BLOCK)
				.pattern("###").pattern("#$#").pattern("###")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.BURNT_WHITE_ROCK.get(), 8)
				.define('#', ECBlocks.WHITE_ROCK.get())
				.define('$', Blocks.MAGMA_BLOCK)
				.pattern("###").pattern("#$#")
				.pattern("###").unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.SPRINGALINE_BLOCK.get())
				.define('#', ECItems.SPRINGALINE_SHARD.get())
				.pattern("##")
				.pattern("##")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.SPRINGALINE_GLASS.get(), 2)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', ECBlocks.BURNT_GLASS.get())
				.pattern(" s ")
				.pattern("sgs")
				.pattern(" s ")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.SPRINGALINE_GLASS_PANE.get(), 16)
				.define('#', ECBlocks.SPRINGALINE_GLASS.get())
				.pattern("###")
				.pattern("###")
				.unlockedBy("has_springaline_glass", has(ECBlocks.SPRINGALINE_GLASS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.SPRINGALINE_LANTERN.get())
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', Items.GLOWSTONE)
				.define('p', Tags.Items.GEMS_PRISMARINE)
				.pattern("psp")
				.pattern("sgs")
				.pattern("psp")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(consumer);
	}

	private void registerSourceBreeding(@Nonnull Consumer<FinishedRecipe> consumer) {
		prepareWhiterockInstrumentRecipe(ECBlocks.SOURCE_BREEDER_PEDESTAL.get(), ECItems.PURE_CRYSTAL.get())
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('s', ECItems.STRONGLY_CONTAINED_CRYSTAL.get())
				.pattern("wsw")
				.pattern("ici")
				.pattern("www")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.SOURCE_BREEDER.get(), ECItems.PURE_CRYSTAL.get())
				.define('f', ECTags.Items.INGOTS_FIREITE)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('p', ECBlocks.SOURCE_BREEDER_PEDESTAL.get())
				.pattern("iwi")
				.pattern("fcf")
				.pattern("wpw")
				.save(consumer);


		ShapedRecipeBuilder.shaped(ECItems.ARTIFICIAL_FIRE_SOURCE_SEED.get(), 8)
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', ECTags.Items.PRISTINE_FIRE_GEMS)
				.pattern("fsf")
				.pattern("sgs")
				.pattern("fsf")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.ARTIFICIAL_WATER_SOURCE_SEED.get(), 8)
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', ECTags.Items.PRISTINE_WATER_GEMS)
				.pattern("fsf")
				.pattern("sgs")
				.pattern("fsf")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.ARTIFICIAL_EARTH_SOURCE_SEED.get(), 8)
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', ECTags.Items.PRISTINE_EARTH_GEMS)
				.pattern("fsf")
				.pattern("sgs")
				.pattern("fsf")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.ARTIFICIAL_AIR_SOURCE_SEED.get(), 8)
				.define('f', ECTags.Items.NUGGETS_FIREITE)
				.define('s', ECItems.SPRINGALINE_SHARD.get())
				.define('g', ECTags.Items.PRISTINE_AIR_GEMS)
				.pattern("fsf")
				.pattern("sgs")
				.pattern("fsf")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE))
				.save(consumer);

		ShapelessRecipeBuilder.shapeless(ECItems.NATURAL_FIRE_SOURCE_SEED.get())
				.requires(ECItems.ARTIFICIAL_FIRE_SOURCE_SEED.get())
				.requires(new NaturalSourceIngredient(ElementType.FIRE))
				.unlockedBy("has_artificial_fire_source_seed", has(ECItems.ARTIFICIAL_FIRE_SOURCE_SEED.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ECItems.NATURAL_WATER_SOURCE_SEED.get())
				.requires(ECItems.ARTIFICIAL_WATER_SOURCE_SEED.get())
				.requires(new NaturalSourceIngredient(ElementType.WATER))
				.unlockedBy("has_artificial_water_source_seed", has(ECItems.ARTIFICIAL_WATER_SOURCE_SEED.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ECItems.NATURAL_EARTH_SOURCE_SEED.get())
				.requires(ECItems.ARTIFICIAL_EARTH_SOURCE_SEED.get())
				.requires(new NaturalSourceIngredient(ElementType.EARTH))
				.unlockedBy("has_artificial_earth_source_seed", has(ECItems.ARTIFICIAL_EARTH_SOURCE_SEED.get()))
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(ECItems.NATURAL_AIR_SOURCE_SEED.get())
				.requires(ECItems.ARTIFICIAL_AIR_SOURCE_SEED.get())
				.requires(new NaturalSourceIngredient(ElementType.AIR))
				.unlockedBy("has_artificial_air_source_seed", has(ECItems.ARTIFICIAL_AIR_SOURCE_SEED.get()))
				.save(consumer);
	}

	private boolean exists(Block block) {
		return existingFileHelper.exists(ForgeRegistries.BLOCKS.getKey(block), PackType.SERVER_DATA, ".json", "recipes");
	}

	private void createSourceDisplacementPlate(RegistryObject<? extends ItemLike> plate, TagKey<Item> gem, Consumer<FinishedRecipe> consumer) {
		var plateItem = plate.get().asItem();

		prepareWhiterockInstrumentRecipe(plateItem, ECItems.PURE_CRYSTAL.get())
				.define('s', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('g', gem).pattern(" g ")
				.pattern("scs").pattern("www")
				.save(consumer);
		ShapelessRecipeBuilder.shapeless(plateItem)
				.requires(ECBlocks.BROKEN_SOURCE_DISPLACEMENT_PLATE.get())
				.requires(gem)
				.unlockedBy("has_broken_source_displacement_plate", has(ECBlocks.BROKEN_SOURCE_DISPLACEMENT_PLATE))
				.save(consumer, ElementalCraft.createRL(ForgeRegistries.ITEMS.getKey(plateItem).getPath() + "_repair"));

	}

	private void createNuggetIngotBlock(ItemLike nugget, TagKey<Item> nuggetTag, ItemLike ingot, TagKey<Item> ingotTag, ItemLike block, TagKey<Item> blockTag, Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ingot).define('#', nuggetTag)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy(buildHas(nugget), has(nuggetTag))
				.save(consumer, from(nugget, ingot));
		ShapelessRecipeBuilder.shapeless(nugget, 9)
				.requires(ingotTag)
				.unlockedBy(buildHas(ingot), has(ingotTag))
				.save(consumer, from(ingot, nugget));
		ShapedRecipeBuilder.shaped(block).define('#', ingotTag)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy(buildHas(ingot), has(ingotTag)
				).save(consumer, from(ingot, block));
		ShapelessRecipeBuilder.shapeless(ingot, 9)
				.requires(blockTag)
				.unlockedBy(buildHas(block), has(blockTag))
				.save(consumer, from(block, ingot));
	}

	private void createStorageBlock(ItemLike item, ItemLike block, Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(block)
				.define('#', item)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.unlockedBy(buildHas(item), has(item))
				.save(consumer, from(item, block));
		ShapelessRecipeBuilder.shapeless(item, 9)
				.requires(block)
				.unlockedBy(buildHas(block), has(block))
				.save(consumer, from(block, item));
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(ItemLike result) {
		return prepareInstrumentRecipe(result, ECItems.CONTAINED_CRYSTAL.get(), 1);
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(RegistryObject<? extends ItemLike> result) {
		return prepareInstrumentRecipe(result.get());
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(ItemLike result, ItemLike crystal, int count) {
		return ShapedRecipeBuilder.shaped(result, count)
				.define('c', crystal)
				.unlockedBy(buildHas(crystal), has(crystal));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(ItemLike result) {
		return prepareInstrumentRecipe(result, ECItems.CONTAINED_CRYSTAL.get(), 1)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(ItemLike result, ItemLike crystal) {
		return prepareInstrumentRecipe(result, crystal, 1)
				.define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()));
	}

	public static ShapedRecipeBuilder shaped(RegistryObject<? extends ItemLike> item) {
		return ShapedRecipeBuilder.shaped(item.get(), 1);
	}

	public static ShapedRecipeBuilder shaped(RegistryObject<? extends ItemLike> item, int count) {
		return ShapedRecipeBuilder.shaped(item.get(), count);
	}


	private Consumer<FinishedRecipe> mapToStaff(Consumer<FinishedRecipe> consumer) {
		return recipe -> consumer.accept(new FinishedRecipe() {
			@Override
			public void serializeRecipeData(@Nonnull JsonObject json) {
				recipe.serializeRecipeData(json);
			}

			@Nonnull
			@Override
			public ResourceLocation getId() {
				return recipe.getId();
			}

			@Nonnull
			@Override
			public RecipeSerializer<?> getType() {
				return ECRecipeSerializers.STAFF.get();
			}

			@Override
			public JsonObject serializeAdvancement() {
				return recipe.serializeAdvancement();
			}

			@Override
			public ResourceLocation getAdvancementId() {
				return recipe.getAdvancementId();
			}
		});
	}

	private void createJewelRecipe(RegistryObject<? extends Jewel> jewel, UnaryOperator<ShapedRecipeBuilder> patternBuilder, Consumer<FinishedRecipe> consumer) {
		createJewelRecipe(jewel.get(), patternBuilder, consumer);
	}

	private void createJewelRecipe(Jewel jewel, UnaryOperator<ShapedRecipeBuilder> patternBuilder, Consumer<FinishedRecipe> consumer) {
		var builder = shaped(ECItems.JEWEL);
		var nameSuffix = "/" + jewel.getKey().getPath();

		patternBuilder.apply(builder)
				.define('U', ECItems.UNSET_JEWEL.get())
				.unlockedBy("has_unset_jewel", has(ECItems.UNSET_JEWEL))
				.save(recipe -> consumer.accept(new FinishedRecipe() {
			@Override
			public void serializeRecipeData(@Nonnull JsonObject json) {
				recipe.serializeRecipeData(json);

				var resultJson = json.getAsJsonObject("result");
				var nbt = new JsonObject();
				var ecNbt = new JsonObject();

				ecNbt.addProperty(ECNames.JEWEL, jewel.getKey().toString());
				nbt.add(ECNames.EC_NBT, ecNbt);
				resultJson.add("nbt", nbt);
			}

			@Nonnull
			@Override
			public ResourceLocation getId() {
				var oldId = recipe.getId();

				return new ResourceLocation(oldId.getNamespace(), oldId.getPath()+ nameSuffix);
			}

			@Nonnull
			@Override
			public RecipeSerializer<?> getType() {
				return recipe.getType();
			}

			@Override
			public JsonObject serializeAdvancement() {
				return recipe.serializeAdvancement();
			}

			@Override
			public ResourceLocation getAdvancementId() {
				var oldId = recipe.getAdvancementId();

				return new ResourceLocation(oldId.getNamespace(), oldId.getPath()+ nameSuffix);
			}
		}));
	}

	private ResourceLocation from(ItemLike from, ItemLike to) {
		return  ElementalCraft.createRL(ForgeRegistries.ITEMS.getKey(to.asItem()).getPath() + FROM + ForgeRegistries.ITEMS.getKey(from.asItem()).getPath());
	}

	private String buildHas(ItemLike item) {
		return "has_" + ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();
	}

	protected static InventoryChangeTrigger.TriggerInstance has(RegistryObject<? extends ItemLike> itemLike) {
		return has(itemLike.get().asItem());
	}
}
