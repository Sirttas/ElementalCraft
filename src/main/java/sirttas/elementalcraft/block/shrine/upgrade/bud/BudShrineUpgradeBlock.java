package sirttas.elementalcraft.block.shrine.upgrade.bud;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sirttas.dpanvil.api.DataPackAnvilApi;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.HorizontalShrineUpgradeBlock;

import javax.annotation.Nonnull;

public class BudShrineUpgradeBlock extends HorizontalShrineUpgradeBlock {

	public static final String SPRINGALINE_NAME = "shrine_upgrade_springaline";
	public static final String CERTUS_QUARTZ_NAME = "shrine_upgrade_certus_quartz";
	public static final MapCodec<BudShrineUpgradeBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.xmap(l -> DataPackAnvilApi.createResourceKey(ElementalCraftApi.SHRINE_UPGRADE_MANAGER_KEY, l), ResourceKey::identifier).fieldOf("shrine_upgrade").forGetter(p -> p.key),
            propertiesCodec()
    ).apply(instance, BudShrineUpgradeBlock::new));

	private static final VoxelShape BASE_NORTH = Block.box(5D, 5D, 4D, 11D, 11D, 10D);
	private static final VoxelShape PLATE_SOUTH_NORTH = Block.box(6D, 6D, 10D, 10D, 10D, 11D);
	private static final VoxelShape PLATE_UP_NORTH = Block.box(6D, 11D, 5D, 10D, 12D, 9D);
	private static final VoxelShape PLATE_DOWN_NORTH = Block.box(6D, 4D, 5D, 10D, 5D, 9D);
	private static final VoxelShape PLATE_WEST_NORTH = Block.box(4D, 6D, 5D, 5D, 10D, 9D);
	private static final VoxelShape PLATE_EAST_NORTH = Block.box(11D, 6D, 5D, 12D, 10D, 9D);
	private static final VoxelShape PIPE_NORTH = Block.box(7D, 7D, 0D, 9D, 9D, 4D);
	private static final VoxelShape SHAPE_NORTH = Shapes.or(BASE_NORTH, PIPE_NORTH, PLATE_SOUTH_NORTH, PLATE_UP_NORTH, PLATE_DOWN_NORTH, PLATE_WEST_NORTH, PLATE_EAST_NORTH);

	private static final VoxelShape BASE_SOUTH = Block.box(5D, 5D, 6D, 11D, 11D, 12D);
	private static final VoxelShape PLATE_NORTH_SOUTH = Block.box(6D, 6D, 5D, 10D, 10D, 6D);
	private static final VoxelShape PLATE_UP_SOUTH = Block.box(6D, 11D, 7D, 10D, 12D, 11D);
	private static final VoxelShape PLATE_DOWN_SOUTH = Block.box(6D, 4D, 7D, 10D, 5D, 11D);
	private static final VoxelShape PLATE_WEST_SOUTH = Block.box(4D, 6D, 7D, 5D, 10D, 11D);
	private static final VoxelShape PLATE_EAST_SOUTH = Block.box(11D, 6D, 7D, 12D, 10D, 11D);
	private static final VoxelShape PIPE_SOUTH = Block.box(7D, 7D, 12D, 9D, 9D, 16D);
	private static final VoxelShape SHAPE_SOUTH = Shapes.or(BASE_SOUTH, PIPE_SOUTH, PLATE_NORTH_SOUTH, PLATE_UP_SOUTH, PLATE_DOWN_SOUTH, PLATE_WEST_SOUTH, PLATE_EAST_SOUTH);

	private static final VoxelShape BASE_WEST = Block.box(4D, 5D, 5D, 10D, 11D, 11D);
	private static final VoxelShape PLATE_EAST_WEST = Block.box(10D, 6D, 6D, 11D, 10D, 10D);
	private static final VoxelShape PLATE_UP_WEST = Block.box(5D, 11D, 6D, 9D, 12D, 10D);
	private static final VoxelShape PLATE_DOWN_WEST = Block.box(5D, 4D, 6D, 9D, 5D, 10D);
	private static final VoxelShape PLATE_NORTH_WEST = Block.box(5D, 6D, 4D, 9D, 10D, 5D);
	private static final VoxelShape PLATE_SOUTH_WEST = Block.box(5D, 6D, 11D, 9D, 10D, 12D);
	private static final VoxelShape PIPE_WEST = Block.box(0D, 7D, 7D, 4D, 9D, 9D);
	private static final VoxelShape SHAPE_WEST = Shapes.or(BASE_WEST, PIPE_WEST, PLATE_EAST_WEST, PLATE_UP_WEST, PLATE_DOWN_WEST, PLATE_NORTH_WEST, PLATE_SOUTH_WEST);

	private static final VoxelShape BASE_EAST = Block.box(6D, 5D, 5D, 12D, 11D, 11D);
	private static final VoxelShape PLATE_WEST_EAST = Block.box(5D, 6D, 6D, 6D, 10D, 10D);
	private static final VoxelShape PLATE_UP_EAST = Block.box(7D, 11D, 6D, 11D, 12D, 10D);
	private static final VoxelShape PLATE_DOWN_EAST = Block.box(7D, 4D, 6D, 11D, 5D, 10D);
	private static final VoxelShape PLATE_NORTH_EAST = Block.box(7D, 6D, 4D, 11D, 10D, 5D);
	private static final VoxelShape PLATE_SOUTH_EAST = Block.box(7D, 6D, 11D, 11D, 10D, 12D);
	private static final VoxelShape PIPE_EAST = Block.box(12D, 7D, 7D, 16D, 9D, 9D);
	private static final VoxelShape SHAPE_EAST = Shapes.or(BASE_EAST, PIPE_EAST,PLATE_WEST_EAST, PLATE_UP_EAST, PLATE_DOWN_EAST, PLATE_NORTH_EAST, PLATE_SOUTH_EAST);

    private final ResourceKey<@NotNull ShrineUpgrade> key;

	public BudShrineUpgradeBlock(ResourceKey<@NotNull ShrineUpgrade> key, BlockBehaviour.Properties properties) {
		super(key, properties);
        this.key = key;
    }

	@Override
	protected @NotNull MapCodec<BudShrineUpgradeBlock> codec() {
		return CODEC;
	}

	public static VoxelShape getShape(BlockState state) {
		return switch (state.getValue(FACING)) {
			case EAST -> SHAPE_EAST;
			case SOUTH -> SHAPE_SOUTH;
			case WEST -> SHAPE_WEST;
			default -> SHAPE_NORTH;
		};
	}
	
	@Nonnull
    @Override
	public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
		return getShape(state);
	}
}
