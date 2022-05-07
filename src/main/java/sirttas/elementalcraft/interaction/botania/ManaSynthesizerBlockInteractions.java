package sirttas.elementalcraft.interaction.botania;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import sirttas.elementalcraft.block.AbstractECEntityBlock;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.manasynthesizer.ManaSynthesizerBlockEntity;
import sirttas.elementalcraft.particle.ParticleHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class ManaSynthesizerBlockInteractions {

    private ManaSynthesizerBlockInteractions() {}


    public static BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
            return new ManaSynthesizerBlockEntity(pos, state);
    }

    @Nullable
    public static <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockEntityType<T> type) {
        return AbstractECEntityBlock.createECServerTicker(level, type, ManaSynthesizerBlockEntity.TYPE.get(), ManaSynthesizerBlockEntity::serverTick);
    }

    @OnlyIn(Dist.CLIENT)
    public static void animateTick(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Random rand) {
        BlockEntityHelper.getBlockEntityAs(level, pos, ManaSynthesizerBlockEntity.class).filter(ManaSynthesizerBlockEntity::isWorking).flatMap(ManaSynthesizerBlockEntity::getElementStorage)
                .ifPresent(storage -> ParticleHelper.createElementFlowParticle(storage.getElementType(), level, Vec3.atCenterOf(pos.below()), Direction.DOWN, 1, rand));
    }

}
