package sirttas.elementalcraft.rune;

import net.minecraft.gametest.framework.GameTestAssertException;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;

public class RuneGameTestHelper {

    private RuneGameTestHelper() {}

    public static void assertRuneIs(Rune rune, String name) {
        if (!rune.is(IDataManager.createKey(ElementalCraftApi.RUNE_MANAGER_KEY, ElementalCraft.createRL(name)))) {
            throw new GameTestAssertException("Expected rune " + name + " but got " + rune);
        }
    }

}
