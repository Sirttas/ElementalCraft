package sirttas.elementalcraft.spell.earth;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.SpellCastResult;
import sirttas.elementalcraft.spell.properties.SpellProperties;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class StoneWallSpell extends Spell {

	public static final String NAME = "stonewall";

	public StoneWallSpell(Holder<SpellProperties> properties) {
		super(properties);
	}

	private void checkAndPlace(Level level, BlockPos pos) {
		if (level.getBlockState(pos).canBeReplaced()) {
			level.setBlockAndUpdate(pos, Blocks.STONE.defaultBlockState());
		}
	}

	public SpellCastResult cast(Level level, Entity sender, BlockPos pos, Direction direction) {
		checkAndPlace(level, pos);
		checkAndPlace(level, pos.relative(direction.getClockWise()));
		checkAndPlace(level, pos.relative(direction.getCounterClockWise()));
		checkAndPlace(level, pos.above(1));
		checkAndPlace(level, pos.above(2));
		checkAndPlace(level, pos.relative(direction.getClockWise()).above(1));
		checkAndPlace(level, pos.relative(direction.getClockWise()).above(2));
		checkAndPlace(level, pos.relative(direction.getCounterClockWise()).above(1));
		checkAndPlace(level, pos.relative(direction.getCounterClockWise()).above(2));
		return SpellCastResult.SUCCESS;
	}

	@Override
	public SpellCastResult castOnSelf(Level level, Entity caster) {
		return Stream.of(Direction.orderedByNearest(caster))
				.filter(d -> d.getAxis() != Axis.Y)
				.findFirst()
				.map(direction -> cast(level, caster, BlockPos.containing(caster.position()).relative(direction, 3), direction))
				.orElse(SpellCastResult.PASS);
    }

	@Override
	public boolean consume(Entity sender, boolean simulate) {
		boolean value = consume(sender, Blocks.STONE, 9, simulate);

		return super.consume(sender, simulate) && value;
	}

    @Override
    public void addInformation(Consumer<Component> builder) {
        builder.accept(Component.translatable("tooltip.elementalcraft.consumes", Component.translatable("tooltip.elementalcraft.count", 9, Blocks.STONE.getName()))
				.withStyle(ChatFormatting.YELLOW));
	}
}
