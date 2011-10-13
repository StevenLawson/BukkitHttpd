package me.StevenLawson.bukkithttpd;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitHttpd extends JavaPlugin
{
    private static final Logger log = Logger.getLogger("Minecraft");
    
    private SimpleWebServer server;
    
	//TODO: Put these into a configuration file:
    public static final int PORT = 28996;
    public static final int TIMEOUT = 5000;
    public static final String PASSWORD = "lalalala";
    
    @Override
    public void onEnable()
    {
        try
        {
            server = new SimpleWebServer(new File("./"), PORT);
        }
        catch (IOException ex)
        {
            Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
        }
        
        log.info("[BukkitHttpd] BukkitHttpd enabled, listening on port: " + PORT);
    }
    
    @Override
    public void onDisable()
    {
        server.stopServer();
        
        log.info("[BukkitHttpd] BukkitHttpd disabled.");
    }
}