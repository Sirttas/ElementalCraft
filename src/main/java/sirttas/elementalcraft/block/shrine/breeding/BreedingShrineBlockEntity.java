package sirttas.elementalcraft.block.shrine.breeding;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgrades;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BreedingShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(BreedingShrineBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	public BreedingShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.BREEDING_SHRINE, PROPERTIES, pos, state);
	}

	private <T extends Entity> List<T> getEntities(Class<T> clazz) {
		return this.getLevel().getEntitiesOfClass(clazz, getRange(), e -> !e.isSpectator());
	}

	@Override
	public AABB lookupRange() {
		if (this.hasUpgrade(ShrineUpgrades.TRANSLOCATION)) {
			return super.lookupRange();
		}
		return lookupRange(this.getBlockState().getValue(BreedingShrineBlock.FACING));
	}

	@Override
	protected boolean doPeriod() {
		EntityType<?> type = null;
		Animal first = null;
		Animal second = null;

		for (Animal entity : getEntities(Animal.class)) {
			if (type == null && entity.canFallInLove()) {
				type = entity.getType();
				first = entity;
			} else if (second == null && type != null && type.equals(entity.getType()) && entity.canFallInLove()) {
				second = entity;
			}
		}
		if (first != null && second != null) {
			return feed(first, second);
		}
		return false;
	}

	public boolean consumeFood(Animal first) {
		List<ItemStack> foodList = getEntities(ItemEntity.class).stream()
				.map(ItemEntity::getItem)
				.filter(stack -> first.isFood(stack) && !stack.isEmpty())
				.toList();

		if (foodList.isEmpty()) {
			return false;
		} else if (foodList.getFirst().getCount() >= 2) {
			foodList.getFirst().shrink(2);
		} else if (foodList.size() >= 2) {
			foodList.getFirst().shrink(1);
			foodList.get(1).shrink(1);
		} else {
			return false;
		}
		return true;
	}

	public boolean feed(Animal first, Animal second) {
		if (!consumeFood(first)) {
			return false;
		}

		setInLove(first);
		setInLove(second);
		return true;
	}

	private void setInLove(Animal first) {
		first.setInLoveTime(600);
		this.level.broadcastEntityEvent(first, EntityEvent.IN_LOVE_HEARTS);
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		BlockState state = this.getBlockState();

		return state.getValue(BreedingShrineBlock.PART) == BreedingShrineBlock.Part.BOWL ? Collections.emptyList()
				: DEFAULT_UPGRADE_DIRECTIONS.stream().filter(direction -> direction != state.getValue(BreedingShrineBlock.FACING)).collect(Collectors.toList());
	}
}
