package bspkrs.directionhud.fml;

import org.apache.logging.log4j.Level;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {}

    public void init(FMLInitializationEvent event)
    {
        FMLLog.log(Reference.MODID, Level.ERROR, "*********************************************************************************");
        FMLLog.log(Reference.MODID, Level.ERROR, "* DirectionHUD is a CLIENT-ONLY mod. Installing it on your server is pointless. *");
        FMLLog.log(Reference.MODID, Level.ERROR, "*********************************************************************************");
    }
}
