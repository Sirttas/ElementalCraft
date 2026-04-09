package sirttas.elementalcraft.block.shrine.upgrade.fortune;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.block.shrine.upgrade.ShrineUpgrade;
import sirttas.elementalcraft.block.shrine.upgrade.HorizontalShrineUpgradeBlock;

public abstract class AbstractFortuneShrineUpgradeBlock extends HorizontalShrineUpgradeBlock {

	protected AbstractFortuneShrineUpgradeBlock(ResourceKey<@NotNull ShrineUpgrade> key, BlockBehaviour.Properties properties) {
		super(key, properties);
	}

	public abstract int getFortuneLevel();
}
