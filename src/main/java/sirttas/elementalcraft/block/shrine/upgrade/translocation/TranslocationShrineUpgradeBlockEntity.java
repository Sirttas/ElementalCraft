package sirttas.elementalcraft.block.shrine.upgrade.translocation;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TranslocationShrineUpgradeBlockEntity extends AbstractECBlockEntity {

    private BlockPos target;

    public TranslocationShrineUpgradeBlockEntity( BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.TRANSLOCATION_SHRINE_UPGRADE, pos, state);
    }

    @Nullable
    public BlockPos getTarget() {
        return target;
    }

    public void setTarget(@Nullable BlockPos target) {
        this.target = target;
    }

    @Override
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);
        if (tag.contains(ECNames.TARGET)) {
            target = NbtUtils.readBlockPos(tag.getCompound(ECNames.TARGET));
        }
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        super.saveAdditional(tag);
        if (target != null) {
            tag.put(ECNames.TARGET, NbtUtils.writeBlockPos(target));
        }
    }
}
