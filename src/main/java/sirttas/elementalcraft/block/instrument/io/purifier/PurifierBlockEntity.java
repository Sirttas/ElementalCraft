package sirttas.elementalcraft.block.instrument.io.purifier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.io.AbstractIOInstrumentBlockEntity;
import sirttas.elementalcraft.recipe.instrument.io.SimpleIOInstrumentRecipeInput;
import sirttas.elementalcraft.recipe.instrument.io.purification.OrePurificationRecipe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PurifierBlockEntity extends AbstractIOInstrumentBlockEntity<SimpleIOInstrumentRecipeInput, OrePurificationRecipe> {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(PurifierBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	private final PurifierContainer inventory;

	public PurifierBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.PURIFIER, PROPERTIES, pos, state);
		inventory = new PurifierContainer(this::setChanged);
	}

	@Nonnull
    @Override
	public IItemHandler getItemHandler(@Nullable Direction direction) {
		return new SidedInvWrapper(inventory, direction);
	}

	@NotNull
	@Override
	protected SimpleIOInstrumentRecipeInput createRecipeInput() {
		return createSimpleIORecipeInput();
	}

	@Nonnull
    @Override
	public @NotNull Container getInventory() {
		return inventory;
	}
}
