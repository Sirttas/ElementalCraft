package sirttas.elementalcraft.datagen.language;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.block.source.SourceBlock;
import sirttas.elementalcraft.jewel.Jewel;
import sirttas.elementalcraft.spell.Spell;

import java.util.function.Supplier;

public abstract class AbstractECLanguageProvider extends LanguageProvider {

    protected AbstractECLanguageProvider(PackOutput output, String locale) {
        super(output, ElementalCraftApi.MODID, locale);
    }

    public void add(ElementType key, String name) {
        add(key.getDescriptionId(), name);
    }

    public void addReceptacle(Supplier<? extends SourceBlock> key, String name) {
        add(key.get().asItem(), name);
    }

    public void addSpell(Supplier<? extends Spell> key, String name) {
        add(key.get(), name);
    }

    public void add(Spell key, String name) {
        add(key.getDescriptionId(), name);
    }

    public void addRune(ResourceKey<Rune> key, String name) {
        var id = key.location();

        add("elementalcraft.rune." + id.getNamespace() + '.' + id.getPath(), name);
    }

    public void addPipeUpgrade(Supplier<? extends PipeUpgradeType<?>> key, String name) {
        add(key.get(), name);
    }

    public void add(PipeUpgradeType<?> key, String name) {
        add(key.getDescriptionId(), name);
    }

    public void addJewel(Supplier<? extends Jewel> key, String name) {
        add(key.get(), name);
    }

    public void add(Jewel key, String name) {
        add(key.getDescriptionId(), name);
    }

    public void addTooltip(String key, String name) {
        add("tooltip.elementalcraft." + key, name);
    }

    public void addPureOre(ResourceLocation key, String name) {
        addTooltip(key.getNamespace() + '.' + key.getPath(), name);
    }

    public void addPureOre(String key, String name) {
        addPureOre(ResourceLocation.fromNamespaceAndPath("c", key), name);
    }

    public void addSourceTrait(String key, String name) {
        add("source_trait.elementalcraft." + key, name);
    }

    public void addAdvancement(String key, String title, String description) {
        add("advancements.elementalcraft." + key + ".title", title);
        add("advancements.elementalcraft." + key + ".description", description);
    }

    public void addPatchouliCategory(String key, String title, String description) {
        add("elementalcraft.category." + key, title);
        add("elementalcraft.category." + key + ".desc", description);
    }

    public void addPatchouliEntry(String key, String name) {
        add("elementalcraft.entry." + key, name);
    }

    public void addPatchouliPages(String key, String ...name) {
        for (int i = 0; i < name.length; i++) {
            add("elementalcraft.page." + key + i, name[i]);
        }
    }
}
