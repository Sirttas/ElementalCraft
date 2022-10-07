package sirttas.elementalcraft.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import sirttas.elementalcraft.api.ElementalCraftApi;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ElementalCraftApi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ECKeyMappings {

    public static final List<KeyMapping> CHANGE_TO_SPELL = List.of(
            spellChangeKey(1),
            spellChangeKey(2),
            spellChangeKey(3),
            spellChangeKey(4),
            spellChangeKey(5),
            spellChangeKey(6),
            spellChangeKey(7),
            spellChangeKey(8),
            spellChangeKey(9),
            spellChangeKey(0)
    );

    private ECKeyMappings() {}

    private static KeyMapping key(String name, int keyCode) {
        return key(name, KeyConflictContext.UNIVERSAL, KeyModifier.NONE, keyCode);
    }

    private static KeyMapping spellChangeKey(int number) {
        return key("change_spell." + number, ECKeyConflictContext.CHANGE_SPELL, KeyModifier.SHIFT, GLFW.GLFW_KEY_0 + number);
    }

    private static KeyMapping key(String name, IKeyConflictContext keyConflictContext, KeyModifier keyModifier, int keyCode) {
        return new KeyMapping("key.elementalcraft." + name, keyConflictContext, keyModifier, InputConstants.Type.KEYSYM.getOrCreate(keyCode), "key.categories.elementalcraft");
    }

    @SubscribeEvent
    public static void registerKeyBinds(RegisterKeyMappingsEvent event) {
        for (KeyMapping key : CHANGE_TO_SPELL) {
            event.register(key);
        }
    }
}
