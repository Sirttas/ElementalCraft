package sirttas.elementalcraft.block.shrine.grove;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class GroveShrineBlockEntity extends AbstractShrineBlockEntity {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + GroveShrineBlock.NAME) public static final BlockEntityType<GroveShrineBlockEntity> TYPE = null;

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(GroveShrineBlock.NAME);

	private static final Lazy<HolderSet.Named<Item>> MYSTICAL_GROVE_FLOWERS = Lazy.of(() -> ECTags.Items.getTag(ECTags.Items.MYSTICAL_GROVE_FLOWERS));
	private static final Lazy<HolderSet.Named<Item>> GROVE_SHRINE_FLOWERS = Lazy.of(() -> ECTags.Items.getTag(ECTags.Items.GROVE_SHRINE_FLOWERS));

	public GroveShrineBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, PROPERTIES_KEY);
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
		List<BlockItem> flowers = (this.hasUpgrade(ShrineUpgrades.MYSTICAL_GROVE) ? MYSTICAL_GROVE_FLOWERS.get().stream() : GROVE_SHRINE_FLOWERS.get().stream().filter(item -> !item.is(ECTags.Items.GROVE_SHRINE_BLACKLIST)))
				.map(Holder::value)
				.mapMulti(ElementalCraftUtils.cast(BlockItem.class))
				.toList();

		return flowers.get(this.level.random.nextInt(flowers.size()));
	}
}
