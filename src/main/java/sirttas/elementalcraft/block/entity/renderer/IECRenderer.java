package sirttas.elementalcraft.block.entity.renderer;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import sirttas.elementalcraft.renderer.IECGenericRenderer;

public interface IECRenderer<T extends BlockEntity> extends BlockEntityRenderer<T>, IECGenericRenderer {

}
