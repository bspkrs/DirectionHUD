package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import bspkrs.client.util.HUDUtils;
import bspkrs.util.BSProp;
import bspkrs.util.BSPropRegistry;
import bspkrs.util.ModVersionChecker;

public class mod_DirectionHUD extends BaseMod
{
    protected float           zLevel               = 0.0F;
    private ScaledResolution  scaledResolution;
    @BSProp(info = "Valid alignment strings are topleft, topcenter, topright, middleleft, middlecenter, middleright, bottomleft, bottomcenter, bottomright")
    public static String      alignMode            = "topcenter";
    @BSProp(info = "Valid color values are 0-9, a-f (color values can be found here: http://www.minecraftwiki.net/wiki/File:Colors.png)")
    public static String      markerColor          = "c";
    @BSProp(info = "The filename of the compass PNG image file. The file must be located in the /gui/ folder of the DirectionHUD zip file or minecraft.jar.")
    public static String      imageFileName        = "compass.png";
    @BSProp(info = "Index of the selected compass in the compass image file starting at 0. Up to 10 compasses can fit in the image (10 would be index 9). Each compass is 24 pixels tall (two lines of height 12).", min = 0, max = 9)
    public static int         compassIndex         = 0;
    @BSProp(info = "Horizontal offset from the edge of the screen (when using right alignments the x offset is relative to the right edge of the screen)")
    public static int         xOffset              = 2;
    @BSProp(info = "Vertical offset from the edge of the screen (when using bottom alignments the y offset is relative to the bottom edge of the screen)")
    public static int         yOffset              = 2;
    @BSProp(info = "Vertical offset used only for the bottomcenter alignment to avoid the vanilla HUD")
    public static int         yOffsetBottomCenter  = 41;
    @BSProp(info = "Set to true if you want the xOffset value to be applied when using a center alignment")
    public static boolean     applyXOffsetToCenter = false;
    @BSProp(info = "Set to true if you want the yOffset value to be applied when using a middle alignment")
    public static boolean     applyYOffsetToMiddle = false;
    @BSProp(info = "Set to true to show info when chat is open, false to disable info when chat is open\n\n**ONLY EDIT WHAT IS BELOW THIS**")
    public static boolean     showInChat           = true;
    
    private ModVersionChecker versionChecker;
    private boolean           allowUpdateCheck;
    private final String      versionURL           = "http://bspk.rs/Minecraft/1.5.1/directionHUD.version";
    private final String      mcfTopic             = "http://www.minecraftforum.net/topic/1114612-";
    
    public mod_DirectionHUD()
    {
        BSPropRegistry.registerPropHandler(this.getClass());
    }
    
    @Override
    public String getName()
    {
        return "DirectionHUD";
    }
    
    @Override
    public String getVersion()
    {
        return "v1.11(1.5.1)";
    }
    
    @Override
    public String getPriorities()
    {
        return "required-after:mod_bspkrsCore";
    }
    
    @Override
    public void load()
    {
        allowUpdateCheck = mod_bspkrsCore.allowUpdateCheck;
        if (allowUpdateCheck)
        {
            versionChecker.checkVersionWithLogging();
            versionChecker = new ModVersionChecker(getName(), getVersion(), versionURL, mcfTopic);
        }
        
        ModLoader.setInGameHook(this, true, false);
    }
    
    @Override
    public boolean onTickInGame(float f, Minecraft mc)
    {
        if ((mc.inGameHasFocus || mc.currentScreen == null || (mc.currentScreen instanceof GuiChat && showInChat)) && !mc.gameSettings.showDebugInfo && !mc.gameSettings.keyBindPlayerList.pressed)
        {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            mc.renderEngine.resetBoundTexture();
            scaledResolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            displayHUD(mc);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            mc.renderEngine.resetBoundTexture();
        }
        
        if (allowUpdateCheck)
        {
            if (!versionChecker.isCurrentVersion())
                for (String msg : versionChecker.getInGameMessage())
                    mc.thePlayer.addChatMessage(msg);
            allowUpdateCheck = false;
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        
        return true;
    }
    
    private int getX(int width)
    {
        if (alignMode.equalsIgnoreCase("topcenter") || alignMode.equalsIgnoreCase("middlecenter") || alignMode.equalsIgnoreCase("bottomcenter"))
            return scaledResolution.getScaledWidth() / 2 - width / 2 + (applyXOffsetToCenter ? xOffset : 0);
        else if (alignMode.equalsIgnoreCase("topright") || alignMode.equalsIgnoreCase("middleright") || alignMode.equalsIgnoreCase("bottomright"))
            return scaledResolution.getScaledWidth() - width - xOffset;
        else
            return xOffset;
    }
    
    private int getY(int rowCount, int height)
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
    
    private void displayHUD(Minecraft mc)
    {
        int direction = MathHelper.floor_double(((mc.thePlayer.rotationYaw * 256F) / 360F) + 0.5D) & 255;
        
        int yBase = getY(1, 12);
        int xBase = getX(65);
        
        if (direction < 128)
            HUDUtils.drawTexturedModalRect(xBase, yBase, direction, (compassIndex * 24), 65, 12, zLevel);
        else
            HUDUtils.drawTexturedModalRect(xBase, yBase, direction - 128, (compassIndex * 24) + 12, 65, 12, zLevel);
        
        mc.fontRenderer.drawString("\247" + markerColor.toLowerCase() + "|", xBase + 32, yBase + 1, 0xffffff);
        mc.fontRenderer.drawString("\247" + markerColor.toLowerCase() + "|", xBase + 32, yBase + 5, 0xffffff);
    }
}
