package sirttas.elementalcraft.datagen.recipe;

import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
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
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.datagen.recipe.builder.PureInfusionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.SpellCraftRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.AirMillGrindingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.BindingRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.CrystallizationRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.InscriptionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion.InfusionRecipeBuilder;
import sirttas.elementalcraft.datagen.recipe.builder.instrument.infusion.ToolInfusionRecipeBuilder;
import sirttas.elementalcraft.infusion.tool.effect.AutoSmeltToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.DodgeToolInfusionEffect;
import sirttas.elementalcraft.infusion.tool.effect.FastDrawToolInfusionEffect;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.jewel.Jewels;
import sirttas.elementalcraft.recipe.StaffRecipe;
import sirttas.elementalcraft.spell.air.DashSpell;
import sirttas.elementalcraft.spell.air.EnderStrikeSpell;
import sirttas.elementalcraft.spell.air.ItemPullSpell;
import sirttas.elementalcraft.spell.air.TranslocationSpell;
import sirttas.elementalcraft.spell.earth.GavelFallSpell;
import sirttas.elementalcraft.spell.earth.SilkVeinSpell;
import sirttas.elementalcraft.spell.earth.StoneWallSpell;
import sirttas.elementalcraft.spell.earth.TreeFallSpell;
import sirttas.elementalcraft.spell.fire.FireBallSpell;
import sirttas.elementalcraft.spell.fire.FlameCleaveSpell;
import sirttas.elementalcraft.spell.fire.InfernoSpell;
import sirttas.elementalcraft.spell.water.AnimalGrowthSpell;
import sirttas.elementalcraft.spell.water.PurificationSpell;
import sirttas.elementalcraft.spell.water.RipeningSpell;
import sirttas.elementalcraft.tag.ECTags;

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

	private final ExistingFileHelper existingFileHelper;

	public ECRecipeProvider(DataGenerator generatorIn, ExistingFileHelper exFileHelper) {
		super(generatorIn);
		existingFileHelper = exFileHelper;
	}

	@Override
	protected void buildCraftingRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
		registerSlabsStairsWalls(consumer);

		ShapedRecipeBuilder.shaped(ECItems.CONTAINED_CRYSTAL).define('g', Tags.Items.NUGGETS_GOLD).define('c', ECItems.INERT_CRYSTAL).pattern(" g ").pattern("gcg").pattern(" g ")
				.unlockedBy(HAS_INERTCRYSTAL, has(ECItems.INERT_CRYSTAL)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.STRONGLY_CONTAINED_CRYSTAL).define('g', ECTags.Items.NUGGETS_SWIFT_ALLOY).define('c', ECItems.CONTAINED_CRYSTAL).define('s', ECItems.SPRINGALINE_SHARD)
				.pattern("sgs").pattern("gcg").pattern("sgs").unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.SHRINE_BASE).define('w', ECBlocks.WHITE_ROCK.get()).define('c', ECItems.INERT_CRYSTAL).define('p', ECBlocks.PIPE).pattern(" p ").pattern("pcp").pattern("www")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get())).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.EMPTY_RECEPTACLE).define('c', ECItems.PURE_CRYSTAL).define('d', Tags.Items.GEMS_DIAMOND).define('g', Tags.Items.INGOTS_GOLD)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).pattern("gig").pattern("dcd").pattern("gig").unlockedBy(HAS_PURECRYSTAL, has(ECItems.PURE_CRYSTAL)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.EMPTY_RECEPTACLE_IMPROVED).define('c', ECItems.PURE_CRYSTAL).define('r', ECItems.EMPTY_RECEPTACLE).define('f', ECTags.Items.INGOTS_FIREITE)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).pattern("ifi").pattern("crc").pattern("ifi").unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.SOURCE_STABILIZER).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).define('s', ECBlocks.SPRINGALINE_GLASS).pattern("sis").pattern("i i").pattern("sis")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.SOURCE_ANALYSIS_GLASS).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).define('g', ECBlocks.SPRINGALINE_GLASS).define('s', ECItems.AIR_SILK)
				.define('h', ECItems.HARDENED_HANDLE).pattern(" sg").pattern(" is").pattern("h  ").unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.CHISEL).define('h', ECItems.HARDENED_HANDLE).define('d', Tags.Items.GEMS_DIAMOND).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).pattern(" i ").pattern(" di")
				.pattern("h  ").unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.SHRINE_UPGRADE_CORE).define('c', ECItems.CONTAINED_CRYSTAL).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).define('r', Tags.Items.DUSTS_REDSTONE)
				.pattern("rir").pattern("ici").pattern("rir").unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY)).save(consumer);
		ShapelessRecipeBuilder.shapeless(ECItems.SCROLL_PAPER).requires(ECItems.AIR_CRYSTAL).requires(Items.PAPER).requires(Items.INK_SAC).unlockedBy("has_air_silk", has(ECItems.AIR_SILK))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.SOLAR_PRISM).define('s', ECItems.SPRINGALINE_SHARD).define('c', Tags.Items.INGOTS_COPPER).define('d', Tags.Items.GEMS_DIAMOND)
				.pattern(" s ").pattern("cdc").pattern(" s ").unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD)).save(consumer);

		ShapedRecipeBuilder.shaped(ECItems.FIRE_HOLDER).define('g', Tags.Items.INGOTS_GOLD).define('e', ECBlocks.EXTRACTOR).define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON).define('c', ECItems.FIRE_CRYSTAL).pattern("geg").pattern("iti").pattern("gcg").unlockedBy("has_firecrystal", has(ECItems.FIRE_CRYSTAL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.WATER_HOLDER).define('g', Tags.Items.INGOTS_GOLD).define('e', ECBlocks.EXTRACTOR).define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON).define('c', ECItems.WATER_CRYSTAL).pattern("geg").pattern("iti").pattern("gcg")
				.unlockedBy("has_watercrystal", has(ECItems.WATER_CRYSTAL)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.EARTH_HOLDER).define('g', Tags.Items.INGOTS_GOLD).define('e', ECBlocks.EXTRACTOR).define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON).define('c', ECItems.EARTH_CRYSTAL).pattern("geg").pattern("iti").pattern("gcg")
				.unlockedBy("has_earthcrystal", has(ECItems.EARTH_CRYSTAL)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.AIR_HOLDER).define('g', Tags.Items.INGOTS_GOLD).define('e', ECBlocks.EXTRACTOR).define('t', ECBlocks.SMALL_CONTAINER.get())
				.define('i', ECTags.Items.INGOTS_DRENCHED_IRON).define('c', ECItems.AIR_CRYSTAL).pattern("geg").pattern("iti").pattern("gcg").unlockedBy("has_aircrystal", has(ECItems.AIR_CRYSTAL))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.PURE_HOLDER_CORE).define('i', ECTags.Items.INGOTS_FIREITE).define('c', ECItems.PURE_CRYSTAL).pattern(" i ").pattern("ici").pattern(" i ")
				.unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.FOCUS).define('d', Tags.Items.GEMS_DIAMOND).define('c', ECItems.CONTAINED_CRYSTAL).define('s', ECItems.HARDENED_HANDLE)
				.define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).pattern(" ic").pattern(" si").pattern("d  ").unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.SPELL_BOOK).define('c', ECItems.PURE_CRYSTAL).define('s', ECItems.AIR_SILK).define('l', Tags.Items.LEATHER).define('p', ECItems.SCROLL_PAPER).pattern("slp")
				.pattern("clp").pattern("slp").unlockedBy(HAS_PURECRYSTAL, has(ECItems.PURE_CRYSTAL)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.STAFF).define('s', ECTags.Items.STAFF_CRAFT_SWORD).define('f', ECItems.FOCUS).define('h', ECItems.HARDENED_HANDLE).define('i', ECTags.Items.INGOTS_FIREITE)
				.pattern(" if").pattern("ihi").pattern("si ").unlockedBy(HAS_FIREITE_INGOT, has(ECTags.Items.INGOTS_FIREITE)).save(mapToStaff(consumer));

		ShapedRecipeBuilder.shaped(ECBlocks.BURNT_GLASS_PANE, 16).define('#', ECBlocks.BURNT_GLASS).pattern("###").pattern("###").unlockedBy("has_burnt_glass", has(ECBlocks.BURNT_GLASS))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.WHITE_ROCK_FENCE, 16).define('#', ECBlocks.WHITE_ROCK.get()).define('i', ECTags.Items.INGOTS_DRENCHED_IRON).pattern("#i#").pattern("#i#")
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get())).save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.WHITE_ROCK_BRICK.get(), 4).define('#', ECBlocks.WHITE_ROCK.get()).pattern("##").pattern("##").unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get())).save(consumer);
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ECBlocks.WHITE_ROCK.get()), ECBlocks.WHITE_ROCK_BRICK.get()).unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get())).save(consumer,
				ElementalCraft.createRL("whiterock_brick_from_whiterock_stonecutting"));
		ShapedRecipeBuilder.shaped(ECBlocks.SPRINGALINE_BLOCK).define('#', ECItems.SPRINGALINE_SHARD).pattern("##").pattern("##").unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.SPRINGALINE_GLASS, 2).define('s', ECItems.SPRINGALINE_SHARD).define('g', ECBlocks.BURNT_GLASS).pattern(" s ").pattern("sgs").pattern(" s ")
				.unlockedBy(HAS_SPRINGALINE_SHARD, has(ECItems.SPRINGALINE_SHARD)).save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.SPRINGALINE_GLASS_PANE, 16).define('#', ECBlocks.SPRINGALINE_GLASS).pattern("###").pattern("###")
				.unlockedBy("has_springaline_glass", has(ECBlocks.SPRINGALINE_GLASS)).save(consumer);

		createNuggetIngotBlock(ECItems.DRENCHED_IRON_NUGGET, ECTags.Items.NUGGETS_DRENCHED_IRON, ECItems.DRENCHED_IRON_INGOT, ECTags.Items.INGOTS_DRENCHED_IRON, ECItems.DRENCHED_IRON_BLOCK,
				ECTags.Items.STORAGE_BLOCKS_DRENCHED_IRON, consumer);
		createNuggetIngotBlock(ECItems.SWIFT_ALLOY_NUGGET, ECTags.Items.NUGGETS_SWIFT_ALLOY, ECItems.SWIFT_ALLOY_INGOT, ECTags.Items.INGOTS_SWIFT_ALLOY, ECItems.SWIFT_ALLOY_BLOCK,
				ECTags.Items.STORAGE_BLOCKS_SWIFT_ALLOY, consumer);
		createNuggetIngotBlock(ECItems.FIREITE_NUGGET, ECTags.Items.NUGGETS_FIREITE, ECItems.FIREITE_INGOT, ECTags.Items.INGOTS_FIREITE, ECItems.FIREITE_BLOCK, ECTags.Items.STORAGE_BLOCKS_FIREITE,
				consumer);

		createStorageBlock(ECItems.INERT_CRYSTAL, ECBlocks.INERT_CRYSTAL_BLOCK, consumer);
		createStorageBlock(ECItems.FIRE_CRYSTAL, ECBlocks.FIRE_CRYSTAL_BLOCK, consumer);
		createStorageBlock(ECItems.WATER_CRYSTAL, ECBlocks.WATER_CRYSTAL_BLOCK, consumer);
		createStorageBlock(ECItems.EARTH_CRYSTAL, ECBlocks.EARTH_CRYSTAL_BLOCK, consumer);
		createStorageBlock(ECItems.AIR_CRYSTAL, ECBlocks.AIR_CRYSTAL_BLOCK, consumer);

		ShapedRecipeBuilder.shaped(ECItems.POWERFUL_FIRE_SHARD).define('#', ECItems.FIRE_SHARD).pattern("###").pattern("###").pattern("###").unlockedBy("has_fire_shard", has(ECItems.FIRE_SHARD))
				.save(consumer, ElementalCraft.createRL("powerful_fire_shard_from_fire_shards"));
		ShapelessRecipeBuilder.shapeless(ECItems.FIRE_SHARD, 9).requires(ECItems.POWERFUL_FIRE_SHARD).unlockedBy("has_powerful_fire_shard", has(ECItems.POWERFUL_FIRE_SHARD)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.POWERFUL_WATER_SHARD).define('#', ECItems.WATER_SHARD).pattern("###").pattern("###").pattern("###").unlockedBy("has_water_shard", has(ECItems.WATER_SHARD))
				.save(consumer, ElementalCraft.createRL("powerful_water_shard_from_water_shards"));
		ShapelessRecipeBuilder.shapeless(ECItems.WATER_SHARD, 9).requires(ECItems.POWERFUL_WATER_SHARD).unlockedBy("has_powerful_water_shard", has(ECItems.POWERFUL_WATER_SHARD)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.POWERFUL_EARTH_SHARD).define('#', ECItems.EARTH_SHARD).pattern("###").pattern("###").pattern("###").unlockedBy("has_earth_shard", has(ECItems.EARTH_SHARD))
				.save(consumer, ElementalCraft.createRL("powerful_earth_shard_from_earth_shards"));
		ShapelessRecipeBuilder.shapeless(ECItems.EARTH_SHARD, 9).requires(ECItems.POWERFUL_EARTH_SHARD).unlockedBy("has_powerful_earth_shard", has(ECItems.POWERFUL_EARTH_SHARD)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.POWERFUL_AIR_SHARD).define('#', ECItems.AIR_SHARD).pattern("###").pattern("###").pattern("###").unlockedBy("has_air_shard", has(ECItems.AIR_SHARD))
				.save(consumer, ElementalCraft.createRL("powerful_air_shard_from_air_shards"));
		ShapelessRecipeBuilder.shapeless(ECItems.AIR_SHARD, 9).requires(ECItems.POWERFUL_AIR_SHARD).unlockedBy("has_powerful_air_shard", has(ECItems.POWERFUL_AIR_SHARD)).save(consumer);

		ShapedRecipeBuilder.shaped(ECBlocks.SMALL_CONTAINER.get()).define('g', Tags.Items.GLASS).define('p', ECBlocks.PIPE_IMPAIRED).pattern(" p ").pattern("pgp").pattern(" p ")
				.unlockedBy(HAS_CONTAINED_CRYSTAL, has(ECItems.CONTAINED_CRYSTAL)).save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.CONTAINER).define('i', ECTags.Items.INGOTS_DRENCHED_IRON).define('g', ECBlocks.BURNT_GLASS).define('p', ECBlocks.PIPE).pattern("ici").pattern("pgp")
				.pattern("www").save(consumer);
		prepareInstrumentRecipe(ECBlocks.EXTRACTOR).define('i', Tags.Items.INGOTS_IRON).pattern(" c ").pattern(" i ").pattern("ici").save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.EXTRACTOR_IMPROVED, ECItems.PURE_CRYSTAL).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).define('e', ECBlocks.EXTRACTOR).pattern(" e ").pattern("eie")
				.pattern("wcw").save(consumer);
		prepareInstrumentRecipe(ECBlocks.EVAPORATOR).define('i', Tags.Items.INGOTS_IRON).define('g', Tags.Items.GLASS).pattern("igi").pattern("igi").pattern("ici").save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.SOLAR_SYNTHESIZER).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).define('d', ECTags.Items.INGOTS_DRENCHED_IRON).define('h', ECItems.HARDENED_HANDLE)
				.define('p', ECItems.SOLAR_PRISM).pattern("dhd").pattern("ipi").pattern("wcw").save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.DIFFUSER).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).define('d', ECTags.Items.INGOTS_DRENCHED_IRON).pattern(" c ").pattern("did").pattern("wcw")
				.save(consumer);
		prepareInstrumentRecipe(ECBlocks.INFUSER).define('i', Tags.Items.INGOTS_IRON).define('n', Tags.Items.NUGGETS_IRON).pattern("n n").pattern("ici").save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.BINDER).define('i', ECTags.Items.INGOTS_DRENCHED_IRON).pattern("i i").pattern("wcw").save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.BINDER_IMPROVED, ECItems.PURE_CRYSTAL).define('f', ECTags.Items.INGOTS_FIREITE).define('d', Tags.Items.GEMS_DIAMOND).define('b', ECItems.BINDER)
				.define('i', ECItems.INFUSER).pattern("did").pattern("fbf").pattern("wcw").save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.CRYSTALLIZER, ECItems.STRONGLY_CONTAINED_CRYSTAL).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).pattern("iwi").pattern("i i").pattern("wcw").save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.AIR_MILL, ECItems.AIR_CRYSTAL).define('i', ECTags.Items.INGOTS_DRENCHED_IRON).define('p', ItemTags.CARPETS).define('g', Items.GRINDSTONE)
				.pattern("pip").pattern("igi").pattern("wcw").save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.INSCRIBER, ECItems.STRONGLY_CONTAINED_CRYSTAL).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).define('d', Tags.Items.GEMS_DIAMOND).pattern(" wi").pattern("wdi").pattern("wcw")
				.save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.PURE_INFUSER, ECItems.STRONGLY_CONTAINED_CRYSTAL).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).define('n', ECBlocks.INFUSER).pattern("wnw").pattern("ici").pattern("www").save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.FIRE_FURNACE, ECItems.FIRE_CRYSTAL).define('i', ECTags.Items.INGOTS_DRENCHED_IRON).define('f', Blocks.FURNACE).pattern("www").pattern("wfw")
				.pattern("ici").save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.FIRE_BLAST_FURNACE, ECItems.FIRE_CRYSTAL).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).define('F', Blocks.BLAST_FURNACE)
				.define('g', ECBlocks.BURNT_GLASS).pattern("www").pattern("gFg").pattern("ici").save(consumer);
		prepareWhiterockInstrumentRecipe(ECBlocks.PURIFIER, ECItems.PURE_CRYSTAL).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).define('e', ECTags.Items.FINE_EARTH_GEMS)
				.define('g', Tags.Items.INGOTS_GOLD).pattern("gig").pattern("wew").pattern("ici").save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.RETRIEVER).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).define('h', Blocks.HOPPER).define('d', Blocks.DISPENSER).define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY)).pattern("iw ").pattern("hdi").pattern("iw ").save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.SORTER).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).define('h', Blocks.HOPPER).define('d', Blocks.DISPENSER)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY)).pattern("ii ").pattern("hdi").pattern("ii ").save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.SPELL_DESK).define('i', ECTags.Items.INGOTS_DRENCHED_IRON).define('l', Blocks.LECTERN).define('w', ECBlocks.WHITE_ROCK.get())
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get())).pattern("wlw").pattern(" i ").pattern(" w ").save(consumer);

		ShapedRecipeBuilder.shaped(ECItems.ACCELERATION_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('i', Items.CLOCK).define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL).define('r', Tags.Items.DUSTS_REDSTONE).pattern("rir").pattern("wCw").pattern(" c ")
				.unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.RANGE_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('g', Tags.Items.DUSTS_GLOWSTONE).define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.EARTH_CRYSTAL).pattern("ggg").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.CAPACITY_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('g', ECBlocks.SPRINGALINE_GLASS).define('b', Items.BUCKET).define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL).pattern("gbg").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.EFFICIENCY_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('g', Tags.Items.INGOTS_GOLD).define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get()).define('c', ECItems.FIRE_CRYSTAL).pattern("gdg").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.STRENGTH_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('g', Tags.Items.DUSTS_GLOWSTONE).define('r', Tags.Items.RODS_BLAZE)
				.define('w', ECBlocks.WHITE_ROCK.get()).define('c', ECItems.FIRE_CRYSTAL).pattern("grg").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.OPTIMIZATION_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('f', ECTags.Items.INGOTS_FIREITE).define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get()).define('c', ECItems.PURE_CRYSTAL).pattern("dfd").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.FORTUNE_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('l', Tags.Items.GEMS_LAPIS).define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL).pattern("lll").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.SILK_TOUCH_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('s', ECItems.AIR_SILK).define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL).pattern("sss").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.PLANTING_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('s', Tags.Items.SEEDS).define('h', Items.DIAMOND_HOE)
				.define('w', ECBlocks.WHITE_ROCK.get()).define('c', ECItems.EARTH_CRYSTAL).pattern("shs").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.BONELESS_GROWTH_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('b', Items.BONE_BLOCK).define('d', Tags.Items.GEMS_DIAMOND)
				.define('w', ECBlocks.WHITE_ROCK.get()).define('c', ECItems.PURE_CRYSTAL).pattern("bdb").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.PICKUP_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('e', Items.ENDER_EYE).define('h', Items.HOPPER).define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.PURE_CRYSTAL).pattern("ehe").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.NECTAR_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('h', Items.HONEY_BLOCK).define('s', Items.SUGAR).define('w', ECBlocks.WHITE_ROCK.get())
				.define('c', ECItems.WATER_CRYSTAL).pattern("shs").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE)).save(consumer);
		ConditionalRecipe.builder().addCondition(new ModLoadedCondition("botania")).addRecipe(ShapedRecipeBuilder.shaped(ECItems.MYSTICAL_GROVE_SHRINE_UPGRADE)
						.define('C', ECItems.SHRINE_UPGRADE_CORE).define('p', ECTags.Items.BOTANIA_PETALS).define('m', ECTags.Items.INGOTS_MANASTEEL).define('w', ECTags.Items.BOTANIA_LIVINGROCK)
						.define('c', ECItems.WATER_CRYSTAL).pattern("pmp").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))::save)
		        .build(consumer, ECItems.MYSTICAL_GROVE_SHRINE_UPGRADE.getRegistryName());
		ShapedRecipeBuilder.shaped(ECItems.STEM_POLLINATION_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('p', Items.PUMPKIN).define('b', Items.BONE_MEAL)
				.define('w', ECBlocks.WHITE_ROCK.get()).define('c', ECItems.EARTH_CRYSTAL).pattern("bpb").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.PROTECTION_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('s', Items.SHIELD).define('i', Tags.Items.INGOTS_IRON)
				.define('w', ECBlocks.WHITE_ROCK.get()).define('c', ECItems.EARTH_CRYSTAL).pattern("isi").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.FILLING_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('b', Items.BUCKET).define('i', Tags.Items.INGOTS_IRON)
				.define('w', ECBlocks.WHITE_ROCK.get()).define('c', ECItems.WATER_CRYSTAL).pattern("ibi").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.SPRINGALINE_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('p', Items.PRISMARINE_CRYSTALS).define('s', ECBlocks.SPRINGALINE_BLOCK)
				.define('w', ECBlocks.WHITE_ROCK.get()).define('c', ECItems.PURE_CRYSTAL).pattern("sps").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		ShapedRecipeBuilder.shaped(ECBlocks.CRYSTAL_HARVEST_SHRINE_UPGRADE).define('C', ECItems.SHRINE_UPGRADE_CORE).define('p', Items.DIAMOND_PICKAXE).define('g', ECBlocks.SPRINGALINE_GLASS)
				.define('w', ECBlocks.WHITE_ROCK.get()).define('c', ECItems.EARTH_CRYSTAL).pattern("gpg").pattern("wCw").pattern(" c ").unlockedBy(HAS_SHRINE_UPGRADE_CORE, has(ECItems.SHRINE_UPGRADE_CORE))
				.save(consumer);
		
		prepareInstrumentRecipe(ECBlocks.PIPE_IMPAIRED, ECItems.CONTAINED_CRYSTAL, 4).define('i', Tags.Items.INGOTS_IRON).pattern("ici").save(consumer);
		prepareInstrumentRecipe(ECBlocks.PIPE, ECItems.CONTAINED_CRYSTAL, 4).define('i', ECTags.Items.INGOTS_DRENCHED_IRON).pattern("ici").save(consumer);
		prepareInstrumentRecipe(ECBlocks.PIPE_IMPROVED, ECItems.CONTAINED_CRYSTAL, 4).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).pattern("ici").save(consumer);
		ShapelessRecipeBuilder.shapeless(ECBlocks.PIPE).requires(ECBlocks.PIPE_IMPAIRED).requires(Ingredient.of(ECTags.Items.NUGGETS_DRENCHED_IRON), 5)
				.unlockedBy(HAS_DRENCHED_IRON_NUGGET, has(ECTags.Items.NUGGETS_DRENCHED_IRON)).save(consumer, ElementalCraft.createRL("elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapeless(ECBlocks.PIPE, 4).requires(ECBlocks.PIPE_IMPAIRED, 4).requires(Ingredient.of(ECTags.Items.INGOTS_DRENCHED_IRON), 2)
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON)).save(consumer, ElementalCraft.createRL("elementpipe_from_impaired_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapeless(ECBlocks.PIPE_IMPROVED).requires(ECBlocks.PIPE).requires(Ingredient.of(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.unlockedBy(HAS_SWIFT_ALLOY_NUGGET, has(ECTags.Items.NUGGETS_SWIFT_ALLOY)).save(consumer, ElementalCraft.createRL("improved_elementpipe_from_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapeless(ECBlocks.PIPE_IMPROVED, 4).requires(ECBlocks.PIPE, 4).requires(Ingredient.of(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY)).save(consumer, ElementalCraft.createRL("improved_elementpipe_from_elementpipe_and_ingot"));
		ShapelessRecipeBuilder.shapeless(ECBlocks.PIPE_IMPROVED).requires(ECBlocks.PIPE_IMPAIRED).requires(Ingredient.of(ECTags.Items.NUGGETS_SWIFT_ALLOY), 5)
				.unlockedBy(HAS_SWIFT_ALLOY_NUGGET, has(ECTags.Items.NUGGETS_SWIFT_ALLOY)).save(consumer, ElementalCraft.createRL("improved_elementpipe_from_impaired_elementpipe_and_nugget"));
		ShapelessRecipeBuilder.shapeless(ECBlocks.PIPE_IMPROVED, 4).requires(ECBlocks.PIPE_IMPAIRED, 4).requires(Ingredient.of(ECTags.Items.INGOTS_SWIFT_ALLOY), 2)
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY)).save(consumer, ElementalCraft.createRL("improved_elementpipe_from_impaired_elementpipe_and_ingot"));
		ShapedRecipeBuilder.shaped(ECItems.COVER_FRAM, 8).define('i', ECTags.Items.INGOTS_DRENCHED_IRON).pattern("iii").pattern("i i").pattern("iii")
				.unlockedBy(HAS_DRENCHED_IRON_INGOT, has(ECTags.Items.INGOTS_DRENCHED_IRON)).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.PIPE_PRIORITY).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY).define('f', ECItems.COVER_FRAM).pattern(" i ").pattern("ifi").pattern(" i ")
				.unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY)).save(consumer);

		ShapedRecipeBuilder.shaped(ECItems.MINOR_RUNE_SLATE, 4).pattern("www").pattern("wiw").pattern("www").define('w', ECBlocks.WHITE_ROCK.get()).define('i', ECTags.Items.INGOTS_DRENCHED_IRON)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get())).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.RUNE_SLATE, 4).pattern("www").pattern("wiw").pattern("www").define('w', ECBlocks.WHITE_ROCK.get()).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get())).save(consumer);
		ShapedRecipeBuilder.shaped(ECItems.MAJOR_RUNE_SLATE, 4).pattern("www").pattern("wiw").pattern("www").define('w', ECBlocks.WHITE_ROCK.get()).define('i', ECTags.Items.INGOTS_FIREITE)
				.unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get())).save(consumer);

		ShapedRecipeBuilder.shaped(ECItems.UNSET_JEWEL).pattern("sis").pattern("idi").pattern("sis").define('s', ECItems.SPRINGALINE_SHARD).define('i', ECTags.Items.INGOTS_SWIFT_ALLOY)
				.define('d', Tags.Items.GEMS_DIAMOND).unlockedBy(HAS_SWIFT_ALLOY_INGOT, has(ECTags.Items.INGOTS_SWIFT_ALLOY)).save(consumer);

		createJewelRecipe(Jewels.SALMON, b -> b.pattern(" w ").pattern("cUc").pattern(" a ").define('w', ECTags.Items.FINE_WATER_GEMS).define('a', ECTags.Items.CRUDE_AIR_GEMS).define('c', ECItems.WATER_CRYSTAL), consumer);
		createJewelRecipe(Jewels.DOLPHIN, b -> b.pattern(" w ").pattern("cUc").pattern(" w ").define('w', ECTags.Items.CRUDE_WATER_GEMS).define('c', ECItems.WATER_CRYSTAL), consumer);
		createJewelRecipe(Jewels.LEOPARD, b -> b.pattern(" a ").pattern("cUc").pattern(" e ").define('a', ECTags.Items.FINE_AIR_GEMS).define('e', ECTags.Items.FINE_EARTH_GEMS).define('c', ECItems.AIR_CRYSTAL), consumer);
		createJewelRecipe(Jewels.PHOENIX, b -> b.pattern("fgf").pattern("bUb").pattern("fpf").define('g', ECTags.Items.PRISTINE_FIRE_GEMS).define('b', Tags.Items.RODS_BLAZE).define('f', Tags.Items.FEATHERS).define('p', ECItems.PURE_CRYSTAL), consumer);
		createJewelRecipe(Jewels.TORTOISE, b -> b.pattern("ses").pattern("gUg").pattern("ses").define('g', Tags.Items.GRAVEL).define('e', ECTags.Items.CRUDE_EARTH_GEMS).define('s', Items.SCUTE), consumer);
		createJewelRecipe(Jewels.DEMIGOD, b -> b.pattern("tat").pattern("cUc").pattern("tet").define('e', ECTags.Items.PRISTINE_EARTH_GEMS).define('a', ECTags.Items.PRISTINE_AIR_GEMS).define('t', Items.TOTEM_OF_UNDYING).define('c', ECItems.PURE_CRYSTAL), consumer);
		createJewelRecipe(Jewels.MOLE, b -> b.pattern(" e ").pattern("sUa").pattern(" p ").define('e', ECTags.Items.PRISTINE_EARTH_GEMS).define('a', Items.DIAMOND_PICKAXE).define('s', Items.DIAMOND_SHOVEL).define('p', ECItems.PURE_CRYSTAL), consumer);
		createJewelRecipe(Jewels.TIGER, b -> b.pattern("sas").pattern("cUc").pattern("sfs").define('a', ECTags.Items.PRISTINE_AIR_GEMS).define('c', ECItems.AIR_CRYSTAL).define('f', ECTags.Items.FINE_AIR_GEMS).define('s', Items.SUGAR), consumer);
		createJewelRecipe(Jewels.BEAR, b -> b.pattern("heh").pattern("cUc").pattern("hfh").define('e', ECTags.Items.PRISTINE_EARTH_GEMS).define('c', ECItems.EARTH_CRYSTAL).define('f', ECTags.Items.FINE_EARTH_GEMS).define('h', Items.HONEY_BOTTLE), consumer);
		createJewelRecipe(Jewels.VIPER, b -> b.pattern(" w ").pattern("sUs").pattern(" e ").define('e', ECTags.Items.FINE_EARTH_GEMS).define('w', ECTags.Items.FINE_WATER_GEMS).define('s', Items.SPIDER_EYE), consumer);
		createJewelRecipe(Jewels.HAWK, b -> b.pattern("gag").pattern("cUc").pattern("gag").define('a', ECTags.Items.FINE_AIR_GEMS).define('g', Tags.Items.DUSTS_GLOWSTONE).define('c', ECItems.AIR_CRYSTAL), consumer);
		createJewelRecipe(Jewels.KIRIN, b -> b.pattern("sfs").pattern("cUc").pattern("sas").define('f', ECTags.Items.PRISTINE_FIRE_GEMS).define('a', ECTags.Items.PRISTINE_AIR_GEMS).define('s', ECItems.SPRINGALINE_SHARD).define('c', ECItems.PURE_CRYSTAL), consumer);
		createJewelRecipe(Jewels.ARCTIC_HARES, b -> b.pattern(" w ").pattern("fUf").pattern(" a ").define('w', ECTags.Items.FINE_WATER_GEMS).define('a', ECTags.Items.CRUDE_AIR_GEMS).define('f', Items.RABBIT_FOOT), consumer);
		createJewelRecipe(Jewels.STRIDER, b -> b.pattern(" f ").pattern("bUb").pattern(" f ").define('f', ECTags.Items.FINE_FIRE_GEMS).define('b', Items.LAVA_BUCKET), consumer);
		createJewelRecipe(Jewels.WATER_STRIDER, b -> b.pattern(" w ").pattern("bUb").pattern(" w ").define('w', ECTags.Items.FINE_WATER_GEMS).define('b', Items.WATER_BUCKET), consumer);
		createJewelRecipe(Jewels.BASILISK, b -> b.pattern("fwf").pattern("cUc").pattern("sws").define('w', ECTags.Items.PRISTINE_WATER_GEMS).define('c', ECItems.PURE_CRYSTAL).define('f', Items.FERMENTED_SPIDER_EYE).define('s', Items.SCUTE), consumer);

		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL), ECItems.FIRE_CRYSTAL, ElementType.FIRE).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL), ECItems.WATER_CRYSTAL, ElementType.WATER).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL), ECItems.AIR_CRYSTAL, ElementType.AIR).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(ECItems.INERT_CRYSTAL), ECItems.EARTH_CRYSTAL, ElementType.EARTH).build(consumer);

		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Items.STONE), ECItems.WHITE_ROCK, ElementType.EARTH).withElementAmount(500).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Tags.Items.INGOTS_IRON), ECItems.DRENCHED_IRON_INGOT, ElementType.WATER).withElementAmount(500).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Tags.Items.GLASS), ECBlocks.BURNT_GLASS, ElementType.FIRE).withElementAmount(500).build(consumer);
		InfusionRecipeBuilder.infusionRecipe(Ingredient.of(Tags.Items.STRING), ECItems.AIR_SILK, ElementType.AIR).withElementAmount(500).build(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.FIRE_PYLON, ElementType.FIRE).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.FIRE_CRYSTAL).addIngredient(Items.LAVA_BUCKET)
				.addIngredient(Tags.Items.INGOTS_GOLD).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.VACUUM_SHRINE, ElementType.AIR).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.AIR_CRYSTAL).addIngredient(Items.ENDER_EYE)
				.addIngredient(Items.HOPPER).addIngredient(Tags.Items.GEMS_DIAMOND).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.GROWTH_SHRINE, ElementType.WATER).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.WATER_CRYSTAL).addIngredient(ECItems.EARTH_CRYSTAL)
				.addIngredient(Items.WHEAT_SEEDS).addIngredient(Items.BONE_MEAL).addIngredient(Tags.Items.GEMS_DIAMOND).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.LAVA_SHRINE, ElementType.FIRE).addIngredient(ECItems.FIRE_PYLON).addIngredient(ECItems.FIRE_CRYSTAL).addIngredient(ECTags.Items.PRISTINE_FIRE_GEMS)
				.addIngredient(Blocks.OBSIDIAN).addIngredient(Items.LAVA_BUCKET).addIngredient(Items.BLAZE_ROD).withElementAmount(20000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.HARVEST_SHRINE, ElementType.EARTH).addIngredient(ECItems.GROWTH_SHRINE).addIngredient(ECItems.EARTH_CRYSTAL)
				.addIngredient(ECTags.Items.CRUDE_EARTH_GEMS).addIngredient(Items.DIAMOND_HOE).addIngredient(Items.DIAMOND_AXE).addIngredient(Items.SHEARS).withElementAmount(5000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.ORE_SHRINE, ElementType.EARTH).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.EARTH_CRYSTAL)
				.addIngredient(ECTags.Items.PRISTINE_EARTH_GEMS).addIngredient(Items.DIAMOND_PICKAXE).withElementAmount(20000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.OVERLOAD_SHRINE, ElementType.AIR).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.AIR_CRYSTAL).addIngredient(ECItems.PURE_CRYSTAL)
				.addIngredient(Items.CLOCK).addIngredient(Items.ENDER_EYE).withElementAmount(20000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.SWEET_SHRINE, ElementType.WATER).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.WATER_CRYSTAL).addIngredient(ECItems.EARTH_CRYSTAL)
				.addIngredient(Items.SUGAR).addIngredient(Items.HONEY_BOTTLE).addIngredient(Items.MILK_BUCKET).withElementAmount(5000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.ENDER_LOCK_SHRINE, ElementType.WATER).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.WATER_CRYSTAL)
				.addIngredient(ECTags.Items.FINE_AIR_GEMS).addIngredient(Items.ENDER_EYE).addIngredient(Items.DRAGON_BREATH).addIngredient(Items.OBSIDIAN).withElementAmount(5000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.BREEDING_SHRINE, ElementType.EARTH).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.EARTH_CRYSTAL)
				.addIngredient(ECTags.Items.CRUDE_WATER_GEMS).addIngredient(Tags.Items.CROPS).addIngredient(Tags.Items.LEATHER).addIngredient(Items.MILK_BUCKET).addIngredient(Tags.Items.GEMS_DIAMOND)
				.withElementAmount(5000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.GROVE_SHRINE, ElementType.WATER).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.WATER_CRYSTAL).addIngredient(ECTags.Items.CRUDE_EARTH_GEMS)
				.addIngredient(ItemTags.FLOWERS).addIngredient(Tags.Items.SEEDS).addIngredient(Tags.Items.CROPS).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPRING_SHRINE, ElementType.WATER).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.WATER_CRYSTAL).addIngredient(Items.BUCKET)
				.addIngredient(ItemTags.FISHES).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.BUDDING_SHRINE, ElementType.EARTH).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.EARTH_CRYSTAL).addIngredient(ECTags.Items.CRUDE_WATER_GEMS)
				.addIngredient(Items.AMETHYST_BLOCK).addIngredient(ECItems.SPRINGALINE_SHARD).addIngredient(Tags.Items.GEMS_DIAMOND).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPAWNING_SHRINE, ElementType.FIRE).addIngredient(ECItems.SHRINE_BASE).addIngredient(ECItems.FIRE_CRYSTAL).addIngredient(ECTags.Items.FINE_EARTH_GEMS)
				.addIngredient(Items.ROTTEN_FLESH).addIngredient(Items.SPIDER_EYE).addIngredient(Items.ENDER_EYE).addIngredient(Items.DIAMOND).build(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.FIRE_PEDESTAL, ElementType.FIRE).addIngredient(ECItems.INFUSER).addIngredient(ECTags.Items.FINE_FIRE_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY).addIngredient(ECItems.WHITE_ROCK).addIngredient(ECItems.WHITE_ROCK).withElementAmount(30000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.WATER_PEDESTAL, ElementType.WATER).addIngredient(ECItems.INFUSER).addIngredient(ECTags.Items.FINE_WATER_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY).addIngredient(ECItems.WHITE_ROCK).addIngredient(ECItems.WHITE_ROCK).withElementAmount(30000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.EARTH_PEDESTAL, ElementType.EARTH).addIngredient(ECItems.INFUSER).addIngredient(ECTags.Items.FINE_EARTH_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY).addIngredient(ECItems.WHITE_ROCK).addIngredient(ECItems.WHITE_ROCK).withElementAmount(30000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.AIR_PEDESTAL, ElementType.AIR).addIngredient(ECItems.INFUSER).addIngredient(ECTags.Items.FINE_AIR_GEMS)
				.addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY).addIngredient(ECItems.WHITE_ROCK).addIngredient(ECItems.WHITE_ROCK).withElementAmount(30000).build(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.SWIFT_ALLOY_INGOT, ElementType.AIR).addIngredient(Tags.Items.INGOTS_GOLD).addIngredient(ECTags.Items.INGOTS_DRENCHED_IRON)
				.addIngredient(Tags.Items.INGOTS_COPPER).addIngredient(Tags.Items.DUSTS_REDSTONE).addIngredient(ECItems.AIR_CRYSTAL).withElementAmount(1250).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.FIREITE_INGOT, ElementType.FIRE).addIngredient(Tags.Items.INGOTS_NETHERITE).addIngredient(ECTags.Items.INGOTS_SWIFT_ALLOY)
				.addIngredient(ECItems.SPRINGALINE_SHARD).addIngredient(ECItems.PURE_CRYSTAL).withElementAmount(30000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.HARDENED_HANDLE, ElementType.EARTH).addIngredient(Items.STICK).addIngredient(ECBlocks.WHITE_ROCK.get()).addIngredient(ECItems.AIR_SILK)
				.addIngredient(ECItems.EARTH_CRYSTAL).withElementAmount(1250).build(consumer);
		
		BindingRecipeBuilder.bindingRecipe(ECItems.SPRINGALINE_SHARD, ElementType.WATER).addIngredient(Items.AMETHYST_SHARD).addIngredient(Tags.Items.GEMS_QUARTZ).addIngredient(ECItems.WATER_CRYSTAL)
				.build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECBlocks.SPRINGALINE_CLUSTER, ElementType.WATER).addIngredient(Items.AMETHYST_BLOCK).addIngredient(Tags.Items.STORAGE_BLOCKS_QUARTZ)
				.addIngredient(ECItems.SPRINGALINE_SHARD).addIngredient(ECItems.WATER_CRYSTAL).build(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.FIRE_LENSE, ElementType.FIRE).addIngredient(ECItems.SPRINGALINE_SHARD).addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE)
				.addIngredient(Tags.Items.INGOTS_COPPER).addIngredient(ECItems.FIRE_CRYSTAL).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.WATER_LENSE, ElementType.WATER).addIngredient(ECItems.SPRINGALINE_SHARD).addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE)
				.addIngredient(Tags.Items.INGOTS_COPPER).addIngredient(ECItems.WATER_CRYSTAL).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.EARTH_LENSE, ElementType.EARTH).addIngredient(ECItems.SPRINGALINE_SHARD).addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE)
				.addIngredient(Tags.Items.INGOTS_COPPER).addIngredient(ECItems.EARTH_CRYSTAL).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.AIR_LENSE, ElementType.AIR).addIngredient(ECItems.SPRINGALINE_SHARD).addIngredient(ECBlocks.SPRINGALINE_GLASS_PANE)
				.addIngredient(Tags.Items.INGOTS_COPPER).addIngredient(ECItems.AIR_CRYSTAL).build(consumer);

		BindingRecipeBuilder.bindingRecipe(ECItems.FIRE_RESERVOIR, ElementType.FIRE).addIngredient(ECBlocks.CONTAINER).addIngredient(ECBlocks.SPRINGALINE_GLASS).addIngredient(ECItems.PURE_CRYSTAL)
				.addIngredient(ECTags.Items.PRISTINE_FIRE_GEMS).withElementAmount(10000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.WATER_RESERVOIR, ElementType.WATER).addIngredient(ECBlocks.CONTAINER).addIngredient(ECBlocks.SPRINGALINE_GLASS).addIngredient(ECItems.PURE_CRYSTAL)
				.addIngredient(ECTags.Items.PRISTINE_WATER_GEMS).withElementAmount(10000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.EARTH_RESERVOIR, ElementType.EARTH).addIngredient(ECBlocks.CONTAINER).addIngredient(ECBlocks.SPRINGALINE_GLASS).addIngredient(ECItems.PURE_CRYSTAL)
				.addIngredient(ECTags.Items.PRISTINE_EARTH_GEMS).withElementAmount(10000).build(consumer);
		BindingRecipeBuilder.bindingRecipe(ECItems.AIR_RESERVOIR, ElementType.AIR).addIngredient(ECBlocks.CONTAINER).addIngredient(ECBlocks.SPRINGALINE_GLASS).addIngredient(ECItems.PURE_CRYSTAL)
				.addIngredient(ECTags.Items.PRISTINE_AIR_GEMS).withElementAmount(10000).build(consumer);

		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.FIRE).setGem(ECTags.Items.INPUT_FIRE_GEMS).setCrystal(ECItems.FIRE_CRYSTAL).setShard(ECTags.Items.FIRE_SHARDS)
				.addOutput(ECItems.CRUDE_FIRE_GEM, 15, -0.5F).addOutput(ECItems.FINE_FIRE_GEM, 4).addOutput(ECItems.PRISTINE_FIRE_GEM, 1, 2).build(consumer, "fire_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.WATER).setGem(ECTags.Items.INPUT_WATER_GEMS).setCrystal(ECItems.WATER_CRYSTAL).setShard(ECTags.Items.WATER_SHARDS)
				.addOutput(ECItems.CRUDE_WATER_GEM, 15, -0.5F).addOutput(ECItems.FINE_WATER_GEM, 4).addOutput(ECItems.PRISTINE_WATER_GEM, 1, 2).build(consumer, "water_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.EARTH).setGem(ECTags.Items.INPUT_EARTH_GEMS).setCrystal(ECItems.EARTH_CRYSTAL).setShard(ECTags.Items.EARTH_SHARDS)
				.addOutput(ECItems.CRUDE_EARTH_GEM, 15, -0.5F).addOutput(ECItems.FINE_EARTH_GEM, 4).addOutput(ECItems.PRISTINE_EARTH_GEM, 1, 2).build(consumer, "earth_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.AIR).setGem(ECTags.Items.INPUT_AIR_GEMS).setCrystal(ECItems.AIR_CRYSTAL).setShard(ECTags.Items.AIR_SHARDS)
				.addOutput(ECItems.CRUDE_AIR_GEM, 15, -0.5F).addOutput(ECItems.FINE_AIR_GEM, 4).addOutput(ECItems.PRISTINE_AIR_GEM, 1, 2).build(consumer, "air_gem");

		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.FIRE).setGem(ECTags.Items.INPUT_FIRE_GEMS).setCrystal(ECItems.PURE_CRYSTAL).setShard(ECTags.Items.FIRE_SHARDS)
				.addOutput(ECItems.PRISTINE_FIRE_GEM, 1).build(consumer, "pristine_fire_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.WATER).setGem(ECTags.Items.INPUT_WATER_GEMS).setCrystal(ECItems.PURE_CRYSTAL).setShard(ECTags.Items.WATER_SHARDS)
				.addOutput(ECItems.PRISTINE_WATER_GEM, 1).build(consumer, "pristine_water_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.EARTH).setGem(ECTags.Items.INPUT_EARTH_GEMS).setCrystal(ECItems.PURE_CRYSTAL).setShard(ECTags.Items.EARTH_SHARDS)
				.addOutput(ECItems.PRISTINE_EARTH_GEM, 1).build(consumer, "pristine_earth_gem");
		CrystallizationRecipeBuilder.crystallizationRecipe(ElementType.AIR).setGem(ECTags.Items.INPUT_AIR_GEMS).setCrystal(ECItems.PURE_CRYSTAL).setShard(ECTags.Items.AIR_SHARDS)
				.addOutput(ECItems.PRISTINE_AIR_GEM, 1).build(consumer, "pristine_air_gem");

		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.PURE_CRYSTAL).setIngredient(Tags.Items.GEMS_DIAMOND).setIngredient(ElementType.WATER, ECItems.WATER_CRYSTAL)
				.setIngredient(ElementType.FIRE, ECItems.FIRE_CRYSTAL).setIngredient(ElementType.EARTH, ECItems.EARTH_CRYSTAL).setIngredient(ElementType.AIR, ECItems.AIR_CRYSTAL).build(consumer);
		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.PURE_ROCK).setIngredient(Items.OBSIDIAN).setIngredient(ElementType.WATER, Items.PRISMARINE)
				.setIngredient(ElementType.FIRE, ECTags.Items.INGOTS_FIREITE).setIngredient(ElementType.EARTH, ECItems.WHITE_ROCK).setIngredient(ElementType.AIR, Items.PURPUR_BLOCK).build(consumer);
		PureInfusionRecipeBuilder.pureInfusionRecipe(ECItems.PURE_HOLDER).setIngredient(ECItems.PURE_HOLDER_CORE).setIngredient(ElementType.WATER, ECItems.WATER_HOLDER)
				.setIngredient(ElementType.FIRE, ECItems.FIRE_HOLDER).setIngredient(ElementType.EARTH, ECItems.EARTH_HOLDER).setIngredient(ElementType.AIR, ECItems.AIR_HOLDER)
				.withElementAmount(100000).build(consumer);

		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("manx"), ElementType.AIR).withElementAmount(2000).setSlate(ECItems.MINOR_RUNE_SLATE)
				.addIngredient(ECTags.Items.CRUDE_AIR_GEMS).addIngredient(Items.SUGAR).addIngredient(Items.SUGAR).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("jita"), ElementType.AIR).setSlate(ECItems.RUNE_SLATE).addIngredient(ECTags.Items.FINE_AIR_GEMS)
				.addIngredient(Tags.Items.STRING).addIngredient(Tags.Items.STRING).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("tano"), ElementType.AIR).withElementAmount(10000).setSlate(ECItems.MAJOR_RUNE_SLATE)
				.addIngredient(ECTags.Items.PRISTINE_AIR_GEMS).addIngredient(ECItems.AIR_SILK).addIngredient(ECItems.AIR_SILK).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("wii"), ElementType.FIRE).withElementAmount(2000).setSlate(ECItems.MINOR_RUNE_SLATE)
				.addIngredient(ECTags.Items.CRUDE_FIRE_GEMS).addIngredient(ItemTags.COALS).addIngredient(ItemTags.COALS).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("fus"), ElementType.FIRE).setSlate(ECItems.RUNE_SLATE).addIngredient(ECTags.Items.FINE_FIRE_GEMS)
				.addIngredient(Items.BLAZE_ROD).addIngredient(Items.BLAZE_ROD).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("zod"), ElementType.FIRE).withElementAmount(10000).setSlate(ECItems.MAJOR_RUNE_SLATE)
				.addIngredient(ECTags.Items.PRISTINE_FIRE_GEMS).addIngredient(Tags.Items.STORAGE_BLOCKS_COAL).addIngredient(Tags.Items.STORAGE_BLOCKS_COAL).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("claptrap"), ElementType.WATER).withElementAmount(2000).setSlate(ECItems.MINOR_RUNE_SLATE)
				.addIngredient(ECTags.Items.CRUDE_WATER_GEMS).addIngredient(Tags.Items.GEMS_LAPIS).addIngredient(Tags.Items.GEMS_LAPIS).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("bombadil"), ElementType.WATER).setSlate(ECItems.RUNE_SLATE).addIngredient(ECTags.Items.FINE_WATER_GEMS)
				.addIngredient(Tags.Items.STORAGE_BLOCKS_LAPIS).addIngredient(Tags.Items.STORAGE_BLOCKS_LAPIS).build(consumer);
		InscriptionRecipeBuilder.inscriptionRecipe(ElementalCraft.createRL("tzeentch"), ElementType.WATER).withElementAmount(10000).setSlate(ECItems.MAJOR_RUNE_SLATE)
				.addIngredient(ECTags.Items.PRISTINE_WATER_GEMS).addIngredient(Tags.Items.GEMS_EMERALD).addIngredient(Tags.Items.GEMS_EMERALD).build(consumer);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ECTags.Items.ORES_INERT_CRYSTAL), ECItems.INERT_CRYSTAL, 0.5F, 200).unlockedBy("has_crystal_ore", has(ECTags.Items.ORES_INERT_CRYSTAL)).save(consumer);
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ECTags.Items.ORES_INERT_CRYSTAL), ECItems.INERT_CRYSTAL, 0.5F, 100).unlockedBy("has_crystal_ore", has(ECTags.Items.ORES_INERT_CRYSTAL)).save(consumer, ElementalCraft.createRL("inertcrystal_from_blasting"));

		AirMillGrindingRecipeBuilder.grindingRecipe(Items.COBBLESTONE).withIngredient(Tags.Items.STONE).build(consumer);
		AirMillGrindingRecipeBuilder.grindingRecipe(Items.GRAVEL).withIngredient(Tags.Items.COBBLESTONE).build(consumer);
		AirMillGrindingRecipeBuilder.grindingRecipe(Items.SAND).withIngredient(Tags.Items.GRAVEL).build(consumer);

		ShapelessRecipeBuilder.shapeless(ECBlocks.SMALL_CONTAINER.get()).requires(ECBlocks.SMALL_CONTAINER.get()).unlockedBy("has_small_container", has(ECBlocks.SMALL_CONTAINER.get())).save(consumer,
				"small_container_emptying");
		ShapelessRecipeBuilder.shapeless(ECBlocks.CONTAINER).requires(ECBlocks.CONTAINER).unlockedBy("has_container", has(ECBlocks.CONTAINER)).save(consumer, "container_emptying");
		ShapelessRecipeBuilder.shapeless(ECBlocks.FIRE_RESERVOIR).requires(ECBlocks.FIRE_RESERVOIR).unlockedBy("has_fire_reservoir", has(ECBlocks.FIRE_RESERVOIR)).save(consumer,
				"fire_reservoir_emptying");
		ShapelessRecipeBuilder.shapeless(ECBlocks.WATER_RESERVOIR).requires(ECBlocks.WATER_RESERVOIR).unlockedBy("has_water_reservoir", has(ECBlocks.WATER_RESERVOIR)).save(consumer,
				"water_reservoir_emptying");
		ShapelessRecipeBuilder.shapeless(ECBlocks.EARTH_RESERVOIR).requires(ECBlocks.EARTH_RESERVOIR).unlockedBy("has_earth_reservoir", has(ECBlocks.EARTH_RESERVOIR)).save(consumer,
				"earth_reservoir_emptying");
		ShapelessRecipeBuilder.shapeless(ECBlocks.AIR_RESERVOIR).requires(ECBlocks.AIR_RESERVOIR).unlockedBy("has_air_reservoir", has(ECBlocks.AIR_RESERVOIR)).save(consumer, "air_reservoir_emptying");
		ShapelessRecipeBuilder.shapeless(ECBlocks.CREATIVE_CONTAINER).requires(ECBlocks.CREATIVE_CONTAINER).unlockedBy("has_creative_container", has(ECBlocks.CREATIVE_CONTAINER)).save(consumer,
				"creative_container_emptying");

		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(GavelFallSpell.NAME)).setGem(ECTags.Items.INPUT_EARTH_GEMS).setCrystal(ECItems.EARTH_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(StoneWallSpell.NAME)).setGem(ECTags.Items.INPUT_EARTH_GEMS).setCrystal(ECItems.EARTH_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(FireBallSpell.NAME)).setGem(ECTags.Items.INPUT_FIRE_GEMS).setCrystal(ECItems.FIRE_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(ItemPullSpell.NAME)).setGem(ECTags.Items.FINE_AIR_GEMS).setCrystal(ECItems.AIR_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(EnderStrikeSpell.NAME)).setGem(ECTags.Items.CRUDE_AIR_GEMS).setCrystal(ECItems.AIR_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(AnimalGrowthSpell.NAME)).setGem(ECTags.Items.CRUDE_WATER_GEMS).setCrystal(ECItems.WATER_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(TreeFallSpell.NAME)).setGem(ECTags.Items.FINE_EARTH_GEMS).setCrystal(ECItems.WATER_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(PurificationSpell.NAME)).setGem(ECTags.Items.CRUDE_WATER_GEMS).setCrystal(ECItems.WATER_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(RipeningSpell.NAME)).setGem(ECTags.Items.INPUT_WATER_GEMS).setCrystal(ECItems.WATER_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(FlameCleaveSpell.NAME)).setGem(ECTags.Items.CRUDE_FIRE_GEMS).setCrystal(ECItems.FIRE_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(InfernoSpell.NAME)).setGem(ECTags.Items.INPUT_FIRE_GEMS).setCrystal(ECItems.FIRE_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(DashSpell.NAME)).setGem(ECTags.Items.FINE_AIR_GEMS).setCrystal(ECItems.AIR_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(SilkVeinSpell.NAME)).setGem(ECTags.Items.PRISTINE_EARTH_GEMS).setCrystal(ECItems.PURE_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL(TranslocationSpell.NAME)).setGem(ECTags.Items.PRISTINE_AIR_GEMS).setCrystal(ECItems.PURE_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL("heal")).setGem(ECTags.Items.PRISTINE_WATER_GEMS).setCrystal(ECItems.PURE_CRYSTAL).build(consumer);
		SpellCraftRecipeBuilder.spellCraftRecipe(ElementalCraft.createRL("speed")).setGem(ECTags.Items.PRISTINE_AIR_GEMS).setCrystal(ECItems.PURE_CRYSTAL).build(consumer);

		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.MOB_LOOTING).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.FIRE_ASPECT).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, Enchantments.SHARPNESS).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SWORDS, ElementalCraft.createRL("attack_speed")).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.BLOCK_FORTUNE).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, ElementalCraft.createRL(AutoSmeltToolInfusionEffect.NAME)).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.UNBREAKING).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_PICKAXES, Enchantments.BLOCK_EFFICIENCY).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.BLOCK_FORTUNE).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, ElementalCraft.createRL(AutoSmeltToolInfusionEffect.NAME)).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.UNBREAKING).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_SHOVELS, Enchantments.BLOCK_EFFICIENCY).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.BLOCK_FORTUNE).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, ElementalCraft.createRL(AutoSmeltToolInfusionEffect.NAME)).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.UNBREAKING).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HOES, Enchantments.BLOCK_EFFICIENCY).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.MOB_LOOTING).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.FIRE_ASPECT).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.SHARPNESS /* TODO chopping ? */).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_AXES, Enchantments.BLOCK_EFFICIENCY).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.PUNCH_ARROWS).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.FLAMING_ARROWS).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, ElementalCraft.createRL(FastDrawToolInfusionEffect.NAME)).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOWS, Enchantments.BLOCK_EFFICIENCY).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.MULTISHOT).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.PIERCING).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.UNBREAKING).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CROSSBOWS, Enchantments.QUICK_CHARGE).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.FISHING_LUCK).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, ElementalCraft.createRL(AutoSmeltToolInfusionEffect.NAME)).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.UNBREAKING).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FISHING_RODS, Enchantments.FISHING_SPEED).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.LOYALTY).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.IMPALING).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.UNBREAKING).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_TRIDENTS, Enchantments.RIPTIDE).build(consumer);

		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.RESPIRATION).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.FIRE_PROTECTION).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.ALL_DAMAGE_PROTECTION).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_HELMETS, Enchantments.PROJECTILE_PROTECTION).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.BLAST_PROTECTION).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.FIRE_PROTECTION).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, Enchantments.ALL_DAMAGE_PROTECTION).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_CHESTPLATES, ElementalCraft.createRL(DodgeToolInfusionEffect.NAME)).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.BLAST_PROTECTION).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.FIRE_PROTECTION).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, Enchantments.ALL_DAMAGE_PROTECTION).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_LEGGINGS, ElementalCraft.createRL("movement_speed")).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.DEPTH_STRIDER).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.FIRE_PROTECTION).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.ALL_DAMAGE_PROTECTION).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_BOOTS, Enchantments.FALL_PROTECTION).build(consumer);

		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraft.createRL("fire_reduction")).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraft.createRL("water_reduction")).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraft.createRL("earth_reduction")).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_FOCUS, ElementalCraft.createRL("air_reduction")).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraft.createRL("fire_staff")).withElementAmount(5000).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraft.createRL("water_staff")).withElementAmount(5000).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraft.createRL("earth_staff")).withElementAmount(5000).build(consumer);
		ToolInfusionRecipeBuilder.toolInfusionRecipe(ECTags.Items.INFUSABLE_STAVES, ElementalCraft.createRL("air_staff")).withElementAmount(5000).build(consumer);
	}

	private boolean exists(Block block) {
		return existingFileHelper.exists(block.getRegistryName(), PackType.SERVER_DATA, ".json", "recipes");
	}

	private void registerSlabsStairsWalls(Consumer<FinishedRecipe> consumer) {
		ForgeRegistries.BLOCKS.forEach(block -> {
			if (ElementalCraftApi.MODID.equals(block.getRegistryName().getNamespace()) && !exists(block) && (block instanceof SlabBlock || block instanceof StairBlock || block instanceof WallBlock)) {
				String name = block.getRegistryName().getPath();
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
				SingleItemRecipeBuilder.stonecutting(Ingredient.of(source), block, block instanceof SlabBlock ? 2 : 1).unlockedBy("has_" + sourceName, has(source)).save(consumer,
						ElementalCraft.createRL(name + "_from_" + sourceName + "_stonecutting"));
			}
		});
	}

	private void createNuggetIngotBlock(ItemLike nugget, TagKey<Item> nuggetTag, ItemLike ingot, TagKey<Item> ingotTag, ItemLike block, TagKey<Item> blockTag, Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(ingot).define('#', nuggetTag).pattern("###").pattern("###").pattern("###").unlockedBy(buildHas(nugget), has(nuggetTag)).save(consumer,
				ElementalCraft.createRL(from(nugget, ingot)));
		ShapelessRecipeBuilder.shapeless(nugget, 9).requires(ingotTag).unlockedBy(buildHas(ingot), has(ingotTag)).save(consumer, from(ingot, nugget));
		ShapedRecipeBuilder.shaped(block).define('#', ingotTag).pattern("###").pattern("###").pattern("###").unlockedBy(buildHas(ingot), has(ingotTag)).save(consumer, from(ingot, block));
		ShapelessRecipeBuilder.shapeless(ingot, 9).requires(blockTag).unlockedBy(buildHas(block), has(blockTag)).save(consumer, from(block, ingot));
	}

	private void createStorageBlock(ItemLike item, ItemLike block, Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(block).define('#', item).pattern("###").pattern("###").pattern("###").unlockedBy(buildHas(item), has(item)).save(consumer,
				ElementalCraft.createRL(from(item, block)));
		ShapelessRecipeBuilder.shapeless(item, 9).requires(block).unlockedBy(buildHas(block), has(block)).save(consumer, from(block, item));
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(ItemLike result) {
		return prepareInstrumentRecipe(result, ECItems.CONTAINED_CRYSTAL, 1);
	}

	private ShapedRecipeBuilder prepareInstrumentRecipe(ItemLike result, ItemLike crystal, int count) {
		return ShapedRecipeBuilder.shaped(result, count).define('c', crystal).unlockedBy(buildHas(crystal), has(crystal));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(ItemLike result) {
		return prepareInstrumentRecipe(result, ECItems.CONTAINED_CRYSTAL, 1).define('w', ECBlocks.WHITE_ROCK.get()).unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()));
	}

	private ShapedRecipeBuilder prepareWhiterockInstrumentRecipe(ItemLike result, ItemLike crystal) {
		return prepareInstrumentRecipe(result, crystal, 1).define('w', ECBlocks.WHITE_ROCK.get()).unlockedBy(HAS_WHITEROCK, has(ECBlocks.WHITE_ROCK.get()));
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
				return StaffRecipe.SERIALIZER;
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

	private void createJewelRecipe(Jewel jewel, UnaryOperator<ShapedRecipeBuilder> patternBuilder, Consumer<FinishedRecipe> consumer) {
		var builder = ShapedRecipeBuilder.shaped(ECItems.JEWEL);
		var nameSuffix = "/" + jewel.getRegistryName().getPath();

		patternBuilder.apply(builder).define('U', ECItems.UNSET_JEWEL).unlockedBy("has_unset_jewel", has(ECItems.UNSET_JEWEL)).save(recipe -> consumer.accept(new FinishedRecipe() {
			@Override
			public void serializeRecipeData(@Nonnull JsonObject json) {
				recipe.serializeRecipeData(json);

				var resultJson = json.getAsJsonObject("result");
				var nbt = new JsonObject();
				var ecNbt = new JsonObject();

				ecNbt.addProperty(ECNames.JEWEL, jewel.getRegistryName().toString());
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

	private String from(ItemLike from, ItemLike to) {
		return to.asItem().getRegistryName().getPath() + "_from_" + from.asItem().getRegistryName().getPath();
	}

	private String buildHas(ItemLike item) {
		return "has_" + item.asItem().getRegistryName().getPath();
	}
}
