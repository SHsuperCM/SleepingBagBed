package SHCM.SHsuperCM.forge.sleepingbagbed;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

@Mod.EventBusSubscriber
public class BedEvents {

    @SubscribeEvent
    public static void interact(PlayerInteractEvent event) {
        if(!event.getWorld().isRemote && !event.getEntityPlayer().isSneaking() && isBed(event.getItemStack())) {
            event.setCanceled(true);

            EntityPlayer player = event.getEntityPlayer();


            if(!player.world.isDaytime() && player.world.provider.isSurfaceWorld()) {
                player.trySleep(player.getPosition());
                event.setCancellationResult(EnumActionResult.SUCCESS);
            }

            event.setCancellationResult(EnumActionResult.FAIL);
        }
    }

    @SubscribeEvent
    public static void handleSleepLocationCheck(SleepingLocationCheckEvent event) {
        if(isBed(event.getEntityPlayer().getHeldItemMainhand()))
            event.setResult(Event.Result.ALLOW);
    }

    private static boolean isBed(ItemStack item) {
        return ModConfig.bag_items.length != 0 && !item.isEmpty() && Arrays.asList(ModConfig.bag_items).contains(item.getItem().getRegistryName().toString());
    }
}
