package sirttas.elementalcraft.item.source.receptacle;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.api.source.trait.holder.SourceTraitHolderHelper;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.item.ECItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public class NaturalSourceIngredient extends Ingredient implements IElementTypeProvider {

    private final ElementType elementType;

    public NaturalSourceIngredient(ElementType elementType) {
        super(Stream.of());
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
        if (stack == null || !stack.is(ECItems.RECEPTACLE.get())) {
            return false;
        }

        return SourceTraitHolderHelper.get(stack)
                .map(t -> !t.getTraits().containsKey(SourceTraits.ARTIFICIAL))
                .orElse(false);
    }

    @Nonnull
    @Override
    public JsonElement toJson() {
        var json = new JsonObject();

        json.addProperty(ECNames.TYPE, CraftingHelper.getID(Serializer.INSTANCE).toString());
        json.addProperty(ECNames.ELEMENT_TYPE, elementType.getSerializedName());
        return json;
    }

    @Nonnull
    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements IIngredientSerializer<NaturalSourceIngredient>
    {
        public static final Serializer INSTANCE = new Serializer();

        @Nonnull
        @Override
        public NaturalSourceIngredient parse(FriendlyByteBuf buffer) {
            return new NaturalSourceIngredient(ElementType.byName(buffer.readUtf()));
        }

        @Nonnull
        @Override
        public NaturalSourceIngredient parse(@Nonnull JsonObject json) {
            return new NaturalSourceIngredient(ElementType.byName(GsonHelper.getAsString(json, ECNames.ELEMENT_TYPE)));
        }

        @Override
        public void write(FriendlyByteBuf buffer, NaturalSourceIngredient ingredient) {
            buffer.writeUtf(ingredient.elementType.getSerializedName());
        }
    }
}
