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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.Lazy;
import sirttas.elementalcraft.ElementalCraftUtils;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.tag.ECTags;

import java.util.List;
import java.util.Optional;

public class GroveShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(GroveShrineBlock.NAME);

	private static final Lazy<HolderSet.Named<Item>> MYSTICAL_GROVE_FLOWERS = Lazy.of(() -> ECTags.Items.getTag(ECTags.Items.MYSTICAL_GROVE_FLOWERS));
	private static final Lazy<HolderSet.Named<Item>> GROVE_SHRINE_FLOWERS = Lazy.of(() -> ECTags.Items.getTag(ECTags.Items.GROVE_SHRINE_FLOWERS));

	public GroveShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.GROVE_SHRINE, pos, state, PROPERTIES_KEY);
	}

	private Optional<BlockPos> findGrass() {
		List<BlockPos> positions = getBlocksInRange()
				.filter(this::canPlant)
				.map(BlockPos::above)
				.toList();

		return positions.isEmpty() ? Optional.empty() : Optional.of(positions.get(this.level.random.nextInt(positions.size())));
	}

	private boolean canPlant(BlockPos pos) {
		BlockPos up = pos.above();

		return level.getBlockState(pos).getBlock() == Blocks.GRASS_BLOCK && level.getBlockState(up).isAir();
	}

	@Override
	public AABB getRangeBoundingBox() {
		var range = getRange();

		return ElementalCraftUtils.stitchAABB(new AABB(this.getBlockPos()).inflate(range, 0, range).expandTowards(0, -1, 0).expandTowards(0, 1, 0));
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
