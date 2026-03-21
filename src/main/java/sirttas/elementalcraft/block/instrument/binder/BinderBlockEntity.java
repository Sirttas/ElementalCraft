package sirttas.elementalcraft.block.instrument.binder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.recipe.input.MultipleItemsSingleElementRecipeInput;
import sirttas.elementalcraft.recipe.instrument.binding.AbstractBindingRecipe;

import javax.annotation.Nonnull;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BinderBlockEntity extends AbstractInstrumentBlockEntity<MultipleItemsSingleElementRecipeInput, AbstractBindingRecipe> implements IBinder {

	public static final ResourceKey<@NotNull IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(BinderBlock.NAME);
	private static final Holder<@NotNull IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);
	protected static final int MAX_INVENTORY_SIZE = 20;
	private final InstrumentContainer inventory;

	public BinderBlockEntity(BlockPos pos, BlockState state) {
		this(ECBlockEntityTypes.BINDER, PROPERTIES, pos, state);
	}

	protected BinderBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, Holder<@NotNull IConfigurableBlockEntityProperties> properties, BlockPos pos, BlockState state) {
		super(blockEntityType, properties, pos, state);
		inventory = new InstrumentContainer(this::setChanged, MAX_INVENTORY_SIZE);
		particleOffset = new Vec3(0, 0.2, 0);
	}

	@Override
	public int getItemCount() {
		return inventory.getItemCount();
	}
	
	@Nonnull
	@Override
	public Container getInventory() {
		return inventory;
	}

	@Override
	protected void setRemainingItems(NonNullList<@NotNull ItemStack> remainingItems) {
		var list = remainingItems.stream()
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toList());

        for (var targetIndex = 1; targetIndex < MAX_INVENTORY_SIZE; targetIndex++) {
			inventory.setItem(targetIndex, list.isEmpty() ? ItemStack.EMPTY : list.removeFirst());
        }
	}

	@Override
	protected @NotNull MultipleItemsSingleElementRecipeInput createRecipeInput() {
		return new MultipleItemsSingleElementRecipeInput(
				inventory.getStacks(),
				getElementType(),
				getContainer().getElementAmount());
	}
}
