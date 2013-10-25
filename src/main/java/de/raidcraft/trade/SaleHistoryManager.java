package de.raidcraft.trade;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.storage.ItemStorage;
import de.raidcraft.api.storage.StorageException;
import de.raidcraft.trade.api.SoldItem;
import de.raidcraft.trade.tables.TSoldItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
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

    public void addSale(ItemStack itemStack, Player player) {

        int storageId = itemStorage.storeObject(itemStack);

        TSoldItem tSoldItem = new TSoldItem();
        tSoldItem.setPlayer(player.getName());
        tSoldItem.setWorld(player.getWorld().getName());
        tSoldItem.setDate(new Timestamp(System.currentTimeMillis()));
        tSoldItem.setStorageId(storageId);
        RaidCraft.getDatabase(TradePlugin.class).save(tSoldItem);
        deleteOldSales(player);
    }

    public List<SoldItem> getSales(Player player) {

        List<SoldItem> soldItems = new ArrayList<>();
        List<TSoldItem> tSoldItems = RaidCraft.getDatabase(TradePlugin.class)
                .find(TSoldItem.class).where().ieq("player", player.getName()).ieq("world", player.getWorld().getName()).order().desc("id").findList();
        for(TSoldItem tSoldItem : tSoldItems) {
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
        if(tSoldItem == null) return;
        try {
            itemStorage.removeObject(tSoldItem.getStorageId());
        } catch (StorageException e) {}
        RaidCraft.getDatabase(TradePlugin.class).delete(tSoldItem);
    }

    private void deleteOldSales(Player player) {

        List<TSoldItem> tSoldItems = RaidCraft.getDatabase(TradePlugin.class)
                .find(TSoldItem.class).where().ieq("player", player.getName()).ieq("world", player.getWorld().getName()).order().desc("id").findList();
        if(tSoldItems == null || tSoldItems.size() <= PLAYER_HISTORY_SIZE) return;
        int i = 0;
        for(TSoldItem tSoldItem : tSoldItems) {
            i++;
            if(i > PLAYER_HISTORY_SIZE) {
                try {
                    itemStorage.removeObject(tSoldItem.getStorageId());
                } catch (StorageException e) {}
                RaidCraft.getDatabase(TradePlugin.class).delete(tSoldItem);
            }
        }
    }
}
