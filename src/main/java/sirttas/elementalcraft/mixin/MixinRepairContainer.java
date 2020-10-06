package sirttas.elementalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.infusion.InfusionHelper;

@Mixin(RepairContainer.class)
public abstract class MixinRepairContainer extends Container {

	@Shadow private IInventory inputSlots;
	@Shadow private IInventory outputSlot;

	protected MixinRepairContainer(ContainerType<?> type, int id) {
		super(type, id);
	}

	public ItemStack getLeft() {
		return this.inputSlots.getStackInSlot(0);
	}

	public ItemStack getRight() {
		return this.inputSlots.getStackInSlot(1);
	}

	public ItemStack getOutput() {
		return this.outputSlot.getStackInSlot(0);
	}

	@Inject(method = "updateRepairOutput", at = @At("HEAD"))
	public void onUpdateRepairOutput(CallbackInfo ci) {
		InfusionHelper.unapplyInfusion(getLeft());
		InfusionHelper.unapplyInfusion(getRight());
	}
	
	@Inject(method = "updateRepairOutput", at = @At("TAIL"))
	public void onUpdateRepairOutputReturn(CallbackInfo ci) {
		ItemStack left = getLeft();
		ItemStack right = getRight();
		ItemStack output = getOutput();

		if (!output.isEmpty()) {
			if (InfusionHelper.hasInfusion(left)) {
				InfusionHelper.setInfusion(output, InfusionHelper.getInfusion(left));
			} else if (InfusionHelper.hasInfusion(right)) {
				InfusionHelper.setInfusion(output, InfusionHelper.getInfusion(right));
			} else {
				InfusionHelper.removeInfusion(output);
			}
		}

		InfusionHelper.applyInfusion(left);
		InfusionHelper.applyInfusion(right);
	}
}
