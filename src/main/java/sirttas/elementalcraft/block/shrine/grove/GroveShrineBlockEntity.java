package sirttas.elementalcraft.block.shrine.grove;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class GroveShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + GroveShrineBlock.NAME) public static final BlockEntityType<GroveShrineBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.WATER)
			.period(ECConfig.COMMON.groveShrinePeriod.get())
			.consumeAmount(ECConfig.COMMON.groveShrineConsumeAmount.get())
			.range(ECConfig.COMMON.groveShrineRange.get());


	public GroveShrineBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, PROPERTIES);
	}

	private Optional<BlockPos> findGrass() {
		int range = getIntegerRange();

		List<BlockPos> positions = IntStream.range(-range, range + 1)
				.mapToObj(x -> IntStream.range(-range, range + 1).mapToObj(z -> IntStream.range(-1, 3).mapToObj(y -> new BlockPos(worldPosition.getX() + x, worldPosition.getY() + y, worldPosition.getZ() + z))))
				.flatMap(s -> s.flatMap(s2 -> s2)).filter(this::canPlant).map(BlockPos::above).toList();
		return positions.isEmpty() ? Optional.empty() : Optional.of(positions.get(this.level.random.nextInt(positions.size())));
	}

	private boolean canPlant(BlockPos pos) {
		BlockPos up = pos.above();

		return level.getBlockState(pos).getBlock() == Blocks.GRASS_BLOCK && level.getBlockState(up).isAir();
	}

	@Override
	public AABB getRangeBoundingBox() {
		int range = getIntegerRange();

		return new AABB(this.getBlockPos()).inflate(range, 0, range).expandTowards(0, -1, 0).expandTowards(0, 1, 0);
	}

	@Override
	protected boolean doPeriod() {
		if (level instanceof ServerLevel) {
			return findGrass().map(p -> {
				BlockItem item = findFlower();
				
				item.place(new DirectionalPlaceContext(level, p, Direction.DOWN, new ItemStack(item), Direction.UP));
				level.levelEvent(2005, p, 0);
				return true;
			}).orElse(false);
		}
		return false;
	}

	private BlockItem findFlower() {
		List<BlockItem> flowers = (this.hasUpgrade(ShrineUpgrades.MYSTICAL_GROVE) ? ECTags.Items.MYSTICAL_GROVE_FLOWERS.getValues().stream()
				: ECTags.Items.GROVE_SHRINE_FLOWERS.getValues().stream().filter(item -> !ECTags.Items.GROVE_SHRINE_BLACKLIST.contains(item)))
				.filter(BlockItem.class::isInstance).map(BlockItem.class::cast).toList();

		return flowers.get(this.level.random.nextInt(flowers.size()));
	}
}
