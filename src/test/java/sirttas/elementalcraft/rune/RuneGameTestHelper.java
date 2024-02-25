package sirttas.elementalcraft.rune;

import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import sirttas.dpanvil.api.data.IDataManager;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.api.rune.Rune;
import sirttas.elementalcraft.item.rune.RuneItem;

public class RuneGameTestHelper {

    private RuneGameTestHelper() {}

    public static void assertRuneIs(Rune rune, ResourceKey<Rune> name) {
        assertRuneIs(rune, name.location());
    }

    public static void assertRuneIs(Rune rune, ResourceLocation name) {
        if (!rune.is(IDataManager.createKey(ElementalCraftApi.RUNE_MANAGER_KEY, name))) {
            throw new GameTestAssertException("Expected rune " + name + " but got " + rune);
        }
    }

    public static void assertRuneIs(ItemStack stack, ResourceKey<Rune> name) {
        assertRuneIs(stack, name.location());
    }

    public static void assertRuneIs(ItemStack stack, ResourceLocation name) {
        var rune = RuneItem.getRune(stack);

        if (rune == null) {
            throw new GameTestAssertException("Expected rune " + name + " but got " + stack);
        }
        assertRuneIs(rune, name);
    }

}
