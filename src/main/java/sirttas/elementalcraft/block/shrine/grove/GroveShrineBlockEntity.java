package sirttas.elementalcraft.block.shrine.grove;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.tag.ECTags;

public class GroveShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + GroveShrineBlock.NAME) public static final TileEntityType<GroveShrineBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.WATER).periode(ECConfig.COMMON.groveShrinePeriode.get()).consumeAmount(ECConfig.COMMON.groveShrineConsumeAmount.get())
			.range(ECConfig.COMMON.groveShrineRange.get());


	public GroveShrineBlockEntity() {
		super(TYPE, PROPERTIES);
	}

	private Optional<BlockPos> findGrass() {
		int range = getIntegerRange();

		List<BlockPos> positions = IntStream.range(-range, range + 1)
				.mapToObj(x -> IntStream.range(-range, range + 1).mapToObj(z -> IntStream.range(-1, 3).mapToObj(y -> new BlockPos(worldPosition.getX() + x, worldPosition.getY() + y, worldPosition.getZ() + z))))
				.flatMap(s -> s.flatMap(s2 -> s2)).filter(this::canPlant).map(BlockPos::above).collect(Collectors.toList());
		return positions.isEmpty() ? Optional.empty() : Optional.of(positions.get(this.level.random.nextInt(positions.size())));
	}

	@SuppressWarnings("deprecation")
	private boolean canPlant(BlockPos pos) {
		BlockPos up = pos.above();

		return level.getBlockState(pos).getBlock() == Blocks.GRASS_BLOCK && level.getBlockState(up).isAir(this.level, up);
	}

	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		int range = getIntegerRange();

		return new AxisAlignedBB(this.getBlockPos()).inflate(range, 0, range).expandTowards(0, -1, 0).expandTowards(0, 1, 0);
	}

	@Override
	protected boolean doTick() {
		if (level instanceof ServerWorld) {
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
				.filter(BlockItem.class::isInstance).map(BlockItem.class::cast).collect(Collectors.toList());

		return flowers.get(this.level.random.nextInt(flowers.size()));
	}
}
