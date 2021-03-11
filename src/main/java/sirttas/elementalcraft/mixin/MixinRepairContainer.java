package sirttas.elementalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.AbstractRepairContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import sirttas.elementalcraft.infusion.InfusionHelper;

@Mixin(RepairContainer.class)
public abstract class MixinRepairContainer extends AbstractRepairContainer {

	protected MixinRepairContainer(ContainerType<?> type, int i, PlayerInventory inv, IWorldPosCallable callable) {
		super(type, i, inv, callable);
	}

	public ItemStack getLeft() {
		return this.field_234643_d_.getStackInSlot(0);
	}

	public ItemStack getRight() {
		return this.field_234643_d_.getStackInSlot(1);
	}

	public ItemStack getOutput() {
		return this.field_234642_c_.getStackInSlot(0);
	}

	@Inject(method = "updateRepairOutput", at = @At("HEAD"))
	public void onUpdateRepairOutput(CallbackInfo ci) {
		InfusionHelper.unapplyInfusion(getLeft());
		InfusionHelper.unapplyInfusion(getRight());
	}
	
	@Inject(method = "updateRepairOutput", at = @At("RETURN"))
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
