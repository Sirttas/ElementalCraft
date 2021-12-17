package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ObjectHolder;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.block.evaporator.EvaporatorBlock;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.elemental.ShardItem;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;

import javax.annotation.Nonnull;

public class CrystallizerBlockEntity extends AbstractInstrumentBlockEntity<CrystallizerBlockEntity, CrystallizationRecipe> {

	@ObjectHolder(ElementalCraftApi.MODID + ":" + CrystallizerBlock.NAME) public static final BlockEntityType<CrystallizerBlockEntity> TYPE = null;

	private final InstrumentContainer inventory;

	public CrystallizerBlockEntity(BlockPos pos, BlockState state) {
		super(TYPE, pos, state, CrystallizationRecipe.TYPE, ECConfig.COMMON.crystallizerTransferSpeed.get(), ECConfig.COMMON.crystallizerMaxRunes.get());
		inventory = new CrystallizerContainer(this::setChanged);
		lockable = true;
		particleOffset = new Vec3(0, 0.2, 0);
	}

	public int getItemCount() {
		return inventory.getItemCount();
	}
	
	@Override
	public void assemble() {
		int luck = (int) Math.round(getRuneHandler().getBonus(BonusType.LUCK) * ECConfig.COMMON.crystallizerLuckRatio.get());
		
		for (int i = 2; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			
			if (EvaporatorBlock.getShardElementType(stack) == recipe.getElementType()) {
				luck += ((ShardItem) stack.getItem()).getElementAmount();
			}
		}
		
		ItemStack gem = inventory.getItem(0);
		
		clearContent();
		inventory.setItem(0, recipe.assemble(gem, this, luck));
	}

	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}
}
