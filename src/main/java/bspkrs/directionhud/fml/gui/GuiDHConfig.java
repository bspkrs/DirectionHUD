package bspkrs.directionhud.fml.gui;

import java.lang.reflect.Method;

import net.minecraft.client.gui.GuiScreen;
import bspkrs.directionhud.ConfigElement;
import bspkrs.directionhud.DirectionHUD;
import bspkrs.util.config.ConfigCategory;
import bspkrs.util.config.ConfigProperty;
import bspkrs.util.config.Configuration;
import bspkrs.util.config.gui.GuiConfig;
import bspkrs.util.config.gui.IConfigProperty;

public class GuiDHConfig extends GuiConfig
{
    public GuiDHConfig(GuiScreen parent) throws NoSuchMethodException, SecurityException
    {
        super(parent, getProps(), Configuration.class.getDeclaredMethod("save"), DirectionHUD.getConfig(),
                DirectionHUD.class.getDeclaredMethod("syncConfig"), null);
    }
    
    public GuiDHConfig(GuiScreen par1GuiScreen, IConfigProperty[] properties, Method saveAction, Object configObject, Method afterSaveAction, Object afterSaveObject)
    {
        super(par1GuiScreen, properties, saveAction, configObject, afterSaveAction, afterSaveObject);
    }
    
    private static IConfigProperty[] getProps()
    {
        ConfigCategory cc = DirectionHUD.getConfig().getCategory(Configuration.CATEGORY_GENERAL);
        IConfigProperty[] props = new IConfigProperty[ConfigElement.values().length];
        for (int i = 0; i < ConfigElement.values().length; i++)
        {
            ConfigElement ce = ConfigElement.values()[i];
            props[i] = new ConfigProperty(cc.get(ce.key()), ce.propertyType());
        }
        
        return props;
    }
}
