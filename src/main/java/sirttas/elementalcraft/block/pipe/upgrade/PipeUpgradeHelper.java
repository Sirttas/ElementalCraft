package sirttas.elementalcraft.block.pipe.upgrade;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;

import javax.annotation.Nonnull;

public class PipeUpgradeHelper {

    private PipeUpgradeHelper() {}

    public static PipeUpgrade load(ElementPipeBlockEntity pipe, Direction direction, CompoundTag tag, @Nonnull HolderLookup.Provider provider) {
        if (tag == null) {
            return null;
        }

        var type = PipeUpgradeTypes.REGISTRY.get(ResourceLocation.parse(tag.getString("id")));

        if (type != null) {
            var upgrade = type.create(pipe, direction);

            upgrade.load(tag, provider);
            return upgrade;
        }
        return null;
    }
}
