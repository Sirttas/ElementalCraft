package sirttas.elementalcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.network.PacketDistributor;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.network.message.MessageHandler;
import sirttas.elementalcraft.network.message.ShrineUpgradesMessage;
import sirttas.elementalcraft.network.message.SpellPropertiesMessage;

@Mixin(PlayerList.class)
public class MixinPlayerList {

	@Inject(method = "reloadResources", at = @At("TAIL"))
	public void onReloadResources(CallbackInfo ci) {
		MessageHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new ShrineUpgradesMessage(ElementalCraft.SHRINE_UPGRADE_MANAGER));
		MessageHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new SpellPropertiesMessage(ElementalCraft.SPELL_PROPERTIES_MANAGER));
	}

}
