package sirttas.elementalcraft.block.shrine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.AbstractECBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;
import sirttas.elementalcraft.block.shrine.upgrade.AbstractShrineUpgradeBlock;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrade.BonusType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class AbstractShrineBlockEntity extends AbstractECBlockEntity implements IElementTypeProvider {

	protected static final List<Direction> DEFAULT_UPGRADE_DIRECTIONS = List.of(Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

	protected final Holder<ShrineProperties> properties;

	private final Map<Direction, ShrineUpgrade> upgrades = new EnumMap<>(Direction.class);
	private final Map<ShrineUpgrade.BonusType, Float> upgradeMultipliers = new EnumMap<>(ShrineUpgrade.BonusType.class);
	protected final ShrineElementStorage elementStorage;

	private boolean running = false;
	private double tick = 0;
	private int rangeRenderTimer = 0;

	protected AbstractShrineBlockEntity(RegistryObject<? extends BlockEntityType<?>> blockEntityType, BlockPos pos, BlockState state, ResourceKey<ShrineProperties> upgradeKey) {
		super(blockEntityType, pos, state);
		elementStorage = new ShrineElementStorage(this);
		properties = ElementalCraft.SHRINE_PROPERTIES_MANAGER.getOrCreateHolder(upgradeKey);
	}

	@Nonnull
	protected static ResourceKey<ShrineProperties> createKey(@Nonnull String name) {
		return IDataManager.createKey(ElementalCraft.SHRINE_PROPERTIES_MANAGER_KEY, ElementalCraft.createRL(name));
	}

	protected int consumeElement(int i) {
		return elementStorage.extractElement(i, false);
	}

	protected abstract boolean doPeriod();

	
	public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractShrineBlockEntity shrine) {
		if (shrine.isDirty()) {
			shrine.refresh();
		}
		double period = shrine.getPeriod();
		int consumeAmount = shrine.getConsumeAmount();

		shrine.running = false;
		if (!shrine.isPowered()) {
			shrine.tick++;
			if (period <= 0) {
				ElementalCraftApi.LOGGER.warn("Shrine period should not be 0");
				period = 1;
			}
			while (shrine.tick >= period) {
				if (shrine.elementStorage.getElementAmount() >= consumeAmount && shrine.doPeriod()) {
					shrine.consumeElement(consumeAmount);
				}
				shrine.tick -= period;
			}
		}
	}
	public static void clientTick(Level level, BlockPos pos, BlockState state, AbstractShrineBlockEntity shrine) {
		if (shrine.rangeRenderTimer > 0) {
			shrine.rangeRenderTimer--;
		}
	}

	public void refresh() {
		if (!this.hasLevel()) {
			return;
		}

		elementStorage.refresh();

		this.upgrades.clear();
		this.upgradeMultipliers.clear();
		getUpgradeDirections().forEach(direction -> {
			BlockPos pos = getBlockPos().relative(direction);
			BlockState state = this.getLevel().getBlockState(pos);
			Block block = state.getBlock();

			if (block instanceof AbstractShrineUpgradeBlock upgradeBlock && upgradeBlock.getFacing(state) == direction.getOpposite()) {
				ShrineUpgrade upgrade = upgradeBlock.getUpgrade();

				if (upgrade != null) {
					setUpgrade(direction, upgrade);
				}
			}
		});
		getUpgradeDirections().forEach(direction -> {
			BlockPos pos = getBlockPos().relative(direction);
			BlockState state = this.getLevel().getBlockState(pos);

			if (!state.canSurvive(this.level, pos)) {
				this.level.destroyBlock(pos, true);
			}
		});
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
		return this.rangeRenderTimer > 0;
	}

	@OnlyIn(Dist.CLIENT)
	public void startShowingRange() {
		this.rangeRenderTimer = 600;
	}

	public AABB getRangeBoundingBox() {
		return new AABB(this.getBlockPos()).inflate(this.getRange());
	}

	public Stream<BlockPos> getBlocksInRange() {
		var box = getRangeBoundingBox();

		return getRange(box.minX, box.maxX)
				.mapToObj(x -> getRange(box.minZ, box.maxZ)
				.mapToObj(z -> getRange(box.minY, box.maxY)
				.mapToObj(y -> new BlockPos(x, y, z))))
				.mapMulti((s, downstream) -> s.forEach(s2 -> s2.forEach(downstream)));
	}

	@Nonnull
	private IntStream getRange(double min, double max) {
		return IntStream.range((int) Math.floor(min), (int) Math.ceil(max) + 1);
	}

	@Override
	public ElementType getElementType() {
		return getProperties().getElementType();
	}

	public float getRange() {
		return getProperties().range() * getMultiplier(BonusType.RANGE);
	}

	public int getIntegerRange() {
		return Math.round(getRange());
	}

	public int getConsumeAmount() {
		return Math.round(getProperties().consumption() * getMultiplier(BonusType.ELEMENT_CONSUMPTION));
	}

	public double getPeriod() {
		return getProperties().period() * getMultiplier(BonusType.SPEED);
	}

	@Nonnull
	public ShrineProperties getProperties() {
		if (properties.isBound()) {
			return this.properties.value();
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
		var value = getProperties().strength().get(index);

		return (value != null ? value : 1) * getMultiplier(BonusType.STRENGTH);
	}

	@Override
	public void load(@Nonnull CompoundTag compound) {
		super.load(compound);
		if (compound.contains(ECNames.ELEMENT_STORAGE)) {
			elementStorage.deserializeNBT(compound.getCompound(ECNames.ELEMENT_STORAGE));
		}
		running = compound.getBoolean(ECNames.RUNNING);
		refresh();
	}

	@Override
	public void saveAdditional(@Nonnull CompoundTag compound) {
		super.saveAdditional(compound);
		compound.put(ECNames.ELEMENT_STORAGE, elementStorage.serializeNBT());
		compound.putBoolean(ECNames.RUNNING, running);
	}

	@Override
	@Nonnull
	public <U> LazyOptional<U> getCapability(@Nonnull Capability<U> cap, @Nullable Direction side) {
		if (!this.remove && cap == ElementalCraftCapabilities.ELEMENT_STORAGE) {
			return LazyOptional.of(elementStorage != null ? () -> elementStorage : null).cast();
		}
		return super.getCapability(cap, side);
	}

	public ISingleElementStorage getElementStorage() {
		return elementStorage;
	}

}
