package sirttas.elementalcraft.spell.earth;

import java.util.List;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import sirttas.elementalcraft.spell.Spell;

public class GavelFallSpell extends Spell {

	public static final String NAME = "gravelfall";

	private void spawn(World world, BlockPos pos) {
		FallingBlockEntity entity = new FallingBlockEntity(world, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, Blocks.GRAVEL.defaultBlockState());

		entity.time = 1;
		entity.setHurtsEntities(true);
		world.addFreshEntity(entity);
	}

	private void checkAndSpawn(World world, BlockPos pos) {
		if (world.isEmptyBlock(pos)) {
			spawn(world, pos);
		}
	}

	private ActionResultType spawnGravel(Entity sender, BlockPos pos) {
		World world = sender.getCommandSenderWorld();

		checkAndSpawn(world, pos.above(4));
		checkAndSpawn(world, pos.above(5));
		checkAndSpawn(world, pos.above(6));
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType castOnBlock(Entity sender, BlockPos target) {
		return spawnGravel(sender, target);
	}

	@Override
	public ActionResultType castOnEntity(Entity sender, Entity target) {
		return spawnGravel(sender, new BlockPos(target.position()));
	}

	@Override
	public boolean consume(Entity sender, boolean simulate) {
		boolean value = consume(sender, Blocks.GRAVEL, 3, simulate);

		return super.consume(sender, simulate) && value;
	}
	
	@Override
	public void addInformation(List<ITextComponent> tooltip) {
		tooltip.add(new TranslationTextComponent("tooltip.elementalcraft.consumes", new TranslationTextComponent("tooltip.elementalcraft.count", 3, Blocks.GRAVEL.getName()))
				.withStyle(TextFormatting.YELLOW));
	}
}
