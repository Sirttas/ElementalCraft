package sirttas.elementalcraft.block.instrument.infuser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.container.SingleItemContainer;
import sirttas.elementalcraft.recipe.input.SingleItemSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.infusion.InfusionRecipe;

import javax.annotation.Nonnull;

public class InfuserBlockEntity extends AbstractInstrumentBlockEntity<SingleItemSingleElementRecipeInput, InfusionRecipe> implements IInfuser {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(InfuserBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	private final SingleItemContainer inventory;

	public InfuserBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.INFUSER, PROPERTIES, pos, state);
		inventory = new SingleItemContainer(this::setChanged);
	}

	@Override
	protected @NotNull SingleItemSingleElementRecipeInput createRecipeInput() {
		return createInfusionRecipeInput();
	}

	@Override
	protected InfusionRecipe lookupRecipe(@NotNull SingleItemSingleElementRecipeInput recipeInput) {
		return this.lookupInfusionRecipe(level);
	}

	@Override
	protected boolean shouldRetrieverExtractOutput() {
		return this.recipe == null;
	}

	@Nonnull
	@Override
	public Container getInventory() {
		return inventory;
	}
}
