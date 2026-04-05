package sirttas.elementalcraft.block.pipe.upgrade.render;

import sirttas.elementalcraft.block.pipe.section.ElementPipeSectionRenderState;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeType;

public class PipeUpgradeRenderState {

    public int lightCoords;
    public PipeUpgradeType<?> type;

    public static void extractBase(PipeUpgrade pipeUpgrade, PipeUpgradeRenderState state, ElementPipeSectionRenderState sectionRenderState) {
        state.type = pipeUpgrade.getType();
        state.lightCoords = sectionRenderState.lightCoords;
    }
}
