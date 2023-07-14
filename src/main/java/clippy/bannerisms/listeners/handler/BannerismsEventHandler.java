package clippy.bannerisms.listeners;

import clippy.bannerisms.Bannerisms;
import org.bukkit.Bukkit;
public class BannerismsEventHandler {
    private final Bannerisms plugin;

    private final PluginManager pluginManager;

    public Bannerisms EventHandler(Bannerisms plugin) {
        this.plugin = plugin;
        this.pluginManager = Bukkit.getPluginManager();
    }

    public void registerListener(BannerismsListener<?> listener) {
        listener.setPlugin(plugin);
        pluginManager.registerEvents(listener, plugin);
    }
}
