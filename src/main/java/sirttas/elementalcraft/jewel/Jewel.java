package sirttas.elementalcraft.jewel;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.element.storage.IElementStorage;
import sirttas.elementalcraft.api.name.ECNames;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class Jewel extends ForgeRegistryEntry<Jewel> implements IElementTypeProvider {

	@SuppressWarnings("removal")
	public static final IForgeRegistry<Jewel> REGISTRY = RegistryManager.ACTIVE.getRegistry(ElementalCraft.createRL(ECNames.JEWEL));

	private final ElementType elementType;
	private final int consumption;
	protected boolean ticking = true;
	
	protected Jewel(ElementType elementType, int consumption) {
		this.elementType = elementType;
		this.consumption = consumption;
	}

	@Override
	public ElementType getElementType() {
		return elementType;
	}

	public int getConsumption() {
		return consumption;
	}

	public boolean isTicking() {
		return ticking;
	}

	public ResourceLocation getModelName() {
		var id = this.getRegistryName();

		return new ResourceLocation(id.getNamespace(), "elementalcraft_jewels/" + id.getPath());
	}

	public Component getDisplayName() {
		var id = this.getRegistryName();

		return new TranslatableComponent("elementalcraft_jewel." + id.getNamespace() + '.' + id.getPath());
	}

	public boolean isActive(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
		return (entity instanceof Player player && player.isCreative()) || (elementStorage != null && elementStorage.extractElement(consumption, elementType, true) == consumption);
	}

	public final void consume(@Nonnull Entity entity) {
		this.consume(entity, CapabilityElementStorage.get(entity).orElse(null));
	}

	public void consume(@Nonnull Entity entity, @Nullable IElementStorage elementStorage) {
		if (elementStorage != null) {
			elementStorage.extractElement(consumption, elementType, false);
		}
	}

	public void appendHoverText(List<Component> tooltip) {
		tooltip.add(new TextComponent(""));
		tooltip.add(new TranslatableComponent("tooltip.elementalcraft.consumes", elementType.getDisplayName()).withStyle(ChatFormatting.YELLOW));
	}
}
