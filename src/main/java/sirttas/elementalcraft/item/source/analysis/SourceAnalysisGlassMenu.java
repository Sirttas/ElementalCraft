package sirttas.elementalcraft.item.source.analysis;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.trait.SourceTraits;
import sirttas.elementalcraft.container.menu.ECMenus;
import sirttas.elementalcraft.container.menu.IMenuOpenListener;

import javax.annotation.Nonnull;
import java.util.Map;

public class SourceAnalysisGlassMenu extends AbstractContainerMenu implements IMenuOpenListener {
	private Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> traits;
	
	public SourceAnalysisGlassMenu(int id, Inventory inventory) {
		this(id, inventory, SourceTraits.createTraitMap());
	}
	
	public SourceAnalysisGlassMenu(int id, Inventory inventory, Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> traits) {
		super(ECMenus.SOURCE_ANALYSIS_GLASS.get(), id);
		this.traits = traits;
		addSlots(inventory);
	}
	
	private void addSlots(Inventory inventory) {
        this.addStandardInventorySlots(inventory, 0, 98);
	}

	public Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> getTraits() {
		return traits;
	}

	public void setTraits(Map<Holder<@NotNull SourceTrait>, ISourceTraitValue> traits) {
		this.traits = traits;
	}
	
	@Override
	public void onOpen(Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			PacketDistributor.sendToPlayer(serverPlayer, new SourceAnalysisGlassPayload(traits));
		}
	}

	@Nonnull
	@Override
	public ItemStack quickMoveStack(@Nonnull Player player, int index) {
		return ItemStack.EMPTY;
	}

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
