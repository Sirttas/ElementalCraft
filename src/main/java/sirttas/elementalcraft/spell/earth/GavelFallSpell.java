package sirttas.elementalcraft.spell.earth;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import sirttas.elementalcraft.spell.Spell;

import java.util.List;

public class GavelFallSpell extends Spell {

	public static final String NAME = "gravelfall";

	public GavelFallSpell(ResourceKey<Spell> key) {
		super(key);
	}

	private void spawn(Level world, BlockPos pos) {
		FallingBlockEntity entity = FallingBlockEntity.fall(world, pos, Blocks.GRAVEL.defaultBlockState());

		entity.time = 1;
		entity.setHurtsEntities(1F, 100); // TODO config
		world.addFreshEntity(entity);
	}

	private void checkAndSpawn(Level world, BlockPos pos) {
		if (world.isEmptyBlock(pos)) {
			spawn(world, pos);
		}
	}

	private InteractionResult spawnGravel(Entity sender, BlockPos pos) {
		Level world = sender.getCommandSenderWorld();

		checkAndSpawn(world, pos.above(4));
		checkAndSpawn(world, pos.above(5));
		checkAndSpawn(world, pos.above(6));
		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult castOnBlock(Entity sender, BlockPos target) {
		return spawnGravel(sender, target);
	}

	@Override
	public InteractionResult castOnEntity(Entity sender, Entity target) {
		return spawnGravel(sender, new BlockPos(target.position()));
	}

	@Override
	public boolean consume(Entity sender, boolean simulate) {
		boolean value = consume(sender, Blocks.GRAVEL, 3, simulate);

		return super.consume(sender, simulate) && value;
	}
	
	@Override
	public void addInformation(List<Component> tooltip) {
		tooltip.add(new TranslatableComponent("tooltip.elementalcraft.consumes", new TranslatableComponent("tooltip.elementalcraft.count", 3, Blocks.GRAVEL.getName()))
				.withStyle(ChatFormatting.YELLOW));
	}
}
