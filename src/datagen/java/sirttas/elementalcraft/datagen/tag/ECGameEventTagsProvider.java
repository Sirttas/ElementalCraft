package sirttas.elementalcraft.datagen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.GameEventTagsProvider;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class ECGameEventTagsProvider extends GameEventTagsProvider {

    public ECGameEventTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, provider, ElementalCraftApi.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@Nonnull HolderLookup.Provider provider) {
        tag(ECTags.GameEvents.SYNTHESIZABLE_TO_AIR).add(
                GameEvent.STEP.key(),
                GameEvent.DRINK.key(),
                GameEvent.EAT.key(),
                GameEvent.ELYTRA_GLIDE.key(),
                GameEvent.TELEPORT.key(),
                GameEvent.SWIM.key(),
                GameEvent.HIT_GROUND.key(),
                GameEvent.SPLASH.key(),
                GameEvent.FLAP.key());
    }

}
