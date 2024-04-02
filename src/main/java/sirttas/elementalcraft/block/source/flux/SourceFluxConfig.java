package sirttas.elementalcraft.block.source.flux;

public record SourceFluxConfig(
    float capacity,
    float recovery,
    float consumption,
    float transfer
) { }
