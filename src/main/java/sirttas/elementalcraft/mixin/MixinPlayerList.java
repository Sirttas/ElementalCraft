package sirttas.elementalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.management.PlayerList;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.block.shrine.upgrade.ShrineUpgradesMessage;
import sirttas.elementalcraft.network.message.MessageHelper;
import sirttas.elementalcraft.spell.properties.SpellPropertiesMessage;

@Mixin(PlayerList.class)
public class MixinPlayerList {

	@Inject(method = "reloadResources", at = @At("TAIL"))
	public void onReloadResources(CallbackInfo ci) {
		MessageHelper.sendToAllPlayers(new ShrineUpgradesMessage(ElementalCraft.SHRINE_UPGRADE_MANAGER));
		MessageHelper.sendToAllPlayers(new SpellPropertiesMessage(ElementalCraft.SPELL_PROPERTIES_MANAGER));
	}

}
