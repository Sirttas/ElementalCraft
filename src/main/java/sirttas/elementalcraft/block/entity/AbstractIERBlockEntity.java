package sirttas.elementalcraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;
import sirttas.elementalcraft.container.IElementStorageBlocKEntity;
import sirttas.elementalcraft.container.IRuneableBlockEntity;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * IER = Inventory ElementStorage RuneHandler
 */
@Deprecated
public abstract class AbstractIERBlockEntity extends AbstractECContainerBlockEntity implements IRuneableBlockEntity, IElementStorageBlocKEntity {

	protected AbstractIERBlockEntity(Supplier<? extends BlockEntityType<?>> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	@Override
	public void loadAdditional(@Nonnull ValueInput valueInput) {
		super.loadAdditional(valueInput);
		IElementStorage elementStorage = getElementStorage();
		
		if (elementStorage instanceof ValueIOSerializable valueIOSerializable) {
            valueInput.child(ECNames.ELEMENT_STORAGE).ifPresent(valueIOSerializable::deserialize);
		}
        valueInput.readChild(ECNames.RUNE_HANDLER, getRuneHandler());
	}
	
	@Override
	public void saveAdditional(@Nonnull ValueOutput valueOutput) {
		super.saveAdditional(valueOutput);
		IElementStorage elementStorage = getElementStorage();
		
		if (elementStorage instanceof ValueIOSerializable serializable) {
            serializable.serialize(valueOutput.child(ECNames.ELEMENT_STORAGE));
		}
        valueOutput.putChild(ECNames.RUNE_HANDLER, getRuneHandler());
	}
}
