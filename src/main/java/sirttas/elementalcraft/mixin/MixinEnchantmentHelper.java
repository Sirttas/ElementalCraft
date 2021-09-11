package sirttas.elementalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentHelper.EnchantmentVisitor;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

@Mixin(EnchantmentHelper.class)
public abstract class MixinEnchantmentHelper {

	@Unique private static final ThreadLocal<ItemStack> STACK = ThreadLocal.withInitial(() -> ItemStack.EMPTY);
	@Unique private static final ThreadLocal<Enchantment> ENCHANTMENT = new ThreadLocal<>();
	
	@Inject(method = "getItemEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/item/ItemStack;)I", 
			at = @At("RETURN"), 
			cancellable = true)
	private static void getEnchantmentLevelReturn(Enchantment enchID, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		int value = ToolInfusionHelper.getInfusionEnchantmentLevel(stack, enchID);

		if (value > 0) {
			cir.setReturnValue(cir.getReturnValueI() + value);
		}
	}
	
	@Inject(method = "runIterationOnItem(Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;Lnet/minecraft/world/item/ItemStack;)V",
			at = @At("HEAD"))
	private static void runIterationOnItemHead(EnchantmentVisitor modifier, ItemStack stack, CallbackInfo ci) {
		STACK.set(stack);
	}
	
	
	@Inject(method = "lambda$runIterationOnItem$1(Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/item/enchantment/Enchantment;)V",
			at = @At("HEAD"))
	private static void runIterationOnItemLambdaHead(EnchantmentVisitor modifier, CompoundTag tag, Enchantment enchantment, CallbackInfo ci) {
		ENCHANTMENT.set(enchantment);
	}
	
	@Redirect(method = "lambda$runIterationOnItem$1(Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/item/enchantment/Enchantment;)V",
			at = @At(value = "INVOKE", target = "getEnchantmentLevel(Lnet/minecraft/nbt/CompoundTag;)I"))
	private static int runIterationOnItemLambdaRedirect(CompoundTag tag) {
		Enchantment enchanment = ENCHANTMENT.get();
		int value = enchanment != null ? ToolInfusionHelper.getInfusionEnchantmentLevel(STACK.get(), enchanment) : 0;
		
		return EnchantmentHelper.getEnchantmentLevel(tag) + value;
	}
	
	
	@Inject(method = "runIterationOnItem(Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;Lnet/minecraft/world/item/ItemStack;)V",
			at = @At("HEAD"))
	private static void runIterationOnItemReturn(EnchantmentVisitor modifier, ItemStack stack, CallbackInfo ci) {
		STACK.remove();
		ENCHANTMENT.remove();
	}
}
