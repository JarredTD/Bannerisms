package clippy.bannerisms;

import clippy.bannerisms.listeners.InventoryClickListener;
import clippy.bannerisms.listeners.handler.BannerismsEventHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bannerisms extends JavaPlugin {

    public final NamespacedKey bannerBase = new NamespacedKey(this, "bannerBase");

    @Override
    public void onEnable() {
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerListeners() {
        BannerismsEventHandler eventHandler = new BannerismsEventHandler(this);
        eventHandler.registerListener(new InventoryClickListener());
    }
}
