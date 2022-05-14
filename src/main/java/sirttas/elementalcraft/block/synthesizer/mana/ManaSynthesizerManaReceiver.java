package sirttas.elementalcraft.block.synthesizer.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.config.ECConfig;
import vazkii.botania.api.mana.IManaReceiver;

public class ManaSynthesizerManaReceiver implements IManaReceiver {

    private int mana;
    private final ManaSynthesizerBlockEntity manaSynthesizer;

    ManaSynthesizerManaReceiver(ManaSynthesizerBlockEntity manaSynthesizer) {
        this.mana = 0;
        this.manaSynthesizer = manaSynthesizer;
    }

    @Override
    public Level getManaReceiverLevel() {
        return manaSynthesizer.getLevel();
    }

    @Override
    public BlockPos getManaReceiverPos() {
        return manaSynthesizer.getBlockPos();
    }

    @Override
    public int getCurrentMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    @Override
    public boolean isFull() {
        return this.mana >= ECConfig.COMMON.manaSythesizerManaCapacity.get();
    }

    @Override
    public void receiveMana(int mana) {
        this.mana = Math.max(0, Math.min(getCurrentMana() + mana, ECConfig.COMMON.manaSythesizerManaCapacity.get()));
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }
}
