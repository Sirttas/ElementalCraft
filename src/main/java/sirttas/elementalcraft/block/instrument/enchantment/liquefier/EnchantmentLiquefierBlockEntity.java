package sirttas.elementalcraft.block.instrument.enchantment.liquefier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.Container;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.instrument.enchantment.liquefaction.EnchantmentLiquefactionRecipe;

public class EnchantmentLiquefierBlockEntity extends AbstractInstrumentBlockEntity<EnchantmentLiquefierBlockEntity, EnchantmentLiquefactionRecipe> {

    private static final Config<EnchantmentLiquefierBlockEntity, EnchantmentLiquefactionRecipe> CONFIG = new Config<>(
            ECBlockEntityTypes.ENCHANTMENT_LIQUEFIER,
            null,
            ECConfig.COMMON.enchantmentLiquefierTransferSpeed,
            ECConfig.COMMON.enchantmentLiquefierMaxRunes,
            1,
            true,
            true
    );

    private final InstrumentContainer inventory;

    public EnchantmentLiquefierBlockEntity(BlockPos pos, BlockState state) {
        super(CONFIG, pos, state);
        inventory = new EnchantmentLiquefierContainer(this);
        particleOffset = new Vec3(0, 0.4, 0);
    }

    @NotNull
    @Override
    public Container getInventory() {
        return inventory;
    }

    @Override
    protected EnchantmentLiquefactionRecipe lookupRecipe() {
        if (level == null) {
            return null;
        }

        var enchantments = EnchantmentHelper.getEnchantments(inventory.getItem(0));

        for (var enchantment : enchantments.keySet()) {
            var recipe = new EnchantmentLiquefactionRecipe(enchantment);

            if (recipe.matches(this, level)) {
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
        var rand = level.random;

        if (rand.nextInt(4) != 0) {
            return;
        }

        var x = worldPosition.getX() + (5 + rand.nextDouble() * 6) / 16;
        var y = worldPosition.getY() + 20D / 16;
        var z = worldPosition.getZ() + (5 + rand.nextDouble() * 6) / 16;

        level.addParticle(ParticleTypes.ENCHANT, x, y, z, 0, 0.33, 0);
    }

}
