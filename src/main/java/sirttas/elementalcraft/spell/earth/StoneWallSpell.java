package sirttas.elementalcraft.spell.earth;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import sirttas.elementalcraft.spell.Spell;

public class StoneWallSpell extends Spell {

	public static final String NAME = "stonewall";

	private void spawn(World world, BlockPos pos) {
		world.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
	}

	private void checkAndSpawn(World world, BlockPos pos) {
		if (world.getBlockState(pos).getMaterial().isReplaceable()) {
			spawn(world, pos);
		}
	}

	public ActionResultType cast(Entity sender, BlockPos pos, Direction direction) {
		World world = sender.getCommandSenderWorld();

		checkAndSpawn(world, pos);
		checkAndSpawn(world, pos.relative(direction.getClockWise()));
		checkAndSpawn(world, pos.relative(direction.getCounterClockWise()));
		checkAndSpawn(world, pos.above(1));
		checkAndSpawn(world, pos.above(2));
		checkAndSpawn(world, pos.relative(direction.getClockWise()).above(1));
		checkAndSpawn(world, pos.relative(direction.getClockWise()).above(2));
		checkAndSpawn(world, pos.relative(direction.getCounterClockWise()).above(1));
		checkAndSpawn(world, pos.relative(direction.getCounterClockWise()).above(2));
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		Optional<Direction> opt = Stream.of(Direction.orderedByNearest(sender)).filter(d -> d.getAxis() != Axis.Y).findFirst();
		
		if (!opt.isPresent()) {
			return ActionResultType.PASS;
		}
		return cast(sender, new BlockPos(sender.position()).relative(opt.get(), 3), opt.get());
	}

	@Override
	public boolean consume(Entity sender, boolean simulate) {
		boolean value = consume(sender, Blocks.STONE, 9, simulate);

		return super.consume(sender, simulate) && value;
	}
	
	@Override
	public void addInformation(List<ITextComponent> tooltip) {
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.consumes", new TranslationTextComponent("tooltip.elementalcraft.count", 9, Blocks.STONE.getName()))
				.withStyle(TextFormatting.YELLOW));
	}
}
