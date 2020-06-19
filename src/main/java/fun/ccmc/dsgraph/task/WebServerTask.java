package fun.ccmc.dsgraph.task;

import fun.ccmc.dsgraph.DSGraph;
import org.apache.commons.io.FileUtils;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.io.File;
import java.io.IOException;

public class WebServerTask extends BukkitRunnable {
    private final Server server = new Server(DSGraph.getInstance().getCfg().getPort());
    private final String path = DSGraph.getInstance().getDataFolder() + "/web";

    @Override
    public void run() {
        ResourceHandler handler = new ResourceHandler();

        handler.setDirectoriesListed(true);

        //Create web server dir
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        //Copy img.html
        String imgHTMLname = "img.html";
        File imgHTML = new File(path + "/" + imgHTMLname);
        if (!imgHTML.exists()) {
            try {
                FileUtils.copyToFile(DSGraph.getInstance().getResource(imgHTMLname), imgHTML);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        //Copy graph.html
        String graphHTMLname = "graph.html";
        File graphHTML = new File(path + "/" + graphHTMLname);
        if (!graphHTML.exists()) {
            try {
                FileUtils.copyToFile(DSGraph.getInstance().getResource(graphHTMLname), graphHTML);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
