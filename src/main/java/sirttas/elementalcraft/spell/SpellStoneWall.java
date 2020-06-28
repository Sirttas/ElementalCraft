package sirttas.elementalcraft.spell;

import java.util.Optional;
import java.util.stream.Stream;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpellStoneWall extends Spell implements ISelfCastedSpell {

	public static final String NAME = "stonewall";

	private void spawn(World world, BlockPos pos) {
		world.setBlockState(pos, Blocks.STONE.getDefaultState());
	}

	private ActionResultType checkAndSpawn(Entity sender, World world, BlockPos pos) {
		if (world.isAirBlock(pos)) {
			if (sender instanceof PlayerEntity && !((PlayerEntity) sender).isCreative()) {
				PlayerInventory inv = ((PlayerEntity) sender).inventory;
				int slot = inv.getSlotFor(new ItemStack(Items.STONE));

				if (slot >= 0) {
					inv.getStackInSlot(slot).shrink(1);
					spawn(world, pos);
					return ActionResultType.SUCCESS;
				}
			} else {
				spawn(world, pos);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	public ActionResultType cast(Entity sender, BlockPos pos, Direction direction) {
		World world = sender.getEntityWorld();

		checkAndSpawn(sender, world, pos);
		checkAndSpawn(sender, world, pos.offset(direction.rotateY()));
		checkAndSpawn(sender, world, pos.offset(direction.rotateYCCW()));
		checkAndSpawn(sender, world, pos.up(1));
		checkAndSpawn(sender, world, pos.up(2));
		checkAndSpawn(sender, world, pos.offset(direction.rotateY()).up(1));
		checkAndSpawn(sender, world, pos.offset(direction.rotateY()).up(2));
		checkAndSpawn(sender, world, pos.offset(direction.rotateYCCW()).up(1));
		return checkAndSpawn(sender, world, pos.offset(direction.rotateYCCW()).up(2));
	}

	@Override
	public ActionResultType castOnSelf(Entity sender) {
		Optional<Direction> opt = Stream.of(Direction.getFacingDirections(sender)).filter(d -> d.getAxis() != Axis.Y).findFirst();
		
		if (!opt.isPresent()) {
			return ActionResultType.PASS;
		}
		return cast(sender, sender.getPosition().offset(opt.get(), 3), opt.get());
	}
}
