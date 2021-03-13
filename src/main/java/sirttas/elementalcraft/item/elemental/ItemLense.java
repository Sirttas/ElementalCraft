package sirttas.elementalcraft.item.elemental;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.single.ISingleElementStorage;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.property.ECProperties;

public class ItemLense extends ItemElemental {
	
	private static final String NAME = "lense";

	public static final String NAME_FIRE = "fire_" + NAME;
	public static final String NAME_WATER = "water_" + NAME;
	public static final String NAME_EARTH = "earth_" + NAME;
	public static final String NAME_AIR = "air_" + NAME;
	
	public ItemLense(ElementType elementType) {
		super(ECProperties.Items.LENSE, elementType);
	}
	
	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
		return new ICapabilityProvider() {
			@Override
			public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
				return CapabilityElementStorage.ELEMENT_STORAGE_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> new Storage(stack, ECConfig.COMMON.lenseElementMultiplier.get())));
			}
		};
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
			return (stack.getMaxDamage() - stack.getDamage()) * multiplier;
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
			ItemStack target = simulate ? stack.copy() : stack;
			int damage = target.getDamage();
			
			target.attemptDamageItem(count / multiplier, new Random(), null);
			return (target.getDamage() - damage) * multiplier;
		}
		
	}
}
