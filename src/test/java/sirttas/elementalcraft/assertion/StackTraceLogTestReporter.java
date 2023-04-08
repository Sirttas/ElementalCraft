package sirttas.elementalcraft.assertion;

import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.gametest.framework.LogTestReporter;
import sirttas.elementalcraft.api.ElementalCraftApi;

import javax.annotation.Nonnull;

public class StackTraceLogTestReporter extends LogTestReporter {

    @Override
    public void onTestFailed(@Nonnull GameTestInfo gameTestInfo) {
        super.onTestFailed(gameTestInfo);
        if (gameTestInfo.isRequired()) {
            ElementalCraftApi.LOGGER.error("Test failed: ", gameTestInfo.getError());
        }
    }
}
