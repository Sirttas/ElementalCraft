package sirttas.elementalcraft.block.instrument.io.purifier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.block.instrument.io.AbstractIOInstrumentBlockEntity;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.recipe.instrument.io.PurifierRecipe;

public class PurifierBlockEntity extends AbstractIOInstrumentBlockEntity<PurifierBlockEntity, PurifierRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + PurifierBlock.NAME) public static final BlockEntityType<PurifierBlockEntity> TYPE = null;

	private final PurifierInventory inventory;

	public PurifierBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, null, ECConfig.COMMON.purifierTransferSpeed.get(), ECConfig.COMMON.purifierMaxRunes.get());
		inventory = new PurifierInventory(this::setChanged);
	}

	@Override
	protected IItemHandler createHandler() {
		return new SidedInvWrapper(inventory, null);
	}

	@Override
	protected PurifierRecipe lookupRecipe() {
		ItemStack input = inventory.getItem(0);

		if (!input.isEmpty() && ElementalCraft.PURE_ORE_MANAGER.isValidOre(input)) {
			PurifierRecipe recipe = new PurifierRecipe(input);

			if (recipe.matches(this)) {
				return recipe;
			}
		}
		return null;
	}

	@Override
	public Container getInventory() {
		return inventory;
	}
}
