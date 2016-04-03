package bspkrs.directionhud;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import bspkrs.directionhud.fml.gui.GuiDHConfig;
import bspkrs.fml.util.DelayedGuiDisplayTicker;

public class CommandDirectionHUD extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "directionhud";
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "commands.directionhud.usage";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 1;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        try
        {
            new DelayedGuiDisplayTicker(10, new GuiDHConfig(null));
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

}
