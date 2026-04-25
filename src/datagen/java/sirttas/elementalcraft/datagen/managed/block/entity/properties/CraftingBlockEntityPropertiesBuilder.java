package sirttas.elementalcraft.datagen.managed.block.entity.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeType;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.block.entity.properties.ConfigurableBlockEntityPropertiesType;

public class CraftingBlockEntityPropertiesBuilder implements IConfigurableBlockEntityPropertiesBuilder {

    public static final MapCodec<CraftingBlockEntityPropertiesBuilder> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            BuiltInRegistries.RECIPE_TYPE.holderByNameCodec().optionalFieldOf("recipe_type", null).forGetter(b -> b.recipeType),
            Codec.INT.fieldOf(ECNames.TRANSFER_SPEED).forGetter(b -> b.transferSpeed),
            Codec.INT.fieldOf(ECNames.MAX_RUNES).forGetter(b -> b.maxRunes),
            Codec.INT.optionalFieldOf("output_slot", 0).forGetter(b -> b.outputSlot),
            Codec.BOOL.optionalFieldOf("retrieve_all", false).forGetter(b -> b.retrieveAll),
            Codec.BOOL.optionalFieldOf("lockable", false).forGetter(b -> b.lockable)
    ).apply(builder, (a1, a2, a3, a4, a5, a6) -> {
        throw new UnsupportedOperationException("Builder deserialization is not supported.");
    }));

    private Holder<RecipeType<?>> recipeType;
    private int transferSpeed;
    private int maxRunes;
    private int outputSlot;
    private boolean retrieveAll;
    private boolean lockable;

    public CraftingBlockEntityPropertiesBuilder() {
        this.recipeType = null;
        this.transferSpeed = 0;
        this.maxRunes = 0;
        this.outputSlot = 0;
        this.retrieveAll = false;
        this.lockable = false;
    }

    @Override
    public ConfigurableBlockEntityPropertiesType<?> getType() {
        return ConfigurableBlockEntityPropertiesType.CRAFTING.get();
    }

    public CraftingBlockEntityPropertiesBuilder recipeType(RecipeType<?> recipeType) {
        return this.recipeType(BuiltInRegistries.RECIPE_TYPE.getResourceKey(recipeType).orElseThrow(() -> new IllegalArgumentException("Unknown recipe type.")));
    }

    public CraftingBlockEntityPropertiesBuilder recipeType(ResourceKey<RecipeType<?>> recipeType) {
        return this.recipeType(BuiltInRegistries.RECIPE_TYPE.getOrThrow(recipeType));
    }

    public CraftingBlockEntityPropertiesBuilder recipeType(Holder<RecipeType<?>> recipeType) {
        this.recipeType = recipeType;
        return this;
    }

    public CraftingBlockEntityPropertiesBuilder transferSpeed(int transferSpeed) {
        this.transferSpeed = transferSpeed;
        return this;
    }

    public CraftingBlockEntityPropertiesBuilder maxRunes(int maxRunes) {
        this.maxRunes = maxRunes;
        return this;
    }

    public CraftingBlockEntityPropertiesBuilder outputSlot(int outputSlot) {
        this.outputSlot = outputSlot;
        return this;
    }

    public CraftingBlockEntityPropertiesBuilder retrieveAll() {
        this.retrieveAll = true;
        return this;
    }

    public CraftingBlockEntityPropertiesBuilder lockable() {
        this.lockable = true;
        return this;
    }
}
