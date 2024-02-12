package sirttas.elementalcraft.item.elemental;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LensItem extends ElementalItem {
	
	private static final String NAME = "lens";

	public static final String NAME_FIRE = "fire_" + NAME;
	public static final String NAME_WATER = "water_" + NAME;
	public static final String NAME_EARTH = "earth_" + NAME;
	public static final String NAME_AIR = "air_" + NAME;

	public LensItem(ElementType elementType) {
		super(new Item.Properties()
				.stacksTo(1)
				.durability(1500), elementType);
	}

	@Nonnull
	public ISingleElementStorage getStorage(ItemStack stack, int multiplier) {
		return new Storage(stack, multiplier);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
    	return enchantment == Enchantments.UNBREAKING;
    }
	
	private class Storage implements ISingleElementStorage {

		private final int multiplier;
		private final ItemStack stack;
		
		private Storage(ItemStack stack, int multiplier) {
			this.stack = stack;
			this.multiplier = multiplier;
		}
		
		@Override
		public ElementType getElementType() {
			return elementType;
		}

		@Override
		public int getElementAmount() {
			return (stack.getMaxDamage() - stack.getDamageValue()) * multiplier;
		}

		@Override
		public int getElementCapacity() {
			return stack.getMaxDamage() * multiplier;
		}

		@Override
		public int insertElement(int count, ElementType type, boolean simulate) {
			return count;
		}

		@Override
		public int extractElement(int count, ElementType type, boolean simulate) {
			if (!stack.isDamageableItem()) {
				return count;
			}

			var rand = RandomSource.create();
			var target = simulate ? stack.copy() : stack;
			var toExtract = Math.min((target.getMaxDamage() - target.getDamageValue()) * multiplier, count);
			var floor = randomFloor(rand, toExtract);

			if (floor == 0) {
				return toExtract;
			}
			target.hurt(floor, rand, null);
			return toExtract;
		}

		private int randomFloor(RandomSource rand, float count) {
			float v = count / multiplier;
			int floor = (int) Math.floor(v);

			if (rand.nextDouble() < v - floor) {
				return floor + 1;
			}
			return floor;
		}

		@Override
		public boolean canPipeInsert(ElementType type, @Nullable Direction side) {
			return false;
		}

		@Override
		public boolean canPipeExtract(ElementType type, @Nullable Direction side) {
			return false;
		}
	}
}
