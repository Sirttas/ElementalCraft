package sirttas.elementalcraft.spell.earth;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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

	private void spawn(Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
	}

	private void checkAndSpawn(Level world, BlockPos pos) {
		if (world.getBlockState(pos).getMaterial().isReplaceable()) {
			spawn(world, pos);
		}
	}

	public InteractionResult cast(Entity sender, BlockPos pos, Direction direction) {
		Level world = sender.getLevel();

		checkAndSpawn(world, pos);
		checkAndSpawn(world, pos.relative(direction.getClockWise()));
		checkAndSpawn(world, pos.relative(direction.getCounterClockWise()));
		checkAndSpawn(world, pos.above(1));
		checkAndSpawn(world, pos.above(2));
		checkAndSpawn(world, pos.relative(direction.getClockWise()).above(1));
		checkAndSpawn(world, pos.relative(direction.getClockWise()).above(2));
		checkAndSpawn(world, pos.relative(direction.getCounterClockWise()).above(1));
		checkAndSpawn(world, pos.relative(direction.getCounterClockWise()).above(2));
		return InteractionResult.SUCCESS;
	}

	@Override
	public @Nonnull InteractionResult castOnSelf(@Nonnull Entity caster) {
		Optional<Direction> opt = Stream.of(Direction.orderedByNearest(caster)).filter(d -> d.getAxis() != Axis.Y).findFirst();
		
		if (!opt.isPresent()) {
			return InteractionResult.PASS;
		}
		return cast(caster, new BlockPos(caster.position()).relative(opt.get(), 3), opt.get());
	}

	@Override
	public boolean consume(Entity sender, boolean simulate) {
		boolean value = consume(sender, Blocks.STONE, 9, simulate);

		return super.consume(sender, simulate) && value;
	}
	
	@Override
	public void addInformation(List<Component> tooltip) {
		tooltip.add(new TranslatableComponent("tooltip.elementalcraft.consumes", new TranslatableComponent("tooltip.elementalcraft.count", 9, Blocks.STONE.getName()))
				.withStyle(ChatFormatting.YELLOW));
	}
}
