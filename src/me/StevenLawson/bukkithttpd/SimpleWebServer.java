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
import java.net.ServerSocket;
import java.net.Socket;
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
    public static final String VERSION = "BukkitHttpd - by Madgeek";
    public static final Map<String, String> MIME_TYPES = new HashMap<String, String>();
    
    private BukkitHttpd _plugin;
    private File _rootDir;
    private ServerSocket _serverSocket;
    private boolean _running = true;

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
    
    public SimpleWebServer(File rootDir, int port, BukkitHttpd instance) throws IOException
    {
        _plugin = instance;
        _rootDir = rootDir.getCanonicalFile();
        if (!_rootDir.isDirectory())
        {
            throw new IOException("Not a directory.");
        }
        _serverSocket = new ServerSocket(port);
    }

    @Override
    public void run()
    {
        while (_running)
        {
            try
            {
                Socket socket = _serverSocket.accept();
                RequestThread requestThread = new RequestThread(socket, _rootDir, _plugin);
                requestThread.start();
            }
            catch (IOException ex)
            {
                Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void stopServer()
    {
        _running = false;
        try
        {
            _serverSocket.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger("Minecraft").log(Level.SEVERE, null, ex);
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