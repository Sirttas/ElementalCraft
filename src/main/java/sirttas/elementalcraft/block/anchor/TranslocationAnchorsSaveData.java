package sirttas.elementalcraft.block.anchor;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TranslocationAnchorsSaveData extends SavedData {

    // "client" as in "client side"
    public static final Set<BlockPos> CLIENT_SET = new HashSet<>();
    private static final Codec<TranslocationAnchorsSaveData> CODEC = BlockPos.CODEC.listOf().xmap(TranslocationAnchorsSaveData::new, t -> List.copyOf(t.getAnchors()));
    public static final SavedDataType<@NotNull TranslocationAnchorsSaveData> TYPE = new SavedDataType<>(
            ElementalCraftApi.createRL("translocation_anchors"),
            TranslocationAnchorsSaveData::new,
            CODEC,
            null);

    private final Set<BlockPos> set;

    public TranslocationAnchorsSaveData() {
        set = new HashSet<>();
    }
    public TranslocationAnchorsSaveData(Collection<BlockPos> list) {
        set = new HashSet<>(list);
    }

    @Nullable
    public static TranslocationAnchorsSaveData get(@Nonnull Level level) {
        return level instanceof ServerLevel serverLevel ? serverLevel.getDataStorage().computeIfAbsent(TYPE) : null;
    }

    public Set<BlockPos> getAnchors() {
        return Set.copyOf(set);
    }

    public void addAnchor(BlockPos pos) {
        set.add(pos);
        setDirty();
    }

    public void removeAnchor(BlockPos pos) {
        set.remove(pos);
        setDirty();
    }
}
