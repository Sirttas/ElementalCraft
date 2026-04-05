package sirttas.elementalcraft.block.shrine.upgrade.translocation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.component.ECDataComponents;

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
    public void loadAdditional(@Nonnull CompoundTag tag, @Nonnull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        NbtUtils.readBlockPos(tag, ECNames.TARGET_POS).ifPresent(this::setTarget);
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag, @Nonnull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if (target != null) {
            tag.put(ECNames.TARGET_POS, NbtUtils.writeBlockPos(target));
        }
    }

    @Override
    protected void applyImplicitComponents(@NotNull DataComponentGetter getter) {
        super.applyImplicitComponents(getter);
        var pos = getter.get(ECDataComponents.TARGET_POS);

        if (pos != null) {
            setTarget(pos);
        }
    }

    @Override
    protected void collectImplicitComponents(@NotNull DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        if (target != null) {
            builder.set(ECDataComponents.TARGET_POS, target);
        }
    }

    @Override
    @Deprecated
    public void removeComponentsFromTag(@NotNull ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard(ECNames.TARGET_POS);
    }
}
