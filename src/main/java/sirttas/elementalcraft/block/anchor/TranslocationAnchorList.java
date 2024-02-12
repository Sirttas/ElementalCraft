package sirttas.elementalcraft.block.anchor;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TranslocationAnchorList extends SavedData {

    // "client" as in "client side"
    public static final List<BlockPos> CLIENT_LIST = new ArrayList<>();

    private final List<BlockPos> list;

    public TranslocationAnchorList() {
        list = new ArrayList<>();
    }

    public TranslocationAnchorList(@Nonnull CompoundTag compoundTag) {
        var tagList = compoundTag.getList("list", 10);
        list = new ArrayList<>(tagList.size());

        for (int i = 0; i < tagList.size(); i++) {
            list.add(NbtUtils.readBlockPos(tagList.getCompound(i)));
        }
    }

    @Nullable
    public static TranslocationAnchorList get(@Nonnull Level level) {
        return level instanceof ServerLevel serverLevel ? serverLevel.getDataStorage().computeIfAbsent(new Factory<>(TranslocationAnchorList::new, TranslocationAnchorList::new, null), "elementalcraft_translocation_anchors") : null;
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag compoundTag) {
        var tagList = new ListTag();

        for (BlockPos blockPos : list) {
            tagList.add(NbtUtils.writeBlockPos(blockPos));
        }
        compoundTag.put("list", tagList);
        return compoundTag;
    }

    public List<BlockPos> getAnchors() {
        return List.copyOf(list);
    }

    public void addAnchor(BlockPos pos) {
        list.add(pos);
        setDirty();
    }

    public void removeAnchor(BlockPos pos) {
        list.remove(pos);
        setDirty();
    }
}
