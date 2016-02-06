package bspkrs.directionhud;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

import org.lwjgl.opengl.GL11;

import bspkrs.client.util.HUDUtils;
import bspkrs.directionhud.fml.Reference;
import bspkrs.util.CommonUtils;

public class DirectionHUD
{
    protected static float          zLevel                      = -100.0F;
    private static ScaledResolution scaledResolution;

    // Config fields
    private final static boolean    enabledDefault              = true;
    public static boolean           enabled                     = enabledDefault;
    private final static String     alignModeDefault            = "topcenter";
    public static String            alignMode                   = alignModeDefault;
    private final static String     markerColorDefault          = "c";
    public static String            markerColor                 = markerColorDefault;
    private final static int        compassIndexDefault         = 0;
    public static int               compassIndex                = compassIndexDefault;
    private final static int        xOffsetDefault              = 2;
    public static int               xOffset                     = xOffsetDefault;
    private final static int        yOffsetDefault              = 2;
    public static int               yOffset                     = yOffsetDefault;
    private final static int        yOffsetBottomCenterDefault  = 41;
    public static int               yOffsetBottomCenter         = yOffsetBottomCenterDefault;
    private final static boolean    applyXOffsetToCenterDefault = false;
    public static boolean           applyXOffsetToCenter        = applyXOffsetToCenterDefault;
    private final static boolean    applyYOffsetToMiddleDefault = false;
    public static boolean           applyYOffsetToMiddle        = applyYOffsetToMiddleDefault;
    private final static boolean    showInChatDefault           = true;
    public static boolean           showInChat                  = showInChatDefault;

    public static void initConfig(File file)
    {
        Reference.config = new Configuration(file);

        if (!CommonUtils.isObfuscatedEnv())
        { // debug settings for deobfuscated execution
          //            if (file.exists())
          //                file.delete();
        }

        syncConfig();
    }

    public static void syncConfig()
    {
        String ctgyGen = Configuration.CATEGORY_GENERAL;

        Reference.config.load();

        Reference.config.addCustomCategoryComment(ctgyGen, "ATTENTION: Editing this file manually is no longer necessary. \n" +
                "Type the command '/directionhud config' without the quotes in-game to modify these settings.");

        List<String> orderedKeys = new ArrayList<String>(ConfigElement.values().length);

        enabled = Reference.config.getBoolean(ConfigElement.ENABLED.key(), ctgyGen, enabledDefault, ConfigElement.ENABLED.desc(),
                ConfigElement.ENABLED.languageKey());
        orderedKeys.add(ConfigElement.ENABLED.key());
        alignMode = Reference.config.getString(ConfigElement.ALIGN_MODE.key(), ctgyGen, alignModeDefault, ConfigElement.ALIGN_MODE.desc(),
                ConfigElement.ALIGN_MODE.validStrings(), ConfigElement.ALIGN_MODE.languageKey());
        orderedKeys.add(ConfigElement.ALIGN_MODE.key());
        markerColor = Reference.config.getString(ConfigElement.MARKER_COLOR.key(), ctgyGen, markerColorDefault,
                ConfigElement.MARKER_COLOR.desc(), ConfigElement.MARKER_COLOR.validStrings(), ConfigElement.MARKER_COLOR.languageKey());
        orderedKeys.add(ConfigElement.MARKER_COLOR.key());
        compassIndex = Reference.config.getInt(ConfigElement.COMPASS_INDEX.key(), ctgyGen, compassIndexDefault, 0, 9, ConfigElement.COMPASS_INDEX.desc(),
                ConfigElement.COMPASS_INDEX.languageKey());
        orderedKeys.add(ConfigElement.COMPASS_INDEX.key());
        showInChat = Reference.config.getBoolean(ConfigElement.SHOW_IN_CHAT.key(), ctgyGen, showInChatDefault, ConfigElement.SHOW_IN_CHAT.desc(),
                ConfigElement.SHOW_IN_CHAT.languageKey());
        orderedKeys.add(ConfigElement.SHOW_IN_CHAT.key());
        xOffset = Reference.config.getInt(ConfigElement.X_OFFSET.key(), ctgyGen, xOffsetDefault, Integer.MIN_VALUE, Integer.MAX_VALUE,
                ConfigElement.X_OFFSET.desc(), ConfigElement.X_OFFSET.languageKey());
        orderedKeys.add(ConfigElement.X_OFFSET.key());
        applyXOffsetToCenter = Reference.config.getBoolean(ConfigElement.APPLY_X_OFFSET_TO_CENTER.key(), ctgyGen, applyXOffsetToCenterDefault,
                ConfigElement.APPLY_X_OFFSET_TO_CENTER.desc(), ConfigElement.APPLY_X_OFFSET_TO_CENTER.languageKey());
        orderedKeys.add(ConfigElement.APPLY_X_OFFSET_TO_CENTER.key());
        yOffset = Reference.config.getInt(ConfigElement.Y_OFFSET.key(), ctgyGen, yOffsetDefault, Integer.MIN_VALUE, Integer.MAX_VALUE,
                ConfigElement.Y_OFFSET.desc(), ConfigElement.Y_OFFSET.languageKey());
        orderedKeys.add(ConfigElement.Y_OFFSET.key());
        applyYOffsetToMiddle = Reference.config.getBoolean(ConfigElement.APPLY_Y_OFFSET_TO_MIDDLE.key(), ctgyGen, applyYOffsetToMiddleDefault,
                ConfigElement.APPLY_Y_OFFSET_TO_MIDDLE.desc(), ConfigElement.APPLY_Y_OFFSET_TO_MIDDLE.languageKey());
        orderedKeys.add(ConfigElement.APPLY_Y_OFFSET_TO_MIDDLE.key());
        yOffsetBottomCenter = Reference.config.getInt(ConfigElement.Y_OFFSET_BOTTOM_CENTER.key(), ctgyGen, yOffsetBottomCenterDefault,
                Integer.MIN_VALUE, Integer.MAX_VALUE, ConfigElement.Y_OFFSET_BOTTOM_CENTER.desc(), ConfigElement.Y_OFFSET_BOTTOM_CENTER.languageKey());
        orderedKeys.add(ConfigElement.Y_OFFSET_BOTTOM_CENTER.key());

        Reference.config.setCategoryPropertyOrder(ctgyGen, orderedKeys);

        Reference.config.save();
    }

    public static boolean onTickInGame(Minecraft mc)
    {
        if (enabled && (mc.inGameHasFocus || mc.currentScreen == null || (mc.currentScreen instanceof GuiChat && showInChat))
                && !mc.gameSettings.showDebugInfo)
        {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            scaledResolution = new ScaledResolution(mc);
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
        mc.fontRendererObj.drawString("\247" + markerColor.toLowerCase() + "|", xBase + 32, yBase + 1, 0xffffff);
        mc.fontRendererObj.drawString("\247" + markerColor.toLowerCase() + "|\247r", xBase + 32, yBase + 5, 0xffffff);
    }
}
