package sirttas.elementalcraft.spell.water;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import sirttas.elementalcraft.ElementType;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.spell.IBlockCastedSpell;
import sirttas.elementalcraft.spell.Spell;

public class SpellRipening extends Spell implements IBlockCastedSpell {

	public static final String NAME = "ripening";

	public SpellRipening() {
		super(Properties.create(Spell.Type.UTILITY).elementType(ElementType.WATER).cooldown(ECConfig.CONFIG.ripeningCooldown.get()).consumeAmount(ECConfig.CONFIG.ripeningConsumeAmount.get()));
	}

	@Override
	public ActionResultType castOnBlock(Entity sender, BlockPos target) {
		World world = sender.getEntityWorld();
		BlockState state = world.getBlockState(target);
		Block block = state.getBlock();

		if (block instanceof IGrowable) {
			IGrowable growable = (IGrowable) block;

			if (growable.canUseBonemeal(world, world.rand, target, state)) {
				if (world instanceof ServerWorld) {
					for (int i = 0; i < 10 && growable.canGrow(world, target, state, world.isRemote); i++) {
						growable.grow((ServerWorld) world, world.rand, target, state);
						state = world.getBlockState(target);
					}
					world.playEvent(2005, target, 0);
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
}
