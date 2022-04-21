package sirttas.elementalcraft.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sirttas.elementalcraft.api.infusion.tool.ToolInfusion;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

import java.util.Objects;

@Mixin(AnvilMenu.class)
public abstract class MixinRepairContainer extends ItemCombinerMenu {

	@Shadow
	@Final
	private DataSlot cost;
	
	protected MixinRepairContainer(MenuType<?> type, int i, Inventory inv, ContainerLevelAccess callable) {
		super(type, i, inv, callable);
	}

	@Unique
	public ItemStack getLeft() {
		return this.inputSlots.getItem(0);
	}

	@Unique
	public ItemStack getRight() {
		return this.inputSlots.getItem(1);
	}

	@Unique
	public ItemStack getOutput() {
		return this.resultSlots.getItem(0);
	}
	
	@Inject(method = "createResult()V", 
			at = @At("RETURN"))
	public void updateRepairOutputReturn(CallbackInfo ci) {
		ToolInfusion left = ToolInfusionHelper.getInfusion(getLeft());
		ToolInfusion right = ToolInfusionHelper.getInfusion(getRight());
		ItemStack output = getOutput();

		if (!output.isEmpty()) {
			if (left != null) {
				ToolInfusionHelper.setInfusion(output, left);
			} else if (right != null) {
				if (!Objects.equals(ToolInfusionHelper.getInfusion(output), right)) {
					cost.set(cost.get() + 4);
				}
				ToolInfusionHelper.setInfusion(output, right);
			} else {
				ToolInfusionHelper.removeInfusion(output);
			}
		}
	}
}
