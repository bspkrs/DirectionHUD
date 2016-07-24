package bspkrs.directionhud.fml;

import bspkrs.directionhud.DirectionHUD;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
            MinecraftForge.EVENT_BUS.unregister(this);
            isRegistered = false;
        }
    }

    public static boolean isRegistered()
    {
        return isRegistered;
    }
}
