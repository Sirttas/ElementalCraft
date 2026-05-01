package sirttas.elementalcraft.datagen.model;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.item.jewel.JewelItem;
import sirttas.elementalcraft.rune.RuneModel;
import sirttas.elementalcraft.rune.RuneSpecialRenderer;

import java.util.function.BiConsumer;

public class ECItemModelGenerator extends ItemModelGenerators implements ECModelGenerator {

    public static final ECModelGenerator.Factory FACTORY = (_, itemModelOutput, _, _, _, modelOutput) -> new ECItemModelGenerator(itemModelOutput, modelOutput);

    public ECItemModelGenerator(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
    }

    @Override
    public void run() {
        // Spell items
        this.generateFlatItem(ECItems.FOCUS.get(), ModelTemplates.FLAT_ITEM);
        this.declareCustomModelItem(ECItems.STAFF.get());
        this.generateScroll(ECItems.SCROLL.get());
        this.generateFlatItem(ECItems.SPELL_BOOK.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.REPAIR_HAMMER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Sources / receptacles
        this.generateFlatItem(ECItems.EMPTY_RECEPTACLE.get(), ModelTemplates.FLAT_ITEM);
        this.declareCustomModelItem(ECItems.SOURCE_STABILIZER.get());
        this.generateFlatItem(ECItems.SOURCE_ANALYSIS_GLASS.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Holders
        this.generateFlatItem(ECItems.FIRE_HOLDER.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.WATER_HOLDER.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.EARTH_HOLDER.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.AIR_HOLDER.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.PURE_HOLDER_CORE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.PURE_HOLDER.get(), ModelTemplates.FLAT_ITEM);

        // Rrunes
        this.generateRune(ECItems.RUNE.get());
        this.generateFlatItem(ECItems.MINOR_RUNE_SLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.RUNE_SLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.MAJOR_RUNE_SLATE.get(), ModelTemplates.FLAT_ITEM);

        // Chisels
        this.generateFlatItem(ECItems.DRENCHED_IRON_CHISEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(ECItems.FIREITE_CHISEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(ECItems.SWIFT_ALLOY_CHISEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Misc crafting items
        this.generateFlatItem(ECItems.ELEMENTAL_FIREFUEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.HARDENED_HANDLE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.DRENCHED_SAW_BLADE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.SHRINE_BASE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.SHRINE_UPGRADE_CORE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.ADVANCED_SHRINE_UPGRADE_CORE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.SCROLL_PAPER.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.AIR_SILK.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.SOLAR_PRISM.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.FIRE_LENS.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        this.generateFlatItem(ECItems.AIR_MILL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Metals
        this.generateFlatItem(ECItems.DRENCHED_IRON_INGOT.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.DRENCHED_IRON_NUGGET.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.SWIFT_ALLOY_INGOT.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.SWIFT_ALLOY_NUGGET.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.FIREITE_INGOT.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.FIREITE_NUGGET.get(), ModelTemplates.FLAT_ITEM);

        // Crystals
        this.generateFlatItem(ECItems.INERT_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.CONTAINED_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.STRONGLY_CONTAINED_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.PURE_CRYSTAL.get(), ECItems.INERT_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.FIRE_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.WATER_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.EARTH_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.AIR_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);

        // Gems
        this.generateFlatItem(ECItems.CRUDE_FIRE_GEM.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.CRUDE_WATER_GEM.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.CRUDE_EARTH_GEM.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.CRUDE_AIR_GEM.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.FINE_FIRE_GEM.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.FINE_WATER_GEM.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.FINE_EARTH_GEM.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.FINE_AIR_GEM.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.PRISTINE_FIRE_GEM.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.PRISTINE_WATER_GEM.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.PRISTINE_EARTH_GEM.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.PRISTINE_AIR_GEM.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.PRISTINE_SHARD.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.SPRINGALINE_SHARD.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.UNSET_JEWEL.get(), ModelTemplates.FLAT_ITEM);

        // Source seeds
        this.generateFlatItem(ECItems.FIRE_SOURCE_SEED.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.WATER_SOURCE_SEED.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.EARTH_SOURCE_SEED.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.AIR_SOURCE_SEED.get(), ModelTemplates.FLAT_ITEM);

        // Jewels
        this.generateJewel(ECItems.SALMON_JEWEL.get());
        this.generateJewel(ECItems.PHOENIX_JEWEL.get());
        this.generateJewel(ECItems.BASILISK_JEWEL.get());
        this.generateJewel(ECItems.BEAR_JEWEL.get());
        this.generateJewel(ECItems.TIGER_JEWEL.get());
        this.generateJewel(ECItems.LEOPARD_JEWEL.get());
        this.generateJewel(ECItems.DOLPHIN_JEWEL.get());
        this.generateJewel(ECItems.KIRIN_JEWEL.get());
        this.generateJewel(ECItems.VIPER_JEWEL.get());
        this.generateJewel(ECItems.TORTOISE_JEWEL.get());
        this.generateJewel(ECItems.ARCTIC_HARE_JEWEL.get());
        this.generateJewel(ECItems.MOLE_JEWEL.get());
        this.generateJewel(ECItems.HAWK_JEWEL.get());
        this.generateJewel(ECItems.DEMIGOD_JEWEL.get());
        this.generateJewel(ECItems.STRIDER_JEWEL.get());
        this.generateJewel(ECItems.WATER_STRIDER_JEWEL.get());
        this.generateJewel(ECItems.PIGLIN_JEWEL.get());

        // Blocks
        this.declareCustomModelItem(ECItems.SPRINGALINE_CLUSTER.get());
        this.declareCustomModelItem(ECItems.LARGE_SPRINGALINE_BUD.get());
        this.declareCustomModelItem(ECItems.MEDIUM_SPRINGALINE_BUD.get());
        this.declareCustomModelItem(ECItems.SMALL_SPRINGALINE_BUD.get());
        this.declareCustomModelItem(ECItems.AIR_MILL_GRINDSTONE.get());
        this.declareCustomModelItem(ECItems.AIR_MILL_WOOD_SAW.get());
        this.declareCustomModelItem(ECItems.AIR_MILL_SYNTHESIZER.get());
        this.declareCustomModelItem(ECBlocks.BREEDING_SHRINE.get());
        this.declareCustomModelItem(ECBlocks.BUDDING_SHRINE.get());
        this.declareCustomModelItem(ECBlocks.CRACKING_SYNTHESIZER.get());
        this.declareCustomModelItem(ECBlocks.DIFFUSER.get());
        this.declareCustomModelItem(ECBlocks.ENCHANTMENT_LIQUEFIER.get());
        this.declareCustomModelItem(ECBlocks.ENDER_LOCK_SHRINE.get());
        this.declareCustomModelItem(ECBlocks.FIRE_PYLON.get());
        this.declareCustomModelItem(ECBlocks.RETRIEVER.get());
        this.declareCustomModelItem(ECBlocks.ORDERED_SORTER.get());
        this.declareCustomModelItem(ECBlocks.OVERLOAD_SHRINE.get());
        this.declareCustomModelItem(ECBlocks.FIRE_RESERVOIR.get());
        this.declareCustomModelItem(ECBlocks.WATER_RESERVOIR.get());
        this.declareCustomModelItem(ECBlocks.EARTH_RESERVOIR.get());
        this.declareCustomModelItem(ECBlocks.AIR_RESERVOIR.get());
        this.declareCustomModelItem(ECBlocks.SCULK_CRACKING_SYNTHESIZER.get());
        this.declareCustomModelItem(ECBlocks.ACCELERATION_SHRINE_UPGRADE.get());
        this.declareCustomModelItem(ECBlocks.OVERCLOCKED_ACCELERATION_SHRINE_UPGRADE.get());
        this.declareCustomModelItem(ECItems.SILK_TOUCH_SHRINE_UPGRADE.get());
        this.declareCustomModelItem(ECItems.TRANSLOCATION_SHRINE_UPGRADE.get());
        this.declareCustomModelItem(ECItems.VORTEX_SHRINE_UPGRADE.get());
        this.declareCustomModelItem(ECBlocks.SOURCE_BREEDER.get());
        this.declareCustomModelItem(ECBlocks.WATER_MILL_GRINDSTONE.get());
        this.declareCustomModelItem(ECBlocks.WATER_MILL_WOOD_SAW.get());
        this.declareCustomModelItem(ECBlocks.WHITE_ROCK_FENCE.get());
        this.generateFlatItem(ECBlocks.FIRE_SOURCE.get().asItem(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECBlocks.WATER_SOURCE.get().asItem(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECBlocks.EARTH_SOURCE.get().asItem(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECBlocks.AIR_SOURCE.get().asItem(), ModelTemplates.FLAT_ITEM);

        // Misc
        this.generatePureOre(ECItems.PURE_ORE.get());
        this.declareCustomModelItem(ECItems.COVER_FRAME.get());
    }

    public void generateScroll(Item item) {
        this.generateFlatItem(item, ModelTemplates.FLAT_ITEM); // TODO
    }

    public void generatePureOre(Item item) {
        this.generateFlatItem(item, ModelTemplates.FLAT_ITEM); // TODO
    }

    public void generateRune(Item item) {
        var slate = ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(RuneModel.Slate.STANDARD.getMaterial()), this.modelOutput);

        this.itemModelOutput.accept(item, ItemModelUtils.specialModel(slate, RuneSpecialRenderer.Unbaked.get()));
    }

    public void generateJewel(JewelItem item) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item);
        this.itemModelOutput.accept(item, ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(new Material(id.withPrefix("elementalcraft/jewels/"))), this.modelOutput)));
    }

    public void declareCustomModelItem(Block block) {
        this.declareCustomModelItem(block.asItem());
    }
}
