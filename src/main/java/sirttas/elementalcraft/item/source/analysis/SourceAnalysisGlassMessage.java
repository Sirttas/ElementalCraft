package sirttas.elementalcraft.item.source.analysis;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.network.NetworkEvent;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.trait.SourceTraitHelper;
import sirttas.elementalcraft.network.message.MessageHelper;

import java.util.Map;
import java.util.function.Supplier;

public final class SourceAnalysisGlassMessage {

	private final Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits;

	public SourceAnalysisGlassMessage(Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits) {
		this.traits = traits;
	}

	// message handling

	public static SourceAnalysisGlassMessage decode(FriendlyByteBuf buf) {
		var traits = SourceTraitHelper.loadTraits(buf.readNbt());
		
		return new SourceAnalysisGlassMessage(traits);
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeNbt(SourceTraitHelper.saveTraits(traits));
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		MessageHelper.handleMenuMessage(ctx, SourceAnalysisGlassMenu.class, m -> m.setTraits(traits));
	}
}
