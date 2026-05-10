package sirttas.elementalcraft.datagen.managed.pure.ore.loader;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.holdersets.AndHolderSet;
import net.neoforged.neoforge.registries.holdersets.NotHolderSet;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.pureore.loader.IPureOreLoader;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PureOreLoaderProvider extends AbstractManagedDataBuilderProvider<IPureOreLoader, IPureOreLoaderBuilder> {

    private static final Identifier FIXED_RESONATING = ElementalCraftApi.identifier("resonating");
    private static final Identifier FIXED_URANINITE = ElementalCraftApi.identifier("uraninite");

    private static final String NAMESPACE_PATTERN = "^(c|forge|blue_skies)$";
    private static final String DEEPSLATE_PATTERN = "^deepslate_";

    public PureOreLoaderProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, ElementalCraft.PURE_ORE_LOADERS_MANAGER, IPureOreLoaderBuilder.ENCODER);
    }

    @Override
    protected void collectBuilders(HolderLookup.Provider registries) {
        ores("ores", ECTags.Items.PURE_ORES_SOURCES_ORES)
                .namespacePattern(NAMESPACE_PATTERN);
        rawMaterials("raw_materials", ECTags.Items.PURE_ORES_SOURCES_RAW_MATERIALS)
                .namespacePattern(NAMESPACE_PATTERN);
        rawMaterialsBlocks("raw_material_blocks", ECTags.Items.PURE_ORES_SOURCES_RAW_MATERIAL_BLOCKS)
                .namespacePattern(NAMESPACE_PATTERN);

        standard("clusters", ECTags.Items.PURE_ORES_SOURCES_CLUSTERS)
                .patterns(DEEPSLATE_PATTERN, "_?cluster$")
                .luckRatio(1);
        standard("clumps", ECTags.Items.PURE_ORES_SOURCES_CLUMPS)
                .patterns(DEEPSLATE_PATTERN, "_?clump$");
        storageBlock("resin_blocks", ECTags.Items.PURE_ORES_SOURCES_RESIN_BLOCKS, "");

        standard("geore_shards", ECTags.Items.PURE_ORES_SOURCES_GEORE_SHARDS)
                .patterns(DEEPSLATE_PATTERN, "_?shard$")
                .consumption(5000)
                .inputSize(4)
                .outputSize(5)
                .luckRatio(1);
        standard("geore_blocks", ECTags.Items.PURE_ORES_SOURCES_GEORE_BLOCKS)
                .patterns(DEEPSLATE_PATTERN, "_?block$")
                .consumption(6250)
                .outputSize(5)
                .luckRatio(4);

        fixedName("resonating", ECTags.Items.PURE_ORES_SOURCES_RESONANT_ORE, FIXED_RESONATING)
                .luckRatio(5);

        fixedName("raw_uraninite", ECTags.Items.PURE_ORES_SOURCES_RAW_URANINITE, FIXED_URANINITE)
                .luckRatio(2);
        fixedName("poor_uraninite", ECTags.Items.PURE_ORES_SOURCES_POOR_URANINITE, FIXED_URANINITE)
                .consumption(3750)
                .outputSize(3)
                .luckRatio(2);
        fixedName("uraninite", ECTags.Items.PURE_ORES_SOURCES_URANINITE, FIXED_URANINITE)
                .consumption(5000)
                .outputSize(5)
                .luckRatio(2);
        fixedName("dense_uraninite", ECTags.Items.PURE_ORES_SOURCES_DENSE_URANINITE, FIXED_URANINITE)
                .consumption(10000)
                .outputSize(10)
                .luckRatio(2);
    }

    protected PatternPureOreLoaderBuilder ores(String name, TagKey<Item> tag) {
        return (PatternPureOreLoaderBuilder) pattern(name, tag, "^ores/")
                .patterns(DEEPSLATE_PATTERN, "_?ore$")
                .luckRatio(5);
    }

    protected PatternPureOreLoaderBuilder rawMaterials(String name, TagKey<Item> tag) {
        return (PatternPureOreLoaderBuilder) pattern(name, tag, "^raw_materials/")
                .patterns(DEEPSLATE_PATTERN, "^raw_?", "_?raw$")
                .consumption(5000)
                .inputSize(3)
                .outputSize(4)
                .luckRatio(2);
    }

    protected PatternPureOreLoaderBuilder rawMaterialsBlocks(String name, TagKey<Item> tag) {
        return (PatternPureOreLoaderBuilder) storageBlock(name, tag, "raw_?(?!_?materials)")
                .patterns("^raw_?")
                .consumption(15000)
                .outputSize(12)
                .luckRatio(18);
    }

    protected PatternPureOreLoaderBuilder storageBlock(String name, TagKey<Item> tag, String suffix) {
        return (PatternPureOreLoaderBuilder) pattern(name, tag, "^storage_blocks/" + suffix)
                .patterns(DEEPSLATE_PATTERN, "_?block$")
                .consumption(22500)
                .outputSize(18);
    }

    protected PatternPureOreLoaderBuilder standard(String name, TagKey<Item> tag) {
        return pattern(name, tag, "^" + name + "/");
    }

    protected FixedNamePureOreLoaderBuilder fixedName(String name, TagKey<Item> tag, Identifier fixedName) {
        return (FixedNamePureOreLoaderBuilder) add(ElementalCraftApi.identifier(name), new FixedNamePureOreLoaderBuilder(createHolderSet(tag), fixedName));
    }

    protected PatternPureOreLoaderBuilder pattern(String name, TagKey<Item> tag, String pattern) {
        return pattern(name, withoutSpecific(tag), pattern);
    }

    protected PatternPureOreLoaderBuilder pattern(String name, HolderSet<Item> holderSet, String pattern) {
        return (PatternPureOreLoaderBuilder) add(ElementalCraftApi.identifier(name), new PatternPureOreLoaderBuilder(holderSet, pattern));
    }

    private HolderSet<Item> withoutSpecific(TagKey<Item> tag) {
        var specific = createHolderSet(ECTags.Items.PURE_ORES_SPECIFICS);

        return new AndHolderSet<>(List.of(
                createHolderSet(tag),
                new NotHolderSet<>(getRegistry(Registries.ITEM), specific) { // FIXME https://github.com/MinecraftForge/MinecraftForge/issues/9634
                    @Override
                    public boolean canSerializeIn(HolderOwner<Item> holderOwner) {
                        return specific.canSerializeIn(holderOwner);
                    }
                }
        ));
    }

    @Override
    public String getName() {
        return "ElementalCraft Pure Ore Loaders";
    }

}
