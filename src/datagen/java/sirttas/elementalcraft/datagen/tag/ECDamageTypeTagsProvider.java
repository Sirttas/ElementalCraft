package sirttas.elementalcraft.datagen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.damagesource.ECDamageTypes;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class ECDamageTypeTagsProvider extends DamageTypeTagsProvider {

    public ECDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, provider, ElementalCraftApi.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
        tag(DamageTypeTags.IS_FIRE).add(ECDamageTypes.HOLY_FIRE);
        tag(DamageTypeTags.BYPASSES_ARMOR).add(ECDamageTypes.HOLY_FIRE);
        tag(DamageTypeTags.BYPASSES_COOLDOWN).add(ECDamageTypes.HOLY_FIRE);
        tag(Tags.DamageTypes.IS_MAGIC).add(ECDamageTypes.HOLY_FIRE);
        tag(ECTags.DamageTypes.BYPASSES_JEWELS).add(ECDamageTypes.HOLY_FIRE);

        tag(ECTags.DamageTypes.BLOCKED_BY_TORTOISE_JEWEL).addTag(DamageTypeTags.DAMAGES_HELMET);
    }

}
