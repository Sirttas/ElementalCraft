package sirttas.elementalcraft.block.pipe.section;

import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.core.Direction;
import sirttas.elementalcraft.block.pipe.ConnectionType;
import sirttas.elementalcraft.block.pipe.upgrade.render.PipeUpgradeRenderState;

import java.util.ArrayList;
import java.util.List;

public class ElementPipeSectionRenderState {
    public int lightCoords;
    public Direction side;
    public ConnectionType connectionType;
    public final List<BlockStateModelPart> parts = new ArrayList<>();
    public PipeUpgradeRenderState upgradeState;

    public boolean isEmpty() {
        return parts.isEmpty() && upgradeState == null;
    }
}
