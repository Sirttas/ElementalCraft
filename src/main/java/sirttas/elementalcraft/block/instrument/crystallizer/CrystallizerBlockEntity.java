package sirttas.elementalcraft.block.instrument.crystallizer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import sirttas.elementalcraft.api.rune.Rune.BonusType;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.instrument.AbstractInstrumentBlockEntity;
import sirttas.elementalcraft.block.instrument.InstrumentContainer;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.item.elemental.ShardItem;
import sirttas.elementalcraft.recipe.ECRecipeTypes;
import sirttas.elementalcraft.recipe.instrument.CrystallizationRecipe;

import javax.annotation.Nonnull;

public class CrystallizerBlockEntity extends AbstractInstrumentBlockEntity<CrystallizerBlockEntity, CrystallizationRecipe> {

	private static final Config<CrystallizerBlockEntity, CrystallizationRecipe> CONFIG = new Config<>(
			ECBlockEntityTypes.CRYSTALLIZER,
			ECRecipeTypes.CRYSTALLIZATION,
			ECConfig.COMMON.crystallizerTransferSpeed,
			ECConfig.COMMON.crystallizerMaxRunes,
			0,
			true
	);

	private final InstrumentContainer inventory;

	public CrystallizerBlockEntity(BlockPos pos, BlockState state) {
		super(CONFIG, pos, state);
		inventory = new CrystallizerContainer(this);
		lockable = true;
		particleOffset = new Vec3(0, 0.2, 0);
	}

	public int getItemCount() {
		return inventory.getItemCount();
	}
	
	@Override
	public void assemble() {
		var luck = getRuneHandler().getBonus(BonusType.LUCK) * ECConfig.COMMON.crystallizerLuckRatio.get();
		
		for (int i = 2; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			
			if (isValidShard(stack) && stack.getItem() instanceof ShardItem shardItem) {
				luck += shardItem.getElementAmount();
			}
		}
		
		ItemStack gem = inventory.getItem(0);
		
		clearContent();
		inventory.setItem(0, recipe.assemble(gem, this, (float) luck));
	}

	public boolean isValidShard(ItemStack stack) {
		return recipe != null && recipe.isValidShard(stack);
	}

	@Nonnull
    @Override
	public Container getInventory() {
		return inventory;
	}
}
