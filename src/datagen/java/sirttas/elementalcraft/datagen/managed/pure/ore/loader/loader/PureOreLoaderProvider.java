package sirttas.elementalcraft.datagen.managed.pure.ore.loader.loader;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.holdersets.AndHolderSet;
import net.minecraftforge.registries.holdersets.NotHolderSet;
import sirttas.dpanvil.api.data.AbstractManagedDataBuilderProvider;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.pureore.loader.IPureOreLoader;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PureOreLoaderProvider extends AbstractManagedDataBuilderProvider<IPureOreLoader, IPureOreLoaderBuilder> {

    private static final ResourceLocation FIXED_RESONATING = ElementalCraft.createRL("resonating");
    private static final ResourceLocation FIXED_URANINITE = ElementalCraft.createRL("uraninite");

    private static final String NAMESPACE_PATTERN = "^(forge|blue_skies)$";

    private static final String DEEPSLATE_PATTERN = "^deepslate_";

    public PureOreLoaderProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, ElementalCraft.PURE_ORE_LOADERS_MANAGER, IPureOreLoaderBuilder.ENCODER);
    }

    @Override
    protected void collectBuilders(HolderLookup.Provider registries) {
        ores("ores", ECTags.Items.PURE_ORES_SOURCE_ORES)
                .namespacePattern(NAMESPACE_PATTERN);
        rawMaterials("raw_materials", ECTags.Items.PURE_ORES_SOURCE_RAW_MATERIALS)
                .namespacePattern(NAMESPACE_PATTERN);
        rawMaterialsBlocks("raw_material_blocks", ECTags.Items.PURE_ORES_SOURCE_RAW_MATERIAL_BLOCKS)
                .namespacePattern(NAMESPACE_PATTERN);

        standard("geore_shards", ECTags.Items.PURE_ORES_SOURCE_GEORE_SHARDS)
                .patterns(DEEPSLATE_PATTERN, "_?shard$")
                .consumption(5000)
                .inputSize(4)
                .outputSize(5)
                .luckRatio(1);
        standard("geore_blocks", ECTags.Items.PURE_ORES_SOURCE_GEORE_BLOCKS)
                .patterns(DEEPSLATE_PATTERN, "_?block$")
                .consumption(6250)
                .outputSize(5)
                .luckRatio(4);

        fixedName("resonating", ECTags.Items.PURE_ORES_SOURCE_RESONANT_ORE, FIXED_RESONATING)
                .luckRatio(5);

        fixedName("raw_uraninite", ECTags.Items.PURE_ORES_SOURCE_RAW_URANINITE, FIXED_URANINITE)
                .luckRatio(2);
        fixedName("poor_uraninite", ECTags.Items.PURE_ORES_SOURCE_POOR_URANINITE, FIXED_URANINITE)
                .consumption(3750)
                .outputSize(3)
                .luckRatio(2);
        fixedName("uraninite", ECTags.Items.PURE_ORES_SOURCE_URANINITE, FIXED_URANINITE)
                .consumption(5000)
                .outputSize(5)
                .luckRatio(2);
        fixedName("dense_uraninite", ECTags.Items.PURE_ORES_SOURCE_DENSE_URANINITE, FIXED_URANINITE)
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
        return (PatternPureOreLoaderBuilder) pattern(name, tag, "^storage_blocks/raw_?(?!_?materials)")
                .patterns(DEEPSLATE_PATTERN, "^raw_?", "_?block$")
                .consumption(15000)
                .outputSize(12)
                .luckRatio(18);
    }

    protected PatternPureOreLoaderBuilder standard(String name, TagKey<Item> tag) {
        return pattern(name, tag, "^" + name + "/");
    }

    protected FixedNamePureOreLoaderBuilder fixedName(String name, TagKey<Item> tag, ResourceLocation fixedName) {
        return (FixedNamePureOreLoaderBuilder) add(ElementalCraft.createRL(name), new FixedNamePureOreLoaderBuilder(createHolderSet(tag), fixedName));
    }

    protected PatternPureOreLoaderBuilder pattern(String name, TagKey<Item> tag, String pattern) {
        return pattern(name, withoutSpecific(tag), pattern);
    }

    protected PatternPureOreLoaderBuilder pattern(String name, HolderSet<Item> holderSet, String pattern) {
        return (PatternPureOreLoaderBuilder) add(ElementalCraft.createRL(name), new PatternPureOreLoaderBuilder(holderSet, pattern));
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

    @Nonnull
    @Override
    public String getName() {
        return "ElementalCraft Pure Ore Loaders";
    }

}
