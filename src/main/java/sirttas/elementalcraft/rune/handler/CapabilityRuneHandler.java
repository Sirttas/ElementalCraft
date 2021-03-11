package sirttas.elementalcraft.rune.handler;

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
import sirttas.elementalcraft.rune.Rune;

public class CapabilityRuneHandler {
	
	@CapabilityInject(IRuneHandler.class) public static final Capability<IRuneHandler> RUNE_HANDLE_CAPABILITY = null;

	private CapabilityRuneHandler() {}
	
	public static void register() {
		CapabilityManager.INSTANCE.register(IRuneHandler.class, new Capability.IStorage<IRuneHandler>() {
			@Override
			@Deprecated
			public INBT writeNBT(Capability<IRuneHandler> capability, IRuneHandler instance, Direction side) {
				ListNBT nbtTagList = new ListNBT();

				instance.getRunes().forEach(rune -> nbtTagList.add(StringNBT.valueOf(rune.getId().toString())));
				return nbtTagList;
			}

			@Override
			@Deprecated
			public void readNBT(Capability<IRuneHandler> capability, IRuneHandler instance, Direction side, INBT base) {
				((ListNBT) base).forEach(nbt -> {
					String name = ((StringNBT) nbt).getString();
					Rune rune = ElementalCraft.RUNE_MANAGER.get(new ResourceLocation(name));

					if (rune != null) {
						instance.addRune(rune);
					} else {
						ElementalCraft.LOGGER.warn("Rune not fount with id: {}", name);
					}
				});
			}
		}, () -> new RuneHandler(3));
	}

	public static @Nonnull IRuneHandler getRuneHandlerAt(IWorldReader world, BlockPos pos) {
		return TileEntityHelper.getTileEntity(world, pos).map(t -> t.getCapability(RUNE_HANDLE_CAPABILITY).orElse(EmptyRuneHandler.INSTANCE)).orElse(EmptyRuneHandler.INSTANCE);
	}

}
