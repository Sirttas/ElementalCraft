package sirttas.elementalcraft.range;

import sirttas.elementalcraft.config.ECConfig;

public class RangeRenderTimer { // TODO capability

    private int timer = 0;

    public void tick() {
        if (timer > 0) {
            timer--;
        }
    }
    public boolean showsRange() {
        return this.timer > 0;
    }

    public void startShowingRange() {
        this.timer = ECConfig.CLIENT.rangeDisplayDuration.get();
    }
}
