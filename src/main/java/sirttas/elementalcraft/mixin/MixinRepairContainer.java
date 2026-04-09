package sirttas.elementalcraft.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
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

    public MixinRepairContainer(@Nullable MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition itemInputSlots) {
        super(menuType, containerId, inventory, access, itemInputSlots);
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
	public void updateRepairOutputReturn(CallbackInfo ci) { // TODO move to event
		var left = ToolInfusionHelper.getInfusion(getLeft());
		var right = ToolInfusionHelper.getInfusion(getRight());
		var output = getOutput();

		if (!output.isEmpty()) {
			if (left.value() != ToolInfusion.NONE) {
				ToolInfusionHelper.setInfusion(output, left);
			} else if (right.value() != ToolInfusion.NONE) {
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
