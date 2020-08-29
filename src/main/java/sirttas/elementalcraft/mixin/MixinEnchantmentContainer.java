package sirttas.elementalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.infusion.InfusionHelper;

@Mixin(EnchantmentContainer.class)
public abstract class MixinEnchantmentContainer extends Container {

	@Shadow private IInventory tableInventory;

	public ItemStack getstack() {
		return tableInventory.getStackInSlot(0);
	}

	protected MixinEnchantmentContainer(ContainerType<?> type, int id) {
		super(type, id);
	}

	@Inject(method = "onCraftMatrixChanged", at = @At("HEAD"))
	public void onOnCraftMatrixChanged(IInventory inventoryIn, CallbackInfo ci) {
		InfusionHelper.unapplyInfusion(getstack());
	}
	
	@Inject(method = "onCraftMatrixChanged", at = @At("TAIL"))
	public void onOnCraftMatrixChangedReturn(IInventory inventoryIn, CallbackInfo ci) {
		InfusionHelper.applyInfusion(getstack());
	}

	@Inject(method = "enchantItem", at = @At("HEAD"))
	public void onEnchantItem(PlayerEntity playerIn, int id, CallbackInfo ci) {
		InfusionHelper.unapplyInfusion(getstack());
	}

	@Inject(method = "enchantItem", at = @At("TAIL"))
	public void onEnchantItemReturn(PlayerEntity playerIn, int id, CallbackInfo ci) {
		InfusionHelper.applyInfusion(getstack());
	}
}
