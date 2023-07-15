package clippy.bannerisms.listeners;

import clippy.bannerisms.listeners.handler.BannerismsListener;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Tag;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.DyeColor;

import com.google.gson.JsonObject;
import com.google.gson.Gson;

import java.util.*;
import java.util.regex.Matcher;


public class InventoryClickListener extends BannerismsListener<InventoryClickEvent> {
    @Override
    @EventHandler(priority = EventPriority.HIGHEST)
    protected void onCall(InventoryClickEvent e) {
        if (e == null || e.isCancelled()) {
            return;
        }

        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        final ArrayList<InventoryAction> PICKUP = new ArrayList<>(Arrays.asList(InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_SOME, InventoryAction.PICKUP_ONE));
        final ArrayList<InventoryAction> PLACE = new ArrayList<>(Arrays.asList(InventoryAction.PLACE_ALL, InventoryAction.PLACE_SOME, InventoryAction.PLACE_ONE));

        if (e.getClickedInventory() != null && e.getClickedInventory().getType() == InventoryType.LOOM) {

            if ((e.getRawSlot() == 0) && PLACE.contains(e.getAction())) {

                ItemStack bannerItem = e.getCursor();
                if (bannerItem != null && Tag.BANNERS.isTagged(bannerItem.getType()) && bannerItem.getItemMeta() instanceof BannerMeta) {

                    BannerMeta bannerMeta = (BannerMeta) bannerItem.getItemMeta();
                    if (!bannerMeta.getPatterns().isEmpty()) {
                        List<String> patternsMapList = new ArrayList<>();

                        List<Pattern> patterns = bannerMeta.getPatterns();

                        while (patterns.size() > 5) {
                            Pattern basePattern = patterns.get(0);

                            HashMap<String, Object> basePatternJson = new HashMap<>(basePattern.serialize());
                            basePatternJson.put("pattern", basePattern.getPattern());
                            String basePatternString = basePatternJson.toString();

                            patternsMapList.add(basePatternString);
                            patterns.remove(0);

                        }
                        bannerMeta.getPersistentDataContainer().set(plugin.bannerBase, PersistentDataType.STRING, patternsMapList.toString());
                        bannerMeta.setPatterns(patterns);
                        bannerItem.setItemMeta(bannerMeta);
                    }
                }
            } else if ((e.getRawSlot() == 3 || e.getRawSlot() == 0) && PICKUP.contains(e.getAction())) {

                ItemStack bannerItem = e.getCurrentItem();
                if (bannerItem != null && Tag.BANNERS.isTagged(bannerItem.getType()) && bannerItem.getItemMeta() instanceof BannerMeta) {

                    BannerMeta bannerMeta = (BannerMeta) bannerItem.getItemMeta();
                    String patternsListMap = bannerMeta.getPersistentDataContainer().get(plugin.bannerBase, PersistentDataType.STRING);
                    if (patternsListMap != null) {

                        Gson gson = new Gson();
                        java.util.regex.Pattern patternRegex = java.util.regex.Pattern.compile("\\{[^{}]+\\}");
                        Matcher matcher = patternRegex.matcher(patternsListMap);

                        List<String> jsonPatterns = new ArrayList<>();
                        while (matcher.find()) {
                            String pattern = matcher.group();
                            jsonPatterns.add(pattern);
                        }
                        Collections.reverse(jsonPatterns);
                        List<Pattern> patterns = bannerMeta.getPatterns();
                        for (String pattern: jsonPatterns) {
                            JsonObject basePatternObject = gson.fromJson(pattern, JsonObject.class);
                            DyeColor baseColor = DyeColor.valueOf(basePatternObject.get("color").getAsString());
                            PatternType basePatternType = PatternType.valueOf(basePatternObject.get("pattern").getAsString());
                            Pattern basePattern = new Pattern(baseColor, basePatternType);

                            patterns.add(0, basePattern);
                        }


                        bannerMeta.setPatterns(patterns);
                        bannerMeta.getPersistentDataContainer().remove(plugin.bannerBase);
                        bannerItem.setItemMeta(bannerMeta);
                    }
                }
            }
        }
    }
}

