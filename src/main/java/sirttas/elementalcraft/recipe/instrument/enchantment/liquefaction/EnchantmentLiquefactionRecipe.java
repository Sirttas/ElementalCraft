package sirttas.elementalcraft.recipe.instrument.enchantment.liquefaction;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.LuckRecipe;
import sirttas.elementalcraft.recipe.RuntimeRecipe;
import sirttas.elementalcraft.recipe.instrument.SingleElementInstrumentRecipe;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.tag.ECTags;

public class EnchantmentLiquefactionRecipe implements SingleElementInstrumentRecipe<SimpleIOInstrumentRecipeInput>, LuckRecipe<SimpleIOInstrumentRecipeInput>, RuntimeRecipe<SimpleIOInstrumentRecipeInput> {

    public static final String NAME = "enchantment_liquefaction";

    private final Holder<@NotNull Enchantment> enchantment;

    public EnchantmentLiquefactionRecipe(Holder<@NotNull Enchantment> enchantment) {
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

    @Override
    public @NotNull ElementType getElementType() {
        return ElementType.WATER;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleIOInstrumentRecipeInput recipeInput) {
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

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    private boolean canEnchant(ItemStack stack) {
        return (stack.isEnchantable() && stack.supportsEnchantment(enchantment)) || stack.is(ECTags.Items.ENCHANTMENT_HOLDER);
    }

    public Holder<@NotNull Enchantment> getEnchantment() {
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
    public int getLuck(SimpleIOInstrumentRecipeInput recipeInput) {
        return Math.round(recipeInput.getRuneBonus(Rune.BonusType.LUCK));
    }

    private int getEnchantmentLevel(ItemStack stack) {
        var enchantments = EnchantmentHelper.getEnchantmentsForCrafting(stack);

        return enchantments.getLevel(enchantment);
    }

    private void setEnchantmentLevel(ItemStack stack, int level) {
        EnchantmentHelper.updateEnchantments(stack, e -> e.set(enchantment, level));
    }
}
