package sirttas.elementalcraft.mixin;


import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.spongepowered.asm.mixin.Mixin;
import sirttas.elementalcraft.infusion.tool.ToolInfusionHelper;

import java.util.Map;

@Mixin(value = ItemStack.class)
public abstract class MixinItemStack extends CapabilityProvider<ItemStack> implements IForgeItemStack {


    protected MixinItemStack(Class<ItemStack> baseClass, boolean isLazy) {
        super(baseClass, isLazy);
    }

    @Override
    public int getEnchantmentLevel(Enchantment enchantment) {
        return IForgeItemStack.super.getEnchantmentLevel(enchantment) + ToolInfusionHelper.getInfusionEnchantmentLevel((ItemStack) (Object) this, enchantment);
    }

    @Override
    public Map<Enchantment, Integer> getAllEnchantments() {
        var map = ToolInfusionHelper.getAllInfusionEnchantments((ItemStack) (Object) this);

        if (!map.isEmpty()) {
            map.forEach(IForgeItemStack.super.getAllEnchantments()::put);
        }
        return IForgeItemStack.super.getAllEnchantments();
    }

}
