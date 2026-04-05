package sirttas.elementalcraft.block.cover;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Coverable {

    BlockCapability<@NotNull Coverable, Void> CAPABILITY = BlockCapability.createVoid(ElementalCraftApi.createRL("coverable"), Coverable.class);

    boolean hasFrame();
    void putFrame();

    @Nonnull BlockState getCoverState();
    @Nonnull BlockState getUncoveredState();

    default boolean isCovered() {
        return hasFrame() && !getCoverState().isAir();
    }

    default boolean showCover(@Nullable Player player) {
        return isCovered() && (player == null || EntityHelper.handStream(player).noneMatch(stack -> !stack.isEmpty() && stack.is(ECTags.Items.COVER_HIDING)));
    }

    default @NotNull BlockState loadCoverState(@NotNull ValueInput input) {
        return input.read(ECNames.COVER, BlockState.CODEC).orElseGet(Blocks.AIR::defaultBlockState);
    }

    default void saveCoverState(@NotNull ValueOutput output) {
        var coverState = getCoverState();

        if (!coverState.isAir()) {
            output.store(ECNames.COVER, BlockState.CODEC, coverState);
        } else {
            output.discard(ECNames.COVER);
        }
    }
}
