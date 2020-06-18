package fun.ccmc.dsgraph.task;

import fun.ccmc.dsgraph.DSGraph;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.io.File;

public class WebServerTask extends BukkitRunnable {
    private final Server server = new Server(8180);
    private final String path = DSGraph.getInstance().getDataFolder() + "/web";

    @Override
    public void run() {
        ResourceHandler handler = new ResourceHandler();

        handler.setDirectoriesListed(true);
        handler.setWelcomeFiles(new String[]{"index.html"});

        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        handler.setResourceBase(path);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{handler, new DefaultHandler()});
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
        super.cancel();
    }
}
