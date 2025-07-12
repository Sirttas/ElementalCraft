package sirttas.elementalcraft.data.predicate.block.pipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import sirttas.dpanvil.api.predicate.block.BlockPosPredicateType;
import sirttas.dpanvil.api.predicate.block.IBlockPosPredicate;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.data.predicate.block.ECBlockPosPredicateTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public record HasPipeUpgrade(PipeUpgradeType<?> type) implements IPipePredicate {

    public static final String NAME = "has_pipe_upgrade";
    public static final MapCodec<HasPipeUpgrade> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            PipeUpgradeTypes.REGISTRY.byNameCodec().fieldOf(ECNames.PIPE_UPGRADE).forGetter(HasPipeUpgrade::type)
    ).apply(builder, HasPipeUpgrade::new));

    @Override
    public boolean test(@Nonnull ElementPipeBlockEntity pipe, @Nullable Direction direction) {
        if (direction == null) {
            return pipe.getUpgrades().values().stream().anyMatch(this::test);
        }
        return test(pipe.getUpgrade(direction));
    }

    private boolean test(PipeUpgrade upgrade) {
        return upgrade != null && upgrade.getType() == type;
    }

    @Override
    public BlockPosPredicateType<? extends IBlockPosPredicate> getType() {
        return ECBlockPosPredicateTypes.HAS_PIPE_UPGRADE.get();
    }

    @Override
    @Nonnull
    public List<Component> getTooltip() {
        return List.of(Component.translatable("tooltip.elementalcraft.predicate.pipe_upgrade", type.asItem().getDescription()));
    }
}
