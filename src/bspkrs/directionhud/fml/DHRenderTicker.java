package bspkrs.directionhud.fml;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import bspkrs.directionhud.DirectionHUD;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class DHRenderTicker implements ITickHandler
{
    private EnumSet<TickType> tickTypes = EnumSet.noneOf(TickType.class);
    private Minecraft         mc;
    
    public DHRenderTicker(EnumSet<TickType> tickTypes)
    {
        this.tickTypes = tickTypes;
        mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void tickStart(EnumSet<TickType> tickTypes, Object... tickData)
    {
        tick(tickTypes, true);
    }
    
    @Override
    public void tickEnd(EnumSet<TickType> tickTypes, Object... tickData)
    {
        tick(tickTypes, false);
    }
    
    private void tick(EnumSet<TickType> tickTypes, boolean isStart)
    {
        for (TickType tickType : tickTypes)
        {
            if (!onTick(tickType, isStart))
            {
                this.tickTypes.remove(tickType);
                this.tickTypes.removeAll(tickType.partnerTicks());
            }
        }
    }
    
    public boolean onTick(TickType tick, boolean isStart)
    {
        if (isStart)
        {
            return true;
        }
        
        return DirectionHUD.onTickInGame(mc);
    }
    
    @Override
    public EnumSet<TickType> ticks()
    {
        return tickTypes;
    }
    
    @Override
    public String getLabel()
    {
        return "DHRenderTicker";
    }
}
