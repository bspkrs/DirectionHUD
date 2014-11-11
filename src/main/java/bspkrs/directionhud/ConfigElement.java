package bspkrs.directionhud;

import static net.minecraftforge.common.config.Property.Type.BOOLEAN;
import static net.minecraftforge.common.config.Property.Type.COLOR;
import static net.minecraftforge.common.config.Property.Type.INTEGER;
import static net.minecraftforge.common.config.Property.Type.STRING;
import net.minecraftforge.common.config.Property.Type;

public enum ConfigElement
{
    ENABLED("enabled", "bspkrs.dh.configgui.enabled", "Enables or disables the compass HUD display.", BOOLEAN),
    ALIGN_MODE("alignMode", "bspkrs.dh.configgui.alignMode",
            "Sets the position of the HUD on the screen. Valid alignment strings are topleft, topcenter, topright, middleleft, middlecenter, middleright, bottomleft, bottomcenter, bottomright",
            STRING, new String[] { "topleft", "topcenter", "topright", "middleleft", "middlecenter", "middleright", "bottomleft", "bottomcenter", "bottomright" }),
    MARKER_COLOR("markerColor", "bspkrs.dh.configgui.markerColor",
            "Valid color values are 0-9, a-f (color values can be found here: http://www.minecraftwiki.net/wiki/File:Colors.png)", COLOR,
            new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" }),
    COMPASS_INDEX("compassIndex", "bspkrs.dh.configgui.compassIndex",
            "Index of the selected compass in the compass image file starting at 0. Up to 10 compasses can fit in the image (10 would be index 9). Each compass is 24 pixels tall (two lines of height 12).", INTEGER),
    X_OFFSET("xOffset", "bspkrs.dh.configgui.xOffset",
            "Horizontal offset from the edge of the screen (when using right alignments the x offset is relative to the right edge of the screen)", INTEGER),
    Y_OFFSET("yOffset", "bspkrs.dh.configgui.yOffset",
            "Vertical offset from the edge of the screen (when using bottom alignments the y offset is relative to the bottom edge of the screen)", INTEGER),
    Y_OFFSET_BOTTOM_CENTER("yOffsetBottomCenter", "bspkrs.dh.configgui.yOffsetBottomCenter",
            "Vertical offset used only for the bottomcenter alignment to avoid the vanilla HUD", INTEGER),
    APPLY_X_OFFSET_TO_CENTER("applyXOffsetToCenter", "bspkrs.dh.configgui.applyXOffsetToCenter",
            "Set to true if you want the xOffset value to be applied when using a center alignment", BOOLEAN),
    APPLY_Y_OFFSET_TO_MIDDLE("applyYOffsetToMiddle", "bspkrs.dh.configgui.applyYOffsetToMiddle",
            "Set to true if you want the yOffset value to be applied when using a middle alignment", BOOLEAN),
    SHOW_IN_CHAT("showInChat", "bspkrs.dh.configgui.showInChat",
            "Set to true to show info when chat is open, false to disable info when chat is open", BOOLEAN);

    private String   key;
    private String   langKey;
    private String   desc;
    private Type     propertyType;
    private String[] validStrings;

    private ConfigElement(String key, String langKey, String desc, Type propertyType, String[] validStrings)
    {
        this.key = key;
        this.langKey = langKey;
        this.desc = desc;
        this.propertyType = propertyType;
        this.validStrings = validStrings;
    }

    private ConfigElement(String key, String langKey, String desc, Type propertyType)
    {
        this(key, langKey, desc, propertyType, new String[0]);
    }

    public String key()
    {
        return key;
    }

    public String languageKey()
    {
        return langKey;
    }

    public String desc()
    {
        return desc;
    }

    public Type propertyType()
    {
        return propertyType;
    }

    public String[] validStrings()
    {
        return validStrings;
    }
}