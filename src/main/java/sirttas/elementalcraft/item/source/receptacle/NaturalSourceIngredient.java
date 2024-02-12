package sirttas.elementalcraft.item.source.receptacle;

import com.mojang.serialization.Codec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.item.ECIngredientTypes;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public class NaturalSourceIngredient extends Ingredient implements IElementTypeProvider {

    public static final Codec<NaturalSourceIngredient> CODEC = ElementType.CODEC.xmap(NaturalSourceIngredient::new, NaturalSourceIngredient::getElementType);

    private final ElementType elementType;

    public NaturalSourceIngredient(ElementType elementType) {
        super(Stream.of(), ECIngredientTypes.NATURAL_SOURCE);
        this.elementType = elementType;
    }

    @Override
    public ElementType getElementType() {
        return elementType;
    }

    @Nonnull
    @Override
    public ItemStack[] getItems() {
        return new ItemStack[]{ReceptacleHelper.create(elementType)};
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty() || !stack.is(ECItems.RECEPTACLE.get())) {
            return false;
        }

        var traitHolder = stack.getCapability(ElementalCraftCapabilities.SourceTrait.ITEM, null);

        if (traitHolder == null) {
            return false;
        }

        return !traitHolder.isArtificial();
    }
}
