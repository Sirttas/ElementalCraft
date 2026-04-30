package sirttas.elementalcraft.datagen.model;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import sirttas.elementalcraft.item.ECItems;
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
        this.generateFlatItem(ECItems.PURE_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
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
        this.generateFlatItem(ECItems.SALMON_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.PHOENIX_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.BASILISK_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.BEAR_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.TIGER_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.LEOPARD_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.DOLPHIN_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.KIRIN_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.VIPER_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.TORTOISE_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.ARCTIC_HARE_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.MOLE_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.HAWK_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.DEMIGOD_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.STRIDER_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.WATER_STRIDER_JEWEL.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ECItems.PIGLIN_JEWEL.get(), ModelTemplates.FLAT_ITEM);

        // Misc
        this.generatePureOre(ECItems.PURE_ORE.get());
        this.declareCustomModelItem(ECItems.SPRINGALINE_CLUSTER.get());
        this.declareCustomModelItem(ECItems.LARGE_SPRINGALINE_BUD.get());
        this.declareCustomModelItem(ECItems.MEDIUM_SPRINGALINE_BUD.get());
        this.declareCustomModelItem(ECItems.SMALL_SPRINGALINE_BUD.get());
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
}
