package sirttas.elementalcraft.block.source;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.ECBlocks;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;

import javax.annotation.Nonnull;

public class SourceBlock extends AbstractECEntityBlock implements IElementTypeProvider {

	private static final VoxelShape SHAPE = Block.box(4D, 0D, 4D, 12D, 8D, 12D);

	public static final String NAME = "source";
	public static final String NAME_FIRE = "fire_" + NAME;
	public static final String NAME_WATER = "water_" + NAME;
	public static final String NAME_EARTH = "earth_" + NAME;
	public static final String NAME_AIR = "air_" + NAME;

	public static final MapCodec<SourceBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ElementType.forGetter(SourceBlock::getElementType),
			propertiesCodec()
	).apply(instance, SourceBlock::new));

	private final ElementType elementType;

	public SourceBlock(ElementType elementType, BlockBehaviour.Properties properties) {
		super(properties);
		this.elementType = elementType;
	}

	@NotNull
	@Override
	public ElementType getElementType() {
		return elementType;
	}

	@Override
	protected @NotNull MapCodec<SourceBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new SourceBlockEntity(pos, state);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public boolean canBeReplaced(@Nonnull BlockState state, @Nonnull BlockPlaceContext context) {
		return super.canBeReplaced(state, context) && BlockEntityHelper.getBlockEntityAs(context.getLevel(), context.getClickedPos(), SourceBlockEntity.class)
				.map(s -> !s.isStabilized())
				.orElse(true);
	}

	@Nonnull
	@Override
	public RenderShape getRenderShape(@Nonnull BlockState state) {
		return RenderShape.INVISIBLE;
	}

	public static SourceBlock findSourceBlock(ElementType type) {
		return switch (type) {
			case FIRE -> ECBlocks.FIRE_SOURCE.get();
			case WATER -> ECBlocks.WATER_SOURCE.get();
			case EARTH -> ECBlocks.EARTH_SOURCE.get();
			case AIR -> ECBlocks.AIR_SOURCE.get();
			default -> throw new IllegalArgumentException("Invalid type: " + type);
		};
	}
}
