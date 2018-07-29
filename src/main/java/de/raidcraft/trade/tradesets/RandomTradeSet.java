package de.raidcraft.trade.tradesets;

import com.avaje.ebean.EbeanServer;
import de.raidcraft.RaidCraft;
import de.raidcraft.api.items.CustomItemException;
import de.raidcraft.api.random.RDSTable;
import de.raidcraft.api.random.objects.ItemLootObject;
import de.raidcraft.trade.TradePlugin;
import de.raidcraft.trade.offers.ItemOffer;
import de.raidcraft.trade.tables.TTradeSetCache;
import de.raidcraft.trade.tables.TTradeSetCacheItem;
import de.raidcraft.util.ConfigUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author mdoering
 */
@Getter
@Setter
public class RandomTradeSet extends ConfigTradeSet {

    private final long cooldown;
    private final RDSTable randomTable;
    private TTradeSetCache cache;

    public RandomTradeSet(String name, ConfigurationSection config, RDSTable randomTable) {

        super(name, config);
        this.cooldown = config.getLong("cooldown");
        this.randomTable = randomTable;
        this.cache = RaidCraft.getDatabase(TradePlugin.class).find(TTradeSetCache.class).where()
                .eq("trade_set", name).findUnique();
    }

    @Override
    public void reloadOffers() {

        super.reloadOffers();

        EbeanServer database = RaidCraft.getDatabase(TradePlugin.class);
        if (cache == null) {
            cache = new TTradeSetCache();
            cache.setTradeSet(getName());
            database.save(cache);
        }

        cache.setLastUpdate(Timestamp.from(Instant.now()));
        database.update(cache);

        if (cooldown > 0) {
            if (Instant.now().isBefore(cache.getLastUpdate().toInstant().plusSeconds(getCooldown()))) {
                // lets load all cached items and do not recalculate random table
                for (TTradeSetCacheItem item : cache.getItems()) {
                    try {
                        ItemStack itemStack = RaidCraft.getSafeItem(item.getItem());
                        itemStack.setAmount(item.getAmount());
                        addOffer(new ItemOffer(item.getPrice(), itemStack));
                    } catch (CustomItemException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }

        cache.getItems().clear();
        database.update(cache);

        randomTable.getResult().stream().filter(object -> object instanceof ItemLootObject).forEach(object -> {
            if (((ItemLootObject) object).getPrice() > 0) {
                ItemOffer itemOffer = new ItemOffer(((ItemLootObject) object).getPrice(), ((ItemLootObject) object).getItemStack());
                addOffer(itemOffer);
                TTradeSetCacheItem cacheItem = new TTradeSetCacheItem();
                cacheItem.setItem(RaidCraft.getItemIdString(itemOffer.getItemStack()));
                cacheItem.setAmount(itemOffer.getItemStack().getAmount());
                cacheItem.setPrice(itemOffer.getPrice());
                cache.getItems().add(cacheItem);
            } else {
                RaidCraft.LOGGER.warning("Price is 0 not adding " + ((ItemLootObject) object).getItemStack()
                        + " to trade set in " + ConfigUtil.getFileName(config));
            }
        });

        database.update(cache);
    }
}
