package sirttas.elementalcraft.item.bauble;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import sirttas.elementalcraft.nbt.ECNBTTags;
import sirttas.elementalcraft.property.ECProperties;

public class ItemHungerlessRing extends ItemECBauble {

	public static final String NAME = "hungerlessring";

	public ItemHungerlessRing() {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
	}

	public void resetCooldown(ItemStack itemstack) {
		CompoundNBT nbt = itemstack.getTag();
		
		nbt.putInt(ECNBTTags.COOLDOWN, 200);
	}

	public void onWornTick(ItemStack itemstack, LivingEntity entity) {
		if (itemstack.getDamage() == 0 && entity.ticksExisted % 39 == 0 && entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			CompoundNBT nbt = itemstack.getTag();
			int cooldown = nbt.getInt(ECNBTTags.COOLDOWN);
			
			if (cooldown > 0) {
				nbt.putInt(ECNBTTags.COOLDOWN, cooldown - 1);
			} else {
				player.getFoodStats().addStats(1, 0);
			}
		}
	}

}
