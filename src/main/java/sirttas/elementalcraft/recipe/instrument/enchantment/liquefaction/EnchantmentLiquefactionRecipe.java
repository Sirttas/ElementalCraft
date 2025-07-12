package sirttas.elementalcraft.recipe.instrument.enchantment.liquefaction;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.IRuntimeRecipe;
import sirttas.elementalcraft.recipe.instrument.AbstractInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.ILuckRecipe;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;

public class EnchantmentLiquefactionRecipe extends AbstractInstrumentRecipe<SimpleIOInstrumentRecipeInput> implements ILuckRecipe<SimpleIOInstrumentRecipeInput>, IRuntimeRecipe<SimpleIOInstrumentRecipeInput> {

    public static final String NAME = "enchantment_liquefaction";

    private final Holder<Enchantment> enchantment;

    public EnchantmentLiquefactionRecipe(Holder<Enchantment> enchantment) {
        super(ElementType.WATER);
        this.enchantment = enchantment;
    }

    @Override
    public boolean matches(SimpleIOInstrumentRecipeInput recipeInput, @NotNull Level level) {
        if (recipeInput.getElementType() != ElementType.WATER) {
            return false;
        }

        var input = recipeInput.getItem(0);
        var output = recipeInput.getItem(1);

        if (input.isEmpty() || output.isEmpty()) {
            return false;
        }

        var inputLevel = getEnchantmentLevel(input);

        if (inputLevel <= 0 || !canEnchant(output)) {
            return false;
        }

        var outputLevel = getEnchantmentLevel(output);

        return inputLevel > outputLevel || (inputLevel == outputLevel && inputLevel < enchantment.value().getMaxLevel());
    }

    private boolean canEnchant(ItemStack stack) {
        return (stack.isEnchantable() && stack.supportsEnchantment(enchantment)) || stack.is(ECTags.Items.ENCHANTMENT_HOLDER);
    }

    public Holder<Enchantment> getEnchantment() {
        return enchantment;
    }

    @Override
    public int getElementAmount() {
        return getEnchantment().value().getAnvilCost() * ECConfig.SERVER.enchantmentLiquefierElementAmount.get();
    }

    @Override
    public int getElementAmount(SimpleIOInstrumentRecipeInput recipeInput) {
        return getElementAmount() * Math.max(1, getEnchantmentLevel(recipeInput.getItem(0)));
    }

    private int getBreakChance() {
        return (int) Math.round(getEnchantment().value().getAnvilCost() * ECConfig.SERVER.enchantmentLiquefierBreakChance.get());
    }

    @Override
    public @NotNull ItemStack getResultItem(@Nonnull HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getLuck(SimpleIOInstrumentRecipeInput recipeInput) {
        return Math.round(recipeInput.getRuneBonus(Rune.BonusType.LUCK));
    }

    @Override
    public @NotNull ItemStack assemble(@Nonnull SimpleIOInstrumentRecipeInput recipeInput, @Nonnull HolderLookup.Provider provider) {
        var input = recipeInput.getItem(0);
        var output = recipeInput.getItem(1).copy();
        var inputLevel = getEnchantmentLevel(input);
        var outputLevel = getEnchantmentLevel(output);

        if (getRandomSource(recipeInput).nextInt(100) < (getBreakChance() - getLuck(recipeInput))) {
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
        } else if (inputLevel == outputLevel && inputLevel < enchantment.value().getMaxLevel()) {
            setEnchantmentLevel(output, inputLevel + 1);
        }
        return output;
    }

    private int getEnchantmentLevel(ItemStack stack) {
        var enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);

        return enchantments.getLevel(enchantment);
    }

    private void setEnchantmentLevel(ItemStack stack, int level) {
        EnchantmentHelper.updateEnchantments(stack, e -> e.set(enchantment, level));
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(SimpleIOInstrumentRecipeInput recipeInput) {
        var input = recipeInput.getItem(0).copy();

        if (input.isEmpty()) {
            return NonNullList.of(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
        }

        var enchantments = EnchantmentHelper.getEnchantmentsForCrafting(input);

        if (enchantments.isEmpty()) {
            return NonNullList.of(ItemStack.EMPTY, input, ItemStack.EMPTY);
        }

        var mutable = new ItemEnchantments.Mutable(enchantments);

        mutable.removeIf(enchantment::is);
        enchantments = mutable.toImmutable();
        if (enchantments.isEmpty() && input.is(Items.ENCHANTED_BOOK)) {
            return NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.BOOK), ItemStack.EMPTY);
        }
        return NonNullList.of(ItemStack.EMPTY, input, ItemStack.EMPTY);
    }
}
