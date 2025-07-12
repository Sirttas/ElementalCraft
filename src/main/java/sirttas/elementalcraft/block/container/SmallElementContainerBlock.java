package sirttas.elementalcraft.block.container;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;

import javax.annotation.Nonnull;

public class SmallElementContainerBlock extends AbstractElementContainerBlock {

	public static final String NAME = "small_container";

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	public static final MapCodec<SmallElementContainerBlock> CODEC = simpleCodec(SmallElementContainerBlock::new);

	private static final VoxelShape GLASS = Block.box(3D, 3D, 3D, 13D, 13D, 13D);

	private static final VoxelShape CONNECTOR_NORTH_1 = Block.box(5D, 5D, 1D, 11D, 11D, 3D);
	private static final VoxelShape CONNECTOR_NORTH_2 = Block.box(6D, 6D, 0D, 10D, 10D, 1D);
	private static final VoxelShape CONNECTOR_SOUTH_1 = Block.box(5D, 5D, 13D, 11D, 11D, 15D);
	private static final VoxelShape CONNECTOR_SOUTH_2 = Block.box(6D, 6D, 15D, 10D, 10D, 16D);

	private static final VoxelShape CONNECTOR_WEST_1 = Block.box(1D, 5D, 5D, 3D, 11D, 11D);
	private static final VoxelShape CONNECTOR_WEST_2 = Block.box(0D, 6D, 6D, 1D, 10D, 10D);
	private static final VoxelShape CONNECTOR_EAST_1 = Block.box(13D, 5D, 5D, 15D, 11D, 11D);
	private static final VoxelShape CONNECTOR_EAST_2 = Block.box(15D, 6D, 6D, 16D, 10D, 10D);

	private static final VoxelShape CONNECTOR_DOWN_1 = Block.box(5D, 1D, 5D, 11D, 3D, 11D);
	private static final VoxelShape CONNECTOR_DOWN_2 = Block.box(6D, 0D, 6D, 10D, 1D, 10D);
	private static final VoxelShape CONNECTOR_UP_1 = Block.box(5D, 13D, 5D, 11D, 15D, 11D);
	private static final VoxelShape CONNECTOR_UP_2 = Block.box(6D, 15D, 6D, 10D, 16D, 10D);

	private static final VoxelShape SHAPE = Shapes.or(GLASS, CONNECTOR_NORTH_1, CONNECTOR_NORTH_2, CONNECTOR_SOUTH_1, CONNECTOR_SOUTH_2, CONNECTOR_WEST_1, CONNECTOR_WEST_2, CONNECTOR_EAST_1,
			CONNECTOR_EAST_2, CONNECTOR_DOWN_1, CONNECTOR_DOWN_2, CONNECTOR_UP_1, CONNECTOR_UP_2);

	public SmallElementContainerBlock(Properties properties) {
		super(properties, PROPERTIES);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected @NotNull MapCodec<SmallElementContainerBlock> codec() {
		return CODEC;
	}
}
