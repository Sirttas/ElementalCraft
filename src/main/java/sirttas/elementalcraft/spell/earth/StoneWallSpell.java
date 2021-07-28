package sirttas.elementalcraft.spell.earth;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import sirttas.elementalcraft.spell.Spell;

public class StoneWallSpell extends Spell {

	public static final String NAME = "stonewall";

	private void spawn(Level world, BlockPos pos) {
		world.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
	}

	private void checkAndSpawn(Level world, BlockPos pos) {
		if (world.getBlockState(pos).getMaterial().isReplaceable()) {
			spawn(world, pos);
		}
	}

	public InteractionResult cast(Entity sender, BlockPos pos, Direction direction) {
		Level world = sender.getCommandSenderWorld();

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
	public InteractionResult castOnSelf(Entity sender) {
		Optional<Direction> opt = Stream.of(Direction.orderedByNearest(sender)).filter(d -> d.getAxis() != Axis.Y).findFirst();
		
		if (!opt.isPresent()) {
			return InteractionResult.PASS;
		}
		return cast(sender, new BlockPos(sender.position()).relative(opt.get(), 3), opt.get());
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
