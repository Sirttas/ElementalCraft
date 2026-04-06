package sirttas.elementalcraft.block.synthesizer.cracking;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.synthesizer.AbstractSynthesizerBlockEntity;
import sirttas.elementalcraft.range.RangeRenderTimer;
import sirttas.elementalcraft.recipe.cracking.AbstractCrackingRecipe;
import sirttas.elementalcraft.recipe.cracking.CrackingRecipeInput;

import java.util.Optional;
import java.util.function.Supplier;

public class AbstractCrackingSynthesizerBlockEntity<T extends AbstractCrackingRecipe> extends AbstractSynthesizerBlockEntity {

    private final RangeRenderTimer rangeRenderTimer = new RangeRenderTimer();

    DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull T>> recipeType;

    public AbstractCrackingSynthesizerBlockEntity(
            Supplier<? extends BlockEntityType<?>> blockEntityType,
            Holder<@NotNull IConfigurableBlockEntityProperties> propertiesHolder,
            DeferredHolder<@NotNull RecipeType<?>, @NotNull RecipeType<@NotNull T>> recipeType,
            BlockPos pos,
            BlockState state) {
        super(blockEntityType, propertiesHolder, pos, state);
        this.recipeType = recipeType;
    }

    @Override
    protected int synthesizeElement() {
        if (!(level instanceof ServerLevel serverLevel)) {
            return 0;
        }
        return findRecipe(serverLevel)
                .map(pair -> {
                    var recipe = pair.getSecond().value();

                    serverLevel.setBlockAndUpdate(pair.getFirst(), recipe.result().defaultBlockState());
                    // TODO play animation?
                    return recipe.elementAmount();
                }).orElse(0);
    }

    private Optional<Pair<BlockPos, RecipeHolder<@NotNull T>>> findRecipe(ServerLevel level) {
        var recipeManager = level.recipeAccess();
        var type = recipeType.get();

        return getBlocksInRange()
                .<Pair<BlockPos, RecipeHolder<@NotNull T>>>mapMulti((pos, downstream) -> {
                    var state = level.getBlockState(pos);

                    if (state.isAir()) {
                        return;
                    }
                    recipeManager.getRecipeFor(type, new CrackingRecipeInput(level.getBlockState(pos)), level).ifPresent(recipe -> downstream.accept(Pair.of(pos, recipe)));
                })
                .findAny();
    }

    public boolean showsRange() {
        return rangeRenderTimer.showsRange();
    }

    public void startShowingRange() {
        rangeRenderTimer.startShowingRange();
    }
}
