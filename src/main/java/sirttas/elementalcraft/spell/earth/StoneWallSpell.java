package sirttas.elementalcraft.spell.earth;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import sirttas.elementalcraft.spell.Spell;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class StoneWallSpell extends Spell {

	public static final String NAME = "stonewall";

	public StoneWallSpell(ResourceKey<Spell> key) {
		super(key);
	}

	private void checkAndPlace(Level level, BlockPos pos) {
		if (level.getBlockState(pos).canBeReplaced()) {
			level.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
		}
	}

	public InteractionResult cast(Entity sender, BlockPos pos, Direction direction) {
		Level world = sender.level();

		checkAndPlace(world, pos);
		checkAndPlace(world, pos.relative(direction.getClockWise()));
		checkAndPlace(world, pos.relative(direction.getCounterClockWise()));
		checkAndPlace(world, pos.above(1));
		checkAndPlace(world, pos.above(2));
		checkAndPlace(world, pos.relative(direction.getClockWise()).above(1));
		checkAndPlace(world, pos.relative(direction.getClockWise()).above(2));
		checkAndPlace(world, pos.relative(direction.getCounterClockWise()).above(1));
		checkAndPlace(world, pos.relative(direction.getCounterClockWise()).above(2));
		return InteractionResult.SUCCESS;
	}

	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
		Optional<Direction> opt = Stream.of(Direction.orderedByNearest(caster)).filter(d -> d.getAxis() != Axis.Y).findFirst();
		
		if (!opt.isPresent()) {
			return InteractionResult.PASS;
		}
		return cast(caster, BlockPos.containing(caster.position()).relative(opt.get(), 3), opt.get());
	}

	@Override
	public boolean consume(Entity sender, boolean simulate) {
		boolean value = consume(sender, Blocks.STONE, 9, simulate);

		return super.consume(sender, simulate) && value;
	}
	
	@Override
	public void addInformation(List<Component> tooltip) {
		tooltip.add(Component.translatable("tooltip.elementalcraft.consumes", Component.translatable("tooltip.elementalcraft.count", 9, Blocks.STONE.getName()))
				.withStyle(ChatFormatting.YELLOW));
	}
}
