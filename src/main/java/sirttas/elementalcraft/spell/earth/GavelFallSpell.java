package sirttas.elementalcraft.spell.earth;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class GavelFallSpell extends Spell {

	public static final String NAME = "gravelfall";

	public GavelFallSpell(ResourceKey<@NotNull Spell> key) {
		super(key);
	}

	private void spawn(Level level, BlockPos pos) {
		FallingBlockEntity entity = FallingBlockEntity.fall(level, pos, Blocks.GRAVEL.defaultBlockState());

		entity.time = 1;
		entity.setHurtsEntities(getStrength(), 100);
		level.addFreshEntity(entity);
	}

	private void checkAndSpawn(Level level, BlockPos pos) {
		if (level.isEmptyBlock(pos)) {
			spawn(level, pos);
		}
	}

    private SpellCastResult spawnGravel(@Nonnull Level level, BlockPos pos) {
        checkAndSpawn(level, pos.above(4));
        checkAndSpawn(level, pos.above(5));
        checkAndSpawn(level, pos.above(6));
        return SpellCastResult.SUCCESS;
    }

	@Nonnull
	@Override
	public SpellCastResult castOnBlock(@Nonnull Level level, @Nonnull Entity sender, @Nonnull BlockPos target, @Nonnull BlockHitResult hitResult) {
		return spawnGravel(level, target);
	}

	@Nonnull
	@Override
	public SpellCastResult castOnEntity(@Nonnull Level level, @Nonnull Entity caster, @Nonnull Entity target) {
		return spawnGravel(level, BlockPos.containing(target.position()));
	}

	@Override
	public boolean consume(Entity sender, boolean simulate) {
		boolean value = consume(sender, Blocks.GRAVEL, 3, simulate);

		return super.consume(sender, simulate) && value;
	}
    @Override
    public void addInformation(Consumer<Component> builder) {
        builder.accept(Component.translatable("tooltip.elementalcraft.consumes", Component.translatable("tooltip.elementalcraft.count", 3, Blocks.GRAVEL.getName()))
				.withStyle(ChatFormatting.YELLOW));
	}
}
