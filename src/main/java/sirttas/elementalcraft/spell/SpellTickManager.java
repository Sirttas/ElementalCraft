package sirttas.elementalcraft.spell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sirttas.elementalcraft.api.ElementalCraftApi;
import sirttas.elementalcraft.network.message.MessageHelper;



@Mod.EventBusSubscriber(modid = ElementalCraftApi.MODID)
public class SpellTickManager {

	public static final SpellTickManager SERVER_INSTANCE = new SpellTickManager();
	public static final SpellTickManager CLIENT_INSTANCE = new SpellTickManager();

	private int tick;
	private final Map<Entity, Map<Spell, Cooldown>> cooldowns;
	private final List<AbstractSpellInstance> spellinstances;

	private SpellTickManager() {
		tick = 0;
		cooldowns = new HashMap<>();
		spellinstances = Lists.newArrayList();
	}

	public static SpellTickManager getInstance(Level world) {
		return world.isClientSide ? CLIENT_INSTANCE : SERVER_INSTANCE;
	}

	private void tick() {
		tick++;
		cooldowns.keySet().removeIf(e -> !e.isAlive());
		cooldowns.forEach((k, v) -> v.values().removeIf(c -> c.expireTicks <= tick));
		if (cooldowns.isEmpty()) {
			tick = 0;
		}
		spellinstances.forEach(i -> {
			i.tick();
			i.decTick();
		});
		spellinstances.removeIf(AbstractSpellInstance::isFinished);
	}

	public void addSpellInstance(AbstractSpellInstance instance) {
		spellinstances.add(instance);
	}

	public void setCooldown(Entity target, Spell spell) {
		Map<Spell, Cooldown> entityCooldowns;
		Cooldown cooldown = new Cooldown(tick, tick + spell.getCooldown());
			
		if (!cooldowns.containsKey(target)) {
			entityCooldowns = new HashMap<>();
			cooldowns.put(target, entityCooldowns);
		} else {
			entityCooldowns = cooldowns.get(target);
		}
		entityCooldowns.put(spell, cooldown);
		if (!target.level.isClientSide && target instanceof ServerPlayer player) {
			MessageHelper.sendToPlayer(player, new SpellTickCooldownMessage(spell));
		}
	}

	public boolean hasCooldown(Entity target, Spell spell) {
		return this.getCooldown(target, spell) > 0.0F;
	}

	public float getCooldown(Entity target, Spell spell) {
		return getCooldown(target, spell, 0);
	}

	public float getCooldown(Entity target, Spell spell, float partialTick) {
		Map<Spell, Cooldown> entityCooldowns = cooldowns.get(target);

		if (spell.isValid() && entityCooldowns != null && entityCooldowns.containsKey(spell)) {
			Cooldown cooldown = entityCooldowns.get(spell);
			float current = cooldown.expireTicks - (tick + partialTick);
			float total = (float) cooldown.expireTicks - cooldown.createTicks;

			return current / total;
		}

		return 0;
	}

	@SubscribeEvent
	public static void serverTick(TickEvent.ServerTickEvent event) {
		if (event.phase == Phase.START) {
			SERVER_INSTANCE.tick();
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.START) {
			CLIENT_INSTANCE.tick();
		}
	}

	private static class Cooldown {
		private final int createTicks;
		private final int expireTicks;

		private Cooldown(int createTicksIn, int expireTicksIn) {
			this.createTicks = createTicksIn;
			this.expireTicks = expireTicksIn;
		}
	}
}
