package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.rune.handler.EmptyRuneHandler;
import sirttas.elementalcraft.api.rune.handler.IRuneHandler;
import sirttas.elementalcraft.particle.ParticleHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class BlockEntityHelper {
	
	private BlockEntityHelper() {}
	
	public static Optional<BlockEntity> getBlockEntity(@Nonnull BlockGetter level, @Nonnull BlockPos pos) {
		return Optional.ofNullable(level.getBlockEntity(pos));
	}

	public static <T> Optional<T> getBlockEntityAs(@Nonnull BlockGetter level, @Nonnull BlockPos pos, @Nonnull Class<T> clazz) {
		return getBlockEntity(level, pos).filter(clazz::isInstance).map(clazz::cast);
	}

	@Nullable
	public static <T, C> T getCapability(BlockCapability<@NotNull T, C> cap, @Nonnull BlockEntity blockEntity, C context) {
		if (!blockEntity.hasLevel()) {
			return null;
		}
		return blockEntity.getLevel().getCapability(cap, blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, context);
	}

	@Nonnull
	public static IRuneHandler getRuneHandlerAt(LevelReader level, BlockPos pos) {
		return getRuneHandlerAt(level, pos, null);
	}

	@Nonnull
	public static IRuneHandler getRuneHandlerAt(LevelReader level, BlockPos pos, @Nullable Direction direction) {
		if (!(level instanceof Level l)) {
			return EmptyRuneHandler.INSTANCE;
		}

		var cap = l.getCapability(ElementalCraftCapabilities.RuneHandlers.BLOCK, pos, direction);

		if (cap == null) {
			return EmptyRuneHandler.INSTANCE;
		}
		return cap;
	}

	public static void renderItemBreaking(Level level, BlockPos pos, ItemStack stack) {
		if (level == null) {
			return;
		}

        var sound = stack.get(DataComponents.BREAK_SOUND);

        if (sound == null || !sound.isBound()) {
            return;
        }

		level.playSound(null, pos, sound.value(), SoundSource.BLOCKS);
		ParticleHelper.createItemBreakParticle(level, pos.getCenter(), level.getRandom(), stack, 5);
	}
}
