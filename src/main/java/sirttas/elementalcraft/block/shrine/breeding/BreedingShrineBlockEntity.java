package sirttas.elementalcraft.block.shrine.breeding;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.shrine.AbstractShrineBlockEntity;
import sirttas.elementalcraft.block.shrine.properties.ShrineProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BreedingShrineBlockEntity extends AbstractShrineBlockEntity {

	public static final ResourceKey<ShrineProperties> PROPERTIES_KEY = createKey(BreedingShrineBlock.NAME);
	public BreedingShrineBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.BREEDING_SHRINE, pos, state, PROPERTIES_KEY);
	}

	private <T extends Entity> List<T> getEntities(Class<T> clazz) {
		return new ArrayList<>(this.getLevel().getEntitiesOfClass(clazz, getRangeBoundingBox(), e -> !e.isSpectator()));
	}

	@Override
	public AABB getRangeBoundingBox() {
		return super.getRangeBoundingBox().move(Vec3.atLowerCornerOf(this.getBlockState().getValue(BreedingShrineBlock.FACING).getNormal()).scale(getRange()));
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

	public boolean feed(Animal first, Animal second) {
		List<ItemStack> foodList = getEntities(ItemEntity.class).stream().map(ItemEntity::getItem).filter(stack -> first.isFood(stack) && stack.getCount() > 0).toList();

		if (!foodList.isEmpty()) {
			if (foodList.get(0).getCount() >= 2) {
				foodList.get(0).shrink(2);
			} else if (foodList.size() >= 2) {
				foodList.get(0).shrink(1);
				foodList.get(1).shrink(1);
			} else {
				return false;
			}
			first.setInLoveTime(600);
			second.setInLoveTime(600);
			return true;
		}
		return false;
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		BlockState state = this.getBlockState();

		return state.getValue(BreedingShrineBlock.PART) == BreedingShrineBlock.Part.BOWL ? Collections.emptyList()
				: DEFAULT_UPGRADE_DIRECTIONS.stream().filter(direction -> direction != state.getValue(BreedingShrineBlock.FACING)).collect(Collectors.toList());
	}
}
