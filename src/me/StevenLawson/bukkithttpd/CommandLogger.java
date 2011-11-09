package me.StevenLawson.bukkithttpd;

import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class CommandLogger extends Handler implements CommandSender
{
    private BukkitHttpd _plugin;
    private String _name;
    private StringBuilder _output = new StringBuilder();
    
    public CommandLogger(BukkitHttpd plugin, String name)
    {
        this._plugin = plugin;
        this._name = name;
        Logger.getLogger("Minecraft").addHandler(this);
    }
    
    public String getLog()
    {
        return this._output.toString();
    }
    
    @Override
    public void publish(LogRecord record)
    {
        _output.append(ChatColor.stripColor(record.getMessage()));
        _output.append("\r\n");
    }

    @Override
    public void flush()
    {
        return;
    }

    @Override
    public void close() throws SecurityException
    {
        Logger.getLogger("Minecraft").removeHandler(this);
    }

    @Override
    public void sendMessage(String string)
    {
        _output.append(ChatColor.stripColor(string));
        _output.append("\r\n");
    }

    @Override
    public Server getServer()
    {
        return this._plugin.getServer();
    }

    @Override
    public String getName()
    {
        return this._name;
    }

    @Override
    public boolean isPermissionSet(String string)
    {
        return true;
    }

    @Override
    public boolean isPermissionSet(Permission prmsn)
    {
        return true;
    }

    @Override
    public boolean hasPermission(String string)
    {
        return true;
    }

    @Override
    public boolean hasPermission(Permission prmsn)
    {
        return true;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String string, boolean bln)
    {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin)
    {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String string, boolean bln, int i)
    {
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int i)
    {
        return null;
    }

    @Override
    public void removeAttachment(PermissionAttachment pa)
    {
        return;
    }

    @Override
    public void recalculatePermissions()
    {
        return;
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions()
    {
        return null;
    }

    @Override
    public boolean isOp()
    {
        return true;
    }

    @Override
    public void setOp(boolean bln)
    {
        return;
    }
}
