package sirttas.elementalcraft.block.shrine.upgrade.translocation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;
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

    @VisibleForTesting
    public void setTarget(@Nullable BlockPos target) {
        this.target = target;
    }

    @Override
    public void loadAdditional(@Nonnull ValueInput input) {
        super.loadAdditional(input);
        input.read(ECNames.TARGET_ANCHOR, BlockPos.CODEC).ifPresent(this::setTarget);
    }

    @Override
    protected void saveAdditional(@Nonnull ValueOutput output) {
        super.saveAdditional(output);
        if (target != null) {
            output.store(ECNames.TARGET_ANCHOR, BlockPos.CODEC, target);
        } else {
            output.discard(ECNames.TARGET_ANCHOR);
        }
    }

    @Override
    protected void applyImplicitComponents(@NotNull DataComponentGetter getter) {
        super.applyImplicitComponents(getter);
        var pos = getter.get(ECDataComponents.TARGET_ANCHOR);

        if (pos != null) {
            setTarget(pos);
        }
    }

    @Override
    protected void collectImplicitComponents(@NotNull DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        if (target != null) {
            builder.set(ECDataComponents.TARGET_ANCHOR, target);
        }
    }

    @Override
    @Deprecated
    public void removeComponentsFromTag(@NotNull ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard(ECNames.TARGET_ANCHOR);
    }
}
