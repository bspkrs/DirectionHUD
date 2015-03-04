package bspkrs.directionhud.fml;

import net.minecraftforge.client.ClientCommandHandler;
import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.directionhud.CommandDirectionHUD;
import bspkrs.directionhud.DirectionHUD;
import bspkrs.util.ModVersionChecker;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        DirectionHUD.initConfig(event.getSuggestedConfigurationFile());
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        FMLCommonHandler.instance().bus().register(new DHGameTicker());
        FMLCommonHandler.instance().bus().register(new DHRenderTicker());

        ClientCommandHandler.instance.registerCommand(new CommandDirectionHUD());

        FMLCommonHandler.instance().bus().register(this);

        if (bspkrsCoreMod.instance.allowUpdateCheck)
        {
            DirectionHUDMod.instance.versionChecker = new ModVersionChecker(Reference.MODID, DirectionHUDMod.metadata.version, DirectionHUDMod.instance.versionURL, DirectionHUDMod.instance.mcfTopic);
            DirectionHUDMod.instance.versionChecker.checkVersionWithLogging();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event)
    {
        if (event.modID.equals(Reference.MODID))
        {
            Reference.config.save();
            DirectionHUD.syncConfig();
        }
    }
}
