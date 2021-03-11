package sirttas.elementalcraft.block.shrine.breeding;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.shrine.AbstractTileShrine;
import sirttas.elementalcraft.config.ECConfig;

public class TileBreedingShrine extends AbstractTileShrine {

	@ObjectHolder(ElementalCraft.MODID + ":" + BlockBreedingShrine.NAME) public static final TileEntityType<TileBreedingShrine> TYPE = null;

	private static final Properties PROPERTIES = Properties.create(ElementType.EARTH).periode(ECConfig.COMMON.breedingShrinePeriode.get())
			.consumeAmount(ECConfig.COMMON.breedingShrineConsumeAmount.get()).range(ECConfig.COMMON.breedingShrineRange.get());

	public TileBreedingShrine() {
		super(TYPE, PROPERTIES);
	}

	private <T extends Entity> List<T> getEntities(Class<T> clazz) {
		return this.getWorld().getEntitiesWithinAABB(clazz, getRangeBoundingBox(), e -> !e.isSpectator()).stream().collect(Collectors.toList());
	}

	@Override
	public AxisAlignedBB getRangeBoundingBox() {
		return super.getRangeBoundingBox().offset(Vector3d.copy(this.getBlockState().get(BlockBreedingShrine.FACING).getDirectionVec()).scale(getRange()));
	}

	@Override
	protected boolean doTick() {
		EntityType<?> type = null;
		AnimalEntity first = null;
		AnimalEntity second = null;

		for (AnimalEntity entity : getEntities(AnimalEntity.class)) {
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

	public boolean feed(AnimalEntity first, AnimalEntity second) {
		List<ItemStack> foodList = getEntities(ItemEntity.class).stream().map(ItemEntity::getItem).filter(stack -> first.isBreedingItem(stack) && stack.getCount() > 0).collect(Collectors.toList());

		if (!foodList.isEmpty()) {
			if (foodList.get(0).getCount() >= 2) {
				foodList.get(0).shrink(2);
			} else if (foodList.size() >= 2) {
				foodList.get(0).shrink(1);
				foodList.get(1).shrink(1);
			} else {
				return false;
			}
			first.setInLove(600);
			second.setInLove(600);
			return true;
		}
		return false;
	}

	@Override
	public List<Direction> getUpgradeDirections() {
		BlockState state = this.getBlockState();

		return state.get(BlockBreedingShrine.PART) == BlockBreedingShrine.Part.BOWL ? Collections.emptyList()
				: DEFAULT_UPGRRADE_DIRECTIONS.stream().filter(direction -> direction != state.get(BlockBreedingShrine.FACING)).collect(Collectors.toList());
	}
}
