package sirttas.elementalcraft.block.shrine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.range.RangeVariants;
import sirttas.elementalcraft.block.anchor.TranslocationAnchorsSaveData;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;
import sirttas.elementalcraft.block.shrine.upgrade.translocation.TranslocationShrineUpgradeBlockEntity;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.range.RangeHelper;
import sirttas.elementalcraft.range.RangeRenderTimer;
import sirttas.elementalcraft.spell.Spells;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class AbstractShrineBlockEntity extends AbstractECBlockEntity implements IElementTypeProvider {

	protected static final List<Direction> DEFAULT_UPGRADE_DIRECTIONS = List.of(Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	private final Holder<IConfigurableBlockEntityProperties> properties;

	private final Map<Direction, ShrineUpgrade> upgrades = new EnumMap<>(Direction.class);
	private final Map<ShrineUpgrade.BonusType, Float> upgradeMultipliers = new EnumMap<>(ShrineUpgrade.BonusType.class);
	private final RangeRenderTimer rangeRenderTimer = new RangeRenderTimer();

	protected final ShrineElementStorage elementStorage;

	private boolean running = false;
	private double tick = 0;
	private BlockPos targetPos;
	private AABB range;

	protected AbstractShrineBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, Holder<IConfigurableBlockEntityProperties> properties, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
		this.elementStorage = new ShrineElementStorage(this);
		this.properties = properties;
		this.targetPos = pos;
		this.range = new AABB(pos);
	}

	protected int consumeElement(int i) {
		this.running = true;
		return elementStorage.extractElement(i, false);
	}

	protected abstract boolean doPeriod();

	public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractShrineBlockEntity shrine) {
		if (!shrine.isTargetPosValid(shrine.targetPos)) {
			shrine.setChanged();
		}

		if (shrine.isDirty()) {
			shrine.refresh();
		}

		var period = shrine.getPeriod();
		var consumeAmount = shrine.getConsumeAmount();
		var running = shrine.running;

		if (!shrine.isPowered()) {
			shrine.tick++;
			if (period <= 0) {
				ElementalCraftApi.LOGGER.warn("Shrine period should not be 0, shrine: {} at {}", shrine.getBlockState().getBlock(), shrine.getBlockPos());
				period = 1;
			}
			while (shrine.tick >= period) {
				shrine.running = false;
				if (shrine.elementStorage.getElementAmount() >= consumeAmount && shrine.doPeriod()) {
					shrine.consumeElement(consumeAmount);
				}
				shrine.tick -= period;
			}
		} else {
			shrine.running = false;
		}

		if (running != shrine.running) {
			shrine.setChanged();
		}
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, AbstractShrineBlockEntity shrine) {
		shrine.rangeRenderTimer.tick();
	}

	public void refresh() {
		var blockPos = getBlockPos();

		if (!this.hasLevel()) {
			targetPos = blockPos;
			range = new AABB(blockPos);
			return;
		}

		elementStorage.refresh();

		this.upgrades.clear();
		this.upgradeMultipliers.clear();
		getUpgradeDirections().forEach(direction -> {
			BlockPos pos = blockPos.relative(direction);
			BlockState state = this.level.getBlockState(pos);
			Block block = state.getBlock();

			if (block instanceof AbstractShrineUpgradeBlock upgradeBlock && upgradeBlock.getFacing(state) == direction.getOpposite()) {
				ShrineUpgrade upgrade = upgradeBlock.getUpgrade();

				if (upgrade != null) {
					setUpgrade(direction, upgrade);
				}
			}
		});
		getUpgradeDirections().forEach(direction -> {
			BlockPos pos = blockPos.relative(direction);
			BlockState state = this.level.getBlockState(pos);

			if (!state.canSurvive(this.level, pos)) {
				this.level.destroyBlock(pos, true);
			}
		});
		targetPos = upgrades.entrySet().stream()
				.filter(e -> e.getValue().is(ShrineUpgrades.TRANSLOCATION))
				.findFirst()
				.flatMap(e -> BlockEntityHelper.getBlockEntityAs(this.level, blockPos.relative(e.getKey()), TranslocationShrineUpgradeBlockEntity.class))
				.map(TranslocationShrineUpgradeBlockEntity::getTarget)
				.filter(this::isTargetPosValid)
				.orElse(blockPos);
		range = lookupRange();
	}

	private boolean isTargetPosValid(BlockPos p) {
		var blockPos = this.getBlockPos();

		if (blockPos.equals(p)) {
			return true;
		} else if (p == null || this.level == null) {
			return false;
		} else if (this.level.isClientSide) {
			return true;
		}

		var maxRange = Spells.TRANSLOCATION.get().getRange(null);

		if (maxRange <= 0) {
			throw new IllegalStateException("Translocation spell ranges should not be 0");
		}

		var maxRangeSq = maxRange * maxRange;
		var rangeSq = p.distSqr(blockPos);

		if (rangeSq > maxRangeSq) {
			return false;
		}

		var list = TranslocationAnchorsSaveData.get(this.level);

		return list != null && list.getAnchors().contains(p);
	}

	protected float getMultiplier(ShrineUpgrade.BonusType type) {
		return this.upgradeMultipliers.getOrDefault(type, 1F);
	}

	public int getUpgradeCount(ShrineUpgrade upgrade) {
		return upgrade == null ? 0 : (int) upgrades.values().stream().filter(upgrade::equals).count();
	}

	public int getUpgradeCount(ResourceKey<ShrineUpgrade> key) {
		return key == null ? 0 : (int) upgrades.values().stream().filter(u -> u.is(key)).count();
	}

	public boolean hasUpgrade(ShrineUpgrade upgrade) {
		return getUpgradeCount(upgrade) > 0;
	}

	public boolean hasUpgrade(ResourceKey<ShrineUpgrade> key) {
		return getUpgradeCount(key) > 0;
	}

	@Nullable
	public Direction getUpgradeDirection(ShrineUpgrade upgrade) {
		return getUpgradeDirection(e -> e.getValue().equals(upgrade));
	}

	@Nullable
	public Direction getUpgradeDirection(ResourceKey<ShrineUpgrade> key) {
		return getUpgradeDirection(e -> e.getValue().is(key));
	}

	@Nullable
	private Direction getUpgradeDirection(Predicate<Map.Entry<Direction, ShrineUpgrade>> predicate) {
		return upgrades.entrySet().stream()
				.filter(predicate)
				.map(Map.Entry::getKey)
				.findFirst()
				.orElse(null);
	}

	private void setUpgrade(Direction direction, ShrineUpgrade upgrade) {
		ShrineUpgrade old = upgrades.get(direction);

		if (old != null) {
			old.getBonuses().forEach((type, bonus) -> upgradeMultipliers.put(type, getMultiplier(type) / bonus));
		}
		upgrades.put(direction, upgrade);
		upgrade.getBonuses().forEach((type, bonus) -> upgradeMultipliers.put(type, getMultiplier(type) * bonus));
	}

	public Collection<ShrineUpgrade> getAllUpgrades() {
		return upgrades.values();
	}

	public List<Direction> getUpgradeDirections() {
		return DEFAULT_UPGRADE_DIRECTIONS;
	}

	public boolean isRunning() {
		return running;
	}

	public boolean showsRange() {
		return rangeRenderTimer.showsRange();
	}

	public void startShowingRange() {
		rangeRenderTimer.startShowingRange();
	}

	public BlockPos getTargetPos() {
		return targetPos;
	}

	public Stream<BlockPos> getBlocksInRange() {
		return RangeHelper.getBlocksInAABB(getRange());
	}

	@Override
	public @NotNull ElementType getElementType() {
		return getProperties().getElementType();
	}

	public final AABB getRange() {
		return range;
	}

	protected AABB lookupRange() {
		var ranges = getProperties().ranges();
		var key = RangeVariants.DEFAULT_KEY;

		if (this.hasUpgrade(ShrineUpgrades.TRANSLOCATION) && ranges.containsKey(RangeVariants.TRANSLOCATION_KEY)) {
			key = RangeVariants.TRANSLOCATION_KEY;
		} else if (!ranges.containsKey(RangeVariants.DEFAULT_KEY)) {
			return new AABB(this.getBlockPos());
		}
		return lookupRange(key);
	}

	protected AABB lookupRange(Direction direction) {
		return lookupRange(direction.getSerializedName());
	}

	protected AABB lookupRange(String key) {
		var box = getProperties().ranges().get(key).value().scaleBox(getMultiplier(BonusType.RANGE)).move(targetPos);
		var top = this.hasLevel() ? Math.min(this.level.getMaxBuildHeight(), box.maxY) : box.maxY;
		var bottom = this.hasLevel() ? Math.max(this.level.getMinBuildHeight(), box.minY) : box.minY;

		return new AABB(box.minX, bottom, box.minZ, box.maxX, top, box.maxZ);
	}

	public int getConsumeAmount() {
		return Math.round(getProperties().consumption() * getMultiplier(BonusType.ELEMENT_CONSUMPTION));
	}

	public double getPeriod() {
		return getProperties().period() * getMultiplier(BonusType.SPEED);
	}

	@Nonnull
	public ShrineProperties getProperties() {
		if (properties.isBound() && this.properties.value() instanceof ShrineProperties shrineProperties) {
			return shrineProperties;
		}
		return ShrineProperties.DEFAULT;
	}

	public int getCapacity() {
		return Math.round(getProperties().capacity() * getMultiplier(BonusType.CAPACITY));
	}

	public double getStrength() {
		return this.getStrength(0);
	}

	public double getStrength(int index) {
		var strength = getProperties().strength();

		if (strength.size() <= index) {
			ElementalCraftApi.LOGGER.warn("Shrine strength index out of bounds: {} for shrine {}",
					() -> index,
					() -> BuiltInRegistries.BLOCK.getKey(this.getBlockState().getBlock()));
			return 1;
		}
		var value = getProperties().strength().get(index);

		return (value != null ? value : 1) * getMultiplier(BonusType.STRENGTH);
	}

	@Override
	public void loadAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.loadAdditional(compound, provider);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.deserializeNBT(provider, compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
		running = compound.getBoolean(ECNames.RUNNING);
		refresh();
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound, @Nonnull HolderLookup.Provider provider) {
		super.saveAdditional(compound, provider);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.serializeNBT(provider));
		compound.putBoolean(ECNames.RUNNING, running);
	}

	@Override
	protected void applyImplicitComponents(@NotNull DataComponentInput input) {
		super.applyImplicitComponents(input);
		elementStorage.setElementAmount(input.getOrDefault(ECDataComponents.ELEMENT_AMOUNT, 0));
	}

	@Override
	protected void collectImplicitComponents(@NotNull DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(ECDataComponents.ELEMENT_AMOUNT, elementStorage.getElementAmount());
	}

	@Override
	@Deprecated
	public void removeComponentsFromTag(@NotNull CompoundTag tag) {
		super.removeComponentsFromTag(tag);
		tag.remove(ECNames.ELEMENT_STORAGE);
	}

	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

}
