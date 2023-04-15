package sirttas.elementalcraft.datagen.managed;

import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.holdersets.AndHolderSet;
import net.minecraftforge.registries.holdersets.NotHolderSet;
import net.minecraftforge.registries.holdersets.OrHolderSet;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.pureore.PureOreLoader;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.List;

public class PureOreLoaderProvider extends AbstractManagedDataBuilderProvider<PureOreLoader, PureOreLoader.Builder> {

    private static final ResourceLocation FIXED_RESONATING = ElementalCraft.createRL("resonating");
    private static final ResourceLocation FIXED_URANINITE = ElementalCraft.createRL("uraninite");

    private static final String NAMESPACE_PATTERN = "^(forge|blue_skies)$";

    public PureOreLoaderProvider(DataGenerator generator) {
        super(generator, ElementalCraft.PURE_ORE_LOADERS_MANAGER,  PureOreLoader.Builder.ENCODER);
    }


    @Override
    public void collectBuilders() {
        ores(standard("ores", ECTags.Items.PURE_SOURCE_ORES_ORES))
                .namespacePattern(NAMESPACE_PATTERN);
        rawMaterials(standard("raw_materials", ECTags.Items.PURE_ORES_SOURCE_RAW_MATERIALS))
                .namespacePattern(NAMESPACE_PATTERN);
        rawMaterialsBlocks(standard("raw_material_blocks", ECTags.Items.PURE_ORES_SOURCE_RAW_MATERIAL_BLOCKS))
                .namespacePattern(NAMESPACE_PATTERN);

        standard("geore_shards", ECTags.Items.PURE_ORES_SOURCE_GEORE_SHARDS)
                .pattern("_?shard$")
                .consumption(5000)
                .inputSize(4)
                .outputSize(5)
                .luckRatio(1);
        standard("geore_blocks", ECTags.Items.PURE_ORES_SOURCE_GEORE_BLOCKS)
                .pattern("_?block$")
                .consumption(6250)
                .outputSize(5)
                .luckRatio(4);

        ores(builder("resonating", ECTags.Items.PURE_ORES_SOURCE_RESONANT_ORE))
                .fixedName(FIXED_RESONATING);

        builder("raw_uraninite", ECTags.Items.PURE_ORES_SOURCE_RAW_URANINITE)
                .fixedName(FIXED_URANINITE)
                .luckRatio(2);
        builder("poor_uraninite", ECTags.Items.PURE_ORES_SOURCE_POOR_URANINITE)
                .fixedName(FIXED_URANINITE)
                .consumption(3750)
                .outputSize(3)
                .luckRatio(2);
        builder("uraninite", ECTags.Items.PURE_ORES_SOURCE_URANINITE)
                .fixedName(FIXED_URANINITE)
                .consumption(5000)
                .outputSize(5)
                .luckRatio(2);
        builder("dense_uraninite", ECTags.Items.PURE_ORES_SOURCE_DENSE_URANINITE)
                .fixedName(FIXED_URANINITE)
                .consumption(10000)
                .outputSize(10)
                .luckRatio(2);
    }

    protected PureOreLoader.Builder ores(PureOreLoader.Builder builder) {
        return builder.tagPattern("^ores/")
                .pattern("_?ore$")
                .luckRatio(5);
    }

    protected PureOreLoader.Builder rawMaterials(PureOreLoader.Builder builder) {
        return builder.tagPattern("^raw_materials/")
                .patterns("^raw_?", "_?raw$")
                .consumption(5000)
                .inputSize(3)
                .outputSize(4)
                .luckRatio(2);
    }

    protected PureOreLoader.Builder rawMaterialsBlocks(PureOreLoader.Builder builder) {
        return builder.tagPattern("^storage_blocks/raw_?(?!_?materials)")
                .patterns("^raw_?", "_?block$")
                .consumption(15000)
                .outputSize(12)
                .luckRatio(18);
    }

    protected PureOreLoader.Builder standard(String name, TagKey<Item> tag) {
        return builder(name, withoutSpecific(tag)).tagPattern("^" + name + "/");
    }

    protected PureOreLoader.Builder builder(String name, TagKey<Item> tag) {
        return builder(name, createHolderSet(tag));
    }

    protected PureOreLoader.Builder builder(String name, HolderSet<Item> set) {
        var builder = PureOreLoader.builder(set);

        add(ElementalCraft.createRL(name), builder);
        return builder;
    }

    private HolderSet<Item> withoutSpecific(TagKey<Item> tag) {
        return new AndHolderSet<>(List.of(
                new OrHolderSet<>(List.of(createHolderSet(tag))), // FIXME we are using an 'or' here to fix a crash with nbtops (https://github.com/MinecraftForge/MinecraftForge/issues/9043)
                new NotHolderSet<>(getRegistry(Registry.ITEM_REGISTRY), createHolderSet(ECTags.Items.PURE_ORES_SPECIFICS))
        ));
    }

    @Nonnull
    @Override
    public String getName() {
        return "ElementalCraft Pure Ore Loaders";
    }

}
