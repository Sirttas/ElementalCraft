package sirttas.elementalcraft.jewel;

import com.mojang.datafixers.Products.P2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder.Instance;
import com.mojang.serialization.codecs.RecordCodecBuilder.Mu;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import sirttas.dpanvil.api.codec.CodecHelper;
import sirttas.elementalcraft.api.element.ElementType;
import sirttas.elementalcraft.api.element.storage.CapabilityElementStorage;
import sirttas.elementalcraft.api.name.ECNames;

public abstract class AbstractJewel {
	
	public static final Codec<AbstractJewel> CODEC = CodecHelper.getRegistryCodec(() -> JewelType.REGISTRY).dispatch(AbstractJewel::getType, JewelType::getCodec);
	
	private ResourceLocation id;
	private final ElementType elementType;
	private final int consumption;
	
	protected AbstractJewel(ElementType elementType, int consumption) {
		this.elementType = elementType;
		this.consumption = consumption;
	}
	
	protected static <U extends AbstractJewel> P2<Mu<U>, ElementType, Integer> codec(Instance<U> builder) {
		return builder.group(
				ElementType.forGetter(AbstractJewel::getElementType),
				Codec.INT.fieldOf(ECNames.CONSUMPTION).forGetter(AbstractJewel::getConsumption)
		);
	}
	
	public ResourceLocation getId() {
		return id;
	}

	public final void setId(ResourceLocation id) {
		this.id = id;
	}

	abstract JewelType<? extends AbstractJewel> getType();
	
	public ElementType getElementType() {
		return elementType;
	}

	public int getConsumption() {
		return consumption;
	}
	
	public boolean isActive(Entity entity) {
		return CapabilityElementStorage.get(entity)
				.map(s -> s.extractElement(consumption, elementType, true) == consumption)
				.orElse(false);
	}
	
	public void consume(Entity entity) {
		CapabilityElementStorage.get(entity).ifPresent(s -> {
			if (s.extractElement(consumption, elementType, true) == consumption) {
				s.extractElement(consumption, elementType, false);
			}
		});
	}
	
}
