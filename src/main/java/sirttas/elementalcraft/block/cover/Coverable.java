package sirttas.elementalcraft.block.cover;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Coverable {

    BlockCapability<Coverable, Void> CAPABILITY = BlockCapability.createVoid(ElementalCraftApi.createRL("coverable"), Coverable.class);

    boolean hasFrame();
    void putFrame();

    @Nonnull BlockState getCoverState();

    default boolean isCovered() {
        return hasFrame() && !getCoverState().isAir();
    }

    default boolean showCover(@Nullable Player player) {
        return isCovered() && (player == null || EntityHelper.handStream(player).noneMatch(stack -> !stack.isEmpty() && stack.is(ECTags.Items.PIPE_COVER_HIDING)));
    }

    default @NotNull BlockState loadCoverState(@NotNull CompoundTag compound, HolderLookup.@NotNull Provider provider) {
        return compound.contains(ECNames.COVER) ? NbtUtils.readBlockState(provider.lookupOrThrow(Registries.BLOCK), compound.getCompound(ECNames.COVER)) : Blocks.AIR.defaultBlockState();
    }

    default void saveCoverState(@NotNull CompoundTag compound) {
        var coverState = getCoverState();

        if (!coverState.isAir()) {
            compound.put(ECNames.COVER, NbtUtils.writeBlockState(coverState));
        } else if (compound.contains(ECNames.COVER)) {
            compound.remove(ECNames.COVER);
        }
    }
}
