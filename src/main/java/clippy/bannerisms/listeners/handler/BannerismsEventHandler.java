package clippy.bannerisms.listeners.handler;

import clippy.bannerisms.Bannerisms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class BannerismsEventHandler {
    private final Bannerisms plugin;
    private final PluginManager pluginManager;

    public BannerismsEventHandler(Bannerisms plugin) {

        this.plugin = plugin;
        this.pluginManager = Bukkit.getPluginManager();
    }

    public void registerListener(BannerismsListener<?> listener) {
        listener.setPlugin(plugin);
        pluginManager.registerEvents(listener, plugin);
    }
}
