package sirttas.elementalcraft.entity.player;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import sirttas.elementalcraft.network.message.MessageHelper;
import sirttas.elementalcraft.spell.Spell;
import sirttas.elementalcraft.spell.tick.ISpellTickManager;
import sirttas.elementalcraft.spell.tick.SpellTickCooldownMessage;
import sirttas.elementalcraft.spell.tick.SpellTickManager;

import javax.annotation.Nullable;

public class PlayerSpellTickManager extends SpellTickManager {

    private final ServerPlayer player;

    public PlayerSpellTickManager(ServerPlayer player) {
        this.player = player;
    }

    @Nullable
    public static ICapabilityProvider createProvider(Player player) {
        return new CapabilityProvider<>(player instanceof ServerPlayer serverPlayer ? new PlayerSpellTickManager(serverPlayer) : new SpellTickManager());
    }

    @Override
    public void startCooldown(Spell spell) {
        super.startCooldown(spell);
        MessageHelper.sendToPlayer(player, new SpellTickCooldownMessage(spell));
    }

    private record CapabilityProvider<T extends SpellTickManager>(T spellTickManager) implements ICapabilitySerializable<CompoundTag> {

        @Override
        public <U> @NotNull LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
            return ISpellTickManager.CAPABILITY.orEmpty(cap, LazyOptional.of(() -> spellTickManager));
        }

        @Override
        public CompoundTag serializeNBT() {
            return spellTickManager.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            spellTickManager.deserializeNBT(nbt);
        }
    }
}
