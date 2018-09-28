package de.raidcraft.trade;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.storage.ItemStorage;
import de.raidcraft.api.storage.StorageException;
import de.raidcraft.trade.api.SoldItem;
import de.raidcraft.trade.tables.TSaleLog;
import de.raidcraft.trade.tables.TSoldItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip Urban
 */
public class SaleHistoryManager {

    private final static int PLAYER_HISTORY_SIZE = 9;
    private TradePlugin plugin;
    private ItemStorage itemStorage;

    public SaleHistoryManager(TradePlugin plugin) {

        this.plugin = plugin;
        this.itemStorage = new ItemStorage("trade");
    }

    public TSoldItem addSale(ItemStack itemStack, Player player) {

        int storageId = itemStorage.storeObject(itemStack);

        TSoldItem tSoldItem = new TSoldItem();
        tSoldItem.setPlayer(player.getName());
        tSoldItem.setPlayerId(player.getUniqueId());
        tSoldItem.setWorld(player.getWorld().getName());
        tSoldItem.setWorldId(player.getWorld().getUID());
        tSoldItem.setDate(new Timestamp(System.currentTimeMillis()));
        tSoldItem.setStorageId(storageId);
        RaidCraft.getDatabase(TradePlugin.class).save(tSoldItem);
        deleteOldSales(player);
        return tSoldItem;
    }

    public List<SoldItem> getSales(Player player) {

        List<SoldItem> soldItems = new ArrayList<>();
        List<TSoldItem> tSoldItems = RaidCraft.getDatabase(TradePlugin.class).find(TSoldItem.class)
                .where()
                .eq("player_id", player.getUniqueId())
                .eq("world_id", player.getWorld().getUID())
                .order().desc("id").findList();
        for (TSoldItem tSoldItem : tSoldItems) {
            ItemStack itemStack;
            try {
                itemStack = itemStorage.getObject(tSoldItem.getStorageId());
            } catch (StorageException e) {
                RaidCraft.getDatabase(TradePlugin.class).delete(tSoldItem);
                continue;
            }

            SoldItem soldItem = new SoldItem(player.getName(), itemStack, tSoldItem.getId(), tSoldItem.getDate());
            soldItems.add(soldItem);
        }

        return soldItems;
    }

    public void removeSale(int databaseId) {

        TSoldItem tSoldItem = RaidCraft.getDatabase(TradePlugin.class).find(TSoldItem.class, databaseId);
        if (tSoldItem == null) return;
        try {
            itemStorage.removeObject(tSoldItem.getStorageId());
        } catch (StorageException e) {
        }
        TSaleLog log = plugin.getRcDatabase().find(TSaleLog.class).where().eq("sold_item_id", tSoldItem.getId()).findOne();
        if (log != null) plugin.getRcDatabase().delete(log);
        plugin.getRcDatabase().delete(tSoldItem);
    }

    private void deleteOldSales(Player player) {

        List<TSoldItem> tSoldItems = RaidCraft.getDatabase(TradePlugin.class).find(TSoldItem.class)
                .where()
                .eq("player_id", player.getUniqueId())
                .eq("world_id", player.getWorld().getUID())
                .order().desc("id").findList();
        if (tSoldItems == null || tSoldItems.size() <= PLAYER_HISTORY_SIZE) return;
        int i = 0;
        for (TSoldItem tSoldItem : tSoldItems) {
            i++;
            if (i > PLAYER_HISTORY_SIZE) {
                try {
                    itemStorage.removeObject(tSoldItem.getStorageId());
                } catch (StorageException e) {
                }
                RaidCraft.getDatabase(TradePlugin.class).delete(tSoldItem);
            }
        }
    }

    public static TSaleLog log(ItemStack itemStack, Player player, double price, String action) {

        TSaleLog log = new TSaleLog();
        log.setAction(action);
        log.setUuid(player.getUniqueId());
        log.setAmount(itemStack.getAmount());
        log.setPrice(price);
        log.setItem(RaidCraft.getItemIdString(itemStack));
        log.setTimestamp(Timestamp.from(Instant.now()));
        RaidCraft.getDatabase(TradePlugin.class).save(log);
        return log;
    }
}
