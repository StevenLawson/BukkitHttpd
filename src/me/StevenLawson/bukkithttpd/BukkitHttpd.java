package me.StevenLawson.bukkithttpd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitHttpd extends JavaPlugin
{
    private static final Logger log = Logger.getLogger("Minecraft");
    private SimpleWebServer server;
    
    public static final String CONFIG_FILE = "config.yml";
    
    public int port = 8181;
    public int timeout = 5000;
    public String password = "";
    
    @Override
    public void onEnable()
    {
        log.log(Level.INFO, "[" + getDescription().getName() + "] - Enabled! - Version: " + getDescription().getVersion() + " by Madgeek1450");

        createDefaultConfiguration(CONFIG_FILE);

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), CONFIG_FILE));
        
        port = config.getInt("port", port);
        timeout = config.getInt("timeout", timeout);
        password = config.getString("password", password);
        
        try
        {
            server = new SimpleWebServer(new File("./"), port, this);
            server.start();
        }
        catch (IOException ex)
        {
            Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
        }
        
        log.info("[BukkitHttpd] Listening on port: " + port);
    }
    
    @Override
    public void onDisable()
    {
        server.stopServer();
        
        log.info("[BukkitHttpd] BukkitHttpd disabled.");
    }

    private void createDefaultConfiguration(String name)
    {
        File actual = new File(getDataFolder(), name);
        if (!actual.exists())
        {
            log.info("[" + getDescription().getName() + "]: Installing default configuration file template: " + actual.getPath());
            InputStream input = null;
            try
            {
                JarFile file = new JarFile(getFile());
                ZipEntry copy = file.getEntry(name);
                if (copy == null)
                {
                    log.severe("[" + getDescription().getName() + "]: Unable to read default configuration: " + actual.getPath());
                    return;
                }
                input = file.getInputStream(copy);
            }
            catch (IOException ioex)
            {
                log.severe("[" + getDescription().getName() + "]: Unable to read default configuration: " + actual.getPath());
            }
            if (input != null)
            {
                FileOutputStream output = null;

                try
                {
                    getDataFolder().mkdirs();
                    output = new FileOutputStream(actual);
                    byte[] buf = new byte[8192];
                    int length = 0;
                    while ((length = input.read(buf)) > 0)
                    {
                        output.write(buf, 0, length);
                    }

                    log.info("[" + getDescription().getName() + "]: Default configuration file written: " + actual.getPath());
                }
                catch (IOException ioex)
                {
                    log.log(Level.SEVERE, "[" + getDescription().getName() + "]: Unable to write default configuration: " + actual.getPath(), ioex);
                }
                finally
                {
                    try
                    {
                        if (input != null)
                        {
                            input.close();
                        }
                    }
                    catch (IOException ioex)
                    {
                    }

                    try
                    {
                        if (output != null)
                        {
                            output.close();
                        }
                    }
                    catch (IOException ioex)
                    {
                    }
                }
            }
        }
    }
}