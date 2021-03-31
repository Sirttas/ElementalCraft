package sirttas.elementalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItemStack;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

@Mixin(ItemStack.class)
public abstract class MixinItemStack extends CapabilityProvider<ItemStack> implements IForgeItemStack {

	protected MixinItemStack(Class<ItemStack> baseClass) {
		super(baseClass);
	}

	@Inject(method = "getAttributeModifiers(Lnet/minecraft/inventory/EquipmentSlotType;)Lcom/google/common/collect/Multimap;", at = @At("RETURN"), cancellable = true)
	public void onGetAttributeModifiers(EquipmentSlotType equipmentSlot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
		Multimap<Attribute, AttributeModifier> map = ToolInfusionHelper.getInfusionAttribute(getStack(), equipmentSlot);
		
		if (!map.isEmpty()) {
			map.putAll(cir.getReturnValue());
			cir.setReturnValue(map);
		}
	}
	
}
