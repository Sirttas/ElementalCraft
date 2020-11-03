package sirttas.elementalcraft.block.shrine.harvest;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.TileShrine;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.loot.LootHelper;

public class TileHarvestShrine extends TileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockHarvestShrine.NAME) public static TileEntityType<TileHarvestShrine> TYPE;

	private static final Properties PROPERTIES = Properties.create(ElementType.EARTH).periode(ECConfig.COMMON.harvestShrinePeriode.get())
			.consumeAmount(ECConfig.COMMON.harvestShrineConsumeAmount.get()).range(ECConfig.COMMON.harvestShrineRange.get());

	protected static final List<Direction> UPGRRADE_DIRECTIONS = ImmutableList.of(Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	public TileHarvestShrine() {
		super(TYPE, PROPERTIES);
	}

	private Optional<BlockPos> findCrop() {
		int range = getIntegerRange();

		return IntStream.range(-range, range + 1)
				.mapToObj(x -> IntStream.range(-range, range + 1).mapToObj(z -> IntStream.range(-3, 1).mapToObj(y -> new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z))))
				.flatMap(s -> s.flatMap(s2 -> s2)).filter(p -> {
					BlockState blockstate = world.getBlockState(p);
					Block block = blockstate.getBlock();

					return block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(blockstate);
				}).findAny();
	}

	private void handlePlanting(BlockPos pos, List<ItemStack> loots) {
		if (this.hasUpgrade(ShrineUpgrades.PLANTING.get())) {
			loots.stream().filter(stack -> Tags.Items.SEEDS.contains(stack.getItem())).findFirst().ifPresent(seeds -> {
				Item item = seeds.getItem();

				if (item instanceof BlockItem && ((BlockItem) item).tryPlace(new DirectionalPlaceContext(this.world, pos, Direction.DOWN, seeds, Direction.UP)).isSuccessOrConsume()) {
					seeds.shrink(1);
					if (seeds.isEmpty()) {
						loots.remove(seeds);
					}
				}
			});
		}
	}

	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		int range = getIntegerRange();

		return new AxisAlignedBB(this.getPos()).grow(range, 0, range).expand(0, -2, 0).offset(0, -1, 0);
	}


	@Override
	protected boolean doTick() {
		if (world instanceof ServerWorld && !world.isRemote) {
			return findCrop().map(p -> {
				List<ItemStack> loots = LootHelper.getDrops((ServerWorld) world, p);

				world.destroyBlock(p, false);
				handlePlanting(p, loots);
				loots.forEach(stack -> Block.spawnAsEntity(world, p, stack));
				return true;
			}).orElse(false);
		}
		return false;
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		return UPGRRADE_DIRECTIONS;
	}
}
