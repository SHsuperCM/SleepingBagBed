package SHCM.SHsuperCM.forge.sleepingbagbed;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = SleepingBagBed.MODID)
@Mod.EventBusSubscriber(modid = SleepingBagBed.MODID)
public class ModConfig {
    @Config.Name("Items that should be considered as sleeping bags")
    @Config.Comment("List of modid:item entries that count as sleeping bags and allow you to sleep with")
    public static String[] bag_items = {"minecraft:bed"};

    @SubscribeEvent
    public static void configChange(ConfigChangedEvent event) {
        if(event.getModID().equals(SleepingBagBed.MODID))
            ConfigManager.sync(SleepingBagBed.MODID, Config.Type.INSTANCE);
    }
}
