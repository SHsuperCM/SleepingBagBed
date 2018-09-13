package SHCM.SHsuperCM.forge.sleepingbagbed;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

@Mod.EventBusSubscriber
@Mod(modid = SleepingBagBed.MODID)
public class SleepingBagBed {
    public static final String MODID = "sleepingbagbed";

    @SideOnly(Side.CLIENT)
    private static KeyBinding keybind = new KeyBinding("Sleep(Sleeping Bag Bed)", 0, "key.categories.gameplay");

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public static void registerKeybinding(FMLInitializationEvent event) { ClientRegistry.registerKeyBinding(keybind); }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void interact(PlayerInteractEvent.RightClickBlock event) {
        if(!event.getEntityPlayer().isSneaking() && event.getEntityPlayer().onGround && isBed(event.getEntityPlayer().getHeldItemMainhand())) {
            event.setCanceled(true);
            if(!event.getWorld().isRemote) {
                EntityPlayer player = event.getEntityPlayer();

                if (player.world.isDaytime())
                    player.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]), true);
                else if (player.world.provider.isSurfaceWorld()) {
                    if (player.trySleep(player.getPosition()) != EntityPlayer.SleepResult.OK) {
                        event.setCancellationResult(EnumActionResult.FAIL);
                    } else {
                        if (ModConfig.set_spawnpoint)
                            player.setSpawnPoint(player.getPosition(),true);

                        event.setCancellationResult(EnumActionResult.SUCCESS);
                    }
                    return;
                }

            }
            event.setCancellationResult(EnumActionResult.PASS);
        }
    }

    @SubscribeEvent
    public static void handleSleepLocationCheck(SleepingLocationCheckEvent event) {
        if(isBed(event.getEntityPlayer().getHeldItemMainhand()))
            event.setResult(Event.Result.ALLOW);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onKeybind(TickEvent.ClientTickEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if(keybind.isPressed() && !player.isSneaking() && player.onGround)
            player.sendMessage(new TextComponentString("Key"));
    }

    private static boolean isBed(ItemStack item) {
        return ModConfig.bag_items.length != 0 && !item.isEmpty() && Arrays.asList(ModConfig.bag_items).contains(item.getItem().getRegistryName().toString());
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRender(RenderPlayerEvent.Pre event) {
        if (event.getEntityPlayer().isPlayerSleeping() && isBed(event.getEntityPlayer().getHeldItemMainhand())) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.8F, -0.6F, 0F);
            renderingPlayer = true;
        }
    }

    @SideOnly(Side.CLIENT)
    private static boolean renderingPlayer = false;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRender(RenderPlayerEvent.Post event) {
        if (renderingPlayer) {
            GlStateManager.popMatrix();
            renderingPlayer = false;
        }
    }


    @SubscribeEvent
    public static void configChange(ConfigChangedEvent event) { if(event.getModID().equals(MODID)) ConfigManager.sync(MODID, Config.Type.INSTANCE); }
    @Config(modid = MODID)
    public static class ModConfig {
        @Config.Name("Bag Items")
        @Config.Comment({"List of modid:item entries that count as sleeping bags and allow you to sleep with","(if Model is set to 2, Anything other than beds would be defaulted to red)"})
        public static String[] bag_items = {"minecraft:bed"};

        @Config.Name("Set Spawnpoint")
        @Config.Comment("Should sleeping in a Bag set your spawnpoint")
        public static boolean set_spawnpoint = false;
    }
}
