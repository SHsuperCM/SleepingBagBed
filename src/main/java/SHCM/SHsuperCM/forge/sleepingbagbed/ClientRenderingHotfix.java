package SHCM.SHsuperCM.forge.sleepingbagbed;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientRenderingHotfix {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRender(RenderPlayerEvent.Pre event) {
        if (event.getEntityPlayer().isPlayerSleeping() && SleepingBagBed.isBed(event.getEntityPlayer().getHeldItemMainhand())) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.8F, -0.6F, 0F);
            renderingPlayer = true;
        }
    }

    private static boolean renderingPlayer = false;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRender(RenderPlayerEvent.Post event) {
        if (renderingPlayer) {
            GlStateManager.popMatrix();
            renderingPlayer = false;
        }
    }
}
