package sirttas.elementalcraft.input;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import sirttas.elementalcraft.entity.EntityHelper;
import sirttas.elementalcraft.tag.ECTags;

public class ECKeyConflictContext {

    public static final IKeyConflictContext CHANGE_SPELL = new IKeyConflictContext() {
        @Override
        public boolean isActive() {
            var player = Minecraft.getInstance().player;

            return player != null && EntityHelper.handStream(player).anyMatch(i -> i.is(ECTags.Items.SPELL_CAST_TOOLS));
        }

        @Override
        public boolean conflicts(IKeyConflictContext other) {
            return other.conflicts(KeyConflictContext.IN_GAME);
        }
    };

    private ECKeyConflictContext() {}
}
