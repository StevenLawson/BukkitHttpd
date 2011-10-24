package me.StevenLawson.bukkithttpd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

public class LoggerTest extends Handler implements CommandSender
{
    private BukkitHttpd _plugin;
    private FileWriter _output_writer;
    private BufferedWriter _output;

    public LoggerTest(BukkitHttpd instance)
    {
        _plugin = instance;
        startup();
    }

    private void startup()
    {
        Logger.getLogger("Minecraft").addHandler(this);

        try
        {
            _output_writer = new FileWriter(new File(_plugin.root_directory + "BukkitHttpd.log"), true);
            _output = new BufferedWriter(_output_writer);
            write("----------------------------------------------------------------------");
        }
        catch (IOException ex)
        {
        }
    }

    private void shutdown()
    {
        Logger.getLogger("Minecraft").removeHandler(this);
        try
        {
            if (_output != null)
            {
                _output.flush();
                _output.close();
            }
            if (_output_writer != null)
            {
                _output_writer.flush();
                _output_writer.close();
            }
        }
        catch (IOException ex)
        {
        }
    }
    
    private void write(String message)
    {
        if (_output != null)
        {
            try
            {
                _output.write(ChatColor.stripColor(message) + "\r\n");
                _output.flush();
            }
            catch (IOException ex)
            {
            }
        }
    }
    
    public boolean sendServerCommand(String command)
    {
        return _plugin.getServer().dispatchCommand(this, command);
    }

    //Handler:
    @Override
    public void publish(LogRecord record)
    {
        write(record.getMessage());
    }

    @Override
    public void flush()
    {
        try
        {
            if (_output != null)
            {
                _output.flush();
            }
            if (_output_writer != null)
            {
                _output_writer.flush();
            }
        }
        catch (IOException ex)
        {
        }
    }

    @Override
    public void close() throws SecurityException
    {
        shutdown();
    }

    //CommandSender:
    @Override
    public void sendMessage(String string)
    {
        write(string);
    }

    @Override
    public Server getServer()
    {
        return _plugin.getServer();
    }

    @Override
    public String getName()
    {
        return _plugin.getDescription().getName();
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
