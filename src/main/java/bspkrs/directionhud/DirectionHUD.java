package bspkrs.directionhud;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import bspkrs.client.util.HUDUtils;
import bspkrs.directionhud.fml.DirectionHUDMod;
import bspkrs.util.CommonUtils;
import bspkrs.util.Const;
import bspkrs.util.config.Configuration;

public class DirectionHUD
{
    public static final String      VERSION_NUMBER       = "1.19(" + Const.MCVERSION + ")";
    
    protected static float          zLevel               = -100.0F;
    private static ScaledResolution scaledResolution;
    
    // Config fields
    public static String            alignMode            = "topcenter";
    public static String            markerColor          = "c";
    public static int               compassIndex         = 0;
    public static int               xOffset              = 2;
    public static int               yOffset              = 2;
    public static int               yOffsetBottomCenter  = 41;
    public static boolean           applyXOffsetToCenter = false;
    public static boolean           applyYOffsetToMiddle = false;
    public static boolean           showInChat           = true;
    
    private static Configuration    config;
    
    public static void loadConfig(File file)
    {
        String ctgyGen = Configuration.CATEGORY_GENERAL;
        
        if (!CommonUtils.isObfuscatedEnv())
        { // debug settings for deobfuscated execution
          //            if (file.exists())
          //                file.delete();
        }
        
        config = new Configuration(file);
        
        config.load();
        
        alignMode = config.getString("alignMode", ctgyGen, alignMode,
                "Valid alignment strings are topleft, topcenter, topright, middleleft, middlecenter, middleright, bottomleft, bottomcenter, bottomright");
        markerColor = config.getString("markerColor", ctgyGen, markerColor,
                "Valid color values are 0-9, a-f (color values can be found here: http://www.minecraftwiki.net/wiki/File:Colors.png)");
        compassIndex = config.getInt("compassIndex", ctgyGen, compassIndex, 0, 9,
                "Index of the selected compass in the compass image file starting at 0. Up to 10 compasses can fit in the image (10 would be index 9). Each compass is 24 pixels tall (two lines of height 12).");
        xOffset = config.getInt("xOffset", ctgyGen, xOffset, Integer.MIN_VALUE, Integer.MAX_VALUE,
                "Horizontal offset from the edge of the screen (when using right alignments the x offset is relative to the right edge of the screen)");
        yOffset = config.getInt("yOffset", ctgyGen, yOffset, Integer.MIN_VALUE, Integer.MAX_VALUE,
                "Vertical offset from the edge of the screen (when using bottom alignments the y offset is relative to the bottom edge of the screen)");
        yOffsetBottomCenter = config.getInt("yOffsetBottomCenter", ctgyGen, yOffsetBottomCenter, 0, Integer.MAX_VALUE,
                "Vertical offset used only for the bottomcenter alignment to avoid the vanilla HUD");
        applyXOffsetToCenter = config.getBoolean("applyXOffsetToCenter", ctgyGen, applyXOffsetToCenter,
                "Set to true if you want the xOffset value to be applied when using a center alignment");
        applyYOffsetToMiddle = config.getBoolean("applyYOffsetToMiddle", ctgyGen, applyYOffsetToMiddle,
                "Set to true if you want the yOffset value to be applied when using a middle alignment");
        showInChat = config.getBoolean("showInChat", ctgyGen, showInChat,
                "Set to true to show info when chat is open, false to disable info when chat is open");
        
        config.save();
    }
    
    public static boolean onTickInGame(Minecraft mc)
    {
        if (DirectionHUDMod.instance.isEnabled() && (mc.inGameHasFocus || mc.currentScreen == null || (mc.currentScreen instanceof GuiChat && showInChat))
                && !mc.gameSettings.showDebugInfo && !mc.gameSettings.keyBindPlayerList.isPressed())
        {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            displayHUD(mc);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        
        return true;
    }
    
    private static int getX(int width)
    {
        if (alignMode.equalsIgnoreCase("topcenter") || alignMode.equalsIgnoreCase("middlecenter") || alignMode.equalsIgnoreCase("bottomcenter"))
            return scaledResolution.getScaledWidth() / 2 - width / 2 + (applyXOffsetToCenter ? xOffset : 0);
        else if (alignMode.equalsIgnoreCase("topright") || alignMode.equalsIgnoreCase("middleright") || alignMode.equalsIgnoreCase("bottomright"))
            return scaledResolution.getScaledWidth() - width - xOffset;
        else
            return xOffset;
    }
    
    private static int getY(int rowCount, int height)
    {
        if (alignMode.equalsIgnoreCase("middleleft") || alignMode.equalsIgnoreCase("middlecenter") || alignMode.equalsIgnoreCase("middleright"))
            return (scaledResolution.getScaledHeight() / 2) - ((rowCount * height) / 2) + (applyYOffsetToMiddle ? yOffset : 0);
        else if (alignMode.equalsIgnoreCase("bottomleft") || alignMode.equalsIgnoreCase("bottomright"))
            return scaledResolution.getScaledHeight() - (rowCount * height) - yOffset;
        else if (alignMode.equalsIgnoreCase("bottomcenter"))
            return scaledResolution.getScaledHeight() - (rowCount * height) - yOffsetBottomCenter;
        else
            return yOffset;
    }
    
    private static void displayHUD(Minecraft mc)
    {
        int direction = MathHelper.floor_double(((mc.thePlayer.rotationYaw * 256F) / 360F) + 0.5D) & 255;
        
        int yBase = getY(1, 12);
        int xBase = getX(65);
        
        mc.getTextureManager().bindTexture(new ResourceLocation("DirectionHUD:textures/gui/compass.png"));
        if (direction < 128)
            HUDUtils.drawTexturedModalRect(xBase, yBase, direction, (compassIndex * 24), 65, 12, zLevel);
        else
            HUDUtils.drawTexturedModalRect(xBase, yBase, direction - 128, (compassIndex * 24) + 12, 65, 12, zLevel);
        
        //mc.renderEngine.resetBoundTexture();
        mc.fontRenderer.drawString("\247" + markerColor.toLowerCase() + "|", xBase + 32, yBase + 1, 0xffffff);
        mc.fontRenderer.drawString("\247" + markerColor.toLowerCase() + "|\247r", xBase + 32, yBase + 5, 0xffffff);
    }
}
