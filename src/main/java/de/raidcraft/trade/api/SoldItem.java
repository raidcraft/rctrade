package de.raidcraft.trade.api;

import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;

/**
 * @author Philip Urban
 */
public class SoldItem {

    private String player;
    private ItemStack itemStack;
    private int databaseId;
    private Timestamp date;

    public SoldItem(String player, ItemStack itemStack, int databaseId, Timestamp date) {

        this.player = player;
        this.itemStack = itemStack;
        this.databaseId = databaseId;
        this.date = date;
    }

    public String getPlayer() {

        return player;
    }

    public ItemStack getItemStack() {

        return itemStack;
    }

    public int getDatabaseId() {

        return databaseId;
    }

    public Timestamp getDate() {

        return date;
    }
}
