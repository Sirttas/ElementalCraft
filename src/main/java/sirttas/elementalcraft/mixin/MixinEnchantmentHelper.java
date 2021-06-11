package sirttas.elementalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentHelper.IEnchantmentVisitor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

	@Unique private static final ThreadLocal<ItemStack> STACK = ThreadLocal.withInitial(() -> ItemStack.EMPTY);
	@Unique private static final ThreadLocal<String> ENCHANTMENT_ID = ThreadLocal.withInitial(() -> "");
	
	@Inject(method = "getItemEnchantmentLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I", 
			at = @At("RETURN"), 
			cancellable = true)
	private static void getEnchantmentLevelReturn(Enchantment enchID, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		int value = ToolInfusionHelper.getInfusionEnchantmentLevel(stack, enchID);

		if (value > 0) {
			cir.setReturnValue(cir.getReturnValueI() + value);
		}
	}
	
	@Inject(method = "runIterationOnItem(Lnet/minecraft/enchantment/EnchantmentHelper$IEnchantmentVisitor;Lnet/minecraft/item/ItemStack;)V",
			locals = LocalCapture.CAPTURE_FAILHARD,
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/nbt/CompoundNBT;getString(Ljava/lang/String;)Ljava/lang/String;"))
	private static void applyEnchantmentModifierHead(IEnchantmentVisitor modifier, ItemStack stack, CallbackInfo ci, ListNBT listnbt, int i, String s) {
		STACK.set(stack);
		ENCHANTMENT_ID.set(s);
	}
	
	@ModifyVariable(method = "runIterationOnItem(Lnet/minecraft/enchantment/EnchantmentHelper$IEnchantmentVisitor;Lnet/minecraft/item/ItemStack;)V", 
			index = 5,
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/nbt/CompoundNBT;getInt(Ljava/lang/String;)I", shift = Shift.AFTER))
	private static int applyEnchantmentModifierSetLevel(int j) {
		Enchantment enchanment = ForgeRegistries.ENCHANTMENTS.getValue(ResourceLocation.tryParse(ENCHANTMENT_ID.get()));
		int value = enchanment != null ? ToolInfusionHelper.getInfusionEnchantmentLevel(STACK.get(), enchanment) : 0;
		
		STACK.remove();
		ENCHANTMENT_ID.remove();
		return j + value;
	}
}
