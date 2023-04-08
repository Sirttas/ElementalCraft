package sirttas.elementalcraft.block.pipe;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.block.shape.ShapeHelper;

import java.util.Map;

public class ElementPipeShapes {

    public static final VoxelShape BASE_SHAPE = Block.box(6.5D, 6.5D, 6.5D, 9.5D, 9.5D, 9.5D);

    public static final VoxelShape FRAME_SHAPE = Shapes.join(Shapes.block(),
            Shapes.or(Block.box(0D, 1D, 1D, 16D, 15D, 15D), Block.box(1D, 0D, 1D, 15D, 16D, 15D), Block.box(1D, 1D, 0D, 15D, 15D, 16D)),
            BooleanOp.ONLY_FIRST);

    public static final Map<Direction, VoxelShape> SECTION_SHAPES = ShapeHelper.directionShapes(Block.box(7D, 9.5D, 7D, 9D, 16D, 9D));
    public static final Map<Direction, VoxelShape> EXTRACTION_SHAPES = ShapeHelper.directionShapes(Shapes.or(Block.box(6D, 15D, 6D, 10D, 16D, 10D), Block.box(6.5D, 14D, 6.5D, 9.5D, 15D, 9.5D)));

    private ElementPipeShapes() {}

}
