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
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.loot.LootHelper;

public class HarvestShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + HarvestShrineBlock.NAME) public static final TileEntityType<HarvestShrineBlockEntity> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.EARTH).periode(ECConfig.COMMON.harvestShrinePeriode.get())
			.consumeAmount(ECConfig.COMMON.harvestShrineConsumeAmount.get()).range(ECConfig.COMMON.harvestShrineRange.get());

	protected static final List<Direction> UPGRRADE_DIRECTIONS = ImmutableList.of(Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	public HarvestShrineBlockEntity() {
		super(TYPE, PROPERTIES);
	}

	private Optional<BlockPos> findCrop() {
		int range = getIntegerRange();

		return IntStream.range(-range, range + 1)
				.mapToObj(x -> IntStream.range(-range, range + 1).mapToObj(z -> IntStream.range(-3, 1).mapToObj(y -> new BlockPos(worldPosition.getX() + x, worldPosition.getY() + y, worldPosition.getZ() + z))))
				.flatMap(s -> s.flatMap(s2 -> s2)).filter(p -> {
					BlockState blockstate = level.getBlockState(p);
					Block block = blockstate.getBlock();

					return block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(blockstate);
				}).findAny();
	}

	private void handlePlanting(BlockPos pos, IItemProvider provider, List<ItemStack> loots) {
		Item item = provider.asItem();

		if (this.hasUpgrade(ShrineUpgrades.PLANTING)) {
			loots.stream().filter(stack -> stack.getItem().equals(item)).findFirst().ifPresent(seeds -> {
				if (item instanceof BlockItem && ((BlockItem) item).place(new DirectionalPlaceContext(this.level, pos, Direction.DOWN, seeds, Direction.UP)).consumesAction()) {
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

		return new AxisAlignedBB(this.getBlockPos()).inflate(range, 0, range).expandTowards(0, -2, 0).move(0, -1, 0);
	}


	@Override
	protected boolean doTick() {
		if (level instanceof ServerWorld && !level.isClientSide) {
			return findCrop().map(p -> {
				List<ItemStack> loots = LootHelper.getDrops((ServerWorld) level, p);
				Block block = level.getBlockState(p).getBlock();

				level.destroyBlock(p, false);
				handlePlanting(p, block, loots);
				loots.forEach(stack -> Block.popResource(level, p, stack));
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
