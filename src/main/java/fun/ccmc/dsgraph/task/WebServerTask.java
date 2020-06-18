package fun.ccmc.dsgraph.task;

import fun.ccmc.dsgraph.DSGraph;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.io.File;

public class WebServerTask extends BukkitRunnable {
    private final Server server = new Server(8180);

    @Override
    public void run() {
        // Create the ResourceHandler. It is the object that will actually
        // handle the request for a given file. It is a Jetty Handler object
        // so it is suitable for chaining with other handlers as you will see
        // in other examples
        ResourceHandler resource_handler = new ResourceHandler();

        // Configure the ResourceHandler. Setting the resource base indicates
        // where the files should be served out of
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
        resource_handler.setResourceBase(DSGraph.getInstance().getDataFolder() + "/web");

        // Add the ResourceHandler to the server
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
        server.setHandler(handlers);

        startServer();
    }

    private void startServer() {
        stopServer();
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopServer() {
        if (server.isStarted()) {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void cancel() throws IllegalStateException {
        stopServer();
        Bukkit.getScheduler().cancelTask(getTaskId());
    }
}
