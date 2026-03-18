package sirttas.elementalcraft.renderer.state;

import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public class RangeRenderState {

    private AABB range;
    private BlockPos pos;
    private int color;

    public void update(BlockEntity blockEntity, AABB range, int color) {
        this.update(range, blockEntity.getBlockPos(), color);
    }

    public void update(AABB range, BlockPos pos, int color) {
        this.range = range;
        this.pos = pos;
        this.color = color;
    }

    public void clear() {
        this.range = null;
    }

    public void submit() {
        if (range == null) {
            return;
        }
        Gizmos.cuboid(range.move(pos), GizmoStyle.stroke(color));
    }
}
