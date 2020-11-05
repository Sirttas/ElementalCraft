package sirttas.elementalcraft.spell.earth;

import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sirttas.elementalcraft.spell.Spell;

public class SpellStoneWall extends Spell {

	public static final String NAME = "stonewall";

	private void spawn(World world, BlockPos pos) {
		world.setBlockState(pos, Blocks.STONE.getDefaultState());
	}

	@Override
	public boolean consume(Entity sender) {
		boolean value = consume(sender, Blocks.STONE, 9);

		return super.consume(sender) && value;
	}

	private void checkAndSpawn(World world, BlockPos pos) {
		if (world.getBlockState(pos).getMaterial().isReplaceable()) {
			spawn(world, pos);
		}
	}

	public ActionResultType cast(Entity sender, BlockPos pos, Direction direction) {
		World world = sender.getEntityWorld();

		checkAndSpawn(world, pos);
		checkAndSpawn(world, pos.offset(direction.rotateY()));
		checkAndSpawn(world, pos.offset(direction.rotateYCCW()));
		checkAndSpawn(world, pos.up(1));
		checkAndSpawn(world, pos.up(2));
		checkAndSpawn(world, pos.offset(direction.rotateY()).up(1));
		checkAndSpawn(world, pos.offset(direction.rotateY()).up(2));
		checkAndSpawn(world, pos.offset(direction.rotateYCCW()).up(1));
		checkAndSpawn(world, pos.offset(direction.rotateYCCW()).up(2));
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		Optional<Direction> opt = Stream.of(Direction.getFacingDirections(sender)).filter(d -> d.getAxis() != Axis.Y).findFirst();
		
		if (!opt.isPresent()) {
			return ActionResultType.PASS;
		}
		return cast(sender, new BlockPos(sender.getPositionVec()).offset(opt.get(), 3), opt.get());
	}

}
