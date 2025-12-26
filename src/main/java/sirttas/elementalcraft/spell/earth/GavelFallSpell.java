package sirttas.elementalcraft.spell.earth;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;
import java.util.List;

public class GavelFallSpell extends Spell {

	public static final String NAME = "gravelfall";

	public GavelFallSpell(ResourceKey<Spell> key) {
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

	private InteractionResult spawnGravel(@Nonnull Level level, Entity sender, BlockPos pos) {
		checkAndSpawn(level, pos.above(4));
		checkAndSpawn(level, pos.above(5));
		checkAndSpawn(level, pos.above(6));
		return InteractionResult.SUCCESS;
	}

	@Nonnull
	@Override
	public InteractionResult castOnBlock(@Nonnull Level level, @Nonnull Entity sender, @Nonnull BlockPos target, @Nonnull BlockHitResult hitResult) {
		return spawnGravel(level, sender, target);
	}

	@Nonnull
	@Override
	public InteractionResult castOnEntity(@Nonnull Level level, @Nonnull Entity caster, @Nonnull Entity target) {
		return spawnGravel(level, caster, BlockPos.containing(target.position()));
	}

	@Override
	public boolean consume(Entity sender, boolean simulate) {
		boolean value = consume(sender, Blocks.GRAVEL, 3, simulate);

		return super.consume(sender, simulate) && value;
	}
	
	@Override
	public void addInformation(List<Component> tooltip) {
		tooltip.add(Component.translatable("tooltip.elementalcraft.consumes", Component.translatable("tooltip.elementalcraft.count", 3, Blocks.GRAVEL.getName()))
				.withStyle(ChatFormatting.YELLOW));
	}
}
