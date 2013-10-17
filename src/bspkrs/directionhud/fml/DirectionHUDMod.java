package bspkrs.directionhud.fml;

import java.util.EnumSet;

import bspkrs.bspkrscore.fml.bspkrsCoreMod;
import bspkrs.directionhud.DirectionHUD;
import bspkrs.util.Const;
import bspkrs.util.ModVersionChecker;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "DirectionHUD", name = "DirectionHUD", version = DirectionHUD.VERSION_NUMBER, dependencies = "required-after:bspkrsCore", useMetadata = true)
public class DirectionHUDMod
{
    protected ModVersionChecker   versionChecker;
    private final String          versionURL = Const.VERSION_URL + "/Minecraft/" + Const.MCVERSION + "/directionHUD.version";
    private final String          mcfTopic   = "http://www.minecraftforum.net/topic/1114612-";
    
    @Metadata(value = "DirectionHUD")
    public static ModMetadata     metadata;
    
    @Instance(value = "DirectionHUD")
    public static DirectionHUDMod instance;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        metadata = event.getModMetadata();
        DirectionHUD.loadConfig(event.getSuggestedConfigurationFile());
        
        if (bspkrsCoreMod.instance.allowUpdateCheck)
        {
            versionChecker = new ModVersionChecker(metadata.name, metadata.version, versionURL, mcfTopic);
            versionChecker.checkVersionWithLogging();
        }
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        TickRegistry.registerTickHandler(new DHGameTicker(EnumSet.of(TickType.CLIENT)), Side.CLIENT);
        TickRegistry.registerTickHandler(new DHRenderTicker(EnumSet.of(TickType.RENDER)), Side.CLIENT);
    }
}
