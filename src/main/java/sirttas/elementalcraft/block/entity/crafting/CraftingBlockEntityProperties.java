package sirttas.elementalcraft.block.entity.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.properties.ConfigurableBlockEntityPropertiesType;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;

import java.util.Optional;

public record CraftingBlockEntityProperties(
        Optional<Holder<RecipeType<?>>> recipeType,
        int transferSpeed,
        int maxRunes,
        int outputSlot,
        boolean retrieveAll,
        boolean lockable
) implements IConfigurableBlockEntityProperties {

    public static final MapCodec<CraftingBlockEntityProperties> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.RECIPE_TYPE.holderByNameCodec().optionalFieldOf("recipe_type").forGetter(CraftingBlockEntityProperties::recipeType),
            Codec.INT.fieldOf(ECNames.TRANSFER_SPEED).forGetter(CraftingBlockEntityProperties::transferSpeed),
            Codec.INT.fieldOf(ECNames.MAX_RUNES).forGetter(CraftingBlockEntityProperties::maxRunes),
            Codec.INT.optionalFieldOf("output_slot", 0).forGetter(CraftingBlockEntityProperties::outputSlot),
            Codec.BOOL.optionalFieldOf("retrieve_all", false).forGetter(CraftingBlockEntityProperties::retrieveAll),
            Codec.BOOL.optionalFieldOf("lockable", false).forGetter(CraftingBlockEntityProperties::lockable)
    ).apply(instance, CraftingBlockEntityProperties::new));

    public static final CraftingBlockEntityProperties DEFAULT = new CraftingBlockEntityProperties(Optional.empty(), 0, 0, 0, false, false);

    @Override
    public ConfigurableBlockEntityPropertiesType<CraftingBlockEntityProperties> getType() {
        return ConfigurableBlockEntityPropertiesType.CRAFTING.get();
    }

    @SuppressWarnings("unchecked")
    public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getRecipeType() {
        return (RecipeType<R>) recipeType()
                .map(Holder::value)
                .orElseThrow(() -> new IllegalStateException("Recipe type not set."));
    }
}
