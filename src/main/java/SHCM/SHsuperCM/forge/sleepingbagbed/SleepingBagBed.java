package SHCM.SHsuperCM.forge.sleepingbagbed;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

@Mod.EventBusSubscriber
@Mod(modid = SleepingBagBed.MODID)
public class SleepingBagBed {
    public static final String MODID = "sleepingbagbed";

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


    @SubscribeEvent
    public static void configChange(ConfigChangedEvent event) {
        if(event.getModID().equals(MODID))
            ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }
    @Config(modid = MODID)
    public static class ModConfig {
        @Config.Name("Bag Items")
        @Config.Comment({"List of modid:item entries that count as sleeping bags and allow you to sleep with","(if Model is set to 2, Anything other than beds would be defaulted to red)"})
        public static String[] bag_items = {"minecraft:bed"};
    }
}
