package sirttas.elementalcraft.item.source.analysis;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.source.trait.SourceTrait;
import sirttas.elementalcraft.api.source.trait.value.ISourceTraitValue;
import sirttas.elementalcraft.block.source.trait.SourceTraitHelper;
import sirttas.elementalcraft.network.payload.IMenuPayload;

import javax.annotation.Nonnull;
import java.util.Map;

public record SourceAnalysisGlassPayload(
		Map<ResourceKey<SourceTrait>, ISourceTraitValue> traits
) implements IMenuPayload<SourceAnalysisGlassMenu> {

	public static final ResourceLocation ID = ElementalCraftApi.createRL("source_analysis_glass");

	public SourceAnalysisGlassPayload(FriendlyByteBuf buf) {
		this(SourceTraitHelper.loadTraits(buf.readNbt()));
	}


	@Override
	public Class<? extends SourceAnalysisGlassMenu> getMenuType() {
		return SourceAnalysisGlassMenu.class;
	}

	@Override
	public void handleOnMenu(PlayPayloadContext ctx, SourceAnalysisGlassMenu menu) {
		menu.setTraits(traits);

	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeNbt(SourceTraitHelper.saveTraits(traits));

	}

	@Override
	@Nonnull
	public ResourceLocation id() {
		return ID;
	}
}
