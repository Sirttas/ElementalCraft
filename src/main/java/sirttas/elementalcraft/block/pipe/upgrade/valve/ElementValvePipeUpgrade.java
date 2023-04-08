package sirttas.elementalcraft.block.pipe.upgrade.valve;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.pipe.ConnectionType;
import sirttas.elementalcraft.block.pipe.ElementPipeBlockEntity;
import sirttas.elementalcraft.block.pipe.ElementPipeShapes;
import sirttas.elementalcraft.block.pipe.upgrade.PipeUpgrade;
import sirttas.elementalcraft.block.pipe.upgrade.type.PipeUpgradeTypes;
import sirttas.elementalcraft.block.shape.ShapeHelper;

import javax.annotation.Nonnull;
import java.util.Map;

public class ElementValvePipeUpgrade extends PipeUpgrade {

    public static final String NAME = "element_valve";

    private static final VoxelShape BASE_SHAPE = Shapes.or(
            Block.box(6.5D, 10D, 6.5D, 9.5D, 10.5D, 9.5D),
            Block.box(6.5D, 12.5D, 6.5D, 9.5D, 13D, 9.5D)
    );

    private static final Map<Direction, VoxelShape> OPEN_SHAPES = ShapeHelper.directionShapes(Shapes.or(
            BASE_SHAPE,
            ElementPipeShapes.SECTION_SHAPES.get(Direction.UP)
    ));

    private static final Map<Direction, VoxelShape> CLOSE_SHAPES = ShapeHelper.directionShapes(Shapes.or(
            BASE_SHAPE,
            Block.box(7D, 9.5D, 7D, 9D, 10D, 9D),
            Block.box(7D, 13D, 7D, 9D, 16D, 9D),
            Block.box(7.5D, 10.5D, 7.5D, 8.5D, 11D, 8.5D),
            Block.box(7.5D, 12D, 7.5D, 8.5D, 12.5D, 8.5D)
    ));

    public ElementValvePipeUpgrade(ElementPipeBlockEntity pipe, Direction direction) {
        super(PipeUpgradeTypes.ELEMENT_VALVE.get(), pipe, direction);
    }

    public boolean isOpen() {
        return this.getPipe().isPowered();
    }

    @Override
    public VoxelShape getShape() {
        return isOpen() ? OPEN_SHAPES.get(this.getDirection()) : CLOSE_SHAPES.get(this.getDirection());
    }

    @Override
    public boolean canTransfer(ElementType type, ConnectionType connection) {
        return isOpen();
    }

    @Override
    public boolean replaceSection() {
        return true;
    }

    @Override
    public boolean replaceExtraction() {
        return false;
    }

    @Override
    @Nonnull
    public RenderShape getRenderShape() {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

}
