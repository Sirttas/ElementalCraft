package sirttas.elementalcraft.rune.capability;

import javax.annotation.Nonnull;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.tile.TileEntityHelper;

public class CapabilityRuneHandler {
	@CapabilityInject(IRuneHandler.class) public static final Capability<IRuneHandler> RUNE_HANDLE_CAPABILITY = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(IRuneHandler.class, new Capability.IStorage<IRuneHandler>() {
			@Override
			public INBT writeNBT(Capability<IRuneHandler> capability, IRuneHandler instance, Direction side) {
				ListNBT nbtTagList = new ListNBT();

				instance.getRunes().forEach(rune -> nbtTagList.add(StringNBT.valueOf(rune.getId().toString())));
				return nbtTagList;
			}

			@Override
			public void readNBT(Capability<IRuneHandler> capability, IRuneHandler instance, Direction side, INBT base) {
				((ListNBT) base).forEach(nbt -> instance.addRune(ElementalCraft.RUNE_MANAGER.get(new ResourceLocation(((StringNBT) nbt).getString()))));
			}
		}, () -> new RuneHandler(3));
	}

	public static @Nonnull IRuneHandler getRuneHandlerAt(IWorldReader world, BlockPos pos) {
		return TileEntityHelper.getTileEntity(world, pos).map(t -> t.getCapability(RUNE_HANDLE_CAPABILITY).orElse(EmptyRuneHandler.INSTANCE)).orElse(EmptyRuneHandler.INSTANCE);
	}

}
