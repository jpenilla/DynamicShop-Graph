package xyz.jpenilla.dsgraph;

import me.sat7.dynamicshop.events.ShopBuySellEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ShopListener implements Listener {
    @EventHandler
    public void onShop(ShopBuySellEvent e) {
        if (DSGraph.getInstance().getTaskManager().getRecordDataTask() != null) {
            DSGraph.getInstance().getCfg().getFiles().forEach(stockConfig -> {
                if (e.getMaterial().equals(stockConfig.getMaterial()) && e.getShopName().equals(stockConfig.getShopName())) {
                    DSGraph.getInstance().getTaskManager().getRecordDataTask().queue(stockConfig);
                }
            });
        }
    }
}