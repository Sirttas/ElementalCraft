package sirttas.elementalcraft.block.pipe.upgrade.priority;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.shape.ShapeHelper;

import java.util.Map;

public class PipePriorityRingsPipeUpgrade extends PipeUpgrade {

    public static final String NAME = "pipe_priority_rings";

    private static final Map<Direction, VoxelShape> SHAPES = ShapeHelper.directionShapes(Shapes.or(
            Block.box(6.5D, 10D, 6.5D, 9.5D, 11D, 9.5D),
            Block.box(6.5D, 12D, 6.5D, 9.5D, 13D, 9.5D)
    ));

    public PipePriorityRingsPipeUpgrade(ElementPipeBlockEntity pipe, Direction direction) {
        super(PipeUpgradeTypes.PIPE_PRIORITY_RINGS.get(), pipe, direction);
    }

    @Override
    public VoxelShape getShape() {
        return SHAPES.get(this.getDirection());
    }

}
