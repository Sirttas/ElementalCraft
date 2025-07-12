package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;

import javax.annotation.Nonnull;

public class CrystallizerBlockEntity extends AbstractInstrumentBlockEntity<MultipleItemsSingleElementRecipeInput, CrystallizationRecipe> {

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(CrystallizerBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	private final InstrumentContainer inventory;

	public CrystallizerBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.CRYSTALLIZER, PROPERTIES, pos, state);
		inventory = new CrystallizerContainer(this);
		particleOffset = new Vec3(0, 0.2, 0);
	}

	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}

	@NotNull
	@Override
	protected MultipleItemsSingleElementRecipeInput createRecipeInput() {
		return new MultipleItemsSingleElementRecipeInput(
				inventory.getStacks(),
				getElementType(),
				getContainer().getElementAmount());
	}
}
