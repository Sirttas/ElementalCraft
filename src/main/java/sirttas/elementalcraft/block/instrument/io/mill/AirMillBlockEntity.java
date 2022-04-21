package sirttas.elementalcraft.block.instrument.io.mill;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.io.AbstractIOInstrumentBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.IOContainer;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.mekanism.MekanismInteraction;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import javax.annotation.Nonnull;

public class AirMillBlockEntity extends AbstractIOInstrumentBlockEntity<AirMillBlockEntity, IGrindingRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + AirMillBlock.NAME) public static final BlockEntityType<AirMillBlockEntity> TYPE = null;

	private final IOContainer inventory;

	public AirMillBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, IGrindingRecipe.TYPE, ECConfig.COMMON.airMillTransferSpeed.get(), ECConfig.COMMON.airMillMaxRunes.get());
		inventory = new IOContainer(this::setChanged);
	}

	@Nonnull
    @Override
	protected IItemHandler createHandler() {
		return new SidedInvWrapper(inventory, null);
	}

	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}
	
	@Override
	protected IGrindingRecipe lookupRecipe() {
		var recipe = super.lookupRecipe();
		
		if (recipe == null && ECinteractions.isMekanismActive()) {
			return MekanismInteraction.lookupCrusherRecipe(level, inventory);
		}
		return recipe;
	}
}
