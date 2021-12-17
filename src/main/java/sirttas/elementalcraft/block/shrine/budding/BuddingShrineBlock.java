package sirttas.elementalcraft.block.shrine.budding;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shape.ECShapes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlock;

import javax.annotation.Nonnull;

public class BuddingShrineBlock extends AbstractShrineBlock<BuddingShrineBlockEntity> {

	public static final String NAME = "buddingshrine";

	public static final EnumProperty<CrystalType> CRYSTAL_TYPE = EnumProperty.create("crystal_type", CrystalType.class);
	
	private static final VoxelShape BASE_1 = Block.box(2D, 10D, 2D, 14D, 12D, 14D);
	private static final VoxelShape BASE_2 = Block.box(0D, 12D, 0D, 16D, 14D, 16D);
	private static final VoxelShape PLATE = Block.box(1D, 14D, 1D, 15D, 16D, 15D);

	private static final VoxelShape SHAPE = Shapes.or(ECShapes.SHRINE_SHAPE, BASE_1, BASE_2, PLATE);

	public BuddingShrineBlock() {
		super(ElementType.EARTH);
		this.registerDefaultState(this.defaultBlockState().setValue(CRYSTAL_TYPE, CrystalType.AMETHYST).setValue(WATERLOGGED, false));
	}

	@Nonnull
    @Override
	@Deprecated
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, CRYSTAL_TYPE);
	}
	
	public enum CrystalType implements StringRepresentable {
		AMETHYST("amethyst"),
		SPRINGALINE("springaline");

		private final String name;
		
		CrystalType(String name) {
			this.name = name;
		}
		
		@Nonnull
        @Override
		public String getSerializedName() {
			return name;
		}
	}
}
