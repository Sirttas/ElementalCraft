package sirttas.elementalcraft.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import sirttas.elementalcraft.ElementalCraft;
import sirttas.elementalcraft.config.ECConfig;
import sirttas.elementalcraft.infusion.InfusionHelper;
import sirttas.elementalcraft.item.ECItems;
import sirttas.elementalcraft.nbt.ECNames;

@Mod.EventBusSubscriber(modid = ElementalCraft.MODID)
public class EntityHandler {

	@SubscribeEvent
	public static void onEntityUseItemTick(LivingEntityUseItemEvent.Tick event) {
		if (InfusionHelper.hasAirInfusionFasterDraw(event.getItem()) && event.getDuration() % 3 == 0) {
			event.setDuration(event.getDuration() - 1);
		}
	}
	
	@SubscribeEvent
	public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();

		if (Boolean.TRUE.equals(ECConfig.CONFIG.playersSpawnWithBook.get()) && !event.getEntityLiving().getEntityWorld().isRemote) {
			CompoundNBT tag = player.getPersistentData().getCompound(PlayerEntity.PERSISTED_NBT_TAG);

			if (!tag.getBoolean(ECNames.HAS_BOOK)) {
				ItemStack book = new ItemStack(ECItems.elementopedia);

				book.getOrCreateTag().putString("patchouli:book", "elementalcraft:element_book");
				ItemHandlerHelper.giveItemToPlayer(player, book);
				tag.putBoolean(ECNames.HAS_BOOK, true);
				player.getPersistentData().put(PlayerEntity.PERSISTED_NBT_TAG, tag);
			}
		}
	}
}
