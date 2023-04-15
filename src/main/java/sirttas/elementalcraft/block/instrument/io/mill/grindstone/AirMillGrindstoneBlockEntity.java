package sirttas.elementalcraft.block.instrument.io.mill.grindstone;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.io.AbstractIOInstrumentBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.container.IOContainer;
import sirttas.elementalcraft.interaction.ECinteractions;
import sirttas.elementalcraft.interaction.ie.IEInteraction;
import sirttas.elementalcraft.interaction.mekanism.MekanismInteraction;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.io.grinding.IGrindingRecipe;

import javax.annotation.Nonnull;

public class AirMillGrindstoneBlockEntity extends AbstractIOInstrumentBlockEntity<AirMillGrindstoneBlockEntity, IGrindingRecipe> {

	private final IOContainer inventory;

	public AirMillGrindstoneBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.AIR_MILL_GRINDSTONE, pos, state, ECRecipeTypes.AIR_MILL_GRINDING.get(), ECConfig.COMMON.airMillGrindstoneTransferSpeed.get(), ECConfig.COMMON.airMillGrindstoneMaxRunes.get());
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
		if (getContainerElementType() == ElementType.NONE) {
			return null;
		}

		var recipe = super.lookupRecipe();
		
		if (recipe == null && ECinteractions.isMekanismActive()) {
			recipe = MekanismInteraction.lookupCrusherRecipe(level, this);
		}
		if (recipe == null && ECinteractions.isImmersiveEngineeringActive()) {
			recipe = IEInteraction.lookupCrusherRecipe(level, this);
		}
		return recipe;
	}
}
