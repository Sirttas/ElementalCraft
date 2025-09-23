package sirttas.elementalcraft.block.synthesizer.solar;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.entity.ECBlockEntityTypes;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;
import sirttas.elementalcraft.block.synthesizer.AbstractContainerSynthesizerBlockEntity;
import sirttas.elementalcraft.container.SingleStackContainer;
import sirttas.elementalcraft.item.ECItems;

public class SolarSynthesizerBlockEntity extends AbstractContainerSynthesizerBlockEntity {

	private final SingleStackContainer inventory;

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(SolarSynthesizerBlock.NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	public SolarSynthesizerBlockEntity(BlockPos pos, BlockState state) {
		super(ECBlockEntityTypes.SOLAR_SYNTHESIZER, PROPERTIES, pos, state);
		inventory = new SolarSynthesizerContainer(this);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, SolarSynthesizerBlockEntity solarSynthesizer) {
		solarSynthesizer.handleSynthesis();
	}

	@Override
	protected int getElementAmountForStack(ItemStack stack) {
		if (isReceivingSkyLight()) {
			return Math.round(synthesisMultiplier);
		}
		return 0;
	}

	@Override
	protected int synthesizeElement() {
		var amount = super.synthesizeElement();

		if (amount > 0 && this.getInventory().isEmpty()) {
			BlockEntityHelper.renderItemBreaking(this.getLevel(), this.getBlockPos(), new ItemStack(ECItems.FIRE_LENS));
		}
		return amount;
	}

	protected boolean isReceivingSkyLight() {
		return level != null && level.dimensionType().hasSkyLight() && level.canSeeSky(this.worldPosition) && level.isDay();
	}

	@NotNull
	@Override
	public Container getInventory() {
		return inventory;
	}
}
