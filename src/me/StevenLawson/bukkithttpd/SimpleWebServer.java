/*
 Copyright Paul James Mutton, 2001-2004, http://www.jibble.org/

 This file is part of Mini Wegb Server / SimpleWebServer.

 This software is dual-licensed, allowing you to choose between the GNU
 General Public License (GPL) and the www.jibble.org Commercial License.
 Since the GPL may be too restrictive for use in a proprietary application,
 a commercial license is also provided. Full license information can be
 found at http://www.jibble.org/licenses/

 $Author: pjm2 $
 $Id: ServerSideScriptEngine.java,v 1.4 2004/02/01 13:37:35 pjm2 Exp $

 */
package me.StevenLawson.bukkithttpd;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright Paul Mutton
 * http://www.jibble.org/
 *
 */
public class SimpleWebServer extends Thread
{
    private static final Logger log = org.bukkit.Bukkit.getLogger();
    private BukkitHttpd _plugin;
    private File _rootDir;
    private ServerSocket _serverSocket;
    protected boolean is_running = false;
    protected boolean is_ready = false;
    public static final String VERSION = "BukkitHttpd - by Madgeek";
    public static final Map<String, String> MIME_TYPES = new HashMap<String, String>();

    static
    {
        MIME_TYPES.put(".gif", "image/gif");
        MIME_TYPES.put(".jpg", "image/jpeg");
        MIME_TYPES.put(".png", "image/png");
        MIME_TYPES.put(".html", "text/html");
        MIME_TYPES.put(".htm", "text/html");
        MIME_TYPES.put(".txt", "text/plain");
        MIME_TYPES.put(".log", "text/plain");
        MIME_TYPES.put(".yml", "text/plain");
    }

    public SimpleWebServer(File rootDir, String address, int port, BukkitHttpd instance)
    {
        _plugin = instance;

        try
        {
            _rootDir = rootDir.getCanonicalFile();
        }
        catch (IOException ex)
        {
            log.log(Level.SEVERE, "[" + _plugin.getDescription().getName() + "]: Invalid root directory.");
            return;
        }

        if (!_rootDir.isDirectory())
        {
            log.log(Level.SEVERE, "[" + _plugin.getDescription().getName() + "]: Invalid root directory.");
            return;
        }

        InetAddress listen_address = null;
        if (address != null)
        {
            try
            {
                listen_address = InetAddress.getByName(address);
            }
            catch (UnknownHostException ex)
            {
                log.log(Level.SEVERE, "[" + _plugin.getDescription().getName() + "]: Unknown host: " + address);
                return;
            }
        }

        try
        {
            if (listen_address == null)
            {
                _serverSocket = new ServerSocket(port);
            }
            else
            {
                _serverSocket = new ServerSocket(port, 0, listen_address);
            }

            String bound_ip = _serverSocket.getInetAddress().getHostAddress();
            if (bound_ip.equals("0.0.0.0"))
            {
                bound_ip = "*";
            }

            log.info("[" + _plugin.getDescription().getName() + "]: Server started. Listening on " + bound_ip + ":" + port);
        }
        catch (IOException ex)
        {
            log.log(Level.SEVERE, "[" + _plugin.getDescription().getName() + "]: Can't bind to: " + (address == null ? "*" : address) + ":" + port);
            return;
        }

        is_ready = true;
    }

    @Override
    public void run()
    {
        while (is_running)
        {
            try
            {
                Socket socket = _serverSocket.accept();
                RequestThread requestThread = new RequestThread(socket, _rootDir, _plugin);
                requestThread.start();
            }
            catch (Exception ex)
            {
            }
        }
    }

    public void startServer()
    {
        if (!this.is_running && this.is_ready)
        {
            this.start();
            this.is_running = true;
        }
    }

    public void stopServer()
    {
        is_running = false;

        try
        {
            if (_serverSocket != null)
            {
                _serverSocket.close();
            }
        }
        catch (Exception ex)
        {
        }
    }

    // Work out the filename extension.  If there isn't one, we keep
    // it as the empty string ("").
    public static String getExtension(java.io.File file)
    {
        String extension = "";
        String filename = file.getName();
        int dotPos = filename.lastIndexOf(".");
        if (dotPos >= 0)
        {
            extension = filename.substring(dotPos);
        }
        return extension.toLowerCase();
    }
}