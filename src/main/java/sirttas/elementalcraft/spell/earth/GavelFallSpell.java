package sirttas.elementalcraft.spell.earth;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.properties.SpellProperties;

import java.util.function.Consumer;

public class GavelFallSpell extends Spell {

	public static final String NAME = "gravelfall";

	public GavelFallSpell(Holder<SpellProperties> properties) {
		super(properties);
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

    private SpellCastResult spawnGravel(Level level, BlockPos pos) {
        checkAndSpawn(level, pos.above(4));
        checkAndSpawn(level, pos.above(5));
        checkAndSpawn(level, pos.above(6));
        return SpellCastResult.SUCCESS;
    }

	@Override
	public SpellCastResult castOnBlock(Level level, Entity sender, BlockPos target, BlockHitResult hitResult) {
		return spawnGravel(level, target);
	}

	@Override
	public SpellCastResult castOnEntity(Level level, Entity caster, Entity target) {
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
