package sirttas.elementalcraft.datagen.model;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import sirttas.elementalcraft.item.ECItems;

import java.util.function.BiConsumer;

public class ECItemModelGenerator extends ItemModelGenerators implements ECModelGenerator {

    public static final ECModelGenerator.Factory FACTORY = (_, itemModelOutput, _, modelOutput) -> new ECItemModelGenerator(itemModelOutput, modelOutput);

    public ECItemModelGenerator(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
    }

    @Override
    public void run() {
        this.generateFlatItem(ECItems.FOCUS.get(), ModelTemplates.FLAT_ITEM);
        this.declareCustomModelItem(ECItems.STAFF.get());
        this.generateScroll(ECItems.SCROLL.get());
        this.generateFlatItem(ECItems.SPELL_BOOK.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.EMPTY_RECEPTACLE.get(), ModelTemplates.FLAT_ITEM);
        this.declareCustomModelItem(ECItems.SOURCE_STABILIZER.get());
        this.generateFlatItem(ECItems.SOURCE_ANALYSIS_GLASS.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(ECItems.FIRE_HOLDER.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.EARTH_HOLDER.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.AIR_HOLDER.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.PURE_HOLDER_CORE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.PURE_HOLDER.get(), ModelTemplates.FLAT_ITEM);
        this.generatePureOre(ECItems.PURE_ORE.get());
        this.generateRune(ECItems.RUNE.get());
        this.generateFlatItem(ECItems.DRENCHED_IRON_CHISEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(ECItems.FIREITE_CHISEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(ECItems.ELEMENTAL_FIREFUEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.INERT_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.CONTAINED_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.STRONGLY_CONTAINED_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.PURE_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.DRENCHED_IRON_INGOT.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.DRENCHED_IRON_NUGGET.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.SWIFT_ALLOY_INGOT.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.SWIFT_ALLOY_NUGGET.get(), ModelTemplates.FLAT_ITEM);

        /*
	public static final DeferredHolder<@NotNull Item, @NotNull Item> HARDENED_HANDLE = register("hardened_handle", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> DRENCHED_SAW_BLADE = register("drenched_saw_blade", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> SHRINE_BASE = register("shrinebase", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> FIREITE_INGOT = register("fireite_ingot", () -> new Item(new Item.Properties()
			.fireResistant()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> FIREITE_NUGGET = register("fireite_nugget", () -> new Item(new Item.Properties()
			.fireResistant()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> AIR_SILK = register("air_silk", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> SHRINE_UPGRADE_CORE = register("shrine_upgrade_core", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> ADVANCED_SHRINE_UPGRADE_CORE = register("advanced_shrine_upgrade_core", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> SCROLL_PAPER = register("scroll_paper", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> SPRINGALINE_SHARD = register("springaline_shard", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> SOLAR_PRISM = register("solar_prism", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> FIRE_CRYSTAL = register("firecrystal", () -> new ElementalItem(ElementType.FIRE, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> WATER_CRYSTAL = register("watercrystal", () -> new ElementalItem(ElementType.WATER, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> EARTH_CRYSTAL = register("earthcrystal", () -> new ElementalItem(ElementType.EARTH, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> AIR_CRYSTAL = register("aircrystal", () -> new ElementalItem(ElementType.AIR, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> CRUDE_FIRE_GEM = register("crude_fire_gem", () -> new ElementalItem(ElementType.FIRE, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> CRUDE_WATER_GEM = register("crude_water_gem", () -> new ElementalItem(ElementType.WATER, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> CRUDE_EARTH_GEM = register("crude_earth_gem", () -> new ElementalItem(ElementType.EARTH, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> CRUDE_AIR_GEM = register("crude_air_gem", () -> new ElementalItem(ElementType.AIR, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> FINE_FIRE_GEM = register("fine_fire_gem", () -> new ElementalItem(ElementType.FIRE, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> FINE_WATER_GEM = register("fine_water_gem", () -> new ElementalItem(ElementType.WATER, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> FINE_EARTH_GEM = register("fine_earth_gem", () -> new ElementalItem(ElementType.EARTH, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> FINE_AIR_GEM = register("fine_air_gem", () -> new ElementalItem(ElementType.AIR, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> PRISTINE_FIRE_GEM = register("pristine_fire_gem", () -> new ElementalItem(ElementType.FIRE, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> PRISTINE_WATER_GEM = register("pristine_water_gem", () -> new ElementalItem(ElementType.WATER, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> PRISTINE_EARTH_GEM = register("pristine_earth_gem", () -> new ElementalItem(ElementType.EARTH, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> PRISTINE_AIR_GEM = register("pristine_air_gem", () -> new ElementalItem(ElementType.AIR, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> PRISTINE_SHARD = register("pristine_shard", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> FIRE_SOURCE_SEED = register("fire_source_seed", () -> new ElementalItem(ElementType.FIRE, new Item.Properties()));
    public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> WATER_SOURCE_SEED = register("water_source_seed", () -> new ElementalItem(ElementType.WATER, new Item.Properties()));
    public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> EARTH_SOURCE_SEED = register("earth_source_seed", () -> new ElementalItem(ElementType.EARTH, new Item.Properties()));
    public static final DeferredHolder<@NotNull Item, @NotNull ElementalItem> AIR_SOURCE_SEED = register("air_source_seed", () -> new ElementalItem(ElementType.AIR, new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull DamageableCraftingElementalItem> FIRE_LENS = register("fire_lens", () -> new DamageableCraftingElementalItem(ElementType.FIRE, new Item.Properties()
			.stacksTo(1)
			.durability(1500)));
	public static final DeferredHolder<@NotNull Item, @NotNull DamageableCraftingElementalItem> AIR_MILL = register("air_mill", () -> new DamageableCraftingElementalItem(ElementType.AIR, new Item.Properties()
			.stacksTo(1)
			.durability(3000)));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> MINOR_RUNE_SLATE = register("minor_rune_slate", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> RUNE_SLATE = register("rune_slate", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> MAJOR_RUNE_SLATE = register("major_rune_slate", () -> new Item(new Item.Properties()));
	public static final DeferredHolder<@NotNull Item, @NotNull Item> UNSET_JEWEL = register("unset_jewel", () -> new Item(new Item.Properties()));

    // Pipe Upgrade
    public static final DeferredHolder<@NotNull Item, @NotNull CoverFrameItem> COVER_FRAME = register(CoverFrameItem.NAME, () -> new CoverFrameItem(new Item.Properties()));
    public static final DeferredHolder<@NotNull Item, @NotNull PipeUpgradeItem> ELEMENT_PUMP = registerPipeUpgrade(PipeUpgradeTypes.ELEMENT_PUMP);
    public static final DeferredHolder<@NotNull Item, @NotNull PipeUpgradeItem> PIPE_PRIORITY_RINGS = registerPipeUpgrade(PipeUpgradeTypes.PIPE_PRIORITY_RINGS);
    public static final DeferredHolder<@NotNull Item, @NotNull PipeUpgradeItem> ELEMENT_VALVE = registerPipeUpgrade(PipeUpgradeTypes.ELEMENT_VALVE);
    public static final DeferredHolder<@NotNull Item, @NotNull PipeUpgradeItem> ELEMENT_BEAM = registerPipeUpgrade(PipeUpgradeTypes.ELEMENT_BEAM);

    // Jewels
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> SALMON_JEWEL = registerJewel(Jewels.SALMON);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> PHOENIX_JEWEL = registerJewel(Jewels.PHOENIX);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> BASILISK_JEWEL = registerJewel(Jewels.BASILISK);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> BEAR_JEWEL = registerJewel(Jewels.BEAR);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> TIGER_JEWEL = registerJewel(Jewels.TIGER);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> LEOPARD_JEWEL = registerJewel(Jewels.LEOPARD);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> DOLPHIN_JEWEL = registerJewel(Jewels.DOLPHIN);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> KIRIN_JEWEL = registerJewel(Jewels.KIRIN);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> VIPER_JEWEL = registerJewel(Jewels.VIPER);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> TORTOISE_JEWEL = registerJewel(Jewels.TORTOISE);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> ARCTIC_HARE_JEWEL = registerJewel(Jewels.ARCTIC_HARE);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> MOLE_JEWEL = registerJewel(Jewels.MOLE);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> HAWK_JEWEL = registerJewel(Jewels.HAWK);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> DEMIGOD_JEWEL = registerJewel(Jewels.DEMIGOD);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> STRIDER_JEWEL = registerJewel(Jewels.STRIDER);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> WATER_STRIDER_JEWEL = registerJewel(Jewels.WATER_STRIDER);
	public static final DeferredHolder<@NotNull Item, @NotNull JewelItem> PIGLIN_JEWEL = registerJewel(Jewels.PIGLIN);*/
    }

    public void generateScroll(Item item) {
        this.generateFlatItem(item, ModelTemplates.FLAT_ITEM); // TODO
    }

    public void generatePureOre(Item item) {
        this.generateFlatItem(item, ModelTemplates.FLAT_ITEM); // TODO
    }

    public void generateRune(Item item) {
        this.generateFlatItem(item, ModelTemplates.FLAT_ITEM); // TODO
    }
}
