package sirttas.elementalcraft.entity.player;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.network.payload.PayloadHelper;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.tick.AbstractSpellInstance;
import sirttas.elementalcraft.spell.tick.ISpellTickManager;
import sirttas.elementalcraft.spell.tick.SpellTickCooldownPayload;

import java.util.List;

public class PlayerSpellTickManager implements ISpellTickManager {

    private final ServerPlayer player;
    private final ISpellTickManager delegate;

    public PlayerSpellTickManager(ServerPlayer player, ISpellTickManager delegate) {
        this.player = player;
        this.delegate = delegate;
    }

    @NotNull
    @Override
    public List<AbstractSpellInstance> getSpellInstances() {
        return delegate.getSpellInstances();
    }

    @Override
    public void addSpellInstance(AbstractSpellInstance instance) {
        delegate.addSpellInstance(instance);
    }

    @Override
    public void startCooldown(Spell spell) {
        delegate.startCooldown(spell);
        PayloadHelper.sendToPlayer(player, new SpellTickCooldownPayload(spell));
    }

    @Override
    public float getCooldown(Spell spell, float partialTick) {
        return delegate.getCooldown(spell, partialTick);
    }

    @Override
    public void tick() {
        delegate.tick();
    }
}
