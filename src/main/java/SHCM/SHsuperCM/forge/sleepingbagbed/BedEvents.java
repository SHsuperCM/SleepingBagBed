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

@Mod.EventBusSubscriber
public class BedEvents {

    @SubscribeEvent
    public static void interact(PlayerInteractEvent event) {
        if(!event.getWorld().isRemote && !event.getEntityPlayer().isSneaking() && event.getItemStack().getItem() == Items.BED) {
            event.setCanceled(true);

            EntityPlayer player = event.getEntityPlayer();


            if(!player.world.isDaytime() && player.world.provider.isSurfaceWorld()) {
                //player.world.setBlockState(player.getPosition(), Blocks.BED.getDefaultState());
                player.trySleep(player.getPosition());
                //TODO change NBT of bed item to reflect person being asleep, Make sure to get rid of the tag
                event.setCancellationResult(EnumActionResult.SUCCESS);
            }

            event.setCancellationResult(EnumActionResult.FAIL);
        }
    }

    @SubscribeEvent
    public static void handleSleepLocationCheck(SleepingLocationCheckEvent event) {
        if(event.getEntityPlayer().getHeldItemMainhand())
        event.setResult(Event.Result.ALLOW);
    }

    private static boolean isBed(ItemStack item) {
        return 
    }
}
