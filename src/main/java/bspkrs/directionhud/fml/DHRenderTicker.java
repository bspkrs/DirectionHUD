package bspkrs.directionhud.fml;

import net.minecraft.client.Minecraft;
import bspkrs.directionhud.DirectionHUD;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DHRenderTicker
{
    private Minecraft      mc;
    private static boolean isRegistered = false;

    public DHRenderTicker()
    {
        mc = Minecraft.getMinecraft();
        isRegistered = true;
    }

    @SubscribeEvent
    public void onTick(RenderTickEvent event)
    {
        if (event.phase.equals(Phase.START))
            return;

        if (!DirectionHUD.onTickInGame(mc))
        {
            FMLCommonHandler.instance().bus().unregister(this);
            isRegistered = false;
        }
    }

    public static boolean isRegistered()
    {
        return isRegistered;
    }
}
