package sirttas.elementalcraft.item.elemental;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.property.ECProperties;

public class LenseItem extends ElementalItem {
	
	private static final String NAME = "lense";

	public static final String NAME_FIRE = "fire_" + NAME;
	public static final String NAME_WATER = "water_" + NAME;
	public static final String NAME_EARTH = "earth_" + NAME;
	public static final String NAME_AIR = "air_" + NAME;
	
	public LenseItem(ElementType elementType) {
		super(ECProperties.Items.LENSE, elementType);
	}
	
	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new ICapabilityProvider() {
			@Nonnull
			@Override
			public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
				return CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> new Storage(stack, ECConfig.COMMON.lenseElementMultiplier.get())));
			}
		};
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
			ItemStack target = simulate ? stack.copy() : stack;
			int damage = target.getDamageValue();
			
			target.hurt(count / multiplier, new Random(), null);
			return (target.getDamageValue() - damage) * multiplier;
		}
		
	}
}
