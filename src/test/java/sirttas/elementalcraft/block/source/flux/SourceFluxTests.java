package sirttas.elementalcraft.block.source.flux;

import net.minecraft.world.level.ChunkPos;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTimeout;
import static sirttas.elementalcraft.assertion.Assertions.assertThat;

class SourceFluxTests {

    @Test
    void should_stayHigh_with1Source() {
        runTest(1, f -> assertThat(f.getRatio()).isGreaterThan(0.75F));
    }

    @Test
    void should_getLow_with2Sources() {
        runTest(2, f -> assertThat(f.getRatio()).isLessThan(0.75F));
    }

    @Test
    void should_reallyLow_with4Sources() {
        runTest(4, f -> assertThat(f.getRatio()).isEqualTo(0.1F));
    }

    @Test
    void shouldNot_takeToMuchTime() {
        var list = createTestMap(new SourceFluxConfig(1200, 1, 1, 1), 32);

        for (int i = 0; i < 1000; i++) {
            assertTimeout(Duration.ofMillis(20), () -> SourceFluxHandler.handleSourceFluxMap(list));
        }

    }

    void runTest(int sourceCount, Consumer<SourceFlux> consumer) {
        var map = createTestMap(new SourceFluxConfig(1200, 1, 1, 1), 2);

        for (int i = 0; i < 1000; i++) {
            map.forEach((l, f) -> {
                if (f.getX() == 0 && f.getY() == 0) {
                    for (int j = 0; j < sourceCount; j++) {
                        f.consume();
                    }
                }
            });
            SourceFluxHandler.handleSourceFluxMap(map);
        }
        map.forEach((l, f) -> {
            if (f.getX() == 0 && f.getY() == 0) {
                consumer.accept(f);
            }
        });
    }

    private static Map<Long, SourceFlux> createTestMap(SourceFluxConfig config, int radius) {
        return IntStream.rangeClosed(-radius, radius)
                .mapToObj(x -> IntStream.rangeClosed(-radius, radius).mapToObj(y -> new SourceFlux(config, x, y)))
                .flatMap(s -> s)
                .collect(Collectors.toMap(f -> ChunkPos.asLong(f.getX(), f.getY()), f -> f));
    }



}
