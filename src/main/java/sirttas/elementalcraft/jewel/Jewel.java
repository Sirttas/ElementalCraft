package sirttas.elementalcraft.jewel;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.IElementTypeProvider;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;

import java.util.List;

public class Jewel extends ForgeRegistryEntry<Jewel> implements IElementTypeProvider {

	@SuppressWarnings("removal")
	public static final IForgeRegistry<Jewel> REGISTRY = RegistryManager.ACTIVE.getRegistry(Jewel.class);

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

	public boolean isActive(Entity entity) {
		return (entity instanceof Player player && player.isCreative()) || CapabilityElementStorage.get(entity)
				.map(s -> s.extractElement(consumption, elementType, true) == consumption)
				.orElse(entity instanceof Mob);
	}
	
	public void consume(Entity entity) {
		CapabilityElementStorage.get(entity).ifPresent(s -> {
			if (s.extractElement(consumption, elementType, true) == consumption) {
				s.extractElement(consumption, elementType, false);
			}
		});
	}

	public void appendHoverText(List<Component> tooltip) {
		tooltip.add(new TextComponent(""));
		tooltip.add(new TranslatableComponent("tooltip.elementalcraft.consumes", elementType.getDisplayName()).withStyle(ChatFormatting.YELLOW));
	}
}
