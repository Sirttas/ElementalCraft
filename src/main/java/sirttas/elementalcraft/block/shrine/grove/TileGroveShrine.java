package sirttas.elementalcraft.block.shrine.grove;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractTileShrine;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.tag.ECTags;

public class TileGroveShrine extends AbstractTileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockGroveShrine.NAME) public static final TileEntityType<TileGroveShrine> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.WATER).periode(ECConfig.COMMON.groveShrinePeriode.get()).consumeAmount(ECConfig.COMMON.groveShrineConsumeAmount.get())
			.range(ECConfig.COMMON.groveShrineRange.get());


	public TileGroveShrine() {
		super(TYPE, PROPERTIES);
	}

	private Optional<BlockPos> findGrass() {
		int range = getIntegerRange();

		List<BlockPos> positions = IntStream.range(-range, range + 1)
				.mapToObj(x -> IntStream.range(-range, range + 1).mapToObj(z -> IntStream.range(-1, 3).mapToObj(y -> new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z))))
				.flatMap(s -> s.flatMap(s2 -> s2)).filter(this::canPlant).map(BlockPos::up).collect(Collectors.toList());
		return positions.isEmpty() ? Optional.empty() : Optional.of(positions.get(this.world.rand.nextInt(positions.size())));
	}

	@SuppressWarnings("deprecation")
	private boolean canPlant(BlockPos pos) {
		BlockPos up = pos.up();

		return world.getBlockState(pos).getBlock() == Blocks.GRASS_BLOCK && world.getBlockState(up).isAir(this.world, up);
	}

	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		int range = getIntegerRange();

		return new AxisAlignedBB(this.getPos()).grow(range, 0, range).expand(0, -1, 0).expand(0, 1, 0);
	}

	@Override
	protected boolean doTick() {
		if (world instanceof ServerWorld) {
			return findGrass().map(p -> {
				BlockItem item = findFlower();
				
				item.tryPlace(new DirectionalPlaceContext(world, p, Direction.DOWN, new ItemStack(item), Direction.UP));
				world.playEvent(2005, p, 0);
				return true;
			}).orElse(false);
		}
		return false;
	}

	private BlockItem findFlower() {
		List<BlockItem> flowers = (this.hasUpgrade(ShrineUpgrades.MYSTICAL_GROVE) ? ECTags.Items.MYSTICAL_GROVE_FLOWERS.getAllElements().stream()
				: ItemTags.FLOWERS.getAllElements().stream().filter(item -> !item.getRegistryName().getNamespace().equalsIgnoreCase("botania"))) // FIXME non-required tags seems glitchy so we filter out all botania flowers
				.filter(BlockItem.class::isInstance).map(BlockItem.class::cast).collect(Collectors.toList());

		return flowers.get(this.world.rand.nextInt(flowers.size()));
	}
}
