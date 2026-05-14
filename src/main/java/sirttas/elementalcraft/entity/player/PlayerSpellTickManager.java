package sirttas.elementalcraft.entity.player;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.tick.ISpellTickManager;
import sirttas.elementalcraft.spell.tick.SpellInstance;
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
    public List<SpellInstance> getSpellInstances() {
        return delegate.getSpellInstances();
    }

    @Override
    public void addSpellInstance(SpellInstance instance) {
        delegate.addSpellInstance(instance);
    }

    @Override
    public void startCooldown(Holder<Spell> spell) {
        delegate.startCooldown(spell);
        PacketDistributor.sendToPlayer(player, new SpellTickCooldownPayload(spell));
    }

    @Override
    public float getCooldown(Holder<Spell> spell, float partialTick) {
        return delegate.getCooldown(spell, partialTick);
    }

    @Override
    public void tick() {
        delegate.tick();
    }
}
