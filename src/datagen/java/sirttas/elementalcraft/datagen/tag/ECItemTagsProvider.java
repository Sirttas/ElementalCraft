package sirttas.elementalcraft.datagen.tag;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.spell.AbstractSpellHolderItem;
import sirttas.elementalcraft.tag.ECTags;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class ECItemTagsProvider extends ItemTagsProvider {

	public ECItemTagsProvider(DataGenerator generatorIn, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
		super(generatorIn, blockTagsProvider, ElementalCraftApi.MODID, existingFileHelper);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags() {

		copy(ECTags.Blocks.STRIPPED_OAK, ECTags.Items.STRIPPED_OAK);
		copy(ECTags.Blocks.STRIPPED_DARK_OAK, ECTags.Items.STRIPPED_DARK_OAK);
		copy(ECTags.Blocks.STRIPPED_BIRCH, ECTags.Items.STRIPPED_BIRCH);
		copy(ECTags.Blocks.STRIPPED_ACACIA, ECTags.Items.STRIPPED_ACACIA);
		copy(ECTags.Blocks.STRIPPED_JUNGLE, ECTags.Items.STRIPPED_JUNGLE);
		copy(ECTags.Blocks.STRIPPED_SPRUCE, ECTags.Items.STRIPPED_SPRUCE);
		copy(ECTags.Blocks.STRIPPED_MANGROVE, ECTags.Items.STRIPPED_MANGROVE);
		copy(ECTags.Blocks.STRIPPED_CRIMSON, ECTags.Items.STRIPPED_CRIMSON);
		copy(ECTags.Blocks.STRIPPED_WARPED, ECTags.Items.STRIPPED_WARPED);

		copy(BlockTags.SLABS, ItemTags.SLABS);
		copy(BlockTags.STAIRS, ItemTags.STAIRS);
		copy(BlockTags.WALLS, ItemTags.WALLS);
		copy(BlockTags.FENCES, ItemTags.FENCES);
		copy(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);
		copy(ECTags.Blocks.ORES_INERT_CRYSTAL, ECTags.Items.ORES_INERT_CRYSTAL);
		copy(Tags.Blocks.ORES, Tags.Items.ORES);

		copy(ECTags.Blocks.PUREROCKS, ECTags.Items.PUREROCKS);
		copy(ECTags.Blocks.PIPES, ECTags.Items.PIPES);
		copy(ECTags.Blocks.SHRINES, ECTags.Items.SHRINES);
		copy(ECTags.Blocks.SHRINE_UPGRADES, ECTags.Items.SHRINE_UPGRADES);
		copy(ECTags.Blocks.SMALL_CONTAINER_COMPATIBLES, ECTags.Items.SMALL_CONTAINER_COMPATIBLES);
		copy(ECTags.Blocks.INSTRUMENTS, ECTags.Items.INSTRUMENTS);
		copy(ECTags.Blocks.CONTAINER_TOOLS, ECTags.Items.CONTAINER_TOOLS);

		copy(ECTags.Blocks.STORAGE_BLOCKS_DRENCHED_IRON, ECTags.Items.STORAGE_BLOCKS_DRENCHED_IRON);
		copy(ECTags.Blocks.STORAGE_BLOCKS_SWIFT_ALLOY, ECTags.Items.STORAGE_BLOCKS_SWIFT_ALLOY);
		copy(ECTags.Blocks.STORAGE_BLOCKS_FIREITE, ECTags.Items.STORAGE_BLOCKS_FIREITE);
		copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);

		tag(ECTags.Items.SPELL_CAST_TOOLS).add(ECItems.FOCUS.get(), ECItems.STAFF.get());
		
		tag(ECTags.Items.INFUSABLE_FOCUS).add(ECItems.FOCUS.get());
		tag(ECTags.Items.INFUSABLE_STAVES).add(ECItems.STAFF.get());
		tag(ECTags.Items.INFUSABLE_SWORDS).addTag(Tags.Items.TOOLS_SWORDS);
		tag(ECTags.Items.INFUSABLE_PICKAXES).addTag(Tags.Items.TOOLS_PICKAXES);
		tag(ECTags.Items.INFUSABLE_SHOVELS).addTag(Tags.Items.TOOLS_SHOVELS);
		tag(ECTags.Items.INFUSABLE_HOES).addTag(Tags.Items.TOOLS_HOES);
		tag(ECTags.Items.INFUSABLE_AXES).addTag(Tags.Items.TOOLS_AXES);
		tag(ECTags.Items.INFUSABLE_SHILDS).addTags(Tags.Items.TOOLS_SHIELDS);
		tag(ECTags.Items.INFUSABLE_BOWS).addTag(Tags.Items.TOOLS_BOWS);
		tag(ECTags.Items.INFUSABLE_CROSSBOWS).addTag(Tags.Items.TOOLS_CROSSBOWS);
		tag(ECTags.Items.INFUSABLE_FISHING_RODS).add(Items.FISHING_ROD);
		tag(ECTags.Items.INFUSABLE_TRIDENTS).add(Items.TRIDENT);

		tag(ECTags.Items.INFUSABLE_HELMETS).addTag(Tags.Items.ARMORS_HELMETS);
		tag(ECTags.Items.INFUSABLE_CHESTPLATES).addTag(Tags.Items.ARMORS_CHESTPLATES);
		tag(ECTags.Items.INFUSABLE_LEGGINGS).addTag(Tags.Items.ARMORS_LEGGINGS);
		tag(ECTags.Items.INFUSABLE_BOOTS).addTag(Tags.Items.ARMORS_BOOTS);

		tag(ECTags.Items.SPELL_HOLDERS).add(getItems(AbstractSpellHolderItem.class));
		tag(ECTags.Items.ELEMENTAL_CRYSTALS).add(ECItems.FIRE_CRYSTAL.get(), ECItems.WATER_CRYSTAL.get(), ECItems.EARTH_CRYSTAL.get(), ECItems.AIR_CRYSTAL.get());
		tag(ECTags.Items.CRYSTALS).add(ECItems.INERT_CRYSTAL.get(), ECItems.CONTAINED_CRYSTAL.get(), ECItems.PURE_CRYSTAL.get()).addTag(ECTags.Items.ELEMENTAL_CRYSTALS);
		tag(ECTags.Items.LENSES).add(ECItems.FIRE_LENS.get(), ECItems.WATER_LENS.get(), ECItems.EARTH_LENS.get(), ECItems.AIR_LENS.get());
		
		tag(ECTags.Items.DEFAULT_SHARDS).add(ECItems.FIRE_SHARD.get(), ECItems.WATER_SHARD.get(), ECItems.EARTH_SHARD.get(), ECItems.AIR_SHARD.get());
		tag(ECTags.Items.POWERFUL_SHARDS).add(ECItems.POWERFUL_FIRE_SHARD.get(), ECItems.POWERFUL_WATER_SHARD.get(), ECItems.POWERFUL_EARTH_SHARD.get(), ECItems.POWERFUL_AIR_SHARD.get());
		tag(ECTags.Items.FIRE_SHARDS).add(ECItems.FIRE_SHARD.get(), ECItems.POWERFUL_FIRE_SHARD.get());
		tag(ECTags.Items.WATER_SHARDS).add(ECItems.WATER_SHARD.get(), ECItems.POWERFUL_WATER_SHARD.get());
		tag(ECTags.Items.EARTH_SHARDS).add(ECItems.EARTH_SHARD.get(), ECItems.POWERFUL_EARTH_SHARD.get());
		tag(ECTags.Items.AIR_SHARDS).add(ECItems.AIR_SHARD.get(), ECItems.POWERFUL_AIR_SHARD.get());
		tag(ECTags.Items.SHARDS).addTag(ECTags.Items.DEFAULT_SHARDS).addTag(ECTags.Items.POWERFUL_SHARDS);

		tag(ECTags.Items.INGOTS_DRENCHED_IRON).add(ECItems.DRENCHED_IRON_INGOT.get());
		tag(ECTags.Items.INGOTS_SWIFT_ALLOY).add(ECItems.SWIFT_ALLOY_INGOT.get());
		tag(ECTags.Items.INGOTS_FIREITE).add(ECItems.FIREITE_INGOT.get());
		tag(Tags.Items.INGOTS).addTags(ECTags.Items.INGOTS_DRENCHED_IRON, ECTags.Items.INGOTS_SWIFT_ALLOY, ECTags.Items.INGOTS_FIREITE);

		tag(ECTags.Items.NUGGETS_DRENCHED_IRON).add(ECItems.DRENCHED_IRON_NUGGET.get());
		tag(ECTags.Items.NUGGETS_SWIFT_ALLOY).add(ECItems.SWIFT_ALLOY_NUGGET.get());
		tag(ECTags.Items.NUGGETS_FIREITE).add(ECItems.FIREITE_NUGGET.get());
		tag(Tags.Items.NUGGETS).addTags(ECTags.Items.NUGGETS_DRENCHED_IRON, ECTags.Items.NUGGETS_SWIFT_ALLOY, ECTags.Items.NUGGETS_FIREITE);

		tag(ECTags.Items.PRISTINE_FIRE_GEMS).add(ECItems.PRISTINE_FIRE_GEM.get());
		tag(ECTags.Items.FINE_FIRE_GEMS).add(ECItems.FINE_FIRE_GEM.get(), ECItems.PRISTINE_FIRE_GEM.get());
		tag(ECTags.Items.CRUDE_FIRE_GEMS).add(ECItems.CRUDE_FIRE_GEM.get(), ECItems.FINE_FIRE_GEM.get(), ECItems.PRISTINE_FIRE_GEM.get());
		tag(ECTags.Items.INPUT_FIRE_GEMS).add(ECItems.CRUDE_FIRE_GEM.get(), ECItems.FINE_FIRE_GEM.get(), ECItems.PRISTINE_FIRE_GEM.get()).addTag(Tags.Items.GEMS_DIAMOND);
		tag(ECTags.Items.PRISTINE_WATER_GEMS).add(ECItems.PRISTINE_WATER_GEM.get());
		tag(ECTags.Items.FINE_WATER_GEMS).add(ECItems.FINE_WATER_GEM.get(), ECItems.PRISTINE_WATER_GEM.get());
		tag(ECTags.Items.CRUDE_WATER_GEMS).add(ECItems.CRUDE_WATER_GEM.get(), ECItems.FINE_WATER_GEM.get(), ECItems.PRISTINE_WATER_GEM.get());
		tag(ECTags.Items.INPUT_WATER_GEMS).add(ECItems.CRUDE_WATER_GEM.get(), ECItems.FINE_WATER_GEM.get(), ECItems.PRISTINE_WATER_GEM.get()).addTag(Tags.Items.GEMS_DIAMOND);
		tag(ECTags.Items.PRISTINE_EARTH_GEMS).add(ECItems.PRISTINE_EARTH_GEM.get());
		tag(ECTags.Items.FINE_EARTH_GEMS).add(ECItems.FINE_EARTH_GEM.get(), ECItems.PRISTINE_EARTH_GEM.get());
		tag(ECTags.Items.CRUDE_EARTH_GEMS).add(ECItems.CRUDE_EARTH_GEM.get(), ECItems.FINE_EARTH_GEM.get(), ECItems.PRISTINE_EARTH_GEM.get());
		tag(ECTags.Items.INPUT_EARTH_GEMS).add(ECItems.CRUDE_EARTH_GEM.get(), ECItems.FINE_EARTH_GEM.get(), ECItems.PRISTINE_EARTH_GEM.get()).addTag(Tags.Items.GEMS_DIAMOND);
		tag(ECTags.Items.PRISTINE_AIR_GEMS).add(ECItems.PRISTINE_AIR_GEM.get());
		tag(ECTags.Items.FINE_AIR_GEMS).add(ECItems.FINE_AIR_GEM.get(), ECItems.PRISTINE_AIR_GEM.get());
		tag(ECTags.Items.CRUDE_AIR_GEMS).add(ECItems.CRUDE_AIR_GEM.get(), ECItems.FINE_AIR_GEM.get(), ECItems.PRISTINE_AIR_GEM.get());
		tag(ECTags.Items.INPUT_AIR_GEMS).add(ECItems.CRUDE_AIR_GEM.get(), ECItems.FINE_AIR_GEM.get(), ECItems.PRISTINE_AIR_GEM.get()).addTag(Tags.Items.GEMS_DIAMOND);
		tag(ECTags.Items.INPUT_GEMS).addTags(ECTags.Items.INPUT_FIRE_GEMS, ECTags.Items.INPUT_WATER_GEMS, ECTags.Items.INPUT_EARTH_GEMS, ECTags.Items.INPUT_AIR_GEMS);
		tag(Tags.Items.GEMS).addTags(ECTags.Items.INPUT_GEMS);

		tag(ECTags.Items.RUNE_SLATES).add(ECItems.MINOR_RUNE_SLATE.get(), ECItems.RUNE_SLATE.get(), ECItems.MAJOR_RUNE_SLATE.get());
		
		tag(ECTags.Items.PIPE_COVER_HIDING).addTag(ECTags.Items.PIPES).add(ECItems.COVER_FRAME.get(), ECItems.PIPE_PRIORITY.get());
		
		tag(ECTags.Items.STAFF_CRAFT_SWORD).add(Items.DIAMOND_SWORD, Items.NETHERITE_SWORD);

		tag(ECTags.Items.GEORE_SHARDS);
		tag(ECTags.Items.PURE_ORES_ORE_SOURCE).addTag(Tags.Items.ORES);
		tag(ECTags.Items.PURE_ORES_RAW_MATERIALS_SOURCE).addTag(Tags.Items.RAW_MATERIALS);
		tag(ECTags.Items.PURE_ORES_GEORE_SHARDS_SOURCE).addTag(ECTags.Items.GEORE_SHARDS);
		tag(ECTags.Items.PURE_ORES_MOD_PROCESSING_BLACKLIST);
		
		tag(ECTags.Items.GROVE_SHRINE_FLOWERS).addTag(ItemTags.FLOWERS);
		tag(ECTags.Items.GROVE_SHRINE_BLACKLIST);
		tag(ECTags.Items.MYSTICAL_GROVE_FLOWERS);

		tag(ItemTags.BEACON_PAYMENT_ITEMS).add(ECItems.DRENCHED_IRON_INGOT.get(), ECItems.SWIFT_ALLOY_INGOT.get(), ECItems.FIREITE_INGOT.get());

		tag(ECTags.Items.JEWEL_SOCKETABLES).addTags(Tags.Items.TOOLS, Tags.Items.ARMORS, ECTags.Items.SPELL_CAST_TOOLS).add(Items.ELYTRA);

		tag(ECTags.Items.CURIOS_ELEMENT_HOLDER).add(ECItems.FIRE_HOLDER.get(), ECItems.WATER_HOLDER.get(), ECItems.EARTH_HOLDER.get(), ECItems.AIR_HOLDER.get(), ECItems.PURE_HOLDER.get());
	}

	protected <T> Item[] getItems(List<String> modIds, Class<T> clazz, Predicate<T> filter) {
		return registry.stream()
				.filter(i -> modIds.contains(ForgeRegistries.ITEMS.getKey(i).getNamespace()) && clazz.isInstance(i))
				.map(clazz::cast)
				.filter(filter)
				.map(Item.class::cast)
				.sorted(Comparator.comparing(ForgeRegistries.ITEMS::getKey))
				.toArray(Item[]::new);
	}

	protected <T> Item[] getItems(String modId, Class<T> clazz, Predicate<T> filter) {
		return getItems(Lists.newArrayList(modId), clazz, filter);
	}

	protected <T> Item[] getItems(String modId, Class<T> clazz) {
		return getItems(modId, clazz, Predicates.alwaysTrue());
	}

	protected <T> Item[] getItems(Class<T> clazz) {
		return getItems(ElementalCraftApi.MODID, clazz);
	}
}
