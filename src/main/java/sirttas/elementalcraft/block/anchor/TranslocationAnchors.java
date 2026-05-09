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

public class TranslocationAnchors extends SavedData {

    // "client" as in "client side"
    public static final Set<BlockPos> CLIENT_SET = new HashSet<>();
    private static final Codec<TranslocationAnchors> CODEC = BlockPos.CODEC.listOf().xmap(TranslocationAnchors::new, t -> List.copyOf(t.anchors()));
    public static final SavedDataType<@NotNull TranslocationAnchors> TYPE = new SavedDataType<>(
            ElementalCraftApi.identifier("translocation_anchors"),
            TranslocationAnchors::new,
            CODEC,
            null);

    private final Set<BlockPos> set;

    public TranslocationAnchors() {
        set = new HashSet<>();
    }

    public TranslocationAnchors(Collection<BlockPos> list) {
        set = new HashSet<>(list);
    }

    @Nullable
    public static TranslocationAnchors get(@Nonnull Level level) {
        return level instanceof ServerLevel serverLevel ? serverLevel.getDataStorage().computeIfAbsent(TYPE) : null;
    }

    public Set<BlockPos> anchors() {
        return Set.copyOf(set);
    }

    public boolean has(BlockPos pos) {
        return set.contains(pos);
    }

    public void add(BlockPos pos) {
        set.add(pos);
        setDirty();
    }

    public void remove(BlockPos pos) {
        set.remove(pos);
        setDirty();
    }

}
