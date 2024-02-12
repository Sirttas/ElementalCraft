package sirttas.elementalcraft.block.pipe.upgrade;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;

public class PipeUpgradeHelper {

    private PipeUpgradeHelper() {}

    public static PipeUpgrade load(ElementPipeBlockEntity pipe, Direction direction, CompoundTag tag) {
        if (tag == null) {
            return null;
        }

        var type = PipeUpgradeTypes.REGISTRY.get(new ResourceLocation(tag.getString("id")));

        if (type != null) {
            var upgrade = type.create(pipe, direction);

            upgrade.load(tag);
            return upgrade;
        }
        return null;
    }
}
