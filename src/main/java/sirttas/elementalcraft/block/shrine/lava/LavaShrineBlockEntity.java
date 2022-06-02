package sirttas.elementalcraft.block.shrine.lava;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class LavaShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + LavaShrineBlock.NAME) public static final BlockEntityType<LavaShrineBlockEntity> TYPE = null;

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(LavaShrineBlock.NAME);

	protected static final List<Direction> UPGRADE_DIRECTIONS = List.of(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	public LavaShrineBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, PROPERTIES_KEY);
	}

	private Optional<BlockPos> findRock() {
		int range = Math.round(this.getProperties().range());

		return IntStream.range(-range, range + 1).mapToObj(x -> IntStream.range(-range, range + 1).mapToObj(z -> new BlockPos(worldPosition.getX() + x, worldPosition.getY() + 1, worldPosition.getZ() + z))).flatMap(s -> s)
				.filter(p -> level.getBlockState(p).is(ECTags.Blocks.LAVASHRINE_LIQUIFIABLES)).findAny();

	}

	@Override
	public AABB getRangeBoundingBox() {
		int range = Math.round(this.getProperties().range());

		return new AABB(this.getBlockPos()).inflate(range, 0, range).move(0, 1, 0);
	}

	@Override
	protected boolean doPeriod() {
		return findRock().map(p -> {
			level.setBlockAndUpdate(p, Blocks.LAVA.defaultBlockState());
			level.levelEvent(1501, p, 0);
			return true;
		}).orElse(false);
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRADE_DIRECTIONS;
	}
}
