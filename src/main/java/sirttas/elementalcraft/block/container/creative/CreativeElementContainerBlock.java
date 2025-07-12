package sirttas.elementalcraft.block.container.creative;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.container.ElementContainerBlock;
import sirttas.elementalcraft.block.entity.properties.IConfigurableBlockEntityProperties;

import javax.annotation.Nonnull;

public class CreativeElementContainerBlock extends ElementContainerBlock {

	public static final String NAME = "creative_container";

	public static final ResourceKey<IConfigurableBlockEntityProperties> PROPERTIES_KEY = IConfigurableBlockEntityProperties.createKey(NAME);
	private static final Holder<IConfigurableBlockEntityProperties> PROPERTIES = ElementalCraft.CONFIGURABLE_BLOCK_ENTITY_PROPERTIES_MANAGER.getOrCreateHolder(PROPERTIES_KEY);

	public static final MapCodec<CreativeElementContainerBlock> CODEC = simpleCodec(CreativeElementContainerBlock::new);

	public CreativeElementContainerBlock(BlockBehaviour.Properties properties) {
		super(properties, PROPERTIES);
	}

	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new CreativeElementContainerBlockEntity(pos, state);
	}

	@Override
	protected @NotNull MapCodec<CreativeElementContainerBlock> codec() {
		return CODEC;
	}
}
