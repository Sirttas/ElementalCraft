package sirttas.elementalcraft.block.anchor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TranslocationAnchorsSaveData extends SavedData {

    // "client" as in "client side"
    public static final Set<BlockPos> CLIENT_SET = new HashSet<>();

    private final Set<BlockPos> set;

    public TranslocationAnchorsSaveData() {
        set = new HashSet<>();
    }

    public TranslocationAnchorsSaveData(@Nonnull CompoundTag compoundTag, @Nonnull HolderLookup.Provider provider) {
        var tagList = compoundTag.getList("list", 10);
        set = HashSet.newHashSet(tagList.size());

        for (int i = 0; i < tagList.size(); i++) {
            NbtUtils.readBlockPos(tagList.getCompound(i), "pos").ifPresent(set::add);
        }
    }

    @Nullable
    public static TranslocationAnchorsSaveData get(@Nonnull Level level) {
        return level instanceof ServerLevel serverLevel ? serverLevel.getDataStorage().computeIfAbsent(new Factory<>(TranslocationAnchorsSaveData::new, TranslocationAnchorsSaveData::new, null), "elementalcraft_translocation_anchors") : null;
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag compoundTag, @NotNull HolderLookup.Provider provider) {
        var tagList = new ListTag();

        for (BlockPos blockPos : set) {
            var tag = new CompoundTag();

            tag.put("pos", NbtUtils.writeBlockPos(blockPos));
            tagList.add(tag);
        }
        compoundTag.put("list", tagList);
        return compoundTag;
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
