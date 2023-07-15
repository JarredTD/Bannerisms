package clippy.bannerisms.listeners.handler;

import clippy.bannerisms.Bannerisms;
import lombok.AccessLevel;
import lombok.Setter;

import org.bukkit.event.Listener;
public abstract class BannerismsListener<T> implements Listener{

        @Setter(AccessLevel.PACKAGE)
        protected Bannerisms plugin;

        protected abstract void onCall(T event);
}
