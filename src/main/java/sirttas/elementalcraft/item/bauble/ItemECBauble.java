package sirttas.elementalcraft.item.bauble;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import sirttas.elementalcraft.item.ItemEC;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;

public abstract class ItemECBauble extends ItemEC {


	public ItemECBauble(Properties properties) {
		super(properties);
	}


	
	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {

		return new ICapabilityProvider() {
			private final LazyOptional<ICurio> curio = LazyOptional.of(() -> new ICurio() {
			});

			@Nonnull
			@Override
			public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
				return CuriosCapability.ITEM.orEmpty(cap, curio);
			}

		};
	}
}
