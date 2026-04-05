package sirttas.elementalcraft.block.instrument.enchantment.liquefier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.recipe.instrument.enchantment.liquefaction.EnchantmentLiquefactionRecipe;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;

public class EnchantmentLiquefierBlockEntity extends AbstractInstrumentBlockEntity<SimpleIOInstrumentRecipeInput, EnchantmentLiquefactionRecipe> {

    public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(EnchantmentLiquefierBlock.NAME);
    private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

    private final InstrumentContainer inventory;

    public EnchantmentLiquefierBlockEntity(BlockPos pos, BlockState state) {
        super(ECBlockEntityTypes.ENCHANTMENT_LIQUEFIER, PROPERTIES, pos, state);
        inventory = new EnchantmentLiquefierContainer(this);
        particleOffset = new Vec3(0, 0.4, 0);
    }

    @NotNull
    @Override
    public Container getInventory() {
        return inventory;
    }

    @Override
    protected @NotNull SimpleIOInstrumentRecipeInput createRecipeInput() {
        var inv = getInventory();
        var container = getContainer();

        return new SimpleIOInstrumentRecipeInput(
                inv.getItem(0),
                inv.getItem(1),
                1,
                level.getRandom(),
                container.getElementType(),
                container.getElementAmount(),
                getRuneHandler().getBonuses()
        );
    }

    @Override
    protected EnchantmentLiquefactionRecipe lookupRecipe(@NotNull ServerLevel level, @NotNull SimpleIOInstrumentRecipeInput recipeInput) {
        if (level == null) {
            return null;
        }

        var enchantments = EnchantmentHelper.getEnchantmentsForCrafting(inventory.getItem(0));

        for (var enchantment : enchantments.keySet()) {
            var recipe = new EnchantmentLiquefactionRecipe(enchantment);

            if (recipe.matches(createRecipeInput(), level)) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    protected void updateLock() {
        locked = !EnchantmentLiquefierHelper.isValidInput(inventory.getItem(0));
    }

    @Override
    protected void renderProgressParticles() {
        var rand = level.getRandom();

        if (rand.nextInt(4) != 0) {
            return;
        }

        var x = worldPosition.getX() + (5 + rand.nextDouble() * 6) / 16;
        var y = worldPosition.getY() + 20D / 16;
        var z = worldPosition.getZ() + (5 + rand.nextDouble() * 6) / 16;

        level.addParticle(ParticleTypes.ENCHANT, x, y, z, 0, 0.33, 0);
    }

}
