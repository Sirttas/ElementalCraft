package sirttas.elementalcraft;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ElementalCraftTab extends CreativeModeTab {

	public static final @Nonnull CreativeModeTab TAB = new ElementalCraftTab();

	public ElementalCraftTab() {
		super(ElementalCraftApi.MODID);
	}

	@Nonnull
	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ECItems.FOCUS.get());
	}

	@Override
	public void fillItemList(@Nonnull NonNullList<ItemStack> list) {
		addItem(ECBlocks.EXTRACTOR, list);
		addItem(ECBlocks.EXTRACTOR_IMPROVED, list);
		addItem(ECBlocks.EVAPORATOR, list);
		addItem(ECBlocks.INFUSER, list);
		addItem(ECBlocks.SOLAR_SYNTHESIZER, list);
		addItem(ECBlocks.MANA_SYNTHESIZER, list);
		addItem(ECBlocks.DIFFUSER, list);
		addItem(ECBlocks.BINDER, list);
		addItem(ECBlocks.BINDER_IMPROVED, list);
		addItem(ECBlocks.CRYSTALLIZER, list);
		addItem(ECBlocks.INSCRIBER, list);
		addItem(ECBlocks.AIR_MILL, list);
		addItem(ECBlocks.FIRE_PEDESTAL, list);
		addItem(ECBlocks.WATER_PEDESTAL, list);
		addItem(ECBlocks.EARTH_PEDESTAL, list);
		addItem(ECBlocks.AIR_PEDESTAL, list);
		addItem(ECBlocks.PURE_INFUSER, list);
		addItem(ECBlocks.FIRE_FURNACE, list);
		addItem(ECBlocks.FIRE_BLAST_FURNACE, list);
		addItem(ECBlocks.PURIFIER, list);
		addItem(ECBlocks.SMALL_CONTAINER, list);
		addItem(ECBlocks.CONTAINER, list);
		addItem(ECBlocks.FIRE_RESERVOIR, list);
		addItem(ECBlocks.WATER_RESERVOIR, list);
		addItem(ECBlocks.EARTH_RESERVOIR, list);
		addItem(ECBlocks.AIR_RESERVOIR, list);
		addItem(ECBlocks.CREATIVE_CONTAINER, list);
		addItem(ECBlocks.PIPE_IMPAIRED, list);
		addItem(ECBlocks.PIPE, list);
		addItem(ECBlocks.PIPE_IMPROVED, list);
		addItem(ECItems.COVER_FRAME, list);
		addItem(ECItems.PIPE_PRIORITY, list);
		addItem(ECBlocks.RETRIEVER, list);
		addItem(ECBlocks.SORTER, list);
		addItem(ECBlocks.SPELL_DESK, list);
		addItem(ECBlocks.FIRE_PYLON, list);
		addItem(ECBlocks.VACUUM_SHRINE, list);
		addItem(ECBlocks.GROWTH_SHRINE, list);
		addItem(ECBlocks.HARVEST_SHRINE, list);
		addItem(ECBlocks.LAVA_SHRINE, list);
		addItem(ECBlocks.ORE_SHRINE, list);
		addItem(ECBlocks.OVERLOAD_SHRINE, list);
		addItem(ECBlocks.SWEET_SHRINE, list);
		addItem(ECBlocks.ENDER_LOCK_SHRINE, list);
		addItem(ECBlocks.BREEDING_SHRINE, list);
		addItem(ECBlocks.GROVE_SHRINE, list);
		addItem(ECBlocks.SPRING_SHRINE, list);
		addItem(ECBlocks.BUDDING_SHRINE, list);
		addItem(ECBlocks.SPAWNING_SHRINE, list);
		addItem(ECBlocks.ACCELERATION_SHRINE_UPGRADE, list);
		addItem(ECBlocks.RANGE_SHRINE_UPGRADE, list);
		addItem(ECBlocks.CAPACITY_SHRINE_UPGRADE, list);
		addItem(ECBlocks.EFFICIENCY_SHRINE_UPGRADE, list);
		addItem(ECBlocks.STRENGTH_SHRINE_UPGRADE, list);
		addItem(ECBlocks.OPTIMIZATION_SHRINE_UPGRADE, list);
		addItem(ECBlocks.FORTUNE_SHRINE_UPGRADE, list);
		addItem(ECBlocks.SILK_TOUCH_SHRINE_UPGRADE, list);
		addItem(ECBlocks.PLANTING_SHRINE_UPGRADE, list);
		addItem(ECBlocks.BONELESS_GROWTH_SHRINE_UPGRADE, list);
		addItem(ECBlocks.PICKUP_SHRINE_UPGRADE, list);
		addItem(ECBlocks.VORTEX_SHRINE_UPGRADE, list);
		addItem(ECBlocks.NECTAR_SHRINE_UPGRADE, list);
		addItem(ECBlocks.MYSTICAL_GROVE_SHRINE_UPGRADE, list);
		addItem(ECBlocks.STEM_POLLINATION_SHRINE_UPGRADE, list);
		addItem(ECBlocks.PROTECTION_SHRINE_UPGRADE, list);
		addItem(ECBlocks.FILLING_SHRINE_UPGRADE, list);
		addItem(ECBlocks.SPRINGALINE_SHRINE_UPGRADE, list);
		addItem(ECBlocks.CRYSTAL_HARVEST_SHRINE_UPGRADE, list);

		addItem(ECBlocks.BROKEN_SOURCE_DISPLACEMENT_PLATE, list);
		addItem(ECBlocks.FIRE_SOURCE_DISPLACEMENT_PLATE, list);
		addItem(ECBlocks.WATER_SOURCE_DISPLACEMENT_PLATE, list);
		addItem(ECBlocks.EARTH_SOURCE_DISPLACEMENT_PLATE, list);
		addItem(ECBlocks.AIR_SOURCE_DISPLACEMENT_PLATE, list);
		addItem(ECBlocks.SOURCE_BREEDER, list);
		addItem(ECBlocks.SOURCE_BREEDER_PEDESTAL, list);
		addItem(ECBlocks.CRYSTAL_ORE, list);
		addItem(ECBlocks.DEEPSLATE_CRYSTAL_ORE, list);
		addItem(ECBlocks.WHITE_ROCK, list);
		addItem(ECBlocks.WHITE_ROCK_SLAB, list);
		addItem(ECBlocks.WHITE_ROCK_STAIRS, list);
		addItem(ECBlocks.WHITE_ROCK_WALL, list);
		addItem(ECBlocks.WHITE_ROCK_FENCE, list);
		addItem(ECBlocks.WHITE_ROCK_BRICK, list);
		addItem(ECBlocks.WHITE_ROCK_BRICK_SLAB, list);
		addItem(ECBlocks.WHITE_ROCK_BRICK_STAIRS, list);
		addItem(ECBlocks.WHITE_ROCK_BRICK_WALL, list);
		addItem(ECBlocks.BURNT_GLASS, list);
		addItem(ECBlocks.BURNT_GLASS_PANE, list);
		addItem(ECBlocks.PURE_ROCK, list);
		addItem(ECBlocks.PURE_ROCK_SLAB, list);
		addItem(ECBlocks.PURE_ROCK_STAIRS, list);
		addItem(ECBlocks.PURE_ROCK_WALL, list);

		addItem(ECItems.ELEMENTOPEDIA, list);
		addItem(ECItems.FOCUS, list);
		addItem(ECItems.STAFF, list);
		addItem(ECItems.SCROLL, list);
		addItem(ECItems.SPELL_BOOK, list);
		addItem(ECItems.SOURCE_ANALYSIS_GLASS, list);
		addItem(ECItems.RECEPTACLE, list);
		addItem(ECItems.SOURCE_STABILIZER, list);
		addItem(ECItems.FIRE_HOLDER, list);
		addItem(ECItems.WATER_HOLDER, list);
		addItem(ECItems.EARTH_HOLDER, list);
		addItem(ECItems.AIR_HOLDER, list);
		addItem(ECItems.PURE_HOLDER_CORE, list);
		addItem(ECItems.PURE_HOLDER, list);
		addItem(ECItems.CHISEL, list);
		addItem(ECItems.PURE_ORE, list);
		addItem(ECItems.INERT_CRYSTAL, list);
		addItem(ECBlocks.INERT_CRYSTAL_BLOCK, list);
		addItem(ECItems.CONTAINED_CRYSTAL, list);
		addItem(ECItems.STRONGLY_CONTAINED_CRYSTAL, list);
		addItem(ECItems.FIRE_CRYSTAL, list);
		addItem(ECBlocks.FIRE_CRYSTAL_BLOCK, list);
		addItem(ECItems.WATER_CRYSTAL, list);
		addItem(ECBlocks.WATER_CRYSTAL_BLOCK, list);
		addItem(ECItems.EARTH_CRYSTAL, list);
		addItem(ECBlocks.EARTH_CRYSTAL_BLOCK, list);
		addItem(ECItems.AIR_CRYSTAL, list);
		addItem(ECBlocks.AIR_CRYSTAL_BLOCK, list);
		addItem(ECItems.PURE_CRYSTAL, list);
		addItem(ECItems.FIRE_SHARD, list);
		addItem(ECItems.WATER_SHARD, list);
		addItem(ECItems.EARTH_SHARD, list);
		addItem(ECItems.AIR_SHARD, list);
		addItem(ECItems.POWERFUL_FIRE_SHARD, list);
		addItem(ECItems.POWERFUL_WATER_SHARD, list);
		addItem(ECItems.POWERFUL_EARTH_SHARD, list);
		addItem(ECItems.POWERFUL_AIR_SHARD, list);
		addItem(ECItems.CRUDE_FIRE_GEM, list);
		addItem(ECItems.CRUDE_WATER_GEM, list);
		addItem(ECItems.CRUDE_EARTH_GEM, list);
		addItem(ECItems.CRUDE_AIR_GEM, list);
		addItem(ECItems.FINE_FIRE_GEM, list);
		addItem(ECItems.FINE_WATER_GEM, list);
		addItem(ECItems.FINE_EARTH_GEM, list);
		addItem(ECItems.FINE_AIR_GEM, list);
		addItem(ECItems.PRISTINE_FIRE_GEM, list);
		addItem(ECItems.PRISTINE_WATER_GEM, list);
		addItem(ECItems.PRISTINE_EARTH_GEM, list);
		addItem(ECItems.PRISTINE_AIR_GEM, list);
		addItem(ECItems.DRENCHED_IRON_NUGGET, list);
		addItem(ECItems.DRENCHED_IRON_INGOT, list);
		addItem(ECBlocks.DRENCHED_IRON_BLOCK, list);
		addItem(ECItems.SWIFT_ALLOY_NUGGET, list);
		addItem(ECItems.SWIFT_ALLOY_INGOT, list);
		addItem(ECBlocks.SWIFT_ALLOY_BLOCK, list);
		addItem(ECItems.FIREITE_NUGGET, list);
		addItem(ECItems.FIREITE_INGOT, list);
		addItem(ECBlocks.FIREITE_BLOCK, list);
		addItem(ECItems.SPRINGALINE_SHARD, list);
		addItem(ECBlocks.SPRINGALINE_BLOCK, list);
		addItem(ECBlocks.SMALL_SPRINGALINE_BUD, list);
		addItem(ECBlocks.MEDIUM_SPRINGALINE_BUD, list);
		addItem(ECBlocks.LARGE_SPRINGALINE_BUD, list);
		addItem(ECBlocks.SPRINGALINE_CLUSTER, list);
		addItem(ECBlocks.SPRINGALINE_GLASS, list);
		addItem(ECBlocks.SPRINGALINE_GLASS_PANE, list);
		addItem(ECItems.SOLAR_PRISM, list);
		addItem(ECItems.FIRE_LENS, list);
		addItem(ECItems.WATER_LENS, list);
		addItem(ECItems.EARTH_LENS, list);
		addItem(ECItems.AIR_LENS, list);
		addItem(ECItems.AIR_SILK, list);
		addItem(ECItems.HARDENED_HANDLE, list);
		addItem(ECItems.SCROLL_PAPER, list);
		addItem(ECItems.SHRINE_BASE, list);
		addItem(ECItems.SHRINE_UPGRADE_CORE, list);
		addItem(ECItems.MINOR_RUNE_SLATE, list);
		addItem(ECItems.RUNE_SLATE, list);
		addItem(ECItems.MAJOR_RUNE_SLATE, list);
		addItem(ECItems.RUNE, list);
		addItem(ECItems.UNSET_JEWEL, list);
		addItem(ECItems.JEWEL, list);
	}

	private <T extends ItemLike> void addItem(@Nonnull Supplier<T> item, @Nonnull NonNullList<ItemStack> list) {
		if (item instanceof RegistryObject<T> registryObject && !registryObject.isPresent()) {
			return;
		}
		addItem(item.get(), list);
	}

	private void addItem(ItemLike item, @Nonnull NonNullList<ItemStack> list) {
		if (item != null) {
			item.asItem().fillItemCategory(this, list);
		}
	}

}
