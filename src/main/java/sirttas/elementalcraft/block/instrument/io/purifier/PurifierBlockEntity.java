package sirttas.elementalcraft.block.instrument.io.purifier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.io.AbstractIOInstrumentBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.instrument.io.IPurifierRecipe;

import javax.annotation.Nonnull;

public class PurifierBlockEntity extends AbstractIOInstrumentBlockEntity<PurifierBlockEntity, IPurifierRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + PurifierBlock.NAME) public static final BlockEntityType<PurifierBlockEntity> TYPE = null;

	private final PurifierContainer inventory;

	public PurifierBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, null, ECConfig.COMMON.purifierTransferSpeed.get(), ECConfig.COMMON.purifierMaxRunes.get());
		inventory = new PurifierContainer(this::setChanged);
	}

	@Nonnull
    @Override
	protected @NotNull IItemHandler createHandler() {
		return new SidedInvWrapper(inventory, null);
	}

	@Override
	protected IPurifierRecipe lookupRecipe() {
		ItemStack input = inventory.getItem(0);

		if (!input.isEmpty()) {
			IPurifierRecipe recipe = ElementalCraft.PURE_ORE_MANAGER.getRecipes(input);

			if (recipe.matches(this)) {
				return recipe;
			}
		}
		return null;
	}

	@Nonnull
    @Override
	public @NotNull Container getInventory() {
		return inventory;
	}
}
