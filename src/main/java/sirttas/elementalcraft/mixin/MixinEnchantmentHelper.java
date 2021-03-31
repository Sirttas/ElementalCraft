package sirttas.elementalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

@Mixin(EnchantmentHelper.class)
public class MixinEnchantmentHelper {

	@Inject(method = "getEnchantmentLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I", at = @At("RETURN"), cancellable = true)
	private static void onGetEnchantmentLevel(Enchantment enchID, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		int value = ToolInfusionHelper.getInfusionEnchantmentLevel(stack, enchID);

		if (value > 0) {
			cir.setReturnValue(cir.getReturnValueI() + value);
		}
	}
}
