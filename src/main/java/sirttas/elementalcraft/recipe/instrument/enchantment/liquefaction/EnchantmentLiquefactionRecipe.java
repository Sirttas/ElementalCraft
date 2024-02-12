package sirttas.elementalcraft.recipe.instrument.enchantment.liquefaction;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.block.instrument.enchantment.liquefier.EnchantmentLiquefierBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.ContainerBlockEntityWrapper;
import sirttas.elementalcraft.recipe.IRuntimeContainerBlockEntityRecipe;
import sirttas.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.ILuckRecipe;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class EnchantmentLiquefactionRecipe extends AbstractInstrumentRecipe<EnchantmentLiquefierBlockEntity> implements ILuckRecipe<EnchantmentLiquefierBlockEntity>, IRuntimeContainerBlockEntityRecipe<EnchantmentLiquefierBlockEntity> {

    public static final String NAME = "enchantment_liquefaction";

    private final ResourceLocation enchantmentId;
    private final Enchantment enchantment;
    private final Lazy<Integer> elementAmount;
    private final Lazy<Integer> breakChance;

    public EnchantmentLiquefactionRecipe(Enchantment enchantment) {
        super(ElementType.WATER);
        this.enchantment = enchantment;
        this.enchantmentId = BuiltInRegistries.ENCHANTMENT.getKey(enchantment);
        this.elementAmount = Lazy.of(() -> switch (this.enchantment.getRarity()) {
            case COMMON -> ECConfig.COMMON.enchantmentLiquefierElementAmountCommon.get();
            case UNCOMMON -> ECConfig.COMMON.enchantmentLiquefierElementAmountUncommon.get();
            case RARE -> ECConfig.COMMON.enchantmentLiquefierElementAmountRare.get();
            case VERY_RARE -> ECConfig.COMMON.enchantmentLiquefierElementAmountVeryRare.get();
        });
        this.breakChance = Lazy.of(() -> switch (this.enchantment.getRarity()) {
            case COMMON -> ECConfig.COMMON.enchantmentLiquefierBreakChanceCommon.get();
            case UNCOMMON -> ECConfig.COMMON.enchantmentLiquefierBreakChanceUncommon.get();
            case RARE -> ECConfig.COMMON.enchantmentLiquefierBreakChanceRare.get();
            case VERY_RARE -> ECConfig.COMMON.enchantmentLiquefierBreakChanceVeryRare.get();
        });
    }

    @Override
    public boolean matches(EnchantmentLiquefierBlockEntity enchantmentLiquefier, @NotNull Level level) {
        if (enchantmentLiquefier.getContainerElementType() != ElementType.WATER) {
            return false;
        }

        var inv = enchantmentLiquefier.getInventory();
        var input = inv.getItem(0);
        var output = inv.getItem(1);

        if (input.isEmpty() || output.isEmpty()) {
            return false;
        }

        var inputLevel = getEnchantmentLevel(input);

        if (inputLevel <= 0 || !canEnchant(output)) {
            return false;
        }

        var outputLevel = getEnchantmentLevel(output);

        return inputLevel > outputLevel || (inputLevel == outputLevel && inputLevel < enchantment.getMaxLevel());
    }

    private boolean canEnchant(ItemStack stack) {
        return (stack.isEnchantable() && enchantment.canEnchant(stack)) || stack.is(ECTags.Items.ENCHANTMENT_HOLDER);
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    @Override
    public int getElementAmount() {
        return this.elementAmount.get();
    }

    @Override
    public int getElementAmount(EnchantmentLiquefierBlockEntity enchantmentLiquefier) {
        return getElementAmount() * Math.max(1, getEnchantmentLevel(enchantmentLiquefier.getInventory().getItem(0)));
    }

    private int getBreakChance() {
        return this.breakChance.get();
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public RandomSource getRand(EnchantmentLiquefierBlockEntity instrument) {
        return instrument.getLevel().getRandom();
    }

    @Override
    public int getLuck(EnchantmentLiquefierBlockEntity instrument) {
        return Math.round(instrument.getRuneHandler().getBonus(Rune.BonusType.LUCK));
    }

    @Override
    public @NotNull ItemStack assemble(@Nonnull EnchantmentLiquefierBlockEntity instrument, @Nonnull RegistryAccess registry) {
        var inv = instrument.getInventory();
        var input = inv.getItem(0);
        var output = inv.getItem(1).copy();
        var inputLevel = getEnchantmentLevel(input);
        var outputLevel = getEnchantmentLevel(output);

        if (getRand(instrument).nextInt(100) < (getBreakChance() - getLuck(instrument))) {
            inputLevel--;
        }
        if (inputLevel <= 0) {
            return output;
        }
        if (output.is(Items.BOOK)) {
            output = new ItemStack(Items.ENCHANTED_BOOK);
        }
        if (inputLevel > outputLevel) {
            setEnchantmentLevel(output, inputLevel);
        } else if (inputLevel == outputLevel && inputLevel < enchantment.getMaxLevel()) {
            setEnchantmentLevel(output, inputLevel + 1);
        }
        return output;
    }

    private int getEnchantmentLevel(ItemStack stack) {
        var map = EnchantmentHelper.getEnchantments(stack);

        return map.getOrDefault(enchantment, 0);
    }

    private void setEnchantmentLevel(ItemStack stack, int level) {
        var map = EnchantmentHelper.getEnchantments(stack);

        map.put(enchantment, level);
        if (stack.is(Items.ENCHANTED_BOOK)) {
            stack.removeTagKey("StoredEnchantments");
        } else {
            stack.removeTagKey("Enchantments");
        }
        EnchantmentHelper.setEnchantments(map, stack);
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(ContainerBlockEntityWrapper<EnchantmentLiquefierBlockEntity> container) {
        var input = container.getItem(0).copy();

        if (input.isEmpty()) {
            return NonNullList.of(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
        }

        input.getEnchantmentTags().removeIf(tag -> tag instanceof CompoundTag compoundTag && enchantmentId.equals(EnchantmentHelper.getEnchantmentId(compoundTag)));
        if (input.getEnchantmentTags().isEmpty()) {
            if (input.is(Items.ENCHANTED_BOOK)) {
                return NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.BOOK), ItemStack.EMPTY);
            }
            input.removeTagKey("Enchantments");
        }
        return NonNullList.of(ItemStack.EMPTY, input, ItemStack.EMPTY);
    }
}
