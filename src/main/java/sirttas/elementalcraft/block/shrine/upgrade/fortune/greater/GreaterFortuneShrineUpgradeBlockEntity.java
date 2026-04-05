package sirttas.elementalcraft.block.shrine.upgrade.fortune.greater;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.api.rune.handler.RuneHandler;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;

import javax.annotation.Nonnull;

public class GreaterFortuneShrineUpgradeBlockEntity extends AbstractECBlockEntity {

    private final IRuneHandler runeHandler;

    public GreaterFortuneShrineUpgradeBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.GREATER_FORTUNE_SHRINE_UPGRADE, pos, state);
        runeHandler = new RuneHandler(1, this::setChanged);
    }

    @Nonnull
    public IRuneHandler getRuneHandler() {
        return runeHandler;
    }

    @Override
    public void loadAdditional(@Nonnull ValueInput input) {
        super.loadAdditional(input);
        input.readChild(ECNames.RUNE_HANDLER, runeHandler);
    }

    @Override
    public void saveAdditional(@Nonnull ValueOutput output) {
        super.saveAdditional(output);
        output.putChild(ECNames.RUNE_HANDLER, runeHandler);
    }
}
