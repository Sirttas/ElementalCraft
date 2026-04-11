package sirttas.elementalcraft.spell.repair;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import sirttas.elementalcraft.spell.renderer.SpellRenderState;

public class RepairSpellRenderState extends SpellRenderState {
    public BlockPos anvilPos;
    public Direction anvilFacing;
    public final BlockModelRenderState anvil = new BlockModelRenderState();
    public boolean isStaff;
    public final ItemStackRenderState item = new ItemStackRenderState();
    public float firstPersonSwing;
    public float partialTicks;

}
