package sirttas.elementalcraft.jewel;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.api.capability.ElementalCraftCapabilities;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.IElementStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class Jewel implements IElementTypeProvider, ItemLike {

	private final ElementType elementType;
	private final int consumption;
	private final boolean ticking;

	private String descriptionId;
	private Identifier key;
	private Item item;

	protected Jewel(ElementType elementType, int consumption, boolean ticking) { // TODO create propery
		this.elementType = elementType;
		this.consumption = consumption;
		this.ticking = ticking;
	}

	@Override
	public @NotNull ElementType getElementType() {
		return elementType;
	}

	public int getConsumption() {
		return consumption;
	}

	public boolean isTicking() {
		return ticking;
	}

	public Identifier getKey() {
		if (key == null) {
			key = Jewels.REGISTRY.getKey(this);
		}
		return key;
	}

	@Nonnull
	public String getDescriptionId() {
		if (descriptionId == null) {
			var id = getKey();

			descriptionId = createDescriptionId(id);
		}
		return descriptionId;
	}

	public static String createDescriptionId(Identifier id) {
		return "elementalcraft.jewel." + id.getNamespace() + '.' + id.getPath();
	}

	public Component getDisplayName() {
		return Component.translatable(getDescriptionId());
	}

	public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
		return (entity instanceof Player player && player.getAbilities().instabuild)
				|| (elementStorage != null && elementStorage.extractElement(consumption, elementType, true) == consumption);
	}

	public final void consume(@Nonnull Entity entity) {
		if (entity instanceof Player player && player.getAbilities().instabuild) {
			return;
		}
		this.consume(entity, entity.getCapability(ElementalCraftCapabilities.ElementStorages.ENTITY, null));
	}

	public void consume(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
		if (elementStorage != null) {
			elementStorage.extractElement(consumption, elementType, false);
		}
	}

	public void appendHoverText(@NotNull Consumer<Component> builder) {
        builder.accept(Component.empty());
        builder.accept(Component.translatable("tooltip.elementalcraft.consumes", elementType.getDisplayName()).withStyle(ChatFormatting.YELLOW));
	}

	@Override
	public @NotNull Item asItem() {
		if (item == null) {
			item = Jewels.getJewelItem(this);
		}
		return item;
	}
}
