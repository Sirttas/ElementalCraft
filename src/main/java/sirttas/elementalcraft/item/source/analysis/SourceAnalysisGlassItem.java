package sirttas.elementalcraft.item.source.analysis;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.entity.BlockEntityHelper;
import sirttas.elementalcraft.block.source.SourceBlockEntity;
import sirttas.elementalcraft.component.ECDataComponents;
import sirttas.elementalcraft.property.ECProperties;
import sirttas.elementalcraft.tag.ECTags;

import javax.annotation.Nonnull;
import java.util.Map;

public class SourceAnalysisGlassItem extends Item {

	public static final String NAME = "source_analysis_glass";

	
	public SourceAnalysisGlassItem() {
		super(ECProperties.Items.ITEM_UNSTACKABLE);
	}

	@Nonnull
    @Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		
		return BlockEntityHelper.getBlockEntityAs(level, pos, SourceBlockEntity.class)
				.map(source -> {
					source.setAnalyzed();
					return open(level, player, source.getTraitHolder().getTraits());
				})
				.orElse(InteractionResult.PASS);
	}

	@Override
	public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
		var hasBeenUsed = false;

		for (var stack : player.getInventory()) {
			if (stack.is(ECTags.Items.FULL_RECEPTACLES) && Boolean.FALSE.equals(stack.get(ECDataComponents.SOURCE_ANALYZED))) {
				stack.set(ECDataComponents.SOURCE_ANALYZED, true);
				hasBeenUsed = true;
			}
		}
		if (hasBeenUsed) {
			return InteractionResult.SUCCESS;
		}
		return super.use(level, player, usedHand);
	}

	public InteractionResult open(Level level, Player player, Map<Holder<SourceTrait>, ISourceTraitValue> traitMap) {
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}
		player.openMenu(new Menu(traitMap));
		return InteractionResult.CONSUME;
	}

	private class Menu implements MenuProvider {

		private final Map<Holder<SourceTrait>, ISourceTraitValue> traits;
		
		private Menu(Map<Holder<SourceTrait>, ISourceTraitValue> traits) {
			this.traits = traits;
		}
		
		@Override
		public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inventory, @Nonnull Player player) {
			return new SourceAnalysisGlassMenu(id, inventory, traits);
		}

		@Nonnull
        @Override
		public Component getDisplayName() {
			return Component.translatable(SourceAnalysisGlassItem.this.descriptionId);
		}
	}
}
